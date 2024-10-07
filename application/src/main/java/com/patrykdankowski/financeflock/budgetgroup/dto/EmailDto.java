package com.patrykdankowski.financeflock.budgetgroup.dto;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailDto {

    @Email
    private String email;
}
