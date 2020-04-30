package com.in.g.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ship
 * @date 2020-04-27 19:50
 */
@ConfigurationProperties(prefix = "transaction.mq")
public class TransactionMQConfiguration {

    /**
     * 回退基数，默认10
     */
    private Long initBackOff = 10L;
    /**
     * 回退因子，默认2
     */
    private Integer backOffFactor = 2;
    /**
     * 最大重试次数，默认5
     */
    private Integer maxRetryTimes = 5;
    /**
     * 业务key，一般为spring.application.name
     */
    private String businessKey;
    /**
     * 获取锁重试时间(ms)
     */
    private Long requireTimeOut = 3000L;
    /**
     * Redis分布式锁过期时间(ms)
     */
    private Long lockTimeOut = 10000L;
    /**
     * 批量补偿推送数量
     */
    private Integer batchRetryLimit = 100;

    public Integer getBatchRetryLimit() {
        return batchRetryLimit;
    }

    public void setBatchRetryLimit(Integer batchRetryLimit) {
        this.batchRetryLimit = batchRetryLimit;
    }

    public Long getRequireTimeOut() {
        return requireTimeOut;
    }

    public void setRequireTimeOut(Long requireTimeOut) {
        this.requireTimeOut = requireTimeOut;
    }

    public Long getLockTimeOut() {
        return lockTimeOut;
    }

    public void setLockTimeOut(Long lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Long getInitBackOff() {
        return initBackOff;
    }

    public void setInitBackOff(Long initBackOff) {
        this.initBackOff = initBackOff;
    }

    public Integer getBackOffFactor() {
        return backOffFactor;
    }

    public void setBackOffFactor(Integer backOffFactor) {
        this.backOffFactor = backOffFactor;
    }

    public Integer getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(Integer maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }
}
