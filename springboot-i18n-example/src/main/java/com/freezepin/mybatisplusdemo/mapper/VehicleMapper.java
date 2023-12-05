package com.freezepin.mybatisplusdemo.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freezepin.mybatisplusdemo.base.BaseMapper;
import com.freezepin.mybatisplusdemo.model.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {

    IPage<Vehicle> selectAllByCondition(Page<?> page, @Param("condition") Map<String, Object> condition);

    int deleteById(@Param("condition")Map<String, Object> condition);

}
