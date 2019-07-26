package com.udun_demo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.udun_demo.dao.entity.TradeRecord;
import com.udun_demo.support.enums.TradeTypeEnum;
import com.udun_demo.support.vo.RecordVo;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
public interface ITradeRecordService extends IService<TradeRecord> {

    /**
     * 保存提币记录
     * @param coinType
     * @param mainCoinType
     * @param withdrawAmount
     * @param feeAmount
     * @param username
     */
    void saveWithdrawRecord(String coinType, String mainCoinType, BigDecimal withdrawAmount, BigDecimal feeAmount, String username, String txid, String businessId);

    /**
     * 保存充币记录
     * @param coinType
     * @param mainCoinType
     * @param amount
     * @param username
     */
    void saveDepositRecord(String coinType, String mainCoinType, BigDecimal amount, String username,String txid);

    /**
     * 查看指定数据是存在
     * @param mainCoinType
     * @param coinType
     * @param txid
     * @param tradeTypeEnum
     * @return
     */
    boolean checkExist(String username,String mainCoinType, String coinType, String txid, TradeTypeEnum tradeTypeEnum);

    /**
     * 查询提币记录
     * @param username
     * @param mainCoinType
     * @param coinType
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<RecordVo> queryDepositRecord(String username, String mainCoinType, String coinType, Integer pageNo, Integer pageSize);

    /**
     * 通过业务号和交易类型查交易记录
     * @param businessId
     * @param withdraw
     * @return
     */
    TradeRecord selectByBusinessId(String businessId, TradeTypeEnum withdraw);
}
