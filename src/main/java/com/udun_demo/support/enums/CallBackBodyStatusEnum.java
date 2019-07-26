package com.udun_demo.support.enums;

import lombok.Getter;

@Getter
public enum CallBackBodyStatusEnum {

    WAIT_AUDIT(0,"待审核"),
    AUDIT_SUCCESS(1,"审核成功"),
    AUDIT_REFUSED(2,"审核驳回"),
    COMPLETED(3,"交易成功"),
    TRADE_FAIL(4,"交易失败");

    private int code;
    private String msg;

    CallBackBodyStatusEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
