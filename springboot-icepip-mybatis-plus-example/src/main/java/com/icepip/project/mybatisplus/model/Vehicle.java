package com.icepip.project.mybatisplus.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 车辆
 */
@Data
@TableName("t_vehicle")
public class Vehicle {
    private Long id;
    private String vehicleNo;
}
