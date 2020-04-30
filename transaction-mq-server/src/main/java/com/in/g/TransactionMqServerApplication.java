package com.in.g;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@MapperScan("com.in.g.dao")
@EnableScheduling
@SpringBootApplication
public class TransactionMqServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionMqServerApplication.class, args);
    }

}
