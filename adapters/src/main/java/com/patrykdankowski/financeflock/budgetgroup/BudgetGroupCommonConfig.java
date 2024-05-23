package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BudgetGroupCommonConfig {

    @Bean
    public CommonDomainServicePort commonDomainService() {
        return new CommonDomainServiceAdapter();
    }

    @Bean
    public BudgetGroupFactory budgetGroupFactory(){
        return new BudgetGroupFactory();
    }

}
