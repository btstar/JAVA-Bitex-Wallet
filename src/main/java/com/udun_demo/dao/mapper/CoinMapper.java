package com.udun_demo.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.udun_demo.dao.entity.Coin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
@Mapper
@Repository
public interface CoinMapper extends BaseMapper<Coin> {

    @Results(id = "CoinMap",value = {
            @Result(column = "coin_id",property = "coinId"),
            @Result(column = "coin_type",property = "coinType"),
            @Result(column = "main_coin_type",property = "mainCoinType"),
            @Result(column = "coin_name",property = "coinName"),
            @Result(column = "master_address",property = "masterAddress"),
            @Result(column = "min_withdraw_amount",property = "minWithdrawAmount"),
            @Result(column = "max_withdraw_amount",property = "maxWithdrawAmount"),
            @Result(column = "min_deposit_amount",property = "minDepositAmount"),
            @Result(column = "fee_amount",property = "feeAmount"),
            @Result(column = "create_time",property = "createTime")
    })
    @Select("SELECT b.*" +
            " FROM udun_user_balance AS a " +
            " LEFT JOIN udun_coin AS b ON(a.coin_type = b.coin_type AND a.main_coin_type = b.main_coin_type) " +
            " WHERE a.username =  #{username}")
    List<Coin> queryUserSupportCoins(@Param("username") String username);
}
