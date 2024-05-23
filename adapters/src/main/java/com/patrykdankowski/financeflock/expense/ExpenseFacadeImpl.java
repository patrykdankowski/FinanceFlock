package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.auth.AuthenticationServicePort;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseFacadeImpl implements ExpenseFacade {

    ExpenseFacadeImpl(
            final ExpenseManagementDomainPort expenseManagementDomain,
            final ExpenseCommandRepositoryPort expenseCommandRepository,
            final AuthenticationServicePort authenticationService,
            final ExpenseCommandServicePort expenseCommandService, final ExpenseGeolocationServicePort expenseGeolocationService) {

        this.expenseManagementDomain = expenseManagementDomain;
        this.expenseCommandRepository = expenseCommandRepository;
        this.authenticationService = authenticationService;
        this.expenseCommandService = expenseCommandService;
        this.expenseGeolocationService = expenseGeolocationService;
    }


    private final ExpenseManagementDomainPort expenseManagementDomain;
    private final ExpenseCommandRepositoryPort expenseCommandRepository;
    private final AuthenticationServicePort authenticationService;
    private final ExpenseCommandServicePort expenseCommandService;
    private final ExpenseGeolocationServicePort expenseGeolocationService;

    @Override
    public long addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp) {

        final UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        final ExpenseDtoWriteModel expenseDtoValidated = expenseGeolocationService.validateAndPrepareExpense(expenseDtoWriteModel, userIp);
        final ExpenseDomainEntity expenseDomainEntity = expenseManagementDomain.addExpense(expenseDtoValidated, userFromContext);
        return expenseCommandRepository.save(expenseDomainEntity).getId();


    }

    @Override
    public void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto) {

        final ExpenseDomainEntity expenseDomainEntity = expenseCommandService.retrieveExpenseById(id);
        final UserDomainEntity userFromContext = authenticationService.getUserFromContext();

        expenseManagementDomain.updateExpense(expenseSourceDto,
                expenseDomainEntity,
                userFromContext);

        expenseCommandRepository.save(expenseDomainEntity);
    }


}
