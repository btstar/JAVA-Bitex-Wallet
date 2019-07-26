package com.udun_demo.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.udun_demo.dao.entity.Coin;
import com.udun_demo.dao.entity.TradeRecord;
import com.udun_demo.dao.entity.UserBalance;
import com.udun_demo.dao.mapper.TradeRecordMapper;
import com.udun_demo.service.ICoinService;
import com.udun_demo.service.ITradeRecordService;
import com.udun_demo.service.IUserBalanceService;
import com.udun_demo.support.common.CommonException;
import com.udun_demo.support.common.Response;
import com.udun_demo.support.enums.TradeTypeEnum;
import com.udun_demo.support.vo.RecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
@Service
@Slf4j
public class TradeRecordServiceImpl extends ServiceImpl<TradeRecordMapper, TradeRecord> implements ITradeRecordService {

    @Autowired
    private ICoinService coinService;
    @Autowired
    private IUserBalanceService userBalanceService;


    @Override
    public void saveWithdrawRecord(String coinType, String mainCoinType, BigDecimal withdrawAmount, BigDecimal feeAmount, String username, String txid, String businessId) {
        if (!saveRecord(TradeTypeEnum.WITHDRAW,coinType,mainCoinType,withdrawAmount,feeAmount,username,txid,businessId)){
            log.warn("保存提币记录失败" );
            throw new CommonException(Response.SAVE_WITHDRAW_RECORD_ERROR);
        }
    }

    @Override
    public void saveDepositRecord(String coinType, String mainCoinType, BigDecimal amount, String username,String txid) {
        if (!saveRecord(TradeTypeEnum.DEPOSIT,coinType,mainCoinType,amount, BigDecimal.ZERO, username,txid,null)){
            log.warn("保存充币记录失败");
            throw new CommonException(Response.SAVE_DEPOSIT_RECORD_ERROR);
        }
    }

    @Override
    public boolean checkExist(String username,String mainCoinType, String coinType, String txid, TradeTypeEnum tradeTypeEnum) {
        int count = selectCount(new EntityWrapper<TradeRecord>()
                .eq("username",username)
                .eq("main_coin_type", mainCoinType)
                .eq("coin_type", coinType)
                .eq("trade_type", tradeTypeEnum.getCode())
                .eq("txid", txid));
        if (count >= 1){
            return true;
        }
        return false;
    }

    /**
     * 查询充币记录
     * @param username
     * @param mainCoinType
     * @param coinType
     * @param pageNo
     * @param pageSize
     */
    @Override
    public Page<RecordVo> queryDepositRecord(String username, String mainCoinType, String coinType, Integer pageNo, Integer pageSize) {
        Coin coin = coinService.checkCoin(mainCoinType, coinType);
        Page<TradeRecord> page = new Page<>((pageNo-1)*pageSize,pageSize);
        selectPage(page, new EntityWrapper<TradeRecord>()
                .eq("username", username)
                .eq("main_coin_type", mainCoinType)
                .eq("coin_type", coinType)
                .eq("trade_type", TradeTypeEnum.DEPOSIT.getCode())
                .orderBy("create_time", false));
        List<TradeRecord> tradeRecords = page.getRecords();
        List<RecordVo> recordVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tradeRecords)) {
            UserBalance userBalance = userBalanceService.queryBalanceByUsername(username, mainCoinType, coinType);
            for (TradeRecord tradeRecord : tradeRecords) {
                RecordVo recordVo = new RecordVo();
                BeanUtils.copyProperties(tradeRecord,recordVo);
                recordVo.setFeeAmount(BigDecimal.ZERO);
//                recordVo.setType(TradeTypeEnum.DEPOSIT.getCode());
                recordVo.setUpdateTime(tradeRecord.getCreateTime());
//                recordVo.setSymbol(coin.getSymbol());
                recordVo.setCoinName(coin.getCoinName());
                recordVo.setAddress(userBalance.getAddress());
                recordVos.add(recordVo);
            }
        }
        Page<RecordVo> recordVoPage = new Page<>();
        BeanUtils.copyProperties(page,recordVoPage);
        recordVoPage.setRecords(recordVos);
        return recordVoPage;
    }

    @Override
    public TradeRecord selectByBusinessId(String businessId, TradeTypeEnum withdraw) {
        TradeRecord tradeRecord = selectOne(
                new EntityWrapper<TradeRecord>()
                        .eq("business_id", businessId)
                        .eq("trade_type", withdraw.getCode()));
        return tradeRecord;
    }

    private boolean saveRecord(TradeTypeEnum tradeTypeEnum, String coinType, String mainCoinType, BigDecimal amount, BigDecimal feeAmount, String username, String txid, String businessId){
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setTradeType(tradeTypeEnum.getCode())
                .setCoinType(coinType)
                .setMainCoinType(mainCoinType)
                .setTradeAmount(amount)
                .setUsername(username)
                .setFee(feeAmount)
                .setTxid(txid)
                .setCreateTime(new Date())
                .setBusinessId(businessId);
        return insert(tradeRecord);
    }

}
