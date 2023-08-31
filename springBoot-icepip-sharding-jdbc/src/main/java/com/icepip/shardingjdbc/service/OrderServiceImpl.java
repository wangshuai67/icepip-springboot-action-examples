package com.icepip.shardingjdbc.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icepip.shardingjdbc.mapper.OrderMapper;
import com.icepip.shardingjdbc.model.OrderInfo;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {



    @Override
    public boolean save(OrderInfo entity) {
        return super.save(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean updateById(OrderInfo entity) {
        return super.updateById(entity);
    }


    @Override
    public IPage<OrderInfo> page(Page<?> page, OrderInfo orderInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", orderInfo.getUserId());
        map.put("orderId", orderInfo.getOrderId());
        map.put("orderName", orderInfo.getOrderName());
        map.put("orderStatus", orderInfo.getOrderStatus());
        page.addOrder(new OrderItem("order_id",false));
        return super.baseMapper.selectAllByCondition(page, map);
    }
}
