package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.common.CommonDomainServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpenseDomainServicesConfig {

    @Bean
    public ExpenseManagementDomainPort expenseManagementDomain(){
        return new ExpenseManagementDomainAdapter(new CommonDomainServiceAdapter());
    }
}
