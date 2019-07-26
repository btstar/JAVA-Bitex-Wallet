package com.udun_demo.support.enums;

import lombok.Getter;

@Getter
public enum  TradeTypeEnum {

    WITHDRAW(1,"提币"),
    DEPOSIT(2,"充币");

    private int code;
    private String msg;

    TradeTypeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
