package com.in.sp;

import com.in.g.annotation.EnableTransactionMQ;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableTransactionMQ
@MapperScan("com.in.sp.dao")
@SpringBootApplication
@EnableScheduling
public class StarterTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterTestApplication.class, args);
    }

}
