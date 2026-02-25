package com.ecommerce_refactor.repository;

import com.ecommerce_refactor.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> getProductById(String productId);
    List<Product> getAllProducts();
}
