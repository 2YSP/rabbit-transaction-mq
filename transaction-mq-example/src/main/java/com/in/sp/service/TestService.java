package com.in.sp.service;

import com.in.sp.bean.Person;
import com.in.sp.dao.PersonDao;
import com.in.g.service.TransactionMessageService;
import com.in.g.support.DefaultDestination;
import com.in.g.support.DefaultTxMessage;
import com.in.g.support.Destination;
import com.in.g.support.ExchangeType;
import com.in.g.support.TxMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Ship
 * @date 2020-04-27 17:49
 */
@Service
public class TestService {

    @Autowired
    private TransactionMessageService transactionMessageService;

    @Resource
    private PersonDao personDao;


    @Transactional(rollbackFor = Exception.class)
    public void add(Person person) {
        personDao.insert(person);
        Destination destination = new DefaultDestination()
                .setExchangeName("test-exchange")
                .setExchangeType(ExchangeType.FANOUT)
                .setQueueName("test-queue")
                .setRoutingKey("test-queue");
        TxMessage txMessage = new DefaultTxMessage()
                .setBusinessModule("order")
                .setContent("测试消息");
        transactionMessageService.sendTransactionMessage(destination, txMessage);
//        int i = 1 / 0;
    }
}
