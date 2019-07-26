package com.udun_demo.service;

import com.baomidou.mybatisplus.service.IService;
import com.udun_demo.dao.entity.Coin;
import com.udun_demo.support.vo.CoinListVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
public interface ICoinService extends IService<Coin> {

    List<CoinListVo> queryAll();


    /**
     * 初始化系统支持币种
     *  查询网关，然后保存到本地
     */
    void initCoins();

    /**
     * 校验币种系统是否支持，不支持抛出异常
     * @param mainCoinType
     * @param coinType
     */
    Coin checkCoin(String mainCoinType, String coinType);

    /**
     * 校验提币数量
     * @param mainCoinType
     * @param coinType
     * @param amountBig
     */
    void checkCoinWithdrawAmount(String mainCoinType, String coinType, BigDecimal amountBig);
}
