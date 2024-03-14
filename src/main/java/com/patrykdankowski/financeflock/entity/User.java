package com.patrykdankowski.financeflock.entity;

import com.patrykdankowski.financeflock.security.validation.ValidPassword;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    private String secondName;

    @ValidPassword
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;


//    private LocalDateTime lastLoggedInAt;
}
