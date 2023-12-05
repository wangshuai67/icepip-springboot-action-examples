package com.icepip.project;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();

        // 在请求被处理之前记录开始时间
        exchange.getAttributes().put("startTime", startTime);

        Mono<Void> runnable = Mono.fromRunnable(() -> {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // 在请求被处理完后计算处理时间并打印日志
            System.out.println("Request took " + duration + " ms");
        });
        return chain.filter(exchange).then(
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
