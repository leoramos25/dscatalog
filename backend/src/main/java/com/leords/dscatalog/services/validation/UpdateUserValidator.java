package com.leords.dscatalog.services.validation;

import com.leords.dscatalog.dto.UpdateUserDTO;
import com.leords.dscatalog.repositories.UserRepository;
import com.leords.dscatalog.resources.exceptions.FieldMessage;
import com.leords.dscatalog.services.validation.annotations.UpdateUserValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateUserValidator implements ConstraintValidator<UpdateUserValid, UpdateUserDTO> {
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void initialize(UpdateUserValid constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        var pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        var userId = Long.parseLong(pathVariables.get("id"));
        
        List<FieldMessage> list = new ArrayList<>();
    
        var user = userRepository.findByEmail(dto.getEmail());
    
        if(user != null && userId != user.getId()) {
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
