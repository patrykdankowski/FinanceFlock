package com.patrykdankowski.financeflock.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDomainServicesConfig {

    @Bean
    public UserMembershipDomainPort userMembershipDomain() {
        return new UserMembershipDomainAdapter();
    }

}
