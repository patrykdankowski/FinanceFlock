package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.CommonDomainServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BudgetGroupDomainServicesConfiguration {

    private final CommonDomainServicePort commonDomainService;

    public BudgetGroupDomainServicesConfiguration(CommonDomainServicePort commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    @Bean
    public BudgetGroupManagementDomainPort budgetGroupManagementDomain() {
        return new BudgetGroupManagementDomainAdapter(new BudgetGroupFactory(), commonDomainService);
    }


    @Bean
    public BudgetGroupMembershipDomainPort budgetGroupMembershipDomain() {
        return new BudgetGroupMembershipDomainAdapter(commonDomainService);
    }


}
