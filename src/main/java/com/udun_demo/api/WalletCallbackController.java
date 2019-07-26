package com.udun_demo.api;

import com.alibaba.fastjson.JSON;
import com.udun_demo.service.IUserBalanceService;
import com.udun_demo.service.IWithdrawAuditService;
import com.udun_demo.support.common.CommonException;
import com.udun_demo.support.common.Response;
import com.udun_demo.support.dto.CallBackBody;
import com.udun_demo.support.enums.CallBackBodyStatusEnum;
import com.udun_demo.support.utils.GlobalPropertiesGetter;
import com.udun_demo.support.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping("/wallet")
@RestController
@Slf4j
public class WalletCallbackController {
    private final IWithdrawAuditService withdrawAuditService;
    private final IUserBalanceService userBalanceService;
    private final GlobalPropertiesGetter propertiesGetter;

    public WalletCallbackController(IWithdrawAuditService withdrawAuditService, IUserBalanceService userBalanceService, GlobalPropertiesGetter propertiesGetter) {
        this.withdrawAuditService = withdrawAuditService;
        this.userBalanceService = userBalanceService;
        this.propertiesGetter = propertiesGetter;
    }


    @PostMapping(value = "/callback", produces = "application/json; charset=utf-8")
    public String callBack(@RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("body") String body,
                           @RequestParam("sign") String sign) {
        // 校验签名
        try {
            SignUtil.sign(sign, timestamp, nonce, body);
        } catch (Exception e) {
            log.warn("签名校验到不合法回调数据，timestamp:{},nonce：{},body:{},sign：{}", timestamp,nonce,body,sign);
            throw new CommonException(Response.SIGN_CHECK_ERROR);
        }
        CallBackBody callBackBody;
        try {
            callBackBody = JSON.parseObject(body, CallBackBody.class);
        }catch (Exception e){
            log.warn("回调JSON：{}解析失败", body);
            throw new CommonException(Response.CALLBACK_PARSE_JSON_ERROR);
        }
        String amountStr = callBackBody.getAmount();
        String decimals = callBackBody.getDecimals();
        BigDecimal amount = BigDecimal.valueOf(Double.valueOf(amountStr)).divide(new BigDecimal(10).pow(Integer.valueOf(decimals)));
        String businessId = callBackBody.getBusinessId();
        String txid = callBackBody.getTxId();
        String memo = callBackBody.getMemo();
        if (callBackBody.getTradeType().equals(2)) {// 提款回调
            log.info("收到提币回调[{}]",body);
            if (callBackBody.getStatus().equals(CallBackBodyStatusEnum.AUDIT_SUCCESS.getCode())) { // 审核通过 =>> 支付成功
                withdrawAuditService.paySuccess(businessId);
            }else if (callBackBody.getStatus().equals(CallBackBodyStatusEnum.AUDIT_REFUSED.getCode())) { // 审核驳回 =>> 支付失败
                withdrawAuditService.payRefused(businessId);
            }else if (callBackBody.getStatus().equals(CallBackBodyStatusEnum.COMPLETED.getCode())) { // 交易完成
                withdrawAuditService.competed(businessId,txid);
            }else if (callBackBody.getStatus().equals(CallBackBodyStatusEnum.TRADE_FAIL.getCode())) { // 交易失败
                withdrawAuditService.tradeFail(businessId);
            }else{
                log.error("提币回调状态[{}],找不到对应的操作",callBackBody.getStatus());
            }
        }else { // 充币回调
            if (callBackBody.getStatus().equals(CallBackBodyStatusEnum.COMPLETED.getCode())) {
                log.info("收到充币成功回调[{}]",body);
                userBalanceService.depositCallbackHandle(callBackBody.getAddress(),
                        callBackBody.getMainCoinType(),callBackBody.getCoinType(),
                        amount,businessId,txid,memo);
            }else{
                log.warn("收到充币失败回调[{}]",body);
            }
        }
        return "OK";
    }
}
