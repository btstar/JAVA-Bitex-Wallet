package com.udun_demo.service.wallet;

import com.udun_demo.support.dto.wallet.*;

import java.util.List;

public interface IWalletService {

    /**
     * 获取支持的币种
     */
    List<SupportCoinDto> getSupportCoins(GainSupportCoinDto gainSupportCoinDto);

    /**
     * 生成地址
     * @param generateAddressDto
     * @return
     */
    String generateAddress(GenerateAddressDto generateAddressDto);

    /**
     * 批量生成地址
     * @param batchGenerateAddressDto
     * @return
     */
    List<String> batchGenerateAddress(BatchGenerateAddressDto batchGenerateAddressDto);

    /**
     * 校验地址
     * @param checkAddressValidDto
     * @return
     */
    boolean checkAddressValid(CheckAddressValidDto checkAddressValidDto);

    /**
     * 提币到钱包等待钱包审核
     * @return  成功返回 true  失败返回 false
     */
    boolean withdraw(WithdrawDto withdrawDto);

    /**
     * 提币到钱包钱包自动审核
     */
    boolean autoWithdraw(AutoWithdrawDto autoWithdrawDto);
}
