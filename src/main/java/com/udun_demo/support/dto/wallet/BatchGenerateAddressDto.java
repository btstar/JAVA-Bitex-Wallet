package com.udun_demo.support.dto.wallet;

import lombok.Data;

@Data
public class BatchGenerateAddressDto extends GenerateAddressDto{
    private Integer count;
}
