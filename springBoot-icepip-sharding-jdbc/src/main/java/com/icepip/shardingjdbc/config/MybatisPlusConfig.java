package com.icepip.shardingjdbc.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * Mybatis-plus 全局拦截器
 * 可通过开关选择启用不同作用的全局拦截器 {@see MybatisPlusConfig}
 *
 * @author 冰点
 * @date 2022-06-23 2:13 PM
 */
@Configuration
@Slf4j
public class MybatisPlusConfig {

    @Value("${icepip.mybatis-plus-ext.db-key.enable:false}")
    private boolean dbKeyEnable;

    @Value("${icepip.datasource.dynamic.primary:ds0}")
    private String primaryCode;

    @Value("${icepip.mybatis-plus-ext.db-key:ds0}")
    private String dbKey;

    @Value("${icepip.mybatis-plus-ext.pagination-interceptor.enable:true}")
    private boolean paginationInterceptorEnable;

    @Value("${icepip.mybatis-plus-ext.optimistic-Locker.enable:true}")
    private boolean optimisticLockerEnable;

    @Value("${icepip.mybatis-plus-ext.sql-attack-interceptor.enable:false}")
    private boolean sqlAttackInterceptorEnable;

    @Value("${icepip.mybatis-plus-ext.tenant-interceptor.enable:true}")
    private boolean tenantEnable;

    @Bean
    public DataSourceHealthIndicator dataSourceHealthIndicator(DataSource dataSource) {
        return new DataSourceHealthIndicator(dataSource, "select 1");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (optimisticLockerEnable) {
            interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
            log.info("已开启乐观锁校验[当要更新一条记录的时候，希望这条记录没有被别人更新]，被@version字段修饰的字段为乐观锁字段，此必须要有默认值或初始值");
        }
        if (sqlAttackInterceptorEnable) {
            interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
            log.info("已开启危险SQL拦截[由于开启了直接输入sql操作数据库，防止误操作]，危险SQL拦截判断规则详见@see BlockAttackInnerInterceptor");
        }
        if (tenantEnable) {
            // 已简化
            log.info("已开启多租户模式，默认项目id为租户id");
        }
        if (paginationInterceptorEnable) {
            PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
            innerInterceptor.setOptimizeJoin(false);
            interceptor.addInnerInterceptor(innerInterceptor);
            log.info("已开启自动分页插件，如果返回类型是 IPage 则入参的 IPage 不能为null,因为 返回的IPage == 入参的IPage，如果返回类型是 List 则入参的 IPage 可以为 null(为 null 则不分页),但需要你手动入参的IPage.setRecords(返回的 List)");
        }

        return interceptor;
    }


}
