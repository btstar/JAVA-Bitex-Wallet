package com.udun_demo.support.dto.wallet;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BaseSignDto {
    /**
     * 时间戳
     */
    @NotBlank(message = "PARAM_IS_NULL")
    private String timestamp;
    /**
     * 随机数
     */
    @NotBlank(message = "PARAM_IS_NULL")
    private String nonce;
    /**
     * 签名
     */
    @NotBlank(message = "PARAM_IS_NULL")
    private String sign;
    /**
     * 消息内容
     */
    private String body;

}

