package com.icepip.shardingjdbc.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icepip.shardingjdbc.mapper.VehicleMapper;
import com.icepip.shardingjdbc.model.Vehicle;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {


    @Override
    public boolean save(Vehicle entity) {
        return super.save(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean updateById(Vehicle entity) {
        return super.updateById(entity);
    }


    @Override
    public IPage<Vehicle> page(Page<?> page, Vehicle vehicle) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", vehicle.getId());
          map.put("vehicleNo", vehicle.getVehicleNo());

        return super.baseMapper.selectAllByCondition(page, map);
    }
}
