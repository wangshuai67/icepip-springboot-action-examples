package com.icepip.project;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 冰点
 * @Version v1.0.0
 * @Date 2023/11/30 14:34
 */
@FeignClient("springcloud-alibaba-nacos-demo3")
@Service
public interface ServiceProviderFeignClient {

    @GetMapping("/echo/{id}")
    String hello(@PathVariable("id") String id);

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestBody User user);

    @RequestMapping("/hello")
    public String hello();
}
