package com.develop.api.services.impl;

import com.develop.api.exception.InvalidProductException;
import com.develop.api.exception.ProductNotFoundException;
import com.develop.api.model.Product;
import com.develop.api.repository.ProductRepo;
import com.develop.api.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public List<Product> findAllProductsByName(String name) {
        return productRepo.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findProductById(Long id) {
        return productRepo.findById(id);
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        try {
            return productRepo.save(product);
        } catch (Exception e) {
            throw new InvalidProductException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        return productRepo.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setCount(product.getCount());
                    return productRepo.save(existingProduct);
                }).orElseThrow(() -> new ProductNotFoundException("Product not found with id " + id));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
