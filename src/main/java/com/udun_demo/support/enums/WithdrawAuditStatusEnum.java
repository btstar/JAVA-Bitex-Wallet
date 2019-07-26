package com.udun_demo.support.enums;

import lombok.Getter;

@Getter
public enum WithdrawAuditStatusEnum {

//    WAIT_AUDIT(0,"待审核"),
    WAIT_PAY(1,"等待支付"),
    WAIT_BLOCK_CONFIRM(2,"待链上确认"),
    COMPLETED(3,"完成"),
//    CANCELED(4,"取消"),
    FAILED(5,"失败"),
//    AUDIT_REFUSED(6,"审核拒绝"),
    PAY_REFUSED(7,"支付拒绝");

    private int code;
    private String msg;

    WithdrawAuditStatusEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
