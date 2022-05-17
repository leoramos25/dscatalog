package com.leords.dscatalog.resources.exceptions;

import com.leords.dscatalog.services.exceptions.DatabaseException;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        var status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError();
        
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Resource not found");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseIntegrationViolation(DatabaseException exception, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError();
        
        error.setTimestamp(Instant.now());
        error.setStatus(status.value());
        error.setError("Database violation");
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        
        return ResponseEntity.status(status).body(error);
    }
    
}
