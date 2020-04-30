package com.in.g.support;


/**
 * Created by 2YSP on 2020/2/7.
 * <p>
 * 事物消息状态
 */
public enum TxMessageStatus {
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 待处理
     */
    PENDING(0),
    /**
     * 失败
     */
    FAIL(-1);

    private final Integer status;

    TxMessageStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
