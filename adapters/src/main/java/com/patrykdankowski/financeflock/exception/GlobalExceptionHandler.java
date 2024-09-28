package com.patrykdankowski.financeflock.exception;

import com.patrykdankowski.financeflock.auth.exception.CustomJwtException;
import com.patrykdankowski.financeflock.auth.exception.PasswordValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.MaxUserCountInBudgetGroupException;
import com.patrykdankowski.financeflock.budgetgroup.exception.SelfManagementInGroupException;
import com.patrykdankowski.financeflock.common.AppConstants;
import com.patrykdankowski.financeflock.common.ErrorDetails;
import com.patrykdankowski.financeflock.expense.exception.ErrorDuringFetchingLocationFromIpException;
import com.patrykdankowski.financeflock.expense.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.expense.exception.ExpenseValidationException;
import com.patrykdankowski.financeflock.user.exception.AdminToggleShareDataException;
import com.patrykdankowski.financeflock.user.exception.BadRoleException;
import com.patrykdankowski.financeflock.user.exception.ToEarlyShareDataPreferenceException;
import com.patrykdankowski.financeflock.user.exception.UserAlreadyExistsException;
import com.patrykdankowski.financeflock.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
class GlobalExceptionHandler {

    private GlobalExceptionHandler() {
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return setErrorDetails("Cannot find user",
                userNotFoundException.getDetails(),
                HttpStatus.CONFLICT);

    }

    @ExceptionHandler(BudgetGroupNotFoundException.class)
    ResponseEntity<ErrorDetails> handleBudgetGroupNotFoundException(BudgetGroupNotFoundException budgetGroupNotFoundException) {
        return setErrorDetails("Cannot find budget group",
                budgetGroupNotFoundException.getDetails(),
                HttpStatus.CONFLICT);

    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    ResponseEntity<ErrorDetails> handleExpenseNotFoundException(ExpenseNotFoundException expenseNotFoundException) {
        return setErrorDetails("Cannot find expense",
                expenseNotFoundException.getDetails(),
                HttpStatus.CONFLICT);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorDetails> handleHttpMessageNotReadable() {
        return setErrorDetails("The request body is missing or incorrect",
                "Enter right credentials",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordValidationException.class)
    ResponseEntity<ErrorDetails> handlePasswordValidationException(PasswordValidationException passwordValidationException) {


        return setErrorDetails("Exception occurred during registration",
                passwordValidationException.getErrorMessage(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> result = methodArgumentNotValidException.getFieldErrors()
                .stream().map(
                        error -> error.getDefaultMessage()
                ).collect(Collectors.toList());

        return setErrorDetails("Exception occurred during validation",
                result.toString(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorDetails> handleAccessDeniedException() {

        return setErrorDetails("Access denied",
                "You dont have right permissions",
                HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ErrorDetails> handleBadCredentialsException() {

        return setErrorDetails("Exception occurred during logging in",
                "Username or password is incorrect",
                HttpStatus.UNAUTHORIZED);


    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<ErrorDetails> handleUsernameNotFoundException() {

        return setErrorDetails("Enter valid credentials",
                "User was not found",
                HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(CustomJwtException.class)
    ResponseEntity<ErrorDetails> handleCustomJwtExceptions(CustomJwtException customJwtException) {

        return setErrorDetails(AppConstants.ENTER_VALID_JWT_TOKEN_MESSAGE,
                customJwtException.getMessage(),
                customJwtException.getHttpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<ErrorDetails> handleNoHandlerFoundException(NoHandlerFoundException noHandlerFoundException) {
        return setErrorDetails(
                "Endpoint not found",
                noHandlerFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MaxUserCountInBudgetGroupException.class)
    ResponseEntity<ErrorDetails> handleMaxSubUsersCountException(MaxUserCountInBudgetGroupException maxUserCountInBudgetGroupException) {
        return setErrorDetails(
                "You've reached the maximum amount of users in group",
                maxUserCountInBudgetGroupException.getDetails(),
                HttpStatus.FORBIDDEN
        );
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDetails> handleIllegalStateException(IllegalStateException illegalStateException) {
        return setErrorDetails("Exception occurred",
                illegalStateException.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException) {
        return setErrorDetails(userAlreadyExistsException.getMessage() + "  already exists in out db",
                userAlreadyExistsException.getDetails(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BudgetGroupValidationException.class)
    ResponseEntity<ErrorDetails> handleBudgetGroupValidationException(BudgetGroupValidationException budgetGroupValidationException) {
        return setErrorDetails("Exception occurred during budget group validation",
                budgetGroupValidationException.getMessage(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRoleException.class)
    ResponseEntity<ErrorDetails> handleBadRoleException(BadRoleException badRoleException) {
        return setErrorDetails("Exception occurred during bad role",
                badRoleException.getName() + " has wrong role (" + badRoleException.getRoleName() + ")",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ToEarlyShareDataPreferenceException.class)
    ResponseEntity<ErrorDetails> handleToEarlyShareDataPreferenceException(ToEarlyShareDataPreferenceException toEarlyShareDataPreferenceException) {
        return setErrorDetails("Error during toggling share data",
                "Your last request was at " + toEarlyShareDataPreferenceException.getLastSharedData() + " try again at " + toEarlyShareDataPreferenceException.getNextPossibleShareData(),
                HttpStatus.TOO_EARLY);
    }

    @ExceptionHandler(ErrorDuringFetchingLocationFromIpException.class)
    ResponseEntity<ErrorDetails> handleExceptionDuringFetchingLocationFromUserIp() {
        return setErrorDetails("Exception occurred during fetching location from ip",
                "Try again or simply enter a location name for given expense",
                HttpStatus.BAD_GATEWAY);
    }


    @ExceptionHandler(ExpenseValidationException.class)
    ResponseEntity<ErrorDetails> handleExpenseValidationException(ExpenseValidationException expenseValidationException) {

        return setErrorDetails("Exception occurred",
                expenseValidationException.getMessage(),
                HttpStatus.CONFLICT);

    }

    @ExceptionHandler(SelfManagementInGroupException.class)
    ResponseEntity<ErrorDetails> handleSelfManagementInGroupException(SelfManagementInGroupException selfManagementInGroupException) {

        return setErrorDetails("Exception occurred",
                selfManagementInGroupException.getExceptionDescription(),
                HttpStatus.CONFLICT);

    }

    @ExceptionHandler(AdminToggleShareDataException.class)
    ResponseEntity<ErrorDetails> handleAdminToggleShareDataException(AdminToggleShareDataException adminToggleShareDataException) {

        return setErrorDetails("Error during toggling share data",
                "As group admin u are not able to toggle your share data",
                HttpStatus.CONFLICT);

    }


    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorDetails> handleException(Exception exception) {
        return setErrorDetails(exception.getClass().toString(),
                exception.getMessage(),
                HttpStatus.CONFLICT);
    }


    private ResponseEntity<ErrorDetails> setErrorDetails(String message, String details, HttpStatus httpStatus) {
        var errorDetails = new ErrorDetails(new Date(), message, details, httpStatus);
        return new ResponseEntity<>(errorDetails, httpStatus);
    }

}