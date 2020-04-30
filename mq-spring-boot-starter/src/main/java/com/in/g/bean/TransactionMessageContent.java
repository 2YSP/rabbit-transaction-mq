package com.in.g.bean;



/**
 * Created by 2YSP on 2020/2/7.
 */
public class TransactionMessageContent {

    private Long id;
    private Long messageId;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TransactionMessageContent{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", content='" + content + '\'' +
                '}';
    }
}
