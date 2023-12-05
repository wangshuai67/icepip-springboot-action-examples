package com.freezepin.mybatisplusdemo.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.freezepin.mybatisplusdemo.model.Vehicle;

import java.io.Serializable;

public interface VehicleService extends IService<Vehicle> {

    @Override
    boolean save(Vehicle entity);

    @Override
    boolean removeById(Serializable id);

    @Override
    boolean updateById(Vehicle entity);

    IPage<Vehicle> page(Page<?> page, Vehicle entity);

}
