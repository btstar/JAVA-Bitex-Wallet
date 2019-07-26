package com.udun_demo.support.dto.wallet;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class WithdrawDto {
    /**
     * 提币地址
     */
    private String address;
    /**
     * 提币金额
     */
    private String amount;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 主币种类型
     */
    private String mainCoinType;
    /**
     * 币种类型
     */
    private String coinType;
    /**
     * 手续费
     */
    private String fee;
    /**
     * 业务id
     */
    private String  businessId;

    private String callUrl;

    private String memo;
}
