package com.springboot.redisson.starter.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author: rocky.liu
 * 2021/8/3 10:16 上午
 */
@Data
@ConfigurationProperties(prefix = "redisson.starter")
public class RedissonProperties {

    private String host;

    private Integer port;

    private String prefix = "redisson_starter_";

    private String password;
}
