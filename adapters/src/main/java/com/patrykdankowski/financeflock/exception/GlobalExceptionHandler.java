package com.patrykdankowski.financeflock.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.patrykdankowski.financeflock.common.AppConstants;
import com.patrykdankowski.financeflock.common.ErrorDetails;
import com.patrykdankowski.financeflock.auth.exception.CustomJwtException;
import com.patrykdankowski.financeflock.auth.exception.PasswordValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.MaxUserCountInBudgetGroupException;
import com.patrykdankowski.financeflock.common.BadRoleException;
import com.patrykdankowski.financeflock.common.ShareDataPreferenceException;
import com.patrykdankowski.financeflock.expense.exception.ErrorDuringFetchingLocationFromIpException;
import com.patrykdankowski.financeflock.expense.exception.ExpenseNotBelongToUserException;
import com.patrykdankowski.financeflock.expense.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.user.exception.UserAlreadyExistsException;
import com.patrykdankowski.financeflock.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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
        String message = String.format("Expense with id %d, doesnt exist in our db", expenseNotFoundException.getId());

        return setErrorDetails("Cannot find expense",
                message,
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
                passwordValidationException.getMessage(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorDetails> handleNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) throws JsonProcessingException {
        List<String> result = methodArgumentNotValidException.getFieldErrors()
                .stream().map(
                        error -> error.getDefaultMessage()
                ).collect(Collectors.toList());

//        Map<String, String> result = new HashMap<>();
//        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(
//                error -> {
//                    String field = ((FieldError) error).getField();
//                    String message = error.getDefaultMessage();
//                    result.put(field, message);
//                }

//        );
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonErrors = objectMapper.writeValueAsString(result);
        return setErrorDetails("Exception occurred during validation",
                result.toString(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorDetails> handleNotValidException(Authentication authentication) {

        return setErrorDetails("Access denied",
                "You dont have permission to enter here with " + authentication.getAuthorities(),
                HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ErrorDetails> handleNotValidCredentialsException() {

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
    ResponseEntity<ErrorDetails> handleJwtExceptions(CustomJwtException customJwtException) {

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
    ResponseEntity<ErrorDetails> handleMaxSubUsersCountException() {
        String details = String.format("You are only allowed to add up %d  users. Remove one of existing users first", AppConstants.MAX_BUDGET_GROUP_SIZE - 1);
        return setErrorDetails(
                "You've reached the maximum amount of users in group",
                details,
                HttpStatus.FORBIDDEN
        );
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDetails> handleIllegalStateException(IllegalStateException illegalStateException) {
        return setErrorDetails("Exception occurred",
                illegalStateException.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpenseNotBelongToUserException.class)
    ResponseEntity<ErrorDetails> handleResourceNotBelongToUserException(ExpenseNotBelongToUserException expenseNotBelongToUserException) {

        String message = String.format("Expense with id %d does not belong to you", expenseNotBelongToUserException.getResourceId());
        String details = String.format("That expense belong to user with id %d", expenseNotBelongToUserException.getUserId());
        return setErrorDetails(message,
                details,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<ErrorDetails> handleResourceAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException) {
        return setErrorDetails(userAlreadyExistsException.getMessage() + "  already exists in out db",
                "Please enter a different email address.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BudgetGroupValidationException.class)
    ResponseEntity<ErrorDetails> handleGroupValidationException(BudgetGroupValidationException budgetGroupValidationException) {
        return setErrorDetails("Exception occurred during budget group validation",
                budgetGroupValidationException.getMessage(),
                HttpStatus.CONFLICT);
    }
    @ExceptionHandler(BadRoleException.class)
    ResponseEntity<ErrorDetails> handleBadRoleException(BadRoleException badRoleException) {
        return setErrorDetails("User has wrong role",
                badRoleException.getMessage()+" has wrong role ("+badRoleException.getRoleName()+")",
                HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ShareDataPreferenceException.class)
    ResponseEntity<ErrorDetails> handleShareDataPreferenceException(ShareDataPreferenceException shareDataPreferenceException) {
        return setErrorDetails("Error during toggling share data",
                "Your last request was at "+ shareDataPreferenceException.getLastSharedData() + " try again at "+ shareDataPreferenceException.getNextPossibleShareData(),
                HttpStatus.TOO_EARLY );
    }

    @ExceptionHandler(ErrorDuringFetchingLocationFromIpException.class)
    ResponseEntity<ErrorDetails> handleException() {
        return setErrorDetails("Exception occurred during fetching location from ip",
                "Try again or simply enter a location name for given expense",
                HttpStatus.BAD_GATEWAY);
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