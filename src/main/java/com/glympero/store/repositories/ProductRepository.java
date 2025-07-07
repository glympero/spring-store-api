package com.glympero.store.repositories;

import com.glympero.store.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findCategoryById(Long categoryId);

    // Hibernate by default does an extra query to fetch the category
    // To avoid this, we can use @EntityGraph or @Query with JOIN FETCH
    // Example with @Query:
//     @Query("SELECT p FROM Product p JOIN FETCH p.category") // without entity graph
     @EntityGraph(attributePaths = "category")
     @Query("SELECT p FROM Product p")
     List<Product> findAllWithCategory();

}