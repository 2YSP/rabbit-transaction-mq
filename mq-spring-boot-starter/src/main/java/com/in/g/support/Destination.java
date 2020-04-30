package com.in.g.support;

/**
 * Created by 2YSP on 2020/2/7.
 */
public interface Destination {

  ExchangeType exchangeType();

  String queueName();

  String exchangeName();

  String routingKey();
}
