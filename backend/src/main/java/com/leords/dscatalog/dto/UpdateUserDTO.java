package com.leords.dscatalog.dto;

import com.leords.dscatalog.services.validation.annotations.UpdateUserValid;

@UpdateUserValid
public class UpdateUserDTO extends UserDTO {
    private static final long serialVersionUID = 8739704203130930959L;
    
    private String password;
    
    public UpdateUserDTO() {
        super();
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
