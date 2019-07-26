package com.udun_demo.service.Impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.udun_demo.dao.entity.Coin;
import com.udun_demo.dao.entity.TradeRecord;
import com.udun_demo.dao.entity.UserBalance;
import com.udun_demo.dao.entity.WithdrawAudit;
import com.udun_demo.dao.mapper.WithdrawAuditMapper;
import com.udun_demo.service.ICoinService;
import com.udun_demo.service.ITradeRecordService;
import com.udun_demo.service.IUserBalanceService;
import com.udun_demo.service.IWithdrawAuditService;
import com.udun_demo.service.wallet.IWalletService;
import com.udun_demo.support.common.CommonException;
import com.udun_demo.support.common.Response;
import com.udun_demo.support.enums.TradeTypeEnum;
import com.udun_demo.support.enums.WithdrawAuditStatusEnum;
import com.udun_demo.support.utils.GlobalPropertiesGetter;
import com.udun_demo.support.vo.WithdrawRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
public class WithdrawAuditServiceImpl extends ServiceImpl<WithdrawAuditMapper, WithdrawAudit> implements IWithdrawAuditService {

    @Autowired
    private IWalletService walletService;
    @Autowired
    private GlobalPropertiesGetter propertiesGetter;
    @Autowired
    private IUserBalanceService userBalanceService;
    @Autowired
    private ITradeRecordService tradeRecordService;
    @Autowired
    private ICoinService coinService;

    @Override
    public List<WithdrawAudit> checkExist(String businessId, List<WithdrawAuditStatusEnum> withdrawAuditStatusEnums) {
        Wrapper<WithdrawAudit> ew = new EntityWrapper<WithdrawAudit>().eq("business_id", businessId);
        int size = withdrawAuditStatusEnums.size();
        for (int i = 0; i < size; i++) {
            if (i == 0){
                ew.andNew();
            }
            ew.eq("status", withdrawAuditStatusEnums.get(i).getCode());
            if (i != size -1) {
                ew.or();
            }
        }
        List<WithdrawAudit> withdrawAudits = selectList(ew);
        if (CollectionUtils.isEmpty(withdrawAudits)){
            log.warn("数据库中没有指定业务id:{}的提币记录", businessId);
            throw new CommonException(Response.NO_BUSINESS_ID_MATCH);
        }
        return withdrawAudits;
    }

    /**
     * 支付成功 回调后处理
     * @param businessId
     */
    @Override
    public void paySuccess(String businessId) {
        WithdrawAudit withdrawAudit = checkExist(businessId, Collections.singletonList(WithdrawAuditStatusEnum.WAIT_PAY)).get(0);
        withdrawAudit.setStatus(WithdrawAuditStatusEnum.WAIT_BLOCK_CONFIRM.getCode()).setUpdateTime(new Date());
        if (!updateById(withdrawAudit)) {
            log.warn("更新提币审核记录{}失败", withdrawAudit);
            throw new CommonException(Response.UPDATE_WITHDRAW_AUDIT_ERROR);
        }
    }

    /**
     * 支付拒绝 回调后处理
     * @param businessId
     */
    @Override
    public void payRefused(String businessId) {
        WithdrawAudit withdrawAudit = checkExist(businessId, Collections.singletonList(WithdrawAuditStatusEnum.WAIT_PAY)).get(0);
        withdrawAudit.setStatus(WithdrawAuditStatusEnum.PAY_REFUSED.getCode()).setUpdateTime(new Date());
        if (!updateById(withdrawAudit)) {
            log.warn("更新提币审核记录{}失败", withdrawAudit);
            throw new CommonException(Response.UPDATE_WITHDRAW_AUDIT_ERROR);
        }
        // 解冻资金
        userBalanceService.thawingBalance(withdrawAudit);
    }

