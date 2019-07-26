package com.udun_demo.support.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceVo {

    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 币种类型
     */
    @ApiModelProperty(value = "币种类型")
    private String coinType;
    /**
     * 主币种类型
     */
    @ApiModelProperty(value = "主币种类型")
    private String mainCoinType;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "冻结资金")
    private BigDecimal frozenBalance;
    @ApiModelProperty(value = "可用资金")
    private BigDecimal availableFund;

    @ApiModelProperty(value = "手续费")
    private BigDecimal feeAmount;

    @ApiModelProperty(value = "最小提币数量")
    private BigDecimal minWithdrawAmount;

    @ApiModelProperty(value = "币种单位")
    private String symbol;

    @ApiModelProperty(value = "币种名称")
    private String coinName;

    @ApiModelProperty(value = "cny汇率")
    private String cnyPrice;

    @ApiModelProperty(value = "usd汇率")
    private String usdPrice;

    public BigDecimal getMinWithdrawAmount(){
        if (minWithdrawAmount.compareTo(BigDecimal.ZERO) <= 0){
            return BigDecimal.ZERO;
        }else {
            return minWithdrawAmount;
        }
    }
}
