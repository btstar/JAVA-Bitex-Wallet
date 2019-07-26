package com.udun_demo.service;

import com.baomidou.mybatisplus.service.IService;
import com.udun_demo.dao.entity.UserBalance;
import com.udun_demo.dao.entity.WithdrawAudit;
import com.udun_demo.support.enums.UserBalanceStatusEnum;
import com.udun_demo.support.vo.BalanceVo;
import com.udun_demo.support.vo.CoinsBalanceVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
public interface IUserBalanceService extends IService<UserBalance> {

    BalanceVo queryBalance(String username, String mainCoinType, String coinType);

    /**
     * 給用戶添加 币种 类似于添加钱包
     */
    void addSupportCoin(String username, String mainCoinType, String coinType);

    void withdrawCoin(String username, String coinType, String mainCoinType, String amount, String address, String transactionPassword, String note);

    /**
     * 冻结指定资金
     */
    void freezingBalance(String address,String coinType,String mainCoinType, BigDecimal amount,UserBalance userBalance);

    /**
     * 解冻指定资金
     */
    void thawingBalance(String address,String coinType,String mainCoinType,BigDecimal amount,UserBalance userBalance);


    /**
     * 资金减少
     *      给指定地址指定币种减去指定金额
     */
    void deducting(String address, String mainCoinType, String coinType, BigDecimal withdrawAmount, BigDecimal feeAmount, UserBalance userBalance);

    /**
     * 充币回调操作
     */
    void depositCallbackHandle(String address, String mainCoinType, String coinType, BigDecimal amount, String businessId, String txid, String memo);

    UserBalance queryBalanceByAddress(String address, String mainCoinType, String coinType);

    /**
     * 根据用户名、币种 查余额表数据
     */
    UserBalance queryBalanceByUsername(String username, String mainCoinType, String coinType);


    void thawingBalance(WithdrawAudit withdrawAudit);

    List<CoinsBalanceVo> queryCoinsBalance(String username, UserBalanceStatusEnum show);

    void changeStatus(String username,String mainCoinType,String coinType,UserBalanceStatusEnum statusEnum);

    void notShowBalance(String username,String mainCoinType,String coinType);
}
