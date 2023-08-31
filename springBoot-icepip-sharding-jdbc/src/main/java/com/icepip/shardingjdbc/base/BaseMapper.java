package com.icepip.shardingjdbc.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    IPage<T> selectAllByCondition(Page<?> page, @Param("condition")Map<String, Object> condition);

    int deleteById(@Param("condition")Map<String, Object> condition);

}