    /**
     * 完成 回调后处理
     * @param businessId
     */
    @Transactional
    @Override
    public void competed(String businessId,String txid) {
        // 同一 businessId 锁
        synchronized (businessId.intern()){
            WithdrawAudit withdrawAudit = checkExist(businessId, Collections.singletonList(WithdrawAuditStatusEnum.WAIT_BLOCK_CONFIRM)).get(0);
            withdrawAudit.setStatus(WithdrawAuditStatusEnum.COMPLETED.getCode()).setUpdateTime(new Date());
            if (!updateById(withdrawAudit)) {
                log.warn("更新提币审核记录{}失败", withdrawAudit);
                throw new CommonException(Response.UPDATE_WITHDRAW_AUDIT_ERROR);
            }
            String username = withdrawAudit.getUsername();
            String mainCoinType = withdrawAudit.getMainCoinType();
            BigDecimal withdrawAmount = withdrawAudit.getWithdrawAmount();
            BigDecimal feeAmount = withdrawAudit.getFeeAmount() == null ? BigDecimal.ZERO : withdrawAudit.getFeeAmount();
            String coinType = withdrawAudit.getCoinType();

            UserBalance userBalance = userBalanceService.queryBalanceByUsername(withdrawAudit.getUsername(),mainCoinType,coinType);
            // 资金减去指定提币数量和费用，并解冻指定资金
            userBalanceService.deducting(userBalance.getAddress(), mainCoinType, coinType, withdrawAmount,feeAmount,userBalance);
            // 保存记录到到交易记录
            tradeRecordService.saveWithdrawRecord(coinType,mainCoinType,withdrawAmount,feeAmount,userBalance.getUsername(),txid,businessId);
        }
    }

    /**
     * 失败 回调后处理
     * @param businessId
     */
    @Override
    public void tradeFail(String businessId) {
        WithdrawAudit withdrawAudit = checkExist(businessId, Collections.singletonList(WithdrawAuditStatusEnum.WAIT_BLOCK_CONFIRM)).get(0);
        withdrawAudit.setStatus(WithdrawAuditStatusEnum.FAILED.getCode()).setUpdateTime(new Date());
        if (!updateById(withdrawAudit)) {
            log.warn("更新提币审核记录{}失败", withdrawAudit);
            throw new CommonException(Response.UPDATE_WITHDRAW_AUDIT_ERROR);
        }
        userBalanceService.thawingBalance(withdrawAudit);
    }

    /**
     * 查询用户的提币记录
     * @param username
     * @param pageSize
     * @param pageNo
     * @return
     */
    @Override
    public Page<WithdrawRecordVo> queryRecord(String username, String mainCoinType, String coinType, int pageSize, int pageNo) {
        Page<WithdrawAudit> withdrawAuditPage = new Page<>((pageNo - 1)*pageSize,pageSize);
        selectPage(withdrawAuditPage, new EntityWrapper<WithdrawAudit>()
                        .eq("username", username)
                        .eq("main_coin_type", mainCoinType)
                        .eq("coin_type", coinType).orderBy("update_time", false));
        List<WithdrawAudit> withdrawAudits = withdrawAuditPage.getRecords();
        Coin coin = coinService.checkCoin(mainCoinType, coinType);
        List<WithdrawRecordVo> recordVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(withdrawAudits)) {
            UserBalance userBalance = userBalanceService.queryBalanceByUsername(username, mainCoinType, coinType);
            for (WithdrawAudit withdrawAudit : withdrawAudits) {
                WithdrawRecordVo recordVo = new WithdrawRecordVo();
                BeanUtils.copyProperties(withdrawAudit,recordVo);
                recordVo.setTradeAmount(withdrawAudit.getWithdrawAmount());
                recordVo.setAddress(userBalance.getAddress());
                // 如果完成了 要查询txid
                if (withdrawAudit.getStatus().equals(WithdrawAuditStatusEnum.COMPLETED.getCode())) {
                    TradeRecord tradeRecord = tradeRecordService.selectByBusinessId(withdrawAudit.getBusinessId(),TradeTypeEnum.WITHDRAW);
                    recordVo.setTxid(tradeRecord == null ? "" : tradeRecord.getTxid());
                }
                recordVo.setCoinName(coin.getCoinName());
                recordVos.add(recordVo);
            }
        }
        Page<WithdrawRecordVo> depositRecordVoPage = new Page<>();
        BeanUtils.copyProperties(withdrawAuditPage,depositRecordVoPage);
        depositRecordVoPage.setRecords(recordVos);
        return depositRecordVoPage;
    }
}
