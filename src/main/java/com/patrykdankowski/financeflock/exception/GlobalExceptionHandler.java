package com.patrykdankowski.financeflock.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import static com.patrykdankowski.financeflock.constants.AppConstants.ENTER_VALID_JWT_TOKEN_MESSAGE;
import static com.patrykdankowski.financeflock.constants.AppConstants.MAX_BUDGET_GROUP_SIZE;

@RestControllerAdvice
class GlobalExceptionHandler {

    private GlobalExceptionHandler() {
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        return setErrorDetails(resourceNotFoundException.getMessage(),
                resourceNotFoundException.getDetails(),
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

        return setErrorDetails(ENTER_VALID_JWT_TOKEN_MESSAGE,
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
        String details = String.format("You are only allowed to add up %d  users. Remove one of existing users first", MAX_BUDGET_GROUP_SIZE - 1);
        return setErrorDetails(
                "You've reached the maximum amount of users in group",
                details,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException userNotFoundException) {


        return setErrorDetails("Something went wrong",
                userNotFoundException.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorDetails> handleIllegalStateException(IllegalStateException illegalStateException) {
        return setErrorDetails("Exception occurred",
                illegalStateException.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotBelongToUserException.class)
    ResponseEntity<ErrorDetails> handleIllegalStateException(ResourceNotBelongToUserException resourceNotBelongToUserException) {
        return setErrorDetails("Exception occurred",
                "That resource does not belong to you",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    ResponseEntity<ErrorDetails> handleResourceAlreadyExistsException(ResourceAlreadyExists resourceAlreadyExists) {
        return setErrorDetails(resourceAlreadyExists.getMessage() + "  already exists in out db",
                "Please enter a different email address.",
                HttpStatus.CONFLICT);
    }

    private ResponseEntity<ErrorDetails> setErrorDetails(String message, String details, HttpStatus httpStatus) {
        var errorDetails = new ErrorDetails(new Date(), message, details, httpStatus);
        return new ResponseEntity<>(errorDetails, httpStatus);
    }

}