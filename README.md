## springboot-redisson-starter
基于redisson的分布式锁demo

## 使用DEMO

### 引用jar
implementation "com.springboot.redisson.starter:springboot-redisson-starter:1.0-SNAPSHOT"

### 添加配置信息
redisson:
  starter:
    redisson:
      enabled: ${REDISSON_ENABLED:false}  
      host: redis服务器地址
      port: 6379  
      password: redis服务密码  
      prefix: e_testing_automation_

### 使用注解
@EnableRedissonLock(waitTime = 1, leaseTime = 2)
