package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBalanceDto extends CoinKeyDto{

    @ApiModelProperty(value = "用户名",required = true)
    private String username;

}
