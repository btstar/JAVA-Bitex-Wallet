package com.udun_demo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.udun_demo.dao.entity.WithdrawAudit;
import com.udun_demo.support.enums.WithdrawAuditStatusEnum;
import com.udun_demo.support.vo.WithdrawRecordVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
public interface IWithdrawAuditService extends IService<WithdrawAudit> {

    /**
     * 检查是否存在符合条件的数据
     * @param businessId 业务号
     * @param withdrawAuditStatusEnums status 可能为的值
     * @return
     */
    List<WithdrawAudit> checkExist(String businessId, List<WithdrawAuditStatusEnum> withdrawAuditStatusEnums);

    void paySuccess(String businessId);

    void payRefused(String businessId);

    void competed(String businessId,String txid);

    void tradeFail(String businessId);

    Page<WithdrawRecordVo> queryRecord(String username, String mainCoinType, String coinType, int pageSize, int pageNo);
}
