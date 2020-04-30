package com.in.g.service;

import com.in.g.bean.TransactionMessage;
import com.in.g.support.Destination;
import com.in.g.support.ExchangeType;
import com.in.g.support.TxMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by 2YSP on 2020/2/7.
 */
public class RabbitTransactionMessageService implements TransactionMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitTransactionMessageService.class);

    private final AmqpAdmin amqpAdmin;

    private final TransactionMessageManageService manageService;

    public RabbitTransactionMessageService(AmqpAdmin amqpAdmin, TransactionMessageManageService manageService) {
        this.amqpAdmin = amqpAdmin;
        this.manageService = manageService;
    }

    private static final ConcurrentHashMap<String, Boolean> QUEUE_ALREADY_DECLARE = new ConcurrentHashMap<>();

    @Override
    public void sendTransactionMessage(Destination destination, TxMessage txMessage) {
        ExchangeType exchangeType = destination.exchangeType();
        String exchangeName = destination.exchangeName();
        String queueName = destination.queueName();
        String routingKey = destination.routingKey();

        // 原子性的预声明
        QUEUE_ALREADY_DECLARE.computeIfAbsent(queueName, k -> {
            Queue queue = new Queue(queueName);
            amqpAdmin.declareQueue(queue);
            Exchange exchange = new CustomExchange(exchangeName, exchangeType.getType());
            amqpAdmin.declareExchange(exchange);
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
            amqpAdmin.declareBinding(binding);
            return true;
        });

        TransactionMessage record = new TransactionMessage();
        record.setExchangeType(exchangeType.getType());
        record.setExchangeName(exchangeName);
        record.setQueueName(queueName);
        record.setRoutingKey(routingKey);
        record.setBusinessModule(txMessage.businessModule());
        String content = txMessage.content();
        // 保存事物消息记录
        manageService.saveTransactionMessageRecord(record, content);

        // 注册事务同步器
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        // 发送mq消息并更改状态
                        manageService.sendMessageSync(record, content);
                    }
                });
    }
}
