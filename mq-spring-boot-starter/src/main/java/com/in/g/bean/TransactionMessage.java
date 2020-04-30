package com.in.g.bean;

import java.time.LocalDateTime;



/**
 * Created by 2YSP on 2020/2/7.
 */
public class TransactionMessage {

  private Long id;

  private LocalDateTime createTime;

  private LocalDateTime editTime;

  private Integer currentRetryTimes;

  private Integer maxRetryTimes;

  private String queueName;

  private String exchangeName;

  private String exchangeType;

  private String routingKey;

  private String businessModule;

  private String businessKey;

  private LocalDateTime nextScheduleTime;

  private Integer messageStatus;

  private Long initBackoff;

  private Integer backoffFactor;
  /**
   * 是否为最终状态：1=是2=不是
   * 最终状态包含以下几种：
   * 1.消息发送成功
   * 2.消息发送失败且达到最大重试次数
   */
  private Byte isFinal;


  public Byte getIsFinal() {
    return isFinal;
  }

  public void setIsFinal(Byte isFinal) {
    this.isFinal = isFinal;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getEditTime() {
    return editTime;
  }

  public void setEditTime(LocalDateTime editTime) {
    this.editTime = editTime;
  }

  public Integer getCurrentRetryTimes() {
    return currentRetryTimes;
  }

  public void setCurrentRetryTimes(Integer currentRetryTimes) {
    this.currentRetryTimes = currentRetryTimes;
  }

  public Integer getMaxRetryTimes() {
    return maxRetryTimes;
  }

  public void setMaxRetryTimes(Integer maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
  }

  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  public String getExchangeType() {
    return exchangeType;
  }

  public void setExchangeType(String exchangeType) {
    this.exchangeType = exchangeType;
  }

  public String getRoutingKey() {
    return routingKey;
  }

  public void setRoutingKey(String routingKey) {
    this.routingKey = routingKey;
  }

  public String getBusinessModule() {
    return businessModule;
  }

  public void setBusinessModule(String businessModule) {
    this.businessModule = businessModule;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public LocalDateTime getNextScheduleTime() {
    return nextScheduleTime;
  }

  public void setNextScheduleTime(LocalDateTime nextScheduleTime) {
    this.nextScheduleTime = nextScheduleTime;
  }

  public Integer getMessageStatus() {
    return messageStatus;
  }

  public void setMessageStatus(Integer messageStatus) {
    this.messageStatus = messageStatus;
  }

  public Long getInitBackoff() {
    return initBackoff;
  }

  public void setInitBackoff(Long initBackoff) {
    this.initBackoff = initBackoff;
  }

  public Integer getBackoffFactor() {
    return backoffFactor;
  }

  public void setBackoffFactor(Integer backoffFactor) {
    this.backoffFactor = backoffFactor;
  }

  @Override
  public String toString() {
    return "TransactionMessage{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", editTime=" + editTime +
            ", currentRetryTimes=" + currentRetryTimes +
            ", maxRetryTimes=" + maxRetryTimes +
            ", queueName='" + queueName + '\'' +
            ", exchangeName='" + exchangeName + '\'' +
            ", exchangeType='" + exchangeType + '\'' +
            ", routingKey='" + routingKey + '\'' +
            ", businessModule='" + businessModule + '\'' +
            ", businessKey='" + businessKey + '\'' +
            ", nextScheduleTime=" + nextScheduleTime +
            ", messageStatus=" + messageStatus +
            ", initBackoff=" + initBackoff +
            ", backoffFactor=" + backoffFactor +
            '}';
  }
}
