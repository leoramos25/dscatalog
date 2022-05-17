package com.leords.dscatalog.services.exceptions;

public class EntityNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = -1279857500944894799L;
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
}
