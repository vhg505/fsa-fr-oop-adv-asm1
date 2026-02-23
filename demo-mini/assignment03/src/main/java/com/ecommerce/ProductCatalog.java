package com.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class ProductCatalog {
    
    // Simulates a slow database query
    public Product getProduct(String productId) {
        simulateSlowQuery();
        System.out.println("DATABASE QUERY: Fetching product " + productId);
        return new Product(productId, "Product " + productId, 29.99);
    }

    // Problem: Every call hits the database, even for the same product.
    // In a single page load, getProduct("P001") might be called 10+ times
    // from different parts of the code (cart, recommendations, reviews).
    //
    // Naive fix: adding a cache Map directly into this class
    // But that violates SRP — ProductCatalog should only handle data access,
    // not caching logic.
    //
    // Also, some users (e.g., admin) should bypass the cache to see real-time data,
    // while regular users can use cached data. There's no access control mechanism.

    private void simulateSlowQuery() {
        try {
            Thread.sleep(100); // Simulate 100ms database latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Product {
    String id;
    String name;
    double price;

    Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=$" + price + "}";
    }
}
