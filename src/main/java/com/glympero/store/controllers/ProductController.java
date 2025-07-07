package com.glympero.store.controllers;

import com.glympero.store.dtos.*;
import com.glympero.store.entities.Product;
import com.glympero.store.mappers.ProductMapper;
import com.glympero.store.repositories.CategoryRepository;
import com.glympero.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping("")
    public Iterable<ProductDto> getProducts(
            @RequestParam(required = false, defaultValue = "", name = "categoryId") Long categoryId
    ) {

        List<Product> products;
        if (categoryId == null) {
//            products = productRepository.findAll();
            products = productRepository.findAllWithCategory();
        } else {
            products = productRepository.findCategoryById(categoryId);
        }

        return products.stream().map(productMapper::toDto).toList();
//        return products.filter(product -> product.getCategoryId().equals(categoryId)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder
    ) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(null);
        }


        var product = productMapper.toEntity(productDto);
        product.setCategory(category);

        productRepository.save(product);

        productDto.setId(product.getId());
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDto productDto){

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(null);
        }

        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
