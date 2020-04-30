package com.in.g.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Ship
 * @date 2020-04-28 11:57
 */
public class RedisLock {

    private final RedisTemplatePlus redisTemplatePlus;

    public RedisLock(RedisTemplatePlus redisTemplatePlus) {
        this.redisTemplatePlus = redisTemplatePlus;
    }

    /**
     * @param lockName
     * @param requireTimeOut
     * @return
     */
    public String requireLockWithTimeOut(String lockName, long requireTimeOut, long lockTimeOut) {
        String key = lockName;
        String identifier = UUID.randomUUID().toString();
        long end = System.currentTimeMillis() + requireTimeOut;
        int lockExpire = (int) (lockTimeOut / 1000);
        while (System.currentTimeMillis() < end) {
            Boolean success = redisTemplatePlus.opsForValue().setIfAbsent(key, identifier);
            if (success) {
                //设置过期时间
                redisTemplatePlus.expire(key, lockExpire, TimeUnit.SECONDS);
                return identifier;
            }
            if (redisTemplatePlus.getExpire(key, TimeUnit.SECONDS) == -1) {
                redisTemplatePlus.expire(key, lockExpire, TimeUnit.SECONDS);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 释放锁
     *
     * @param lockName
     * @param identifier
     * @return
     */
    public boolean releaseLock(String lockName, String identifier) {
        String key = lockName;
        while (true) {
            redisTemplatePlus.watch(key);
            if (identifier.equals(redisTemplatePlus.opsForValue().get(key))) {
                //检查是否还未释放
                SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
                    @Nullable
                    @Override
                    public Object execute(RedisOperations operations) throws DataAccessException {
                        operations.multi();
                        operations.delete(key);
                        List obj = operations.exec();
                        return obj;
                    }
                };
                Object object = redisTemplatePlus.execute(sessionCallback);
                if (object != null) {
                    return true;
                }
                continue;
            }
            redisTemplatePlus.unwatch();
            break;
        }
        return false;
    }
}
