package com.foxandgrapes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.foxandgrapes.mapper")
public class ChatBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatBackApplication.class, args);
    }
}