package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DepositDto extends CoinKeyDto{
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",required = true)
    private String username;

}
