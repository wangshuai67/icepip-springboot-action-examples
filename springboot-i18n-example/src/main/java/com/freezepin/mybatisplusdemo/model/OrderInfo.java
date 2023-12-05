package com.freezepin.mybatisplusdemo.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_order")
public class OrderInfo {

    @TableId(value = "order_id")
    private Long orderId;

    @TableField(value = "order_name")
    private String orderName;

    @TableField(value = "order_status")
    private Integer orderStatus;

    @TableField(value = "user_id")
    private Long userId;

}
