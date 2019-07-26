package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WithdrawCoinDto extends CoinKeyDto{
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    @ApiModelProperty(value = "提币数量",required = true)
    private String amount;
//    /**
//     * 手续费
//     */
//    @ApiModelProperty(value = "费用")
//    private String fee;
    /**
     * 到账地址
     */
    @ApiModelProperty(value = "提币地址",required = true)
    private String address;

    @ApiModelProperty(value = "交易密码",required = true)
    private String transactionPassword;

    @ApiModelProperty(value = "备注")
    private String note;
}
