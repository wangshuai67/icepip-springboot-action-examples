package com.freezepin.mybatisplusdemo.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.freezepin.mybatisplusdemo.model.OrderInfo;

import java.io.Serializable;

public interface OrderService extends IService<OrderInfo> {

    @Override
    boolean save(OrderInfo entity);

    @Override
    boolean removeById(Serializable id);

    @Override
    boolean updateById(OrderInfo entity);

    IPage<OrderInfo> page(Page<?> page, OrderInfo orderInfo);

}
