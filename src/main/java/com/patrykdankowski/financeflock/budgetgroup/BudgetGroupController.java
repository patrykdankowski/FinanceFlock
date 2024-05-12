package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDtoReadModel;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserDtoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Void> deleteBudgetGroup() {
        budgetGroupFacade.closeBudgetGroup();
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> addUserToGroup(EmailDtoReadModel emailDto) {
        budgetGroupFacade.addUserToGroup(emailDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("User successfully added to group");
    }

    @Override
    public ResponseEntity<String> removeUserFromGroup(EmailDtoReadModel emailDto) {
        budgetGroupFacade.removeUserFromGroup(emailDto.getEmail());
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
