package com.leords.dscatalog.resources;

import com.leords.dscatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
    
    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        var categoriesList = new ArrayList<Category>();
        categoriesList.add(new Category(1L, "Books"));
        categoriesList.add(new Category(2L, "Electronics"));
        return ResponseEntity.ok().body(categoriesList);
    }
    
}
