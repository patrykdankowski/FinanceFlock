package com.patrykdankowski.financeflock;

import com.patrykdankowski.financeflock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing

public class FinanceFlockApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceFlockApplication.class, args);





    }

}
