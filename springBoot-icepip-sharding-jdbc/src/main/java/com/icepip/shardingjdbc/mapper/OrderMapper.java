package com.icepip.shardingjdbc.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icepip.shardingjdbc.base.BaseMapper;
import com.icepip.shardingjdbc.model.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {

    IPage<OrderInfo> selectAllByCondition(Page<?> page, @Param("condition") Map<String, Object> condition);

    int deleteById(@Param("condition")Map<String, Object> condition);

}
