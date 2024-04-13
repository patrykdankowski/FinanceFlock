package com.patrykdankowski.financeflock.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class ExpenseDto {
    @Length(min = 2)
    @NotBlank(groups = onCreate.class)
    private String description;
    @Digits(groups = onCreate.class, integer = 7, fraction = 2)
    private BigDecimal amount;
    private String location;
    private LocalDateTime expenseDate;

    public interface onCreate {
    }

//    public interface onUpdate {
//    }


}
