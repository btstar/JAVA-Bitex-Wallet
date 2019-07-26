package com.udun_demo.dao.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
@Accessors(chain = true)
@Data
@TableName("udun_trade_record")
public class TradeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易id
     */
    @TableId(value = "trade_id", type = IdType.AUTO)
    private Long tradeId;
    /**
     * 用户id
     */
    @TableField("username")
    private String username;
    /**
     * 币种类型
     */
    @TableField("coin_type")
    private String coinType;
    /**
     * 主币种类型
     */
    @TableField("main_coin_type")
    private String mainCoinType;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 交易类型
     */
    @TableField("trade_type")
    private Integer tradeType;

    @TableField("txid")
    private String txid;
    /**
     * 业务id
     */
    @TableField("business_id")
    private String businessId;
    /**
     * 交易金额
     */
    @TableField("trade_amount")
    private BigDecimal tradeAmount;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}
