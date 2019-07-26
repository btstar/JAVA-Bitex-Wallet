package com.udun_demo.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.udun_demo.dao.entity.User;
import com.udun_demo.dao.entity.UserBalance;
import com.udun_demo.dao.mapper.UserMapper;
import com.udun_demo.service.IUserBalanceService;
import com.udun_demo.service.IUserService;
import com.udun_demo.service.wallet.IWalletService;
import com.udun_demo.support.common.CommonException;
import com.udun_demo.support.common.Response;
import com.udun_demo.support.dto.wallet.GenerateAddressDto;
import com.udun_demo.support.enums.UserBalanceStatusEnum;
import com.udun_demo.support.utils.GlobalPropertiesGetter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IWalletService walletService;

    @Autowired
    private GlobalPropertiesGetter propertiesGetter;

    @Autowired
    private IUserBalanceService userBalanceService;

    @Transactional
    @Override
    public User register(String username, String password) {
        synchronized (username.intern()) {
            checkPhone(username);
            // 检查用户名是否存在，如果存在抛出异常
            checkUserName(username);
            // 新建用户数据，这里密码没有进行加密，开发者根据自己需求进行密码加密保存数库
            User newUser = new User();
            newUser.setCreateTime(new Date())
                    .setUpdateTime(new Date())
                    .setUsername(username)
                    .setPassword(password);
            if (!insert(newUser)) {
                log.warn("保存{}失败", newUser);
                throw new CommonException(Response.REGISTER_SAVE_FAIL);
            }
            /**
             *  给用户申请地址
             *  初始化用户
             *  给用户默认添加一个CNT币种支持，并给与10个CNT
             */
            GenerateAddressDto addressDto = new GenerateAddressDto();
            addressDto.setCoinType(520)
                    .setMerchantId(propertiesGetter.getMerchantId()) // 开发者商户
                    .setCallUrl(propertiesGetter.getCallbackUrl());  // 回调地址
            // 向网关获取地址，获取地址数据详情，点进去
            String address = walletService.generateAddress(addressDto);
            UserBalance userBalance = new UserBalance();
            userBalance.setAddress(address)
                    .setCoinType("520")
                    .setMainCoinType("520")
                    .setUsername(username)
                    .setCreateTime(new Date())
                    .setBalance(new BigDecimal(10))
                    .setFrozenBalance(BigDecimal.ZERO)
                    .setStatus(UserBalanceStatusEnum.SHOW.getCode())
                    .setUpdateTime(new Date());
            userBalanceService.insert(userBalance);
            return newUser;
        }
    }

    private void checkPhone(String phone) {
        String regex = "^((13[0-9])|(14[1]|[4-9])|(15([0-3]|[5-9]))|(16[2]|[5-7])|(17[0-3]|[5-8])|(18[0-9])|(19[1|8|9]))\\d{8}$";
        if (phone.length() == 11) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (isMatch) {
                return ;
            }
        }
        throw new CommonException(Response.PHONE_ERROR);

    }

    @Override
    public void checkUserName(String username) {
        List<User> users = selectList(new EntityWrapper<User>().eq("username", username));
        if (!CollectionUtils.isEmpty(users)) {
            log.warn("用户名{}已经存在，不能重复注册", username);
            throw new CommonException(Response.USERNAME_EXIST);
        }
    }

    @Override
    public User queryUser(String username) {
        List<User> users = selectList(new EntityWrapper<User>().eq("username", username));
        if (CollectionUtils.isEmpty(users)){
            log.warn("用户名{}不存在",username);
            throw new CommonException(Response.BAD_USERNAME);
        }
        return users.get(0);
    }

    @Override
    public User login(String username, String password) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.eq("username",username).eq("password",password);
        User user = selectOne(ew);
        if (user == null) {
            log.warn("用户名{}或者密码{}错误", username,password);
            throw new CommonException(Response.USERNAME_OR_PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public void checkTransactionPassword(String username, String transactionPassword) throws CommonException {
        Wrapper<User> ew = new EntityWrapper<User>().eq("username", username).eq("transaction_password", transactionPassword);
        User user = selectOne(ew);
        if (user == null) {
            log.warn("用户名{}或者交易密码{}错误", username,transactionPassword);
            throw new CommonException(Response.TRANSACTION_PASSWORD_ERROR);
        }
    }

    @Override
    public void updateTransactionPassword(String username, String transactionPassword, String newTransactionPassword) {
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.eq("username", username);
        User user = selectOne(ew);
        if (user == null) {
            log.warn("用户名{}不存在", username);
            throw new CommonException(Response.BAD_USERNAME);
        }
        String oldTransactionPassword = user.getTransactionPassword();
        //设置过交易密码
        if (!StringUtils.isEmpty(oldTransactionPassword)) {
            if (!transactionPassword.equals(oldTransactionPassword)) {
                throw new CommonException(Response.TRANSACTION_PASSWORD_ERROR);
            }
        }
        if (StringUtils.isEmpty(newTransactionPassword)) {
            log.info("交易密码为空" );
            throw new CommonException(Response.TRANSACTION_PASSWORD_EMPTY);
        }
        user.setTransactionPassword(newTransactionPassword);
        if (!updateById(user)) {
            log.warn("更新{}交易密码错误", user);
            throw new CommonException(Response.UPDATE_TRANSACTION_PASSWORD_ERROR);
        }
    }

    @Override
    public void changePassword(String username, String password, String newPassword) {
        if (StringUtils.isEmpty(newPassword)) {
            log.info("用户{}设置密码{}为空", username,newPassword);
            throw new CommonException(Response.NEW_PASSWORD_IS_EMPTY);
        }
        User user = login(username, password);
        user.setPassword(newPassword).setUpdateTime(new Date());
        if (!updateById(user)) {
            log.info("用户{}修改密码{}错误", username,newPassword);
            throw new CommonException(Response.SAVE_PASSWORD_ERROR);
        }
    }
}
