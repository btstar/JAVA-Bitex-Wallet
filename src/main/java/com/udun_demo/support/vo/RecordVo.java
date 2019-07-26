package com.udun_demo.support.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RecordVo {
    @ApiModelProperty(value = "主币种类型")
    private String mainCoinType;
    @ApiModelProperty(value = "币种类型")
    private String coinType;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "交易数量")
    private BigDecimal tradeAmount;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "d地址")
    private String address;

    @ApiModelProperty
    private BigDecimal feeAmount;
//    /**
//     * 类型  提币：1  充币：2
//     */
//    @ApiModelProperty(value = "类型  提币：1  充币：2")
//    private Integer type;
    @ApiModelProperty(value = "txid")
    private String txid;
    @ApiModelProperty(value = "备注")
    private String note;

    private String coinName;


    public Long getUpdateTime(){
        return updateTime != null ? updateTime.getTime() : null;
    }

    public Long getCreateTime(){
        return createTime != null ? createTime.getTime() : null;
    }


}
