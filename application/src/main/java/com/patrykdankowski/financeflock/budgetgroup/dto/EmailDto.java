package com.patrykdankowski.financeflock.budgetgroup.dto;


import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailDto {

    @Email
    private String email;
}
