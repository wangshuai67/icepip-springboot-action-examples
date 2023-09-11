package com.icepip.project.redisson;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *  基于 RedissonClient 实现类似Spring-data-redis提供的RedisTemplate的模板方法操作类
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/11 16:15
 */

@Component
public class RedissonTemplate {
    private final RedissonClient redissonClient;

    @Autowired
    public RedissonTemplate(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 向频道发布消息
     *
     * @param channel 频道名称
     * @param message 消息内容
     */
    public void convertAndSend(String channel, Object message) {
        redissonClient.getTopic(channel).publish(message);
    }

    /**
     * 用管道批量执行操作
     *
     * @param batch 批处理对象
     * @return 执行结果列表
     */
    public List<Object> executePipelined(RBatch batch) {
        // 执行批处理命令并返回结果
        BatchResult<?> result = batch.execute();
        return (List<Object>) result.getResponses();
    }

    /**
     * 设置键的过期时间
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 设置是否成功
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redissonClient.getBucket(key).expire(timeout, unit);
    }

    /**
     * 设置键的过期时间戳
     *
     * @param key       键
     * @param timestamp 过期时间戳
     * @return 设置是否成功
     */
    public boolean expireAt(String key, long timestamp) {
        return redissonClient.getBucket(key).expireAt(timestamp);
    }

    /**
     * 获取键的剩余过期时间
     *
     * @param key 键
     * @return 键的剩余过期时间
     */
    public long getExpire(String key) {
        return redissonClient.getBucket(key).remainTimeToLive();
    }

    /**
     * 检查键是否存在
     *
     * @param key 键
     * @return 键是否存在
     */
    public boolean hasKey(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 根据模式查询键
     *
     * @param pattern 模式
     * @return 匹配的键集合
     */
    public Iterable<String> keys(String pattern) {
        return redissonClient.getKeys().getKeysByPattern(pattern);
    }

    /**
     * 持久化键
     *
     * @param key 键
     * @return 是否成功持久化键
     */
    public boolean persist(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        // TODO: 实现持久化键的逻辑
        return true;
    }

    /**
     * 随机获取一个键
     *
     * @return 随机键
     */
    public String randomKey() {
        return redissonClient.getKeys().randomKey();
    }

    /**
     * 重命名键
     *
     * @param oldKey 旧键名
     * @param newKey 新键名
     */
    public void rename(String oldKey, String newKey) {
        redissonClient.getKeys().rename(oldKey, newKey);
    }

    /**
     * 当新键名不存在时，重命名键
     *
     * @param oldKey 旧键名
     * @param newKey 新键名
     * @return 是否成功重命名键
     */
    public boolean renameIfAbsent(String oldKey, String newKey) {
        // TODO: 实现当新键名不存在时重命名键的逻辑
        return true;
    }

    /**
     * 删除一组键
     *
     * @param keys 键的集合
     * @return 删除的键的数量
     */
    public long delete(Collection<String> keys) {
        return redissonClient.getKeys().delete(keys.toArray(new String[0]));
    }

    /**
     * 关闭Redisson客户端连接
     */
    public void close() {
        redissonClient.shutdown();
    }

    /**
     * 使用指定的排序方式对列表进行排序
     *
     * @param key 列表的键
     * @return 排序后的列表
     */
    public List<String> sort(String key) {
        RList<String> list = redissonClient.getList(key);
        return list.readSort(SortOrder.valueOf("alpha"));
    }

    /**
     * 取消监视（事务回滚）
     */
    public void unwatch() {
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());
        transaction.rollback();
    }

    /**
     * 获取地理位置操作的对象
     *
     * @param key 键
     * @return 地理位置操作对象
     */
    public RGeo<String> opsForGeo(String key) {
        return redissonClient.getGeo(key);
    }

    /**
     * 获取HyperLogLog操作的对象
     *
     * @param key 键
     * @return HyperLogLog操作对象
     */
    public RHyperLogLog<String> opsForHyperLogLog(String key) {
        return redissonClient.getHyperLogLog(key);
    }

    /**
     * 获取Set操作的对象
     *
     * @param key 键
     * @return Set操作对象
     */
    public RSet<String> opsForSet(String key) {
        return redissonClient.getSet(key);
    }

    /**
     * 获取有序集合操作的对象
     *
     * @param key 键
     * @return 有序集合操作对象
     */
    public RScoredSortedSet<String> opsForZSet(String key) {
        return redissonClient.getScoredSortedSet(key);
    }

    /**
     * 创建一个事务对象
     *
     * @return 事务对象
     */
    public RTransaction multi() {
        return redissonClient.createTransaction(TransactionOptions.defaults());
    }

    /**
     * 判断哈希表中是否存在指定字段
     *
     * @param key   哈希表的键
     * @param field 哈希表中的字段
     * @return 哈希表中是否存在指定字段
     */
    public boolean hExists(String key, String field) {
        RMap<String, Object> map = redissonClient.getMap(key);
        return map.containsKey(field);
    }

    /**
     * 哈希表中指定字段的值增加指定的增量
     *
     * @param key   哈希表的键
     * @param field 哈希表中的字段
     * @param delta 增量
     * @return 增加后的值
     */
    public long hIncrBy(String key, String field, long delta) {
        RMap<String, Object> map = redissonClient.getMap(key);
        return (long) map.addAndGet(field, delta);
    }

    /**
     * 将成员及其分数添加到有序集合中
     *
     * @param key    有序集合的键
     * @param score  成员的分数
     * @param member 成员
     * @return 是否成功添加成员
     */
    public boolean zAdd(String key, double score, String member) {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        return set.add(score, member);
    }

    /**
     * 获取有序集合中指定范围的成员
     *
     * @param key   有序集合的键
     * @param start 范围的起始索引
     * @param end   范围的结束索引
     * @return 指定范围的成员集合
     */
    public Collection<String> zRange(String key, int start, int end) {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        return set.valueRange(start, end);
    }

    /**
     * 将元素添加到HyperLogLog中
     *
     * @param key      HyperLogLog的键
     * @param elements 元素集合
     * @return 是否成功添加元素
     */
    public boolean pfAdd(String key, Collection<String> elements) {
        RHyperLogLog<String> hll = redissonClient.getHyperLogLog(key);
        return hll.addAll(elements);
    }

    /**
     * 获取HyperLogLog的基数估算值
     *
     * @param key HyperLogLog的键
     * @return 基数估算值
     */
    public long pfCount(String key) {
        RHyperLogLog<String> hll = redissonClient.getHyperLogLog(key);
        return hll.count();
    }

    /**
     * 将地理位置及其成员添加到地理位置数据结构中
     *
     * @param key       地理位置数据结构的键
     * @param longitude 经度
     * @param latitude  纬度
     * @param member    成员
     * @return 添加的成员数量
     */
    public long geoAdd(String key, double longitude, double latitude, String member) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.add(longitude, latitude, member);
    }

