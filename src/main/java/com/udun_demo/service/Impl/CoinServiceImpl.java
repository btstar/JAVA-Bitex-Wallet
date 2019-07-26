package com.udun_demo.service.Impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.udun_demo.dao.entity.Coin;
import com.udun_demo.dao.mapper.CoinMapper;
import com.udun_demo.service.ICoinService;
import com.udun_demo.service.wallet.IWalletService;
import com.udun_demo.support.common.CommonException;
import com.udun_demo.support.common.Response;
import com.udun_demo.support.dto.wallet.GainSupportCoinDto;
import com.udun_demo.support.dto.wallet.SupportCoinDto;
import com.udun_demo.support.utils.GlobalPropertiesGetter;
import com.udun_demo.support.vo.CoinListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
@Slf4j
@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements ICoinService {

    @Autowired
    private CoinMapper coinMapper;
    @Autowired
    private GlobalPropertiesGetter propertiesGetter;
    @Autowired
    private IWalletService walletService;

    @Override
    public List<CoinListVo> queryAll() {
        List<CoinListVo> coinListVos = new ArrayList<>();
        List<Coin> coins = this.selectList(null);
        for (Coin coin : coins) {
            CoinListVo coinListVo = new CoinListVo();
            BeanUtils.copyProperties(coin,coinListVo);
            coinListVos.add(coinListVo);
        }
        return  coinListVos;
    }

    @Override
    public void initCoins() {
        GainSupportCoinDto gainSupportCoinDto = new GainSupportCoinDto();
        gainSupportCoinDto.setMerchantId(propertiesGetter.getMerchantId());
        gainSupportCoinDto.setShowBalance(true);
        List<SupportCoinDto> supportCoins = walletService.getSupportCoins(gainSupportCoinDto);
        // 查询本地保存好的所有币种
        List<Coin> coins = selectList(null);
        List<Coin> saveCoins = new ArrayList<>();
        for (SupportCoinDto supportCoin : supportCoins) {
            boolean exist = false;
            for (Coin coin : coins) {
                if (coin.getMainCoinType().equals(supportCoin.getMainCoinType())
                        && coin.getCoinType().equals(supportCoin.getCoinType())){
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                Coin coin = new Coin();
                coin.setFullName(supportCoin.getCoinName());
                coin.setCoinName(supportCoin.getName());
                coin.setCoinType(supportCoin.getCoinType());
                coin.setMainCoinType(supportCoin.getMainCoinType());
                coin.setDecimal(supportCoin.getDecimals());
                coin.setCreateTime(new Date());
                coin.setSymbol(supportCoin.getSymbol());
                coin.setLogo(supportCoin.getLogo());
                coin.setFeeAmount(BigDecimal.ZERO);
                coin.setMinWithdrawAmount(BigDecimal.valueOf(-1));
                coin.setMaxWithdrawAmount(BigDecimal.valueOf(-1));
                coin.setMinDepositAmount(BigDecimal.valueOf(-1));
                saveCoins.add(coin);
            }
        }
        if (!CollectionUtils.isEmpty(saveCoins)) {
            boolean success = insertBatch(saveCoins);
            if (!success) {
                throw new CommonException(Response.INIT_COINS_ERROR);
            }
        }
    }

    @Override
    public Coin checkCoin(String mainCoinType, String coinType) {
        Coin coin = selectOne(uniqueCoinEw(mainCoinType, coinType));
        if (null == coin){
            log.warn("商户不支持该币种，主币种类型{}，币种类型{}", mainCoinType,coinType);
            throw new CommonException(Response.NO_SUPPORT_COIN);
        }
        return coin;
    }


    @Override
    public void checkCoinWithdrawAmount(String mainCoinType, String coinType, BigDecimal amountBig) {
        Coin coin = checkCoin(mainCoinType, coinType);
        BigDecimal minWithdrawAmount = coin.getMinWithdrawAmount();
        BigDecimal maxWithdrawAmount = coin.getMaxWithdrawAmount();
        if (minWithdrawAmount != null && minWithdrawAmount.compareTo(BigDecimal.valueOf(-1)) != 0){
            if (amountBig.compareTo(minWithdrawAmount) < 0) {
                throw new CommonException(Response.WITHDRAW_AMOUNT_TOO_SMALL);
            }
        }
        if (maxWithdrawAmount != null && maxWithdrawAmount.compareTo(BigDecimal.valueOf(-1)) != 0){
            if (amountBig.compareTo(maxWithdrawAmount) > 0) {
                throw new CommonException(Response.WITHDRAW_AMOUNT_TOO_LARGE);
            }
        }

//        Wrapper<Coin> ew = uniqueCoinEw(mainCoinType, coinType).le("min_withdraw_amount", amountBig).ge("max_withdraw_amount", amountBig);
//        if (selectCount(ew) < 1) {
//            log.warn("主币种类型{},币种类型{},提币{},不在设定的最小提币~最大币种之间", mainCoinType,coinType,amountBig.toPlainString());
//            throw new CommonException(Response.WITHDRAW_AMOUNT_TOO_LARGE_OR_SMALL);
//        }
    }

    private Wrapper<Coin> uniqueCoinEw(String mainCoinType, String coinType) {
        return new EntityWrapper<Coin>()
                .eq("main_coin_type", mainCoinType)
                .eq("coin_type", coinType);
    }
}
