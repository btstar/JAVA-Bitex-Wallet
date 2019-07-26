package com.udun_demo.dao.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

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
@Data
@TableName("udun_coin")
public class Coin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "coin_id",type = IdType.AUTO )
    private Long coinId;

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
     * 币种名称
     */
    @TableField("coin_name")
    private String coinName;

    /**
     * logo地址
     */
    private String logo;


    /**
     * 币种全称
     */
    @TableField("full_name")
    private String fullName;
    /**
     * 单位
     */
    private String symbol;
    /**
     * 精度
     */
    private Integer decimal;
    /**
     * 主地址
     */
    @TableField("master_address")
    private String masterAddress;
    /**
     * 最小提币金额
     */
    @TableField("min_withdraw_amount")
    private BigDecimal minWithdrawAmount;
    /**
     * 最大提币金额
     */
    @TableField("max_withdraw_amount")
    private BigDecimal maxWithdrawAmount;
    /**
     * 最小充币金额
     */
    @TableField("min_deposit_amount")
    private BigDecimal minDepositAmount;
    /**
     * 基础费用
     */
    @TableField("fee_amount")
    private BigDecimal feeAmount;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

}
