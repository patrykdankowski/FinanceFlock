package com.patrykdankowski.financeflock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@EnableAsync
public class Config {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable("system");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
