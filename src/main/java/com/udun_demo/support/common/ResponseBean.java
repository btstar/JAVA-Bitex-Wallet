package com.udun_demo.support.common;

import lombok.Data;

@Data
public class ResponseBean {

    private int code;

    private String message;

    public ResponseBean(){
        super();
    }


    public ResponseBean(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
