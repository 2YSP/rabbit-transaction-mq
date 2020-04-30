package com.in.g.bean;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by 2YSP on 2020/2/7.
 */
@Data
@Accessors(chain = true)
@TableName("transaction_message_content")
public class TransactionMessageContent {

    @TableId
    private Long id;

    private Long messageId;

    private String content;
}
