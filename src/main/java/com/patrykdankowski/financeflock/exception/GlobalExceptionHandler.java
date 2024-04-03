package com.patrykdankowski.financeflock.exception;

import com.patrykdankowski.financeflock.dto.ErrorDetails;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException emailAlreadyExistsException) {
        return setErrorDetails("Enter valid email",
                emailAlreadyExistsException.getMessage() + " already exists in our database",
                HttpStatus.CONFLICT);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleHttpMessageNotReadable() {
        return setErrorDetails("The request body is missing or incorrect",
                "Enter right credentials",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ErrorDetails> handlePasswordValidationException(PasswordValidationException passwordValidationException) {


        return setErrorDetails("Exception occurred during registration",
                passwordValidationException.getMessage(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> result = methodArgumentNotValidException.getFieldErrors()
                .stream().map(
                        error -> error.getDefaultMessage()
                ).collect(Collectors.toList());
        return setErrorDetails("Exception occurred during validation",
                result.toString(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleNotValidException(Authentication authentication) {

        return setErrorDetails("Access denied",
                "You dont have permission to enter here with " + authentication.getAuthorities(),
                HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleNotValidCredentialsException() {

        return setErrorDetails("Exception occurred during logging in",
                "Username or password is incorrect",
                HttpStatus.UNAUTHORIZED);


    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException() {

        return setErrorDetails("Enter valid credentials",
                "Username or password is incorrect",
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<ErrorDetails> handleJwtExceptions(CustomJwtException customJwtException) {

        return setErrorDetails(ENTER_VALID_JWT_TOKEN_MESSAGE,
                customJwtException.getMessage(),
                customJwtException.getHttpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetails> handleNoHandlerFoundException(NoHandlerFoundException noHandlerFoundException) {
        return setErrorDetails(
                "Endpoint not found",
                noHandlerFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MaxUserCountInBudgetGroupException.class)
    public ResponseEntity<ErrorDetails> handleMaxSubUsersCountException() {
        String details = String.format("You are only allowed to add up %d  users. Remove one of existing users first", MAX_BUDGET_GROUP_SIZE - 1);
        return setErrorDetails(
                "You've reached the maximum amount of users in group",
                details,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException userNotFoundException) {


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


    private ResponseEntity<ErrorDetails> setErrorDetails(String message, String details, HttpStatus httpStatus) {
        var errorDetails = new ErrorDetails(new Date(), message, details, httpStatus);
        return new ResponseEntity<>(errorDetails, httpStatus);
    }

}