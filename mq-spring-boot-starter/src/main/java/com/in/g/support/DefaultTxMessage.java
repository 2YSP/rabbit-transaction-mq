package com.in.g.support;


/**
 * Created by 2YSP on 2020/2/7.
 */
public class DefaultTxMessage implements TxMessage {

    private String businessModule;

    private String content;

    public DefaultTxMessage() {
    }

    @Override
    public String businessModule() {
        return businessModule;
    }

    @Override
    public String content() {
        return content;
    }

    public DefaultTxMessage setBusinessModule(String businessModule) {
        this.businessModule = businessModule;
        return this;
    }

    public DefaultTxMessage setContent(String content) {
        this.content = content;
        return this;
    }
}
