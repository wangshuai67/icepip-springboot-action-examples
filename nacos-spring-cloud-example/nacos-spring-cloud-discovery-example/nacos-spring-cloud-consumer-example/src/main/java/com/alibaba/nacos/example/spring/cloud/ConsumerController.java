package com.alibaba.nacos.example.spring.cloud;

import com.alibaba.nacos.example.spring.cloud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Autowired
    private ServiceProviderFeignClient serviceProviderFeignClient;

    @GetMapping("/call")
    public String call() {
        return serviceProviderFeignClient.hello("didi");
    }

    @GetMapping("/save")
    public String save() {
        return serviceProviderFeignClient.save(new User("zhangsan", 18));
    }
}
