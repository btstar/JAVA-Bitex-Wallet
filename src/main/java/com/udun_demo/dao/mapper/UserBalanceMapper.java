package com.udun_demo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.udun_demo.dao.entity.UserBalance;
import com.udun_demo.support.vo.CoinsBalanceVo;
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
public interface UserBalanceMapper extends BaseMapper<UserBalance> {

    @Results(id = "balanceUniCoinMap" , value = {
            // coin map
            @Result(column = "master_address",property = "masterAddress"),
            @Result(column = "min_withdraw_amount",property = "minWithdrawAmount"),
            @Result(column = "max_withdraw_amount",property = "maxWithdrawAmount"),
            @Result(column = "min_deposit_amount",property = "minDepositAmount"),
            @Result(column = "fee_amount",property = "feeAmount"),
            @Result(column = "coin_name",property = "coinName"),
            @Result(column = "full_name",property = "fullName"),
            @Result(column = "symbol",property = "symbol"),
            @Result(column = "decimal",property = "decimal"),
            @Result(column = "logo",property = "logo"),
            // user balance map
            @Result(column = "username",property = "username"),
            @Result(column = "address",property = "address"),
            @Result(column = "balance",property = "balance"),
            @Result(column = "frozen_balance",property = "frozenBalance"),
            // 公共的字段
            @Result(column = "coin_type",property = "coinType"),
            @Result(column = "main_coin_type",property = "mainCoinType"),
    })
    @Select({" SELECT u.username,u.coin_type,u.main_coin_type,c.coin_name,c.full_name,c.logo,c.symbol,c.decimal,u.address,u.balance,u.frozen_balance " +
            " FROM udun_user_balance AS u LEFT JOIN udun_coin AS c ON(u.main_coin_type = c.main_coin_type AND u.coin_type = c.coin_type) " +
            " WHERE username = #{username} and u.status = #{status}",})
    List<CoinsBalanceVo> queryCoinsBalance(String username, int status);
}
