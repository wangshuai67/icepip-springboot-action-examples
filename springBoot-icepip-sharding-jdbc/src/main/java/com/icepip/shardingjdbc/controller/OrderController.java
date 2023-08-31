package com.icepip.shardingjdbc.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icepip.shardingjdbc.model.OrderInfo;
import com.icepip.shardingjdbc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("add")
    public boolean save(@RequestBody OrderInfo orderInfo) {
        return orderService.save(orderInfo);
    }

    @DeleteMapping("/{orderId}")
    public boolean deleteById(
            @PathVariable("orderId")
            Long id) {
        return orderService.removeById(id);
    }

    @PutMapping("/{orderId}")
    public boolean updateById(
            @PathVariable("orderId")
            Long orderId,
            @RequestBody OrderInfo orderInfo) {
        orderInfo.setOrderId(orderId);
        return orderService.updateById(orderInfo);
    }

    @GetMapping("/page")
    public IPage page(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    Integer pageSize,
            @RequestParam(name = "orderId", required = false)
            Long orderId,
            @RequestParam(name = "orderStatus", required = false)
            Integer orderStatus,
            @RequestParam(name = "orderName", required = false)
            String orderName) {
        Page<OrderInfo> page = new Page<>(pageNum,pageSize);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setOrderName(orderName);
        orderInfo.setOrderStatus(orderStatus);
        return orderService.page(page, orderInfo);
    }
}
