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
@TableName("udun_withdraw_audit")
public class WithdrawAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
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
     * 到账地址
     */
    @TableField("address")
    private String address;

    /**
     * 提币金额
     */
    @TableField("withdraw_amount")
    private BigDecimal withdrawAmount;
    /**
     * 状态: 0 待审核  1： 待支付 2：待链上确认 3：完成 4：取消 5：失败 6：审核被拒绝 7：支付拒绝
     */
    private Integer status;
    /**
     * 资金
     */
    @TableField("fee_amount")
    private BigDecimal feeAmount;

    /**
     * 备注
     */
    private String note;


    @TableField("business_id")
    private String businessId;
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
