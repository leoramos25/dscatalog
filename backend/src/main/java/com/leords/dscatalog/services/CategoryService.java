package com.leords.dscatalog.services;

import com.leords.dscatalog.dto.CategoryDTO;
import com.leords.dscatalog.repositories.CategoryRepository;
import com.leords.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository repository;
    
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        var categoriesList = repository.findAll();
        return categoriesList.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        var category = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new CategoryDTO(category);
    }
    
}
