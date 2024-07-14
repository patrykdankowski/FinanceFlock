package com.patrykdankowski.financeflock.auth.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordEqualsValidator.class)
public @interface EqualsPassword {
    String message() default "Password must match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

