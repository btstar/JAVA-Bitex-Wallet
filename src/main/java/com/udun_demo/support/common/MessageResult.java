package com.udun_demo.support.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class MessageResult {

    private int code;
    private String message;
    private Object data;

    public MessageResult() {
    }

    public MessageResult(int code , String msg){
        this.code = code;
        this.message = msg;
    }

    public static MessageResult success(Object data) {
        MessageResult messageResult = new MessageResult(200, getSuccessDefaultMsg());
        messageResult.setData(data);
        return messageResult;
    }

    private static String getSuccessDefaultMsg() {
        return "SUCCESS";
    }

    public static MessageResult success(){
        return new MessageResult(200,"SUCCESS");
    }


}