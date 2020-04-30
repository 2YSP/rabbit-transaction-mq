package com.in.g.service;

import com.in.g.bean.TransactionMessage;
import com.in.g.bean.TransactionMessageContent;
import com.in.g.config.RedisKeyConstants;
import com.in.g.config.TransactionMQConfiguration;
import com.in.g.redis.RedisTemplatePlus;
import com.in.g.support.TxMessageStatus;
import com.in.g.support.YesOrNoEnum;
import com.in.g.util.BeanUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by 2YSP on 2020/2/7.
 */
public class TransactionMessageManageService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionMessageManageService.class);

    private final RabbitTemplate rabbitTemplate;

    private final RedisTemplatePlus redisTemplatePlus;

    private final TransactionMQConfiguration configuration;

    public TransactionMessageManageService(RabbitTemplate rabbitTemplate, RedisTemplatePlus redisTemplatePlus, TransactionMQConfiguration configuration) {
        this.rabbitTemplate = rabbitTemplate;
        this.configuration = configuration;
        this.redisTemplatePlus = redisTemplatePlus;
    }

    private static final LocalDateTime END = LocalDateTime.of(2999, 1, 1, 0, 0, 0);


    public void saveTransactionMessageRecord(TransactionMessage record, String content) {
        try {
            long id = System.currentTimeMillis();
            record.setId(id);
            record.setCurrentRetryTimes(0);
            record.setMessageStatus(TxMessageStatus.PENDING.getStatus());
            record.setNextScheduleTime(calculateNextScheduleTime(LocalDateTime.now(),
                    configuration.getInitBackOff(), configuration.getBackOffFactor(), 0));
            record.setInitBackoff(configuration.getInitBackOff());
            record.setBackoffFactor(configuration.getBackOffFactor());
            record.setMaxRetryTimes(configuration.getMaxRetryTimes());
            record.setBusinessKey(configuration.getBusinessKey());
            record.setCreateTime(LocalDateTime.now());
            record.setIsFinal(YesOrNoEnum.NO.getValue());
            String recordKey = buildRecordKey(record.getId());
            redisTemplatePlus.opsForHash().putAll(recordKey, BeanUtils.beanToMap(record));

            TransactionMessageContent transactionMessageContent = new TransactionMessageContent();
            transactionMessageContent.setContent(content);
            transactionMessageContent.setMessageId(record.getId());
            transactionMessageContent.setId(id);
            String contentKey = buildContentKey(transactionMessageContent.getId());
            redisTemplatePlus.opsForHash().putAll(contentKey, BeanUtils.beanToMap(transactionMessageContent));
        } catch (Exception e) {
            LOG.error("save Transaction Message Record failed", e);
        }
    }

    private String buildRecordKey(Long id) {
        return String.format(RedisKeyConstants.TRANSACTION_MESSAGE_KEY, configuration.getBusinessKey(), id);
    }

    private String buildContentKey(Long id) {
        return String.format(RedisKeyConstants.TRANSACTION_MESSAGE_CONTENT_KEY, configuration.getBusinessKey(), id);
    }


    public void sendMessageSync(TransactionMessage record, String content) {
        try {
            rabbitTemplate.convertAndSend(record.getExchangeName(), record.getRoutingKey(), content);
            LOG.info("发送消息成功，目标队列：{},消息内容：{}", record.getQueueName(), content);
            markSuccess(record);
        } catch (Exception e) {
            LOG.error("发送消息失败，目标队列：{}", record.getQueueName(), e);
            markFail(record);
        }
    }

    /**
     * 标记失败
     */
    private void markFail(TransactionMessage record) {
        record.setCurrentRetryTimes(
                record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ? record
                        .getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        // 计算下次执行时间
        LocalDateTime nextScheduleTime = calculateNextScheduleTime(record.getNextScheduleTime(),
                record.getInitBackoff(), record.getBackoffFactor(), record.getCurrentRetryTimes());

        record.setNextScheduleTime(nextScheduleTime);
        record.setMessageStatus(TxMessageStatus.FAIL.getStatus());
        record.setEditTime(LocalDateTime.now());
        if (record.getMaxRetryTimes().equals(record.getCurrentRetryTimes())){
            record.setIsFinal(YesOrNoEnum.YES.getValue());
        }
        redisTemplatePlus.opsForHash().putAll(buildRecordKey(record.getId()), BeanUtils.beanToMap(record));
    }

    private void markSuccess(TransactionMessage record) {
        record.setMessageStatus(TxMessageStatus.SUCCESS.getStatus());
        // 标记下次执行时间为最大值
        record.setNextScheduleTime(END);
        record.setEditTime(LocalDateTime.now());
        record.setCurrentRetryTimes(
                record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ? record
                        .getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        record.setIsFinal(YesOrNoEnum.YES.getValue());
        redisTemplatePlus.opsForHash().putAll(buildRecordKey(record.getId()), BeanUtils.beanToMap(record));
    }

    /**
     * 计算下次执行时间
     *
     * @param base          基础时间
     * @param initBackoff   退避基准值
     * @param backoffFactor 退避指数
     * @param round         轮数
     */
    private LocalDateTime calculateNextScheduleTime(LocalDateTime base, long initBackoff,
                                                    long backoffFactor,
                                                    long round) {
        double delta = initBackoff * Math.pow(backoffFactor, round);
        return base.plusSeconds((long) delta);
    }

    /**
     * 推送补偿
     */
    public void processPendingCompensationRecords() {
        List<TransactionMessage> messageList = queryPendingCompensationRecords(configuration.getBatchRetryLimit());
        Map<Long, TransactionMessage> map = messageList.stream()
                .collect(Collectors.toMap(TransactionMessage::getId, x -> x));
        if (!map.isEmpty()) {
            map.forEach((key, value) -> {
                String contentKey = buildContentKey(key);
                String content = (String) redisTemplatePlus.opsForHash().get(contentKey, "content");
                LOG.info("开始补偿推送消息,recordKey:[{}]", buildRecordKey(key));
                sendMessageSync(value, content);
            });
        }
    }

    /**
     * 查询发送失败且未达到最大重试次数的消息
     *
     * @return
     */
    private List<TransactionMessage> queryPendingCompensationRecords(int limit) {
        String pattern = "TRANSACTION_MESSAGE_" + configuration.getBusinessKey() + "*";
        Set<String> keys = redisTemplatePlus.keys(pattern);
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }
        LocalDateTime now = LocalDateTime.now();
        List<TransactionMessage> list = new ArrayList<>(limit);
        for (String key : keys) {
            Map map = redisTemplatePlus.boundHashOps(key).entries();
            TransactionMessage message = BeanUtils.mapToBean(map, TransactionMessage.class);
            if (!TxMessageStatus.FAIL.getStatus().equals(message.getMessageStatus())) {
                continue;
            }
            if (message.getCurrentRetryTimes().equals(message.getMaxRetryTimes())) {
                continue;
            }
            // 未达到下次触发时间
            if (now.isBefore(message.getNextScheduleTime())) {
                continue;
            }
            if (list.size() == limit) {
                break;
            }
            list.add(message);
        }
        return list;
    }
}
