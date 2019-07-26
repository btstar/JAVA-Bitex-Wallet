package com.udun_demo.api;


import com.baomidou.mybatisplus.plugins.Page;
import com.udun_demo.dao.entity.User;
import com.udun_demo.service.*;
import com.udun_demo.support.common.MessageResult;
import com.udun_demo.support.dto.*;
import com.udun_demo.support.enums.UserBalanceStatusEnum;
import com.udun_demo.support.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/app")
@RestController
@Api(description = "APP端接口")
@Slf4j
public class MobileController {

    private final ICoinService coinService;
    private final IUserService userService;
    private final IUserBalanceService userBalanceService;
    private final IWithdrawAuditService withdrawAuditService;
    private final ITradeRecordService tradeRecordService;

    @Autowired
    public MobileController(ICoinService coinService,
                            IUserService userService,
                            IUserBalanceService userBalanceService,
                            IWithdrawAuditService withdrawAuditService,
                            ITradeRecordService tradeRecordService) {
        this.coinService = coinService;
        this.userService = userService;
        this.userBalanceService = userBalanceService;
        this.withdrawAuditService = withdrawAuditService;
        this.tradeRecordService = tradeRecordService;
    }
    /**
     * 用户注册
     *   1、存数据库
     *      查询用户名是否在用户中存在，存在就不可以添加，提示用户名已经存在
     *   2、初始化用户
     *      给用户默认添加一个CNT币种支持，并给与10个CNT（根据自己开发的业务逻辑来，这里为了方便测试使用）
     * @return
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public MessageResult register(@RequestBody UserNamePasswordDto userNamePasswordDto){
        String username = userNamePasswordDto.getUsername().trim();
        String password = userNamePasswordDto.getPassword().trim();
        log.info("用户注册，用户名{}，密码{}", username,password);
        // 注册，如果失败会抛出异常，通过全局异常处理方式返回提示
        userService.register(username,password);
        User login = userService.login(username, password);
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(30000+login.getUserId()); //自己根据业务需求来出来，这里只是简单的展示
        loginVo.setTxPssSet(!StringUtils.isEmpty(login.getTransactionPassword()));
        return MessageResult.success(loginVo);
    }
    /**
     * 登录
     *      验证密码和用户名
     * @param userNamePasswordDto
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public MessageResult login(@RequestBody UserNamePasswordDto userNamePasswordDto){
        String username = userNamePasswordDto.getUsername().trim();
        String password = userNamePasswordDto.getPassword().trim();
        log.info("用户登录，用户名{}，密码{}", username,password);
        User login = userService.login(username, password);
        Long userId = login.getUserId();
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(30000+userId); //自己根据业务需求来出来，这里只是简单的展示
        loginVo.setTxPssSet(!StringUtils.isEmpty(login.getTransactionPassword()));
        return MessageResult.success(loginVo);
    }

    /**
     * 修改密码
     * @param changePasswordDto
     * @return
     */
    @ApiOperation(value = "修改密码")
    @PostMapping("/change/password")
    public  MessageResult changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        String password = changePasswordDto.getPassword();
        String username = changePasswordDto.getUsername();
        String newPassword = changePasswordDto.getNewPassword();
        log.info("用户修改密码，用户名{}，密码{}，新密码{}", username,password,newPassword);
        userService.changePassword(username,password,newPassword);
        return MessageResult.success();
    }

    /**
     * 查询用户支持的币种以及资金信息
     * @param userNameDto
     * @return
     */
    @ApiOperation(value = "查询用户支持的币种以及资金信息",response = CoinsBalanceVo.class)
    @PostMapping("/coins/balance")
    public MessageResult listCoinsBalance(@RequestBody UserNameDto userNameDto){
        String username = userNameDto.getUsername();
        log.info("用户查询支持用户支持的币种以及资金信息，用户名{}", username);
        List<CoinsBalanceVo> coinsBalanceVos =
                userBalanceService.queryCoinsBalance(username, UserBalanceStatusEnum.SHOW);
        List<CoinListVo> coinListVos = coinService.queryAll();
        // 合并用户支持币种和该商户支持的所有币种
        for (CoinListVo coinListVo : coinListVos) {
            boolean flag = true;
            for (CoinsBalanceVo coinsBalanceVo : coinsBalanceVos) {
                if (coinsBalanceVo.getMainCoinType().equals(coinListVo.getMainCoinType())
                        && coinsBalanceVo.getCoinType().equals(coinListVo.getCoinType())){
                    flag = false;
                    break;
                }
            }
            if (flag){
                CoinsBalanceVo coinsBalanceVo = new CoinsBalanceVo();
                BeanUtils.copyProperties(coinListVo, coinsBalanceVo);
                coinsBalanceVo.setSupport(false);
                coinsBalanceVos.add(coinsBalanceVo);
            }
        }
        return MessageResult.success(coinsBalanceVos);
    }
    /**
     *
     * 根据 用户名，主币种类型，币种类型查询该钱包下的资金信息
     * @param queryBalanceDto
     * @return
     */
    @ApiOperation(value = "根据 用户名，主币种类型，币种类型查询该钱包下的资金信息",response = BalanceVo.class)
    @PostMapping("/balance")
    public MessageResult queryBalance(@RequestBody QueryBalanceDto queryBalanceDto){
        String coinType = queryBalanceDto.getCoinType();
        String mainCoinType = queryBalanceDto.getMainCoinType();
        String username = queryBalanceDto.getUsername();
        BalanceVo balanceVo =  userBalanceService.queryBalance(username,mainCoinType,coinType);
        return MessageResult.success(balanceVo);
    }
    /**
     * 给用户添加支持币种
     * @param addSupportCoinDto
     * @return
     */
    @ApiOperation(value = "给用户添加/删除  支持币种")
    @PostMapping("/coin/add")
    public MessageResult addSupportCoin(@RequestBody AddSupportCoinDto addSupportCoinDto){
        String coinType = addSupportCoinDto.getCoinType();
        String mainCoinType = addSupportCoinDto.getMainCoinType();
        String username = addSupportCoinDto.getUsername();
        Boolean type = addSupportCoinDto.getSupport();
        log.info("用户{}{}币种{主币种类型{},币种类型{}}", username,type == null || type.equals(true) ? "添加": "删除",mainCoinType,coinType);
        if (type == null || type.equals(true)) { //不传type参数或者为true时  添加
            userBalanceService.addSupportCoin(username,mainCoinType,coinType);
        }else{ // 删除
            userBalanceService.notShowBalance(username,mainCoinType,coinType);
        }
        return MessageResult.success();
    }
    /**
     * 发送提币
     * @param
     * @return
     */
    @ApiOperation(value = "发送提币")
    @PostMapping("/withdraw")
    public MessageResult withdrawCoin(@RequestBody WithdrawCoinDto withdrawCoinDto){
        String username = withdrawCoinDto.getUsername();
        String coinType = withdrawCoinDto.getCoinType();
        String mainCoinType = withdrawCoinDto.getMainCoinType();
        String amount = withdrawCoinDto.getAmount();
//        BigDecimal fee = BigDecimal.valueOf(Double.valueOf(withdrawCoinDto.getFee()));
        String address = withdrawCoinDto.getAddress();
        String transactionPassword = withdrawCoinDto.getTransactionPassword();
        String note = withdrawCoinDto.getNote();
        log.info("用户{},提币地址{}，发送提币{主币种类型：{},币种类型:{}，提币数量：{}}", username,address,mainCoinType,coinType,amount);
        userBalanceService.withdrawCoin(username,coinType,mainCoinType,amount,address,transactionPassword,note);
        return MessageResult.success();
    }
    /**
     * 获取地址，提供充值
     * @param depositDto
     * @return
     */
    @PostMapping("/address")
    @ApiOperation(value = "获取地址，提供充值")
    public MessageResult address(@RequestBody DepositDto depositDto){
        String username = depositDto.getUsername();
        String coinType = depositDto.getCoinType();
        String mainCoinType = depositDto.getMainCoinType();
        log.info("用户{}获取币种{主币种类型{},币种类型{}}地址充值", username,mainCoinType,coinType);
        String address = userBalanceService.queryBalance(username, mainCoinType, coinType).getAddress();
        AddressVo addressVo = new AddressVo();
        addressVo.setAddress(address);
        return MessageResult.success(addressVo);
    }

    /**
     * 查询提币记录
     * @param recordDto
     * @return
     */
    @ApiOperation(value = "提币记录",response = WithdrawRecordVo.class)
    @PostMapping("/withdraw/record")
    public MessageResult withdrawRecord(@RequestBody RecordDto recordDto){
        String username = recordDto.getUsername();
        String mainCoinType = recordDto.getMainCoinType();
        String coinType = recordDto.getCoinType();
        Integer pageSize = recordDto.getPageSize();
        Integer pageNo = recordDto.getPageNo();
        log.info("用户{},查询币种{主币种类型{}，币种类型{}}提币记录", username,mainCoinType,coinType);
        Page<WithdrawRecordVo> depositRecordVoPage =
                withdrawAuditService.queryRecord(username, mainCoinType, coinType, pageSize, pageNo);
        return  MessageResult.success(depositRecordVoPage);
    }
    /**
     * 查询充币记录
     * @param recordDto
     * @return
     */
    @ApiOperation(value = "充币记录",response = RecordVo.class)
    @PostMapping("/deposit/record")
    public MessageResult depositRecord(@RequestBody RecordDto recordDto){
        String username = recordDto.getUsername();
        String mainCoinType = recordDto.getMainCoinType();
        String coinType = recordDto.getCoinType();
        Integer pageNo = recordDto.getPageNo();
        Integer pageSize = recordDto.getPageSize();
        log.info("用户{}查询充币{主币种类型{},币种类型{}}记录", username,mainCoinType,coinType);
        Page<RecordVo> recordVoPage =
                tradeRecordService.queryDepositRecord(username, mainCoinType, coinType,pageNo,pageSize);
        return MessageResult.success(recordVoPage);
    }


    /**
     * 修改交易密码
     * @param transactionPasswordDto
     * @return
     */
    @ApiOperation(value = "修改交易密码")
    @PostMapping("/transaction/password")
    public MessageResult transactionPassword(@RequestBody TransactionPasswordDto transactionPasswordDto){
        String username = transactionPasswordDto.getUsername();
        String transactionPassword = transactionPasswordDto.getTransactionPassword();
        String newTransactionPassword = transactionPasswordDto.getNewTransactionPassword();
        log.info("用户{}设置交易密码{}",username,transactionPassword);
        userService.updateTransactionPassword(username,transactionPassword,newTransactionPassword);
        return MessageResult.success();
    }
}
