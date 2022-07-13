package com.leords.dscatalog.services;

import com.leords.dscatalog.repositories.ProductRepository;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceIntegrationTest {
    
    @Autowired
    private ProductService service;
    
    @Autowired
    private ProductRepository repository;
    
    private Long existingId;
    private Long categoryId;
    private Long nonExistingId;
    private Long countTotalProducts;
    
    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 0L;
        countTotalProducts = 25L;
        categoryId = 1L;
    }
    
    @Test
    void findAllProductShouldReturnPageWhenPage0Size10() {
        var pageRequest = PageRequest.of(0, 10);
        
        var result = service.findAllProducts(categoryId, pageRequest);
        
        assertFalse(result.isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }
    
    @Test
    void findAllProductsShouldReturnEmptyPageWhenPageDoesNotExist() {
        var pageRequest = PageRequest.of(50, 10);
        
        var result = service.findAllProducts(categoryId, pageRequest);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    void findAllProductsShouldReturnOrderedPageWhenSortByName() {
        var pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        
        var result = service.findAllProducts(categoryId, pageRequest);
        
        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
    
    @Test
    void deleteProductShouldDeleteResourceWhenIdExists() {
        service.deleteProduct(existingId);
        
        assertEquals(countTotalProducts - 1, repository.count());
    }
    
    @Test
    void deleteProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteProduct(nonExistingId);
        });
        assertEquals(countTotalProducts, repository.count());
    }
    
}
