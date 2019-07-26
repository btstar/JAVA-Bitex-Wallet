package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransactionPasswordDto extends UserNameDto{
    @ApiModelProperty(value = "交易密码",required = true)
    private String transactionPassword;

    @ApiModelProperty(value = "新交易密码",required = true)
    private String newTransactionPassword;
}
