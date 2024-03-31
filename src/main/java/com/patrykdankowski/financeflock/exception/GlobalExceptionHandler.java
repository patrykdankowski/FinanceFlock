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

import static com.patrykdankowski.financeflock.constants.AppConstants.MAX_SUB_USERS;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String ENTER_VALID_JWT_TOKEN_MESSAGE = "Enter valid JWT token";

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {

//        var errorDetails = new ErrorDetails(new Date(),
//                "Enter valid email",
//                exception.getMessage() + " already exists in our database",
//                HttpStatus.CONFLICT);
//        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
        return setErrorDetails("Enter valid email",
                exception.getMessage() + " already exists in our database",
                HttpStatus.CONFLICT);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
//        ErrorDetails errorDetails = new ErrorDetails(
//                new Date(),
//                "The request body is missing or incorrect",
//                "Enter right credentials",
//                HttpStatus.BAD_REQUEST
//        );
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        return setErrorDetails("The request body is missing or incorrect",
                "Enter right credentials",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ErrorDetails> handlePasswordValidationException(PasswordValidationException exception) {

//        var errorDetails = new ErrorDetails(new Date(),
//                "Password is not valid",
//                exception.getMessage(),
//                HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        return setErrorDetails("Password is not valid",
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleNotValidException(MethodArgumentNotValidException exception) {
//        var errorDetails = new ErrorDetails(new Date(),
//                "Your email " + exception.getMessage() + "is not valid",
//                "Enter valid email",
//                HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        return setErrorDetails("Password is not valid",
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleNotValidException(Authentication authentication) {
//        var errorDetails = new ErrorDetails(new Date(),
//                "Access denied",
//                "You dont have permission to enter here with " + authentication.getAuthorities(),
//                HttpStatus.FORBIDDEN);
//        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
        return setErrorDetails("Access denied",
                "You dont have permission to enter here with " + authentication.getAuthorities(),
                HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleNotValidCredentialsException() {
//        var errorDetails = new ErrorDetails(new Date(),
//                "Enter valid credentials",
//                "Username or password is incorrect",
//                HttpStatus.UNAUTHORIZED);
//        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        return setErrorDetails("Password is not valid",
                "Username or password is incorrect",
                HttpStatus.UNAUTHORIZED);


    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
//        var errorDetails = new ErrorDetails(new Date(),
//                "Enter valid credentials",
//                "Username or password is incorrect",
//                HttpStatus.UNAUTHORIZED);
//        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
        return setErrorDetails("Enter valid credentials",
                "Username or password is incorrect",
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<ErrorDetails> handleJwtExceptions(CustomJwtException customJwtException) {

//        var errorDetails = new ErrorDetails(new Date(),
//                ENTER_VALID_JWT_TOKEN_MESSAGE,
//                customJwtException.getMessage(),
//                customJwtException.getHttpStatus());
//        return new ResponseEntity<>(errorDetails, customJwtException.getHttpStatus());
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

    @ExceptionHandler(MaxSubUsersCountException.class)
    public ResponseEntity<ErrorDetails> handleMaxSubUsersCountException(MaxSubUsersCountException maxSubUsersCountException) {
        String details = String.format("You are only allowed to add up %d sub users. Remove one of existing sub users first", MAX_SUB_USERS);
        return setErrorDetails(
                "You've reached the maximum amount of sub users",
                details,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleSubUserNotBelongToMainUserException(UserNotFoundException userNotFoundException) {
        return setErrorDetails("Something went wrong",
               "User with given Id doesnt exist in our db",
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SubUserNotBelongToMainUserException.class)
    public ResponseEntity<ErrorDetails> handleSubUserNotBelongToMainUserException(SubUserNotBelongToMainUserException subUserNotBelongToMainUserException) {
        return setErrorDetails("Cannot remove",
                subUserNotBelongToMainUserException.getMessage(),
                HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<ErrorDetails> setErrorDetails(String message, String details, HttpStatus httpStatus) {
        var errorDetails = new ErrorDetails(new Date(), message, details, httpStatus);
        return new ResponseEntity<>(errorDetails, httpStatus);
    }

}