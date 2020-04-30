package com.in.g.util;

import com.alibaba.fastjson.JSON;
import com.in.g.bean.TransactionMessage;
import com.in.g.support.TxMessageStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ship
 * @date 2020-04-28 20:34
 */
public class BeanUtils {

    private static final Logger LOG = LoggerFactory.getLogger(BeanUtils.class);

    public static <T> Map<String, Object> beanToMap(T t) {
        Map<String, Object> map = new HashMap<>(1);
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                map.put(field.getName(), field.get(t));
            }
        } catch (Exception e) {
            LOG.error("beanToMap 异常", e);
        }
        return map;
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        try {
            T t = JSON.parseObject(JSON.toJSONString(map), clazz);
            return t;
        } catch (Exception e) {
            LOG.error("mapToBean 异常", e);
        }
        return null;
    }


    public static void main(String[] args) {
        TransactionMessage record = new TransactionMessage();
        record.setId(1L);
        record.setCurrentRetryTimes(0);
        record.setMessageStatus(TxMessageStatus.PENDING.getStatus());
        record.setCreateTime(LocalDateTime.now());
        Map<String, Object> map = beanToMap(record);
        String text = JSON.toJSONString(map);
        System.out.println(text);

        TransactionMessage message = JSON.parseObject(text, TransactionMessage.class);
        System.out.println(message);
    }
}
