package com.leords.dscatalog.dto;

import com.leords.dscatalog.services.validation.annotations.CreateUserValid;

@CreateUserValid
public class CreateUserDTO extends UserDTO {
    private static final long serialVersionUID = 6033541029413918615L;
    
    private String password;
    
    public CreateUserDTO() {
        super();
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
