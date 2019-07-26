package com.udun_demo.support.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WithdrawRecordVo extends RecordVo{

    @ApiModelProperty(value = "状态：1：待审核 2：审核成功，正在放币  3 完成  5：交易失败 6 驳回 ")
    private Integer status;

    private String businessId;
}
