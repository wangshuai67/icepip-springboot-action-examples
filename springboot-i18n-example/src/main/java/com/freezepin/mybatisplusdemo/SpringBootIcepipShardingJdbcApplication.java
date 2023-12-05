package com.freezepin.mybatisplusdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用sharding-jdbc 实现分库分表
 * @author 冰点 icepip
 * @date 2021-06-29 15:27:00
 */
@SpringBootApplication
public class SpringBootIcepipShardingJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootIcepipShardingJdbcApplication.class, args);
	}

}
