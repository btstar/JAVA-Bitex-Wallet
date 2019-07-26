package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditWithdrawTradeDto {
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id号",required = true)
    private String businessId;

    /**
     * 审核是否通过 true  通过   false 驳回
     */
    @ApiModelProperty(value = "审核是否通过 true  通过   false 驳回",required = true)
    private Boolean pass;
}