    /**
     * 在给定的地理位置范围内，获取与中心点最近的成员
     *
     * @param key       地理位置数据结构的键
     * @param longitude 中心点经度
     * @param latitude  中心点纬度
     * @param radius    范围半径
     * @param unit      半径单位
     * @return 与中心点最近的成员列表
     */
    public List<String> geoRadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        RGeo<String> geo = redissonClient.getGeo(key);
        return geo.radius(longitude, latitude, radius, unit);
    }

    /**
     * 将元素添加到列表的左侧
     *
     * @param key   列表的键
     * @param value 要添加的元素
     */
    public void lPush(String key, String value) {
        RList<String> list = redissonClient.getList(key);
        list.add(0, value);
    }

    /**
     * 获取列表中指定范围的元素
     *
     * @param key   列表的键
     * @param start 范围的起始索引
     * @param end   范围的结束索引
     * @return 指定范围的元素列表
     */
    public List<String> lRange(String key, int start, int end) {
        RList<String> list = redissonClient.getList(key);
        return list.range(start, end);
    }

    /**
     * 将一个或多个成员添加到集合中
     *
     * @param key     集合的键
     * @param members 要添加的成员
     * @return 是否成功添加成员
     */
    public boolean sAdd(String key, String... members) {
        RSet<String> set = redissonClient.getSet(key);
        return set.addAll(Arrays.asList(members));
    }

    /**
     * 获取集合中的所有成员
     *
     * @param key 集合的键
     * @return 所有成员的集合
     */
    public Set<String> sMembers(String key) {
        RSet<String> set = redissonClient.getSet(key);
        return set.readAll();
    }

    /**
     * 获取有序集合中成员的排名
     *
     * @param key    有序集合的键
     * @param member 成员
     * @return 成员的排名
     */
    public Integer zRank(String key, String member) {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        return set.rank(member);
    }

    /**
     * 从有序集合中移除指定的成员
     *
     * @param key    有序集合的键
     * @param member 要移除的成员
     * @return 是否成功移除成员
     */
    public boolean zRem(String key, String member) {
        RScoredSortedSet<String> set = redissonClient.getScoredSortedSet(key);
        return set.remove(member);
    }


}
