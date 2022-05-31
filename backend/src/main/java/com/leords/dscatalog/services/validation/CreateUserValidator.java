package com.leords.dscatalog.services.validation;

import com.leords.dscatalog.dto.CreateUserDTO;
import com.leords.dscatalog.repositories.UserRepository;
import com.leords.dscatalog.resources.exceptions.FieldMessage;
import com.leords.dscatalog.services.validation.annotations.CreateUserValid;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CreateUserValidator implements ConstraintValidator<CreateUserValid, CreateUserDTO> {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void initialize(CreateUserValid constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(CreateUserDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        List<FieldMessage> list = new ArrayList<>();
        
        var user = userRepository.findByEmail(dto.getEmail());
        
        if(user != null) {
            list.add(new FieldMessage("email", "Email already exists"));
        }
        
        for (FieldMessage e : list) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        
        return list.isEmpty();
    }
}
