package com.udun_demo.support.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordDto extends UserNamePasswordDto{

    @ApiModelProperty(value = "新密码",required = true)
    @NotBlank
    private String newPassword;
}
