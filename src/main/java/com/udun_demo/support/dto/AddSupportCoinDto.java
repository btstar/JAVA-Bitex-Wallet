package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddSupportCoinDto extends CoinKeyDto {
    @ApiModelProperty(value = "用户名",required = true)
    private String username;
    /**
     * 操作类型
     *     true:添加
     *     false：删除
     */
    @ApiModelProperty(value = "操作类型true:添加 false：删除",required = true)
    private Boolean support;
}
