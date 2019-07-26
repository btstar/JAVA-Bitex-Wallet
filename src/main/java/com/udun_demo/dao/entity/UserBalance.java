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
@TableName("udun_user_balance")
public class UserBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "balance_id",type = IdType.AUTO)
    private Long balanceId;

    /**
     * 用户名
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
     * 地址
     */
    private String address;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 冻结资金
     */
    @TableField("frozen_balance")
    private BigDecimal frozenBalance;

    /**
     * 是否可以展示 0:不展示，1:展示
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

}
