package com.leords.dscatalog.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = -1279857500944894799L;
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
