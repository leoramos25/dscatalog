package com.leords.dscatalog.services.exceptions;

public class DatabaseException extends RuntimeException {
    
    private static final long serialVersionUID = 8778686281967345893L;
    
    public DatabaseException(String message) {
        super(message);
    }
    
}
