package com.in.g.annotation;

import com.in.g.config.TransactionMQAutoConfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ship
 * @date 2020-04-27 16:22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TransactionMQAutoConfigure.class)
public @interface EnableTransactionMQ {

}
