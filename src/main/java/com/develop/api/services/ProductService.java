package com.develop.api.services;

import com.develop.api.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();

    List<Product> findAllProductsByName(String name);

    Optional<Product> findProductById(Long id);

    Product saveProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
