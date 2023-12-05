package com.freezepin.mybatisplusdemo.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freezepin.mybatisplusdemo.model.Vehicle;
import com.freezepin.mybatisplusdemo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("add")
    public boolean save(@RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(
            @PathVariable("id")
            Long id) {
        return vehicleService.removeById(id);
    }

    @PutMapping("/{id}")
    public boolean updateById(
            @PathVariable("id")
            Long id,
            @RequestBody Vehicle vehicle) {
        vehicle.setId(id);
        return vehicleService.updateById(vehicle);
    }

    @GetMapping("/page")
    public IPage page(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    Integer pageSize,
            @RequestParam(name = "id", required = false)
            Long id,
            @RequestParam(name = "vehicleNo", required = false)
            String vehicleNo) {
        Page<Vehicle> page = new Page<>(pageNum,pageSize);
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNo(vehicleNo);
        vehicle.setId(id);
        return vehicleService.page(page, vehicle);
    }
}
