package com.udun_demo.support.dto.wallet;

import lombok.Data;

@Data
public class GainSupportCoinDto {
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 是否显示余额，true 显示 、false 不显示
     */
    private boolean showBalance;

}
