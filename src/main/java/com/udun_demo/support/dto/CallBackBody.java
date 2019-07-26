package com.udun_demo.support.dto;

import lombok.Data;

@Data
public class CallBackBody {

    private String address;

    private String amount;

    private String blockHigh;

    private String businessId;

    private String coinType;

    private String mainCoinType;

    private String decimals;

    private String fee;

    private String memo;

    private Integer status;

    private String tradeId;

    private Integer tradeType;

    private String txId;

}
