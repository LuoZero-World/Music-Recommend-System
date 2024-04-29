package com.llq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MusicBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicBackendApplication.class, args);
    }

}
