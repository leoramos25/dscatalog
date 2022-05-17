package com.leords.dscatalog.resources;

import com.leords.dscatalog.dto.CategoryDTO;
import com.leords.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
    
    @Autowired
    private CategoryService service;
    
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        var categoriesList = service.findAll();
        return ResponseEntity.ok().body(categoriesList);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findOne(@PathVariable(value = "id") Long id) {
        var categoryDto = service.findById(id);
        return ResponseEntity.ok().body(categoryDto);
    }
    
}
