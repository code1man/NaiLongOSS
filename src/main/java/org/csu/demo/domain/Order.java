package org.csu.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@TableName("`order`")
public class Order {
    //订单号
    @TableId(value = "order_id", type = IdType.INPUT)
    private String order_id;
    //下单用户ID
    private int client;
    //收货地址ID
    private int address_id;
    //商品id
    private int item_id;
    //商品数量
    private int amount;
    //订单总金额
    private int total_price;
    //供应商ID
    private int supplier;
    //订单状态(0未支付，1已支付，2已发货，3已收货，4异常)
    private int status;
    //订单创建时间
    private Date create_time;
    //支付时间
    private Date pay_time;
    //支付方式
    private String pay_method;
    //发货时间
    private Date ship_time;
    //确认收货时间
    private Date confirm_time;
    //申请售后时间
    private Date after_sale_time;
    //备注
    private String remark;

}
