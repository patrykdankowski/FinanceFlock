package com.patrykdankowski.financeflock.expense;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpenseDomainServicesConfig {

    @Bean
    public ExpenseManagementDomainPort expenseManagementDomain(){
        return new ExpenseManagementDomainAdapter();
    }
}
