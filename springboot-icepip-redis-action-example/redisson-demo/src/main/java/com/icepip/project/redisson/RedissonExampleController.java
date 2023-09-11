package com.icepip.project.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redisson 一些简单特性验证
 * 以及实现 hash 数据结构 的field 过期的方法
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/11 11:15
 */
@Slf4j
@RestController
@RequestMapping("/redisson")
public class RedissonExampleController {


    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedissonHashFieldExpireHandler hashFieldExpireHandler;


    /**
     * 第一种实现 hash field 过期的方法
     * hash Field 设置过期时间实现方式1
     * 额外定义String 类型的 一个key 设置过期时间，key的值为 "hash:field:expire" + field
     *
     * @return
     */
    @GetMapping("/hashFieldExpire")
    public RMap<String, String> hashFieldExpire() {
        // 设置Hash的key和value
        RMap<String, String> map = redissonClient.getMap("UserInfo");
        String field = UUID.randomUUID().toString();
        map.put(field, "value");
        // 对于相应的field设置过期时间
        String keyPrefix = "hash:field:expire:";
        RBucket<String> bucket = redissonClient.getBucket(keyPrefix + field);
        bucket.set(field, 30, TimeUnit.SECONDS);

        bucket.addListener((ExpiredObjectListener) key -> {
            String fieldOld = key.replace(keyPrefix, "");
            System.out.println("过期键 key=" + fieldOld);
            System.out.println("删除hash数据field=" + map.remove(fieldOld));

        });

        return map;
    }


    /**
     * 第2种实现 hash field 过期的方法
     *  利用Redisson 的延迟队列
     * @return
     */
    @GetMapping("/hashFieldExpire2")
    public RMap<String, String> hashFieldExpire2() {
        // 设置Hash的key和value
        RMap<String, String> map = redissonClient.getMap("UserInfo");
        String field = UUID.randomUUID().toString();
        map.put(field, "value");
        RBlockingQueue<String> blockingDestinationQueue = redissonClient.getBlockingQueue("destinationQueue");
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDestinationQueue);
        // 将元素 "field" 添加到延时队列中，并设定 10 秒后才能取到
        delayedQueue.offer(field, 10, TimeUnit.SECONDS);
        // TODO 为了演示我们使用这种方式 ，实际使用过程应该使用线程池创建线程，并且线程中处理数据可以多线程操控
        new Thread(() -> {
            String item = null;
            while (true) {
                try {
                    item = blockingDestinationQueue.take();
                    System.out.println("过期键 key=" + item);
                    System.out.println("删除hash数据field=" + map.remove(item));
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (Exception e) {
                    log.error("hash过期field清理异常");
                }
            }
        }).start();

        return map;
    }


    /**
     * hash Field 设置过期时间实现方式1
     *
     * @return
     */
    @GetMapping("/hashFieldExpire3")
    public RMap<String, String> hashFieldExpire3() {
        // 设置Hash的key和value
        RMap<String, String> map = redissonClient.getMap("UserInfo");
        String field = UUID.randomUUID().toString();
        map.put(field, "value");
        // 将元素 "field" 添加到延时队列中，并设定 10 秒后才能取到
        hashFieldExpireHandler.offer(map, field, 10, TimeUnit.SECONDS);
        return map;
    }


    @PostMapping("/addToList")
    public void addToList(@RequestParam String element) {
        RList<String> list = redissonClient.getList("myList");
        list.add(element);
    }

    @GetMapping("/readFromList")
    public RList<String> readFromList() {
        return redissonClient.getList("myList");
    }

    @PostMapping("/addToSet")
    public void addToSet(@RequestParam String element) {
        RSet<String> set = redissonClient.getSet("mySet");
        set.add(element);
    }

    @GetMapping("/readFromSet")
    public RSet<String> readFromSet() {
        return redissonClient.getSet("mySet");
    }

    // 新增元素至有序集合
    @PostMapping("/addToZSet")
    public void addToZSet(@RequestParam String element, @RequestParam double score) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet("myZset");
        sortedSet.add(score, element);
    }

    // 读取有序集合
    @GetMapping("/readFromZSet")
    public RScoredSortedSet<String> readFromZSet() {
        return redissonClient.getScoredSortedSet("myZset");
    }

    // 新增键值对至哈希
    @PostMapping("/addToHash")
    public void addToHash(@RequestParam String key, @RequestParam String value) {
        RMap<String, String> map = redissonClient.getMap("myHash");
        map.put(key, value);
    }

    // 读取哈希
    @GetMapping("/readFromHash")
    public RMap<String, String> readFromHash() {
        return redissonClient.getMap("myHash");

    }    // 读取哈希


}
