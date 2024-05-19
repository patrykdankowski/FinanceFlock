package com.patrykdankowski.financeflock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinanceFlockApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceFlockApplication.class, args);





    }

}