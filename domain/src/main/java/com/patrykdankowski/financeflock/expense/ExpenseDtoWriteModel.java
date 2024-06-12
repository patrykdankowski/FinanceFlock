package com.patrykdankowski.financeflock.expense;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class ExpenseDtoWriteModel {
    @Length(min = 2)
    @NotBlank(groups = onCreate.class)
    private String description;
    @Digits(groups = onCreate.class, integer = 7, fraction = 2)
    private BigDecimal amount;
    @NotBlank(groups = onCreate.class)
    private String location;
    private LocalDateTime expenseDate;

    public interface onCreate {
    }

    //TODO pozbyć sie adnotacji hibernate.
//    public interface onUpdate {
//    }


    public String getDescription() {
        return description;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(final LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }
}
