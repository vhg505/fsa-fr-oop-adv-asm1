package com.ecommerce_refactor.repository;

import com.ecommerce_refactor.model.Product;
import com.ecommerce_refactor.model.enumeration.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {
    List<Product> products = new ArrayList<>();

    public InMemoryProductRepository(){
        products.add(new Product("P001", "Laptop", 999.99, ProductCategory.ELECTRONICS, 50));
        products.add(new Product("P002", "T-Shirt", 29.99,  ProductCategory.CLOTHING, 50));
        products.add(new Product("P003", "Coffee Beans", 15.99,  ProductCategory.FOOD, 50));
        products.add(new Product("P004", "Headphones", 149.99, ProductCategory.ELECTRONICS, 50));
    }

    @Override
    public Optional<Product> getProductById(String productId) {
        return products.stream().filter(p -> p.getProductId().equals(productId)).findFirst();
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
}
