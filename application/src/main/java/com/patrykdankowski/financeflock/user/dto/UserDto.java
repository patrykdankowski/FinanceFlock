package com.patrykdankowski.financeflock.user.dto;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

//    private String name;
//    private List<ExpenseDto> expenses;
//    private BigDecimal totalExpensesForUser;

    private String name;
    private List<ExpenseDto> expenses;
    private BigDecimal totalExpensesForUser;

    // Konstruktor dla zapytania, które zwraca sumę wydatków bez listy
    public UserDto(String name, BigDecimal totalExpensesForUser) {
        this.name = name;
        this.totalExpensesForUser = totalExpensesForUser;
        this.expenses = new ArrayList<>();
    }

    // Konstruktor dla zapytania, które zwraca wydatki
    public UserDto(String name, String description, BigDecimal amount, String location) {
        this.name = name;
        this.expenses = new ArrayList<>();
        this.expenses.add(new ExpenseDto(description, amount, location));
        this.totalExpensesForUser = amount; // Tymczasowa suma dla tego pojedynczego wydatku
    }
}
