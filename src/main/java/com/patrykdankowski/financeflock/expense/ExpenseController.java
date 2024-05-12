package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.geolocationAPI.ExpenseGeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@Slf4j
class ExpenseController implements ExpenseControllerApi {

    private final ExpenseFacade expenseFacade;
    private final ExpenseGeolocationService geolocationService;

    @Override
    public ResponseEntity<URI> addExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                                          HttpServletRequest request) {

        String userIp = geolocationService.getUserIpAddress(request);
        final long expenseId = expenseFacade.addExpense(expenseDtoWriteModel, userIp);

        URI location = ServletUriComponentsBuilder
                //TODO zmodyfikować url z post na get w ścieżce
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(expenseId)
                .toUri();

        return ResponseEntity.created(location).body(location);
    }

    @Override
    public ResponseEntity<String> updateExpense(Long id,
                                                ExpenseDtoWriteModel expenseDtoWriteModel) {

        expenseFacade.updateExpense(id, expenseDtoWriteModel);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Resource updated");
    }
}
