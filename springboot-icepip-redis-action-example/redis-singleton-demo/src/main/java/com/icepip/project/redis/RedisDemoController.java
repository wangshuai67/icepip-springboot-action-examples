package com.icepip.project.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/testRedis")
public class RedisDemoController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @PostMapping("/data")
    public String processData(@RequestBody String data) {
        // 处理接收到的数据
        return "Data received: " + data;
    }

    @PutMapping("/update/{id}")
    public String updateData(@PathVariable("id") Long id, @RequestBody String data) {
        // 根据ID更新数据
        return "Data updated for ID " + id + ": " + data;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteData(@PathVariable("id") Long id) {
        // 根据ID删除数据
        return "Data deleted for ID " + id;
    }
}