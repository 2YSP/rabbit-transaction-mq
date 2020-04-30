package com.in.g.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ship
 * @date 2020-04-29 14:50
 */
public class RedisTemplatePlus extends RedisTemplate<String, Object> {

    /**
     * Date格式化字符串
     */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * DateTime格式化字符串
     */
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Time格式化字符串
     */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");


    public RedisTemplatePlus(RedisConnectionFactory redisConnectionFactory) {
        this.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        //key序列化
        this.setKeySerializer(stringSerializer);
        //value序列化
        this.setValueSerializer(jackson2JsonRedisSerializer);
        //Hash key序列化
        this.setHashKeySerializer(stringSerializer);
        //Hash value序列化
        this.setHashValueSerializer(jackson2JsonRedisSerializer);
        this.afterPropertiesSet();
    }

    private Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        SimpleModule simpleModule = new SimpleModule();
//
//        // java8 时间格式化
//        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMAT));
//        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMAT));
//        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(TIME_FORMAT));
//        om.registerModule(simpleModule);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }
}
