package com.in.g.service;

import com.in.g.bean.TransactionMessage;
import com.in.g.bean.TransactionMessageContent;
import com.in.g.constant.YesOrNoEnum;
import com.in.g.dao.TransactionMessageContentDao;
import com.in.g.dao.TransactionMessageDao;
import com.in.g.util.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ship
 * @date 2020-04-30 15:14
 */
@Slf4j
@Service
public class TransactionMessageEnduranceService {
    @Resource
    private TransactionMessageDao transactionMessageDao;
    @Resource
    private TransactionMessageContentDao contentDao;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY_PATTERN = "TRANSACTION_MESSAGE_*";

    /**
     * 格式：MESSAGE_CONTENT_businessKey_id
     */
    private static final String TRANSACTION_MESSAGE_CONTENT_KEY = "MESSAGE_CONTENT_%s_%s";

    public void executeEndurance() {
        Set<String> keys = redisTemplate.keys(KEY_PATTERN);
        if (keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            Map map = redisTemplate.boundHashOps(key).entries();
            TransactionMessage message = BeanUtils.mapToBean(map, TransactionMessage.class);
            if (message != null && YesOrNoEnum.YES.getValue().equals(message.getIsFinal())) {
                addTransactionMessage(key, message);
            }
        }
    }

    private void addTransactionMessage(String messageKey, TransactionMessage message) {
        String businessKey = parseBusinessKey(messageKey);
        Long id = message.getId();
        String contentKey = String.format(TRANSACTION_MESSAGE_CONTENT_KEY, businessKey, id);
        Map contentMap = redisTemplate.boundHashOps(contentKey).entries();
        TransactionMessageContent transactionMessageContent = BeanUtils.mapToBean(contentMap, TransactionMessageContent.class);
        int i = transactionMessageDao.insert(message);
        int count = contentDao.insert(transactionMessageContent);
        if (i > 0 && count > 0) {
            redisTemplate.delete(messageKey);
            redisTemplate.delete(contentKey);
            log.info("MQ消息记录持久化成功，删除缓存，messageKey[{}]", messageKey);
        }
    }

    private String parseBusinessKey(String messageKey) {
        return messageKey.split("_")[2];
    }
}
