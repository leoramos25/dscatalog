package com.leords.dscatalog.services.validation.annotations;

import com.leords.dscatalog.services.validation.CreateUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CreateUserValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUserValid {
    String message() default "Validation error";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
