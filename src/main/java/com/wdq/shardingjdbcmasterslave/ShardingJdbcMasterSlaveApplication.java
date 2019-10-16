package com.wdq.shardingjdbcmasterslave;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author BG405275
 */
@SpringBootApplication
@MapperScan("com.wdq.shardingjdbcmasterslave.dao")
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@EnableEncryptableProperties
public class ShardingJdbcMasterSlaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcMasterSlaveApplication.class, args);
    }

}
