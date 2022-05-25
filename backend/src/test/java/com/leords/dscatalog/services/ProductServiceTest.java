package com.leords.dscatalog.services;

import com.leords.dscatalog.dto.ProductDTO;
import com.leords.dscatalog.entities.Category;
import com.leords.dscatalog.entities.Product;
import com.leords.dscatalog.repositories.CategoryRepository;
import com.leords.dscatalog.repositories.ProductRepository;
import com.leords.dscatalog.services.exceptions.DatabaseException;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import com.leords.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class ProductServiceTest {
    
    @InjectMocks
    private ProductService service;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    private long existingId;
    private long nonExistsId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDto;
    private Category category;
    
    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistsId = 0L;
        dependentId = 4L;
        product = Factory.createProduct();
        productDto = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));
        
        when(productRepository.findAll((Pageable) any())).thenReturn(page);
        
        when(productRepository.save(any())).thenReturn(product);
        
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistsId)).thenReturn(Optional.empty());
        
        when(productRepository.getOne(existingId)).thenReturn(product);
        when(productRepository.getOne(nonExistsId)).thenThrow(EntityNotFoundException.class);
        
        when(categoryRepository.getOne(existingId)).thenReturn(category);
        when(categoryRepository.getOne(nonExistsId)).thenThrow(EntityNotFoundException.class);
        
        doNothing().when(productRepository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistsId);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }
    
    @Test
    void findAllProductsShouldReturnOnePage() {
        var pageable = PageRequest.of(0, 10);
        var products = service.findAllProducts(pageable);
        
        assertNotNull(products);
        assertEquals(products.getTotalPages(), 1);
        verify(productRepository, times(1)).findAll(pageable);
    }
    
    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() {
        var productDto = service.findProductById(existingId);
        
        verify(productRepository, times(1)).findById(existingId);
        assertNotNull(productDto);
    }
    
    @Test
    void findByIdThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findProductById(nonExistsId);
        });
        
        verify(productRepository, times(1)).findById(nonExistsId);
    }
    
    @Test
    void updateProductShouldReturnProductDtoWhenIdExist() {
        var result = service.updateProduct(existingId, productDto);
        
        assertNotNull(result);
        verify(productRepository, times(1)).getOne(existingId);
    }
    
    @Test
    void updateProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.updateProduct(nonExistsId, productDto);
        });
        
        verify(productRepository, times(1)).getOne(nonExistsId);
    }
    
    @Test
    void deleteByIdShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> {
            service.deleteProduct(existingId);
        });
        
        verify(productRepository, times(1)).deleteById(existingId);
    }
    
    @Test
    void deleteByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteProduct(nonExistsId);
        });
        
        verify(productRepository, times(1)).deleteById(nonExistsId);
    }
    
    @Test
    void deleteByIdShouldThrowDatabaseExceptionWhenDependentId() {
        assertThrows(DatabaseException.class, () -> {
            service.deleteProduct(dependentId);
        });
        
        verify(productRepository, times(1)).deleteById(dependentId);
    }
}