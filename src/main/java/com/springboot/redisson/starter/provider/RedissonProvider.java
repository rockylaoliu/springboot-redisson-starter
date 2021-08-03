package com.springboot.redisson.starter.provider;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: rocky.liu
 * 2021/8/3 10:28 上午
 */
@Slf4j
@ConditionalOnClass(Redisson.class)
public class RedissonProvider {

    private RedissonClient redissonClient;

    private String prefix;

    public RedissonProvider(RedissonClient redissonClient, String prefix) {
        this.redissonClient = redissonClient;
        this.prefix = prefix;
    }

    public RLock lock(String lockKey) {
        lockKey = prefix + lockKey;
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    public RLock lock(String lockKey, int leaseTime) {
        lockKey = prefix + lockKey;
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    public RLock lock(String lockKey, TimeUnit unit ,int timeout) {
        lockKey = prefix + lockKey;
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    public void unlock(String lockKey) {
        try {
            lockKey = prefix + lockKey;
            RLock lock = redissonClient.getLock(lockKey);
            lock.unlock();
        } catch (Exception ex) {

        }
    }

    public void unlock(RLock lock) {
        lock.unlock();
    }

    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        lockKey = prefix + lockKey;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }
}
