package com.alibaba.nacos.example.spring.cloud;

import com.alibaba.nacos.example.spring.cloud.model.User;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 冰点
 * @Version v1.0.0
 * @Date 2023/11/30 15:17
 */
@RestController
public class EchoController {
    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string) {
        return "Hello Nacos Discovery " + string;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public User save(@RequestBody User user) {
        user.setName("Nacos-" + user.getName());
        return user;
    }
}
