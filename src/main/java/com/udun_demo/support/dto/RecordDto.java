package com.udun_demo.support.dto;

import lombok.Data;

@Data
public class RecordDto extends CoinKeyDto{

    private String username;

    private Integer pageNo = 1;

    private Integer pageSize = 3;

}
