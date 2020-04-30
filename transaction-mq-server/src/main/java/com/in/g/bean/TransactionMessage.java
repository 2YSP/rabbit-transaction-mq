package com.in.g.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by 2YSP on 2020/2/7.
 */
@Data
@Accessors(chain = true)
@TableName("transaction_message")
public class TransactionMessage {

  @TableId
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


}
