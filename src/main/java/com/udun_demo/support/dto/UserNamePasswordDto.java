package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Accessors(chain = true)
@Data
public class UserNamePasswordDto extends UserNameDto {

    @ApiModelProperty(value = "登录密码",required = true)
    @NotBlank
    private String password;
}
