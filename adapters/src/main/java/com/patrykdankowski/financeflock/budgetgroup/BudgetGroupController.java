package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgetGroups")
@RequiredArgsConstructor
class BudgetGroupController implements BudgetGroupControllerApi {
    private final BudgetGroupFacade budgetGroupFacade;
    private final BudgetGroupQueryService budgetGroupQueryService;


    @Override
    public ResponseEntity<String> createBudgetGroup(BudgetGroupRequest budgetGroupRequest) {
        budgetGroupFacade.createBudgetGroup(budgetGroupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Budget group created");
    }

    @Override
    public ResponseEntity<Void> deleteBudgetGroup(Long id) {
        budgetGroupFacade.closeBudgetGroup(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> addUserToGroup(Long id, EmailDtoReadModel emailDto) {
        budgetGroupFacade.addUserToGroup(emailDto.getEmail(), id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("User successfully added to group");
    }

    @Override
    public ResponseEntity<String> removeUserFromGroup(Long id, EmailDtoReadModel emailDto) {
        budgetGroupFacade.removeUserFromGroup(emailDto.getEmail(), id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("User successfully removed from group");
    }

    @Override
    public ResponseEntity<List<UserDtoResponse>> getListOfMembersInBudgetGroup() {
        List<UserDtoResponse> list = budgetGroupQueryService.listOfUsersInGroup();
        return ResponseEntity.status(HttpStatus.OK)
                .body(list);
    }

    @Override
    public ResponseEntity<List<UserDtoProjections>> getList() {

        List<UserDtoProjections> budgetGroupExpenses = budgetGroupQueryService.getBudgetGroupExpenses();
        return ResponseEntity.status(HttpStatus.OK)
                .body(budgetGroupExpenses);

    }


}
