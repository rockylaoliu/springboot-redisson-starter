package com.springboot.redisson.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author: rocky.liu
 * 2021/8/3 10:23 上午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnableRedissonLock {
    int waitTime() default 0;//等待时间
    TimeUnit timeUnit() default TimeUnit.SECONDS;//时间单位
    int leaseTime() default 3;//锁定时间
}
