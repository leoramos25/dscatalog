package com.leords.dscatalog.repositories;

import com.leords.dscatalog.entities.Category;
import com.leords.dscatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT product FROM Product product INNER JOIN product.categories cats WHERE "
            + "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
            + "(LOWER(product.name) LIKE LOWER(CONCAT('%',:name,'%')))")
    Page<Product> search(List<Category> categories, String name, Pageable pageable);
    
    @Query("SELECT product FROM Product product JOIN FETCH product.categories WHERE product IN :products")
    List<Product> searchProductsWithCategories(List<Product> products);
}
