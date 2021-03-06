package com.leords.dscatalog.dto;

import com.leords.dscatalog.entities.Category;
import com.leords.dscatalog.entities.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO implements Serializable {
    private static final long serialVersionUID = -7172540041056521256L;
    
    private Long id;
    @Size(min = 2, max = 60, message = "must have between 2 and 60 characters")
    @NotBlank(message = "First name cannot be empty")
    private String name;
    private String description;
    @Positive(message = "Price is positive")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "Product date cannot be future")
    private Instant date;
    private List<CategoryDTO> categories = new ArrayList<>();
    
    public ProductDTO() {
    }
    
    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }
    
    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        date = entity.getDate();
    }
    
    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getImgUrl() {
        return imgUrl;
    }
    
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    
    public Instant getDate() {
        return date;
    }
    
    public void setDate(Instant date) {
        this.date = date;
    }
    
    public List<CategoryDTO> getCategories() {
        return categories;
    }
}
