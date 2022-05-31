package com.leords.dscatalog.services.validation.annotations;

import com.leords.dscatalog.services.validation.UpdateUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UpdateUserValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateUserValid {
    String message() default "Validation error";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
