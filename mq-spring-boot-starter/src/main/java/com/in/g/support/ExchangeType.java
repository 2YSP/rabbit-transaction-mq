package com.in.g.support;


/**
 * Created by 2YSP on 2020/2/7.
 */
public enum ExchangeType {

    FANOUT("fanout"),
    DIRECT("direct"),
    TOPIC("topic"),
    DEFAULT("");

    private final String type;

    ExchangeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
