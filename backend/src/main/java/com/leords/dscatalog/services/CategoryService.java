package com.leords.dscatalog.services;

import com.leords.dscatalog.entities.Category;
import com.leords.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository repository;
    
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return repository.findAll();
    }
    
}