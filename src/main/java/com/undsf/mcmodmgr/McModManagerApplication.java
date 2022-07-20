package com.undsf.mcmodmgr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.undsf.mcmodmgr.mappers")
public class McModManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McModManagerApplication.class, args);
    }
}
