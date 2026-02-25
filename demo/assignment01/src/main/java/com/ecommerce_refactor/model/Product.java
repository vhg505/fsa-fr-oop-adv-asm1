package com.ecommerce_refactor.model;

import com.ecommerce_refactor.model.enumeration.ProductCategory;

public class Product {
    private String productId;
    private String name;
    private double price;
    private ProductCategory category;
    private int stock;

    public Product(String productId, String name, double price, ProductCategory category, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
