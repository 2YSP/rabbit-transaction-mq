package com.in.g.support;


/**
 * Created by 2YSP on 2020/2/7.
 */
public class DefaultDestination implements Destination {

    private ExchangeType exchangeType;

    private String queueName;

    private String exchangeName;

    private String routingKey;

    public DefaultDestination() {
    }

    @Override
    public ExchangeType exchangeType() {
        return exchangeType;
    }

    @Override
    public String queueName() {
        return queueName;
    }

    @Override
    public String exchangeName() {
        return exchangeName;
    }

    @Override
    public String routingKey() {
        return routingKey;
    }

    public DefaultDestination setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
        return this;
    }

    public DefaultDestination setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public DefaultDestination setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
        return this;
    }

    public DefaultDestination setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }
}
