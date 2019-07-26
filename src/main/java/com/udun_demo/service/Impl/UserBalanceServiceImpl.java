package com.udun_demo.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.udun_demo.dao.entity.Coin;
import com.udun_demo.dao.entity.User;
import com.udun_demo.dao.entity.UserBalance;
import com.udun_demo.dao.entity.WithdrawAudit;
import com.udun_demo.dao.mapper.UserBalanceMapper;
import com.udun_demo.service.*;
import com.udun_demo.service.wallet.IWalletService;
import com.udun_demo.support.common.CommonException;
import com.udun_demo.support.common.Response;
import com.udun_demo.support.dto.wallet.GenerateAddressDto;
import com.udun_demo.support.dto.wallet.WithdrawDto;
import com.udun_demo.support.enums.TradeTypeEnum;
import com.udun_demo.support.enums.UserBalanceStatusEnum;
import com.udun_demo.support.enums.WithdrawAuditStatusEnum;
import com.udun_demo.support.utils.BusinessIdUtil;
import com.udun_demo.support.utils.GlobalPropertiesGetter;
import com.udun_demo.support.vo.BalanceVo;
import com.udun_demo.support.vo.CoinsBalanceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class UserBalanceServiceImpl extends ServiceImpl<UserBalanceMapper, UserBalance> implements IUserBalanceService {
    @Autowired
    private IWalletService walletService;
    @Autowired
    private GlobalPropertiesGetter propertiesGetter;
    @Autowired
    private IWithdrawAuditService withdrawAuditService;
    @Autowired
    private ICoinService coinService;
    @Autowired
    private ITradeRecordService tradeRecordService;
    @Autowired
    private IUserService userService;

    @Autowired
    private UserBalanceMapper userBalanceMapper;

//    @Autowired
//    public UserBalanceServiceImpl(IWalletService walletService, GlobalPropertiesGetter propertiesGetter, IWithdrawAuditService withdrawAuditService, ICoinService coinService) {
//        this.walletService = walletService;
//        this.propertiesGetter = propertiesGetter;
//        this.withdrawAuditService = withdrawAuditService;
//        this.coinService = coinService;
//    }


    @Override
    public BalanceVo queryBalance(String username, String mainCoinType, String coinType) {
        Coin coin = coinService.checkCoin(mainCoinType, coinType);
        UserBalance userBalance = queryBalanceByUsername(username, mainCoinType, coinType);
        BalanceVo balanceVo = new BalanceVo();
        BeanUtils.copyProperties(userBalance,balanceVo);
        balanceVo.setAvailableFund(userBalance.getBalance().subtract(userBalance.getFrozenBalance()));
        balanceVo.setMinWithdrawAmount(coin.getMinWithdrawAmount());
        balanceVo.setFeeAmount(coin.getFeeAmount());
        balanceVo.setSymbol(coin.getSymbol());
        balanceVo.setCoinName(coin.getCoinName());
        return balanceVo;
    }

    @Override
    public void addSupportCoin(String username, String mainCoinType, String coinType) {
        // 首先校验是否支持了该币种 支持 就改为可显示状态
        UserBalance userBalance1 = selectOne(uniqueSupportCoinEw(username, mainCoinType, coinType));
        if (userBalance1 != null) { //
            Integer status = userBalance1.getStatus();
            if (status == null) {
                log.warn("币种{主币种类型{},币种类型{}}的是否可显示状态为空", mainCoinType,coinType);
                throw new CommonException(Response.USER_BALANCE_STATUS_NULL);
            }
            if (status.equals(UserBalanceStatusEnum.SHOW)) {
                log.info("用户{}已经支持币种,主币种类型{}，币种类型{}", username,mainCoinType,coinType);
                throw new CommonException(Response.SUPPORTED_THIS_COIN);
            }else{
                if (!mainCoinType.equals(coinType)){ // 子币种要改变主币种也为显示状态
                    changeStatus(username,mainCoinType, mainCoinType,UserBalanceStatusEnum.SHOW);
                }
                changeStatus(username,mainCoinType, coinType,UserBalanceStatusEnum.SHOW);
            }
        }else{
            // 校验币种是否支持
            Coin coin = coinService.checkCoin(mainCoinType, coinType);
            User user = userService.queryUser(username);
            String address = "";
            List<UserBalance> userBalanceList = new ArrayList<>();
            // 查询主币种是否已经生成地址，如果有生成地址，代币使用主币种的地址
            UserBalance mainCoinUserBalance = selectOne(uniqueSupportCoinEw(username, mainCoinType, mainCoinType));
            if (mainCoinUserBalance != null) {
                address = mainCoinUserBalance.getAddress();
            }else { // 如果主币种没有生成地址，

                if (!StringUtils.isEmpty(coin.getMasterAddress())) {  //特殊币种生成地址处理
                    // 如果是EOS和XRP币种获取地址 通过住地址拼接方式 生成地址
                    address = coin.getMasterAddress() + ":" + user.getUserId();
                } else {
                    // 通过网关获取地址
                    GenerateAddressDto generateAddressDto = new GenerateAddressDto();
                    generateAddressDto.setMerchantId(propertiesGetter.getMerchantId())
                            .setCallUrl(propertiesGetter.getCallbackUrl())
                            .setCoinType(Integer.parseInt(mainCoinType));
                    address = walletService.generateAddress(generateAddressDto);
                    if (!coinType.equals(mainCoinType)){//如果是子币种,并且主币种资金没有添加，还要添加对应主币种
                        UserBalance mainBalance =
                        new UserBalance().setUsername(username)
                                .setMainCoinType(mainCoinType)
                                .setCoinType(mainCoinType)
                                .setAddress(address)
                                .setBalance(BigDecimal.ZERO)
                                .setStatus(UserBalanceStatusEnum.SHOW.getCode())
                                .setFrozenBalance(BigDecimal.ZERO)
                                .setCreateTime(new Date())
                                .setUpdateTime(new Date());
                        userBalanceList.add(mainBalance);
                    }
                }

            }
            UserBalance userBalance =
                    new UserBalance().setUsername(username)
                    .setMainCoinType(mainCoinType)
                    .setCoinType(coinType)
                    .setAddress(address)
                    .setStatus(UserBalanceStatusEnum.SHOW.getCode())
                    .setBalance(BigDecimal.ZERO)
                    .setFrozenBalance(BigDecimal.ZERO)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            userBalanceList.add(userBalance);
            boolean success = insertBatch(userBalanceList);
            if (!success){
                log.warn("保存支持币种{}失败", userBalance);
                throw new CommonException(Response.ADD_SUPPORT_COIN_ERROR);
            }
        }
    }

    private Wrapper<UserBalance> uniqueSupportCoinEw(String username, String mainCoinType, String coinType){
        return new EntityWrapper<UserBalance>().eq("username", username)
                .eq("main_coin_type", mainCoinType)
                .eq("coin_type", coinType);
    }

    private Wrapper<UserBalance> uniqueUserBalanceEw(String address, String mainCoinType,String coinType){
        return new EntityWrapper<UserBalance>().eq("address", address)
                .eq("coin_type", coinType)
                .eq("main_coin_type", mainCoinType);
    }

    @Transactional
    @Override
    public void withdrawCoin(String username, String coinType, String mainCoinType, String amount, String address, String transactionPassword, String note) {
        synchronized ((username+mainCoinType+coinType).intern()) {
            Coin coin = coinService.checkCoin(mainCoinType, coinType);
            // 判断用户是否支持该币种
            UserBalance userBalance = selectOne(uniqueSupportCoinEw(username, mainCoinType, coinType));
            if (userBalance == null) {
                log.warn("没有找到指定资金账户信息,用户名{},主币种类型{},币种类型{}",username,mainCoinType,coinType);
                throw new CommonException(Response.NO_BALANCE_INFORMATION);
            }
            // 校验交易密码
            userService.checkTransactionPassword(username,transactionPassword);
            BigDecimal balance = userBalance.getBalance();
            BigDecimal frozenBalance = userBalance.getFrozenBalance();
            BigDecimal fee = coin.getFeeAmount();
            fee = fee == null ? BigDecimal.ZERO : fee;
            BigDecimal amountBig = BigDecimal.valueOf(Double.valueOf(amount)).subtract(fee);
            // 校验提币数量是否符合币种配置的提币数量限制
            coinService.checkCoinWithdrawAmount(mainCoinType,coinType,amountBig);
            // 判断资金是否充足
            if (balance.subtract(frozenBalance).subtract(amountBig).compareTo(BigDecimal.ZERO) < 0) {
                log.info("用户{}提币{},主币种类型{}，币种类型{},资金不足", username,amount,mainCoinType,coinType);
                throw new CommonException(Response.INSUFFICIENT_BALANCE);
            }
            String businessId = BusinessIdUtil.getGuid();
//            String businessId = UUID.randomUUID().toString();

            WithdrawDto withdrawDto = new WithdrawDto();
            withdrawDto.setAddress(address)
                    .setAmount(amountBig.toPlainString())
                    .setMainCoinType(mainCoinType)
                    .setCoinType(coinType)
                    .setFee(fee.toPlainString())
                    .setCallUrl(propertiesGetter.getCallbackUrl())
                    .setMerchantId(propertiesGetter.getMerchantId())
                    .setBusinessId(businessId);
            // 发送提币到网关
            boolean withdraw = walletService.withdraw(withdrawDto);
            if (!withdraw) {
                throw new CommonException(Response.SEND_WITHDRAW_COIN_ERROR);
            }
            // 保存提币申请到数据库
            WithdrawAudit withdrawAudit = new WithdrawAudit();
            withdrawAudit.setCoinType(coinType)
                    .setMainCoinType(mainCoinType)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date())
                    .setWithdrawAmount(amountBig)
                    .setStatus(WithdrawAuditStatusEnum.WAIT_PAY.getCode())
                    .setUsername(username)
                    .setAddress(address)
                    .setFeeAmount(fee)
                    .setNote(note)
                    .setBusinessId(businessId);
            if (!withdrawAuditService.insert(withdrawAudit)) {
                throw new CommonException(Response.SAVE_WITHDRAW_AUDIT_ERROR);
            }
            // 保存提币申请到数据库
//            WithdrawAudit withdrawAudit = new WithdrawAudit();
//            withdrawAudit.setCoinType(coinType)
//                    .setAddress(address)
//                    .setMainCoinType(mainCoinType)
//                    .setCreateTime(new Date())
//                    .setWithdrawAmount(BigDecimal.valueOf(Double.valueOf(amount)))
//                    .setStatus(WithdrawAuditStatusEnum.WAIT_AUDIT.getCode())
//                    .setUsername(username)
//                    .setBusinessId(businessId)
//                    .setFeeAmount(fee)
//                    .setNote(note);
//            if (!withdrawAuditService.insert(withdrawAudit)) {
//                log.warn("保存提币记录{}失败", withdrawAudit);
//                throw new CommonException(Response.SAVE_WITHDRAW_AUDIT_ERROR);
//            }
            /**
             * 冻结提币资金
             */
            freezingBalance(userBalance.getAddress(),userBalance.getCoinType(),userBalance.getMainCoinType(),amountBig.add(fee), userBalance);
        }
    }

    @Override
    public void freezingBalance(String address, String coinType, String mainCoinType,BigDecimal amount,UserBalance userBalance) {
        if (null == userBalance) {
            userBalance = selectOne(uniqueUserBalanceEw(address,mainCoinType,coinType));
        }
        userBalance.setUpdateTime(new Date()).setFrozenBalance(userBalance.getFrozenBalance().add(amount));
        updateById(userBalance);
    }

    @Override
    public void thawingBalance(String address, String coinType, String mainCoinType,BigDecimal amount,UserBalance userBalance) {
        if (null == userBalance) {
            userBalance = selectOne(uniqueUserBalanceEw(address,mainCoinType,coinType));
        }
        userBalance.setUpdateTime(new Date()).setFrozenBalance(userBalance.getFrozenBalance().subtract(amount));
        updateById(userBalance);
    }

    /**
     * 提款消费
     *   减去金额
     *   并解冻指定金额
     * @param address
     * @param mainCoinType
     * @param coinType
     * @param withdrawAmount
     * @param feeAmount
     */
    @Override
    public void deducting(String address, String mainCoinType, String coinType, BigDecimal withdrawAmount, BigDecimal feeAmount, UserBalance userBalance) {
        if (userBalance == null){
            userBalance = selectOne(uniqueUserBalanceEw(address,mainCoinType,coinType));
        }
        BigDecimal subBalance = userBalance.getBalance().subtract(withdrawAmount).subtract(feeAmount);
        if (subBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("地址{}提币{},主币种类型{}，币种类型{},资金不足", address,withdrawAmount.toPlainString(),mainCoinType,coinType);
            subBalance = BigDecimal.ZERO;
        }
        userBalance.setBalance(subBalance)
                .setFrozenBalance(userBalance.getFrozenBalance().subtract(withdrawAmount.add(feeAmount)))
                .setUpdateTime(new Date());
        updateById(userBalance);
    }

    /**
     * 处理充币回调
     *  给账户添加资金
     * @param address
     * @param mainCoinType
     * @param coinType
     * @param amount
     * @param memo
     */
    @Transactional
    @Override
    public void depositCallbackHandle(String address, String mainCoinType, String coinType, BigDecimal amount, String businessId, String txid, String memo) {
        // 查看数据是否存在
        checkTradeRecord(address,mainCoinType,coinType, txid, TradeTypeEnum.DEPOSIT);
        synchronized ((address+mainCoinType+coinType+txid).intern()) {
            //查看数据是否存在
            checkTradeRecord(address,mainCoinType,coinType, txid,TradeTypeEnum.DEPOSIT);
            Coin coin = coinService.checkCoin(mainCoinType, coinType);
            // 过滤充币数量太小的回调
            BigDecimal minDepositAmount = coin.getMinDepositAmount();

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("地址：{}充币（币种：{},主币种：{}）数量({})小于等于0，系统不允许录入数据"
                        ,address,coinType,mainCoinType,amount.toPlainString());
                throw new CommonException(Response.DEPOSIT_AMOUNT_ERROR);
            }

            if (minDepositAmount == null) {
                log.error("没有配 币种：{},主币种：{}的最小充币数量",coinType,mainCoinType);
            }else{
                if (minDepositAmount.compareTo(amount) > 0) {
                    log.error("地址：{}充币（币种：{},主币种：{}）数量({})太小，系统不允许录入数据"
                            ,address,coinType,mainCoinType,amount.toPlainString());
                    throw new CommonException(Response.MIN_DEPOSIT_AMOUNT);
                }
            }
            // 特殊币种处理
            if (!StringUtils.isEmpty(coin.getMasterAddress())) {
                if (!address.equals(coin.getMasterAddress()) || StringUtils.isEmpty(memo)) {
                    log.warn("特殊币种处理，币种包含主币种类型，但是mono为空，不合法充币回调，地址：{}", address);
                    throw new CommonException(Response.ADDRESS_DATA_ERROR);
                }
                address = address + ":" + memo;
            }
            UserBalance userBalance = selectOne(uniqueUserBalanceEw(address, mainCoinType, coinType));
            userBalance.setUpdateTime(new Date())
                    .setBalance(userBalance.getBalance().add(amount));
            updateById(userBalance);
            // 保存充币数据到记录表
            tradeRecordService.saveDepositRecord(coinType, mainCoinType, amount, userBalance.getUsername(), txid);
        }
    }

    private void checkTradeRecord(String address,String mainCoinType,String coinType,String txid,TradeTypeEnum tradeTypeEnum){
        UserBalance userBalance = queryBalanceByAddress(address, mainCoinType, coinType);
        boolean exist = tradeRecordService.checkExist(userBalance.getUsername(),mainCoinType,coinType,txid, tradeTypeEnum);
        if (exist){
            log.info("重复[{}]推送 txid:[{}]",tradeTypeEnum.getMsg(),txid);
            throw new CommonException(Response.REPEAT_CALLBACK_ERROR);
        }
    }

    @Override
    public UserBalance queryBalanceByAddress(String address, String mainCoinType, String coinType) {
        UserBalance userBalance = selectOne(uniqueUserBalanceEw(address, mainCoinType, coinType));
        if (userBalance == null) {
            log.warn("没有找到指定资金账户信息,地址{},主币种类型{},币种类型{}",address,mainCoinType,coinType);
            throw new CommonException(Response.NO_BALANCE_INFORMATION);
        }
        return userBalance;
    }

    @Override
    public UserBalance queryBalanceByUsername(String username, String mainCoinType, String coinType) {
        UserBalance userBalance = selectOne(uniqueSupportCoinEw(username, mainCoinType, coinType));
        if (userBalance != null){
            return userBalance;
        }else{
            log.warn("没有找到指定资金账户信息,用户名{},主币种类型{},币种类型{}",username,mainCoinType,coinType);
            throw new CommonException(Response.NO_BALANCE_INFORMATION);
        }
    }

    @Override
    public void thawingBalance(WithdrawAudit withdrawAudit) {
        String username = withdrawAudit.getUsername();
        String coinType = withdrawAudit.getCoinType();
        String mainCoinType = withdrawAudit.getMainCoinType();
        BigDecimal withdrawAmount = withdrawAudit.getWithdrawAmount();
        BigDecimal feeAmount = withdrawAudit.getFeeAmount();
        UserBalance userBalance = queryBalanceByUsername(username, mainCoinType, coinType);
        thawingBalance(userBalance.getAddress(), coinType,mainCoinType, withdrawAmount.add(feeAmount),userBalance);
    }

    /**
     * 查询用户的所有币种以及资金信息
     * @param username
     * @param show
     * @return
     */
    @Override
    public List<CoinsBalanceVo> queryCoinsBalance(String username, UserBalanceStatusEnum show) {
        List<CoinsBalanceVo> coinsBalanceVos = userBalanceMapper.queryCoinsBalance(username,show.getCode());
        return coinsBalanceVos;
    }

    /**
     * 改变用户资金是否展示的状态
     * @param username
     * @param mainCoinType
     * @param coinType
     * @param statusEnum
     */
    @Override
    public void changeStatus(String username, String mainCoinType, String coinType, UserBalanceStatusEnum statusEnum) {
        UserBalance userBalance = queryBalanceByUsername(username, mainCoinType, coinType);
        if (userBalance == null) {
            log.warn("没有找到指定资金账户信息,用户名{},主币种类型{},币种类型{}",username,mainCoinType,coinType);
            throw new CommonException(Response.NO_BALANCE_INFORMATION);
        }
        if (userBalance.getStatus().equals(statusEnum.getCode())) { //状态一样，不修改
            return ;
        }
        userBalance.setStatus(statusEnum.getCode());
        userBalance.setUpdateTime(new Date());
        if (!updateById(userBalance)) {
            log.warn("修改用户资金表状态失败", userBalance);
            throw new CommonException(Response.CHANGE_USER_BALANCE_STATUS_ERROR);
        }
    }

    /**
     * 不显示指定币种资金
     * @param username
     * @param mainCoinType
     * @param coinType
     */
    @Override
    public void notShowBalance(String username, String mainCoinType, String coinType) {
        changeStatus(username,mainCoinType,coinType,UserBalanceStatusEnum.NOT_SHOW);
        if (mainCoinType.equals(coinType)){ //主币种
            List<UserBalance> userBalanceList =
                    selectList(new EntityWrapper<UserBalance>()
                            .eq("username", username)
                            .eq("main_coin_type", mainCoinType));
            for (int i = 0; i < userBalanceList.size(); i++) {
                UserBalance balance = userBalanceList.get(i);
                int statusCode = UserBalanceStatusEnum.NOT_SHOW.getCode();
                if (!balance.getStatus().equals(statusCode)) {
                    balance.setStatus(statusCode);
                    balance.setUpdateTime(new Date());
                }
            }
            if (!updateBatchById(userBalanceList)) {
                throw new CommonException(Response.CHANGE_USER_BALANCE_STATUS_ERROR);
            }
        }
    }
}
