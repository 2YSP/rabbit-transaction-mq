package com.in.g.task;

import com.in.g.service.TransactionMessageEnduranceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ship
 * @date 2020-04-30 14:58
 */
@Slf4j
@Component
public class TransactionMessageEnduranceJob {

    @Autowired
    private TransactionMessageEnduranceService messageEnduranceService;

    @Scheduled(fixedDelay = 10000)
    public void run(){
      log.info("MQ消息记录持久化定时任务开始...");
      messageEnduranceService.executeEndurance();
      log.info("MQ消息记录持久化定时任务结束...");

    }
}
