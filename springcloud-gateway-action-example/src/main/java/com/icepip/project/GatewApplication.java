package com.icepip.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewApplication.class, args);
    }

}
