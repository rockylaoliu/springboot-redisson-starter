package com.springboot.redisson.starter.aspect;

import com.springboot.redisson.starter.annotation.EnableRedissonLock;
import com.springboot.redisson.starter.provider.RedissonProvider;
import com.springboot.redisson.starter.utils.ResponseUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: rocky.liu
 * 2021/8/3 10:27 上午
 */
@Aspect
@ConditionalOnWebApplication
@Slf4j
public class RedissonLockAspect {

    private RedissonProvider redissonProvider;

    public RedissonLockAspect(RedissonProvider redissonProvider) {
        this.redissonProvider = redissonProvider;
    }

    @Around(value = "@annotation(com.springboot.redisson.starter.annotation.EnableRedissonLock)")
    public Object handleRedisLock(ProceedingJoinPoint joinPoint) throws Throwable, Exception {
        Object obj = null;
        EnableRedissonLock enableRedisLock = ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(EnableRedissonLock.class);
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String userId = request.getHeader("USER-ID");
        String params = Objects.nonNull(request.getParameterMap()) ? request.getParameterMap().toString() : "";
        StringBuilder lockKey = new StringBuilder();
        lockKey.append(uri)
                .append("_")
                .append(method)
                .append("_")
                .append(userId)
                .append("_")
                .append(params);
        int waitTime = enableRedisLock.waitTime();
        TimeUnit timeUnit = enableRedisLock.timeUnit();
        int leaseTime = enableRedisLock.leaseTime();
        log.debug("handleRedissonLock lockKey={}", lockKey.toString());
        try {
            if (redissonProvider.tryLock(lockKey.toString(), timeUnit, waitTime, leaseTime)) {
                log.debug("handleRedissonLock lock success");
                obj = joinPoint.proceed(args);
                redissonProvider.unlock(lockKey.toString());
                return obj;
            } else {
                log.debug("handleRedissonLock lock failure");
                Map<String, String> map = new HashMap<>();
                map.put("error", "请求太频繁");
                ResponseUtil response = new ResponseUtil(905, map, null);
                return response;
            }
        } catch (Exception ex){
            redissonProvider.unlock(lockKey.toString());
            throw ex;
        }
    }
}
