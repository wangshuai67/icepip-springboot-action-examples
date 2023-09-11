package com.icepip.project.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 定义一个 工具类专门用来处理程序中需要field 过期的hash 结构数据
 * @Author 冰点
 * @Version v1.0.0
 * @Date 2023/9/11 14:55
 */

@Component
@Slf4j
public class RedissonHashFieldExpireHandler {
    private static RBlockingQueue<String> blockingDestinationQueue = null;
    private static RDelayedQueue<String> delayedQueue = null;
    private static List<RMap<String, String>> hashList = new CopyOnWriteArrayList<>();

    @Autowired
    private RedissonClient redissonClient;

    public boolean put(RMap<String, String> key) {
        if (hashList.contains(key)) {
            return true;
        }
        return hashList.add(key);
    }

    public boolean remove(RMap<String, String> key) {
        return hashList.remove(key);
    }


    @PostConstruct
    public void initExpireHashFieldDelTask() {
        blockingDestinationQueue = redissonClient.getBlockingQueue("destinationQueue");
        delayedQueue = redissonClient.getDelayedQueue(blockingDestinationQueue);
        // TODO 为了演示我们使用这种方式 ，实际使用过程应该使用线程池创建线程，并且线程中处理数据可以多线程操控
        new Thread(() -> {
            String item = null;
            while (true) {
                try {
                    item = blockingDestinationQueue.take();
                    log.info("过期键 key={}", item);
                    String finalItem = item;
                    hashList.forEach((map) -> {
                        log.info("删除hash数据field={}", map.remove(finalItem));
                    });
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (Exception e) {
                    log.error("hash过期field清理异常");
                }

            }
        }).start();
    }

    public void offer(RMap<String, String> map, String field, long delay, TimeUnit timeUnit) {
        put(map);
        delayedQueue.offer(field, delay, timeUnit);
    }
}
