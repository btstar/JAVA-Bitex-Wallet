package com.udun_demo.support.enums;

import lombok.Getter;

@Getter
public enum UserBalanceStatusEnum {

    NOT_SHOW(0,"不显示"),
    SHOW(1,"显示");



    private int code;
    private String msg;

    UserBalanceStatusEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
