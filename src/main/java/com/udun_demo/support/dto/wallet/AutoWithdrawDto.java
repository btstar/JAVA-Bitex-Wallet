package com.udun_demo.support.dto.wallet;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AutoWithdrawDto {
    /**
     * 转入地址
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
     * 币种类型
     */
    private String coinType;
    /**
     * 主币种类型
     */
    private String mainCoinType;
    /**
     * 回调地址
     */
    private String callUrl;
    /**
     * 业务id
     */
    private String businessId;
}
