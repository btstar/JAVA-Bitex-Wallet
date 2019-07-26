package com.udun_demo.support.dto.wallet;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CheckAddressValidDto {
    /**
     * 商户号
     */
    private String merchantId;

    /**
     * 币种类型（主币种）
     */
    private String coinType;

    /**
     * 需要校验的地址
     */
    private String address;
}
