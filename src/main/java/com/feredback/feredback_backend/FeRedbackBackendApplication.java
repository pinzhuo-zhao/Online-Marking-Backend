package com.feredback.feredback_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan("com.feredback.feredback_backend.mapper")
@ComponentScan(basePackages = "com.feredback")
public class FeRedbackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeRedbackBackendApplication.class, args);
    }


}
