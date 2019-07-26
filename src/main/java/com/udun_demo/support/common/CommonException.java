package com.udun_demo.support.common;

import lombok.Data;

@Data
public class CommonException extends RuntimeException{

    private int code;

    public CommonException(){

    }

    public CommonException(int code, String message){
        super(message,null);
        this.code = code;
    }

    public CommonException(ResponseBean responseBean){
        this(responseBean.getCode(),responseBean.getMessage());
    }

}
