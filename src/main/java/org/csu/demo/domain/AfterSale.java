package org.csu.demo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("after_sale")
@AllArgsConstructor
public class AfterSale {
    @TableId(value = "order_id")  // 指定 order_id 为表的主键
    private String orderId;  // 使用 Java 规范的驼峰命名
    private int after_sale_status;
    private int business_solve;
    private int admin_solve;
    private Date business_solve_time;
    private Date admin_solve_time;
}
