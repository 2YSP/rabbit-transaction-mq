package com.in.g.config;

import com.in.g.redis.RedisLock;
import com.in.g.redis.RedisTemplatePlus;
import com.in.g.service.RabbitTransactionMessageService;
import com.in.g.service.TransactionMessageManageService;
import com.in.g.service.TransactionMessageService;
import com.in.g.task.TransactionMessageCompensationTask;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;


/**
 * @author Ship
 * @date 2020-04-27 15:57
 */
@Configuration
@EnableConfigurationProperties(value = {TransactionMQConfiguration.class})
public class TransactionMQAutoConfigure {

    @Autowired
    private TransactionMQConfiguration configuration;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;


    @Bean("redisTemplatePlus")
    public RedisTemplatePlus redisTemplatePlus(@Autowired RedisConnectionFactory connectionFactory) {
        return new RedisTemplatePlus(connectionFactory);
    }

    @Bean("transactionMessageManageService")
    public TransactionMessageManageService messageManageService(@Autowired RedisTemplatePlus redisTemplatePlus) {
        return new TransactionMessageManageService(rabbitTemplate, redisTemplatePlus, configuration);
    }

    @Bean("redisLock")
    public RedisLock redisLock(@Autowired RedisTemplatePlus redisTemplatePlus) {
        return new RedisLock(redisTemplatePlus);
    }

    @Bean("transactionMessageService")
    public TransactionMessageService rabbitTransactionMessageService(@Autowired TransactionMessageManageService manageService) {
        return new RabbitTransactionMessageService(amqpAdmin, manageService);
    }


    @Bean("transactionMessageCompensationTask")
    public TransactionMessageCompensationTask compensationTask(@Autowired TransactionMessageManageService manageService, @Autowired RedisLock redisLock) {
        return new TransactionMessageCompensationTask(manageService, redisLock, configuration);
    }


}
