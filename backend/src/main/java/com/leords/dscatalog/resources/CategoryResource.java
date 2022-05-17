package com.leords.dscatalog.resources;

import com.leords.dscatalog.dto.CategoryDTO;
import com.leords.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
    
    @Autowired
    private CategoryService service;
    
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAllCategories() {
        var categoriesList = service.findAllCategories();
        return ResponseEntity.ok().body(categoriesList);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable(value = "id") Long id) {
        var categoryDto = service.findCategoryById(id);
        return ResponseEntity.ok().body(categoryDto);
    }
    
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO dto) {
        dto = service.createCategory(dto);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }
    
}
