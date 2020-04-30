# rabbit-transaction-mq
RabbitMQ实现事务消息

该项目主要用于保证RabbitMQ的消息可靠投递，类似RocketMQ的事务消息，保证只有生产者事务提交成功才发生MQ消息，
同时具备消息定时补偿推送机制。

## 1.mq-spring-boot-starter



<p>消息生产者客户端，为了便于消息发送记录的集中管理和避免每个服务的业务库都创建消息表，该starter暂时将消息发送记录保存到Redis中。</p>

**相关配置参数如下：**
|     配置参数|   是否必填  |   描述  | 
| --- | --- | --- |
|   transaction.mq.business-key | 是    | 业务key，一般为spring.application.name    | 
|   transaction.mq.init-back-off |   否  |  回退基数，默认10   |   
|   transaction.mq.back-off-factor |  否   | 回退因子，默认2    |    
|   transaction.mq.max-retry-times |    否 |  最大重试次数，默认5   |    
|   transaction.mq.require-time-out |   否  |  获取锁重试时间(ms)，默认3000ms   |    
|   transaction.mq.lock-time-out |   否  |  Redis分布式锁过期时间(ms)，默认10000ms   |  
|   transaction.mq.batch-retry-limit |   否  |   批量补偿推送数量，默认100  |  


## 2.transaction-mq-server
负责将消息记录定时地从Redis持久化到Mysql，以后可能会弄一些数据展示。

## 3.transaction-mq-example
mq-spring-boot-starter的使用示例
