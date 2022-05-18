package com.leords.dscatalog.services;

import com.leords.dscatalog.dto.ProductDTO;
import com.leords.dscatalog.entities.Product;
import com.leords.dscatalog.repositories.ProductRepository;
import com.leords.dscatalog.services.exceptions.DatabaseException;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;
    
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllProducts(PageRequest pageRequest) {
        var productList = repository.findAll(pageRequest);
        return productList.map(ProductDTO::new);
    }
    
    @Transactional(readOnly = true)
    public ProductDTO findProductById(Long id) {
        var product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product, product.getCategories());
    }
    
    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        var entity = new Product();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }
    
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        try {
            var entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setPrice(dto.getPrice());
            entity.setImgUrl(dto.getImgUrl());
            return new ProductDTO(entity);
        } catch (EntityNotFoundException error) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }
    
    public void deleteProduct(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException error) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException error) {
            throw new DatabaseException("Integrity violation");
        }
    }
    
}
