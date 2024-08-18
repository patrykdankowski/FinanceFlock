package com.patrykdankowski.financeflock.expense_category.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
@Getter
public class ExpenseCategoryDto {
    @Length(min = 3, max = 25)
    @NotBlank
    private String categoryName;

}
