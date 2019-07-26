package com.udun_demo.support.dto.wallet;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class GenerateAddressDto {
    /**商户id*/
    private String merchantId;
    /**币种*/
    private Integer coinType;
    /**回调地址*/
    private String callUrl;
}
