package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CoinKeyDto {

    @ApiModelProperty(value = "币种类型",required = true)
    private String coinType;

    @ApiModelProperty(value = "币种类型",required = true)
    private String mainCoinType;
}
