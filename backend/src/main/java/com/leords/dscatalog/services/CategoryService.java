package com.leords.dscatalog.services;

import com.leords.dscatalog.dto.CategoryDTO;
import com.leords.dscatalog.entities.Category;
import com.leords.dscatalog.repositories.CategoryRepository;
import com.leords.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository repository;
    
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAllCategories() {
        var categoriesList = repository.findAll();
        return categoriesList.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CategoryDTO findCategoryById(Long id) {
        var category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(category);
    }
    
    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        var entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }
    
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        try {
            var entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch(EntityNotFoundException error) {
            throw new ResourceNotFoundException("Id not found " + dto.getId());
        }
    }
    
}
