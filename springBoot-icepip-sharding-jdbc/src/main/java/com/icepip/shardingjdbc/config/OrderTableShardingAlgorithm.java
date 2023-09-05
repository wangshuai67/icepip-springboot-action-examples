package com.icepip.shardingjdbc.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 *  自定义分片策略
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/5 14:52
 */

public class OrderTableShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        Date orderCreateTime = shardingValue.getValue();
        int year = getYear(orderCreateTime);

        for (String each : availableTargetNames) {
            if (each.endsWith(year + "")) {
                return each;
            }
        }

        throw new IllegalArgumentException();
    }

    private int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}