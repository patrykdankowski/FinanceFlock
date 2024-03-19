package com.patrykdankowski.financeflock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception,
                                                                          WebRequest webRequest) {

        var errorDetails = new ErrorDetails(new Date(),
                webRequest.getDescription(false),
                exception.getMessage() + " already exists in our database",
                HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ErrorDetails> handlePasswordValidationException(PasswordValidationException exception,
                                                                          WebRequest webRequest) {

        var errorDetails = new ErrorDetails(new Date(),
                webRequest.getDescription(false),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleNotValidException(MethodArgumentNotValidException exception,
                                                                WebRequest webRequest){
        var errorDetails = new ErrorDetails(new Date(),
                "Your email is not valid",
                "Enter valid email",
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    // Poprzez AuthenticationEntryPoint
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleUserNotFoundException(UsernameNotFoundException exception,
//                                                                WebRequest webRequest){
//        var errorDetails = new ErrorDetails(new Date(),
//                "Your email" +exception.getMessage()+"doesn't exist in our data base",
//                "Enter valid email or register",
//                HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
//    }

}
