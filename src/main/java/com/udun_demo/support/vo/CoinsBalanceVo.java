package com.udun_demo.support.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinsBalanceVo {
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
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    private String coinName;

    @ApiModelProperty(value = "币种全称")
    private String fullName;

    @ApiModelProperty(value = "logo地址")
    private String  logo;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String symbol;
    /**
     * 精度
     */
    @ApiModelProperty(value = "精度")
    private Integer decimal;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 余额
     */
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    /**
     * 冻结资金
     */
    @ApiModelProperty(value = "冻结资金")
    private BigDecimal frozenBalance;

    @ApiModelProperty(value = "cny汇率")
    private String cnyPrice;

    @ApiModelProperty(value = "usd汇率")
    private String usdPrice;

    @ApiModelProperty(value = "用户时候支持：true 支持")
    private Boolean support = true;
}
