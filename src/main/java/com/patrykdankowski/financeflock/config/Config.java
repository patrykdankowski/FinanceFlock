package com.patrykdankowski.financeflock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class Config {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable("system");
    }
}
