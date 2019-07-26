package com.udun_demo.support.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CoinListVo {

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
    private String logo;
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

}
