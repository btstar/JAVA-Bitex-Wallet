package com.udun_demo.support.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddressVo {
    @ApiModelProperty(value = "地址")
    private String address;
}
