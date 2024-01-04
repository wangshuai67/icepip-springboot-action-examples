package com.icepip.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

//    @Autowired
    private ServiceProviderFeignClient serviceProviderFeignClient;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate  stringRedisTemplate;
    @Autowired
    @Qualifier("database0RedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate0;

    @Autowired
    @Qualifier("database1RedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate1;

    @Autowired
    @Qualifier("database0StringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate0;

    @Autowired
    @Qualifier("database1StringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate1;

    @GetMapping("/call")
    public String call() {
        return serviceProviderFeignClient.hello("didi");
    }

    @GetMapping("/save")
    public String save() {
        return serviceProviderFeignClient.save(new User("zhangsan", 18));
    }
}
