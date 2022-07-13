package com.leords.dscatalog.tests;

import com.leords.dscatalog.dto.ProductDTO;
import com.leords.dscatalog.entities.Category;
import com.leords.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFactory {
    
    public static Product createProduct() {
        var product = new Product(
                1L,
                "Phone",
                "Good phone",
                800.00,
                "http://img.com/img.png",
                Instant.parse("2022-07-12T03:00:00Z")
        );
        
        product.getCategories().add(new Category(2L, "Electronics"));
        
        return product;
    }
    
    public static ProductDTO createProductDTO() {
        var product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
    
    public static Category createCategory() {
        return new Category(2L, "Electronics");
    }
    
}
