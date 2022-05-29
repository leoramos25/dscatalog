package com.leords.dscatalog.dto;

import com.leords.dscatalog.entities.Role;

import java.io.Serializable;

public class RoleDTO implements Serializable {
    private static final long serialVersionUID = -5807259609174987802L;
    
    private Long id;
    private String authority;
    
    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }
    
    public RoleDTO(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAuthority() {
        return authority;
    }
    
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
