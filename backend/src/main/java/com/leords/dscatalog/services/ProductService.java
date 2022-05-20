package com.leords.dscatalog.services;

import com.leords.dscatalog.dto.CategoryDTO;
import com.leords.dscatalog.dto.ProductDTO;
import com.leords.dscatalog.entities.Product;
import com.leords.dscatalog.repositories.CategoryRepository;
import com.leords.dscatalog.repositories.ProductRepository;
import com.leords.dscatalog.services.exceptions.DatabaseException;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllProducts(Pageable pageable) {
        var productList = productRepository.findAll(pageable);
        return productList.map(ProductDTO::new);
    }
    
    @Transactional(readOnly = true)
    public ProductDTO findProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product, product.getCategories());
    }
    
    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        var entity = new Product();
        dtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }
    
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        try {
            var entity = productRepository.getOne(id);
            dtoToEntity(dto, entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException error) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }
    
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException error) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException error) {
            throw new DatabaseException("Integrity violation");
        }
    }
    
    private void dtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());
        
        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            var category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }
    
}
