package com.leords.dscatalog.repositories;

import com.leords.dscatalog.tests.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository repository;
    
    private long existsId;
    private long nonExistedId;
    private long countTotalProducts;
    
    
    @BeforeEach
    void setup() {
        existsId = 1L;
        nonExistedId = 0L;
        countTotalProducts = 25L;
    }
    
    @Test
    void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        var product = ProductFactory.createProduct();
        
        product.setId(null);
        product = repository.save(product);
        
        assertNotNull(product.getId());
        assertEquals(countTotalProducts + 1, product.getId());
    }
    
    @Test
    void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existsId);
        var find = repository.findById(existsId);
        
        assertFalse(find.isPresent());
    }
    
    @Test
    void deleteShouldThrowEmptyResultDataNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistedId);
        });
    }
    
    @Test
    void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
        var product = repository.findById(existsId);
        
        assertTrue(product.isPresent());
    }
    
    @Test
    void findByIdShouldReturnEmptyOptionalProductWhenIdNotExists() {
        var product = repository.findById(nonExistedId);
        
        assertFalse(product.isPresent());
    }
    
}