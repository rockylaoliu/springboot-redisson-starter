package com.springboot.redisson.starter.autoconfigure;

import com.springboot.redisson.starter.aspect.RedissonLockAspect;
import com.springboot.redisson.starter.properties.RedissonProperties;
import com.springboot.redisson.starter.provider.RedissonProvider;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

@Configuration
@ConditionalOnProperty(prefix = "redisson.starter", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(RedissonProperties.class)
@Import({RedissonLockAspect.class})
public class RedissonConfiguration {

    @Resource
    private RedissonProperties redissonProperties;

    @Bean
    public RedissonProvider redissonProvider() {
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", redissonProperties.getHost()+"", redissonProperties.getPort()+"");
        if(StringUtils.isNotEmpty(redissonProperties.getPassword())) {
            config.useSingleServer()
                    .setAddress(redisUrl)
                    .setPassword(redissonProperties.getPassword())
                    .setTimeout(3000)
                    .setConnectionMinimumIdleSize(2)
                    .setConnectionPoolSize(32);
        } else {
            config.useSingleServer()
                    .setAddress(redisUrl)
                    .setTimeout(3000)
                    .setConnectionMinimumIdleSize(2)
                    .setConnectionPoolSize(32);
        }
        return new RedissonProvider(Redisson.create(config), redissonProperties.getPrefix());
    }
}
