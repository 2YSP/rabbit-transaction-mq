package com.in.g.task;

import com.in.g.config.TransactionMQConfiguration;
import com.in.g.redis.RedisLock;
import com.in.g.service.TransactionMessageManageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

/**
 * @author Ship
 * @date 2020-04-28 11:48 消息推送补偿
 */
public class TransactionMessageCompensationTask {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionMessageCompensationTask.class);

    private final TransactionMessageManageService manageService;

    private final RedisLock redisLock;

    private final TransactionMQConfiguration configuration;

    public TransactionMessageCompensationTask(TransactionMessageManageService manageService, RedisLock redisLock, TransactionMQConfiguration configuration) {
        this.manageService = manageService;
        this.redisLock = redisLock;
        this.configuration = configuration;
    }

    @Scheduled(fixedDelay = 10000)
    public void execute() {
        String identifier = redisLock.requireLockWithTimeOut(configuration.getBusinessKey(),
                configuration.getRequireTimeOut(), configuration.getLockTimeOut());
        if (StringUtils.isEmpty(identifier)) {
            LOG.error("获取分布式锁失败。。。");
            return;
        }
        try {
            long start = System.currentTimeMillis();
            LOG.info("开始执行事务消息推送补偿定时任务...");
            manageService.processPendingCompensationRecords();
            long end = System.currentTimeMillis();
            long usedTime = end - start;
            long time = 5000;
            if (usedTime < time) {
                // 防止过早释放锁
                Thread.sleep(time - usedTime);
            }
            LOG.info("执行事务消息推送补偿定时任务完毕，耗时:{}ms...", usedTime);
        } catch (Exception e) {
            LOG.error("执行事务消息推送补偿定时任务异常", e);
        } finally {
            boolean success = redisLock.releaseLock(configuration.getBusinessKey(), identifier);
            LOG.info("释放分布式锁,success[{}]", success);
        }
    }

}
