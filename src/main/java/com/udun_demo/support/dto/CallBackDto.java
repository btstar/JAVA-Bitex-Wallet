package com.udun_demo.support.dto;

import lombok.Data;

@Data
public class CallBackDto {

    private String timestamp;

    private String nonce;

    private String sign;

    private String body;

}
