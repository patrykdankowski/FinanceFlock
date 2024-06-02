package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.CommonDomainServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDomainServicesConfig {

    @Bean
    public UserMembershipDomainPort userMembershipDomain() {
        return new UserMembershipDomainAdapter(new CommonDomainServiceAdapter());
    }

}
