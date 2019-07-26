package com.udun_demo.service.wallet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.udun_demo.support.common.CommonException;
import com.udun_demo.support.common.MessageResult;
import com.udun_demo.support.common.Response;
import com.udun_demo.support.constant.WalletAPI;
import com.udun_demo.support.dto.wallet.*;
import com.udun_demo.support.utils.GlobalPropertiesGetter;
import com.udun_demo.support.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WalletServiceImpl implements IWalletService {

    private GlobalPropertiesGetter globalPropertiesGetter;

    @Autowired
    public WalletServiceImpl(GlobalPropertiesGetter globalPropertiesGetter) {
        this.globalPropertiesGetter = globalPropertiesGetter;
    }

    /**
     * 向网关发送请求生成地址，注意传递的参数对象属性有：
     * merchantId：开发者商户的的商户号
     * coinType：币种类型（获取地址，只能获取主币种的地址，如果需要代币地址，请先查看自己本地是否已经生成了该代币的主币种，
     *                    如果生成了，使用主币种的地址，如果没有，请先获取主币种的地址）
     * callUrl：回调URL （在获取地址的时候，传递回调URL的目的在于，网关收到您申请的地址有收到充币信息，会通过该地址给您发送请求，让你修改您系统资金信息）
     *          在该示例中通过配置的方式，读取配置的回调URL数据。
     *
     * 注意，本对象中所有的方法方法还需要配置商户的APIkey属性，通过APIkey对数据进行加密，然后发送给网关，网关进行解密，保证数据发送的安全。
     * 所以在回调处理的时候，我们也要进行通过APIkey进行解密，防止非法的回调，错误引导您修改你系统的资金数据
     * @param generateAddressDto
     * @return
     */
    @Override
    public String generateAddress(GenerateAddressDto generateAddressDto) {
        String url = globalPropertiesGetter.getGatewayHost() + WalletAPI.GENERATE_ADDRESS;
        JSONObject param = (JSONObject) JSON.toJSON(generateAddressDto);
        List<JSONObject> params = new ArrayList<>();
        params.add(param);
        try {
            MessageResult result = HttpUtils.sendPost(url, HttpUtils.wrapperParams(globalPropertiesGetter.getApiKey(), JSONObject.toJSONString(params)));
            if (result.getCode() == 200){
                return ((JSONObject)result.getData()).getString("address");
            }else {
                /*
                 * 其他返回码 以及解释
                 * 4005非法参数，数组参数为空，或者数组长度不等于1
                 * 4001网关商户不存在
                 * 4169商户被禁用了
                 * 4017没有指定钱包id的数据
                 * 4176暂无该币种公钥数据
                 * 4166商户没有配置套餐
                 * 4168商户配置的套餐、地址达到上限
                 * 4045币种cppcode为空
                 * -1获取地址失败
                 */
                 log.error("向网关获取地址失败，返回值{}", result);
                 throw new CommonException(Response.GENERATE_ADDRESS_ERROR);
            }
        } catch (Exception e) {
            throw new CommonException(Response.GENERATE_ADDRESS_ERROR);
        }
    }

    /**
     * 获取开发者商户支持的所有币种：需要的参数
     *  merchantId: 开发者商户
     *  showBalance: 是否查询余额信息（在该系统中一般不需要）
     *  注意其他参数：参见生成地址接口注释
     * @param gainSupportCoinDto
     * @return
     */
    @Override
    public List<SupportCoinDto> getSupportCoins(GainSupportCoinDto gainSupportCoinDto) {
        String url = globalPropertiesGetter.getGatewayHost() + WalletAPI.GET_SUPPORT_COINS;
        JSONObject param = (JSONObject) JSON.toJSON(gainSupportCoinDto);
        try {
            MessageResult result = HttpUtils.sendPost(url, HttpUtils.wrapperParams(globalPropertiesGetter.getApiKey(), JSONObject.toJSONString(param)));
            if (result.getCode() == 200){
                List<SupportCoinDto> supportCoins = JSONArray.parseArray(result.getData().toString(), SupportCoinDto.class);
                return supportCoins;
            }else {
                /*
                 * 其他返回码 以及解释
                 * 4001网关商户不存在在
                 * 4169商户被禁用了
                 */
                log.warn("向网关获取商户支持的币种失败，返回{}",result);
                throw new CommonException(Response.GET_SUPPORT_COIN_ERROR);
            }
        } catch (Exception e) {
            log.warn("向网关获取商户支持的币种发生异常");
            throw new CommonException(Response.GET_SUPPORT_COIN_ERROR);
        }
    }

    @Override
    public List<String> batchGenerateAddress(BatchGenerateAddressDto batchGenerateAddressDto) {
        String url = globalPropertiesGetter.getGatewayHost() + WalletAPI.BATCH_GENERATE_ADDRESS;
        JSONObject param = (JSONObject) JSON.toJSON(batchGenerateAddressDto);
        List<JSONObject> params = new ArrayList<>();
        params.add(param);
        // TODO 网关待实现
        return null;
    }

    /**
     * 检查地址的合法性，建议在关于地址的操作的时候进行地址校验。保证系统的更加安全。
     * 参数 ：
     * merchantId:开发者商户id
     * coinType:币种类型（主币种类型，注意参考地址生成接口注释，只有主币种才生成地址）
     * address: 需要校验的地址
     *
     * 注意其他参数：参见生成地址接口注释
     * @param checkAddressValidDto
     * @return
     */
    @Override
    public boolean checkAddressValid(CheckAddressValidDto checkAddressValidDto) {
        String url = globalPropertiesGetter.getGatewayHost() + WalletAPI.CHECK_ADDRESS_LEGAL;
        JSONObject param = (JSONObject) JSON.toJSON(checkAddressValidDto);
        try {
            MessageResult result = HttpUtils.sendPost(url, HttpUtils.wrapperParams(globalPropertiesGetter.getApiKey(), JSONObject.toJSONString(param)));
            if (result.getCode() == 200){
                return true;
            }else {
                /*
                 * 其他返回码 以及解释
                 * 4005非法参数传入的数组数据为空
                 * 4612签名错误签名验签发生异常
                 * 4163签名信息错误
                 * 4165签名不合法不合法签名
                 */
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 向网关发送提币申请：
     *      需要商户在客户端进行审核（审核通过或者驳回会通过传入的回调URL{这里使用配置文件中配置的URL}通知系统处理，审核通过会发送到链上确认），
     *      链上确认通过后也会给系统回调，以及成功等一系列通知，都会通过回调的方式给您返回。关于回调的细节，请参照 WalletCallBackController接口。
     *      建议使用流程：用户发送提币=> 发送到网关，发送成功 => 保存数据到本地服务器，等待回调，通过回调进行下一步处理
     *      address：提币的到账地址
     *      amount：提币数量
     *      merchantId：开发者商户
     *      mainCoinType：主币种类型
     *      coinType：币种类型
     *      fee：网关没有使用，兼容老版本
     *      businessId：您系统中生成的业务id,需要您保证在您系统中，该值唯一，在提币回调的时候会给你返回，通过该属性，您需要对你业务进行处理
     *      callUrl：回调的url,下面示例中，使用全局配置的方式
     *      memo：备注，在特殊币种的处理时，需要使用该字段。
     *
     * 注意其他参数：参见生成地址接口注释
     * @param withdrawDto
     * @return
     */
    @Override
    public boolean withdraw(WithdrawDto withdrawDto) {
        String url = globalPropertiesGetter.getGatewayHost() + WalletAPI.WITHDRAW;
        JSONObject param = (JSONObject) JSON.toJSON(withdrawDto);
        List<JSONObject> params = new ArrayList<>();
        params.add(param);
        try {
            MessageResult result = HttpUtils.sendPost(url, HttpUtils.wrapperParams(globalPropertiesGetter.getApiKey(), JSONObject.toJSONString(params)));
            if (result.getCode() == 200) {
                return true;
            }else{
                /*
                 * 其他错误码 以及解释
                 * 4005非法参数传入的数组数据为空
                 * 4183到账地址异常
                 * 4034币种精度为空 保存提币申请时，查询币种的精度返回值为空
                 */
                log.warn("发送提币返回码{},返回消息{}", result.getCode(),result.getMessage());
            }
        } catch (Exception e) {
            log.error("发送提币异常：{}",e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 向网关发送提币（不需要审核）：与上面接口的区别在于，如果商户开启了代付，就不需要商户审核，直接发送到链上
     * 参数：参考withdraw（）
     * @param autoWithdrawDto
     * @return
     */
    @Override
    public boolean autoWithdraw(AutoWithdrawDto autoWithdrawDto) {
        String url = globalPropertiesGetter.getGatewayHost() + WalletAPI.AUTO_WITHDRAW;
        JSONObject param = (JSONObject) JSON.toJSON(autoWithdrawDto);
        List<JSONObject> params = new ArrayList<>();
        params.add(param);
        try {
            MessageResult result = HttpUtils.sendPost(url, HttpUtils.wrapperParams(globalPropertiesGetter.getApiKey(), JSONObject.toJSONString(params)));
            if (result.getCode() == 200) {
                return true;
            }
            /*
             * 其他错误码 以及解释
             * 4166商户没有配置套餐
             * 4169商户被禁用了
             * 4612签名错误签名验签发生异常
             * 4163签名信息错误
             * 596无效的地址
             * 571已存在审核记录,将不再进行处理
             * 581提币金额为空，或者提币数量为负数
             * 554商户不支持该币种
             */
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
