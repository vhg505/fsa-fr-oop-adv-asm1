package com.ecommerce_refactor.model;

import com.ecommerce_refactor.model.enumeration.ProductCategory;

/**
 * Product domain model with proper stock management.
 * 
 * Stock Management:
 * - onHandStock: Total physical inventory
 * - reservedStock: Stock reserved for unconfirmed orders
 * - availableStock: Stock available for new orders (onHand - reserved)
 */
public class Product {
    private String productId;
    private String name;
    private double price;
    private ProductCategory category;
    private int onHandStock;      // Total physical inventory
    private int reservedStock;    // Reserved for pending orders
    
    public Product(String productId, String name, double price, ProductCategory category, int onHandStock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.onHandStock = onHandStock;
        this.reservedStock = 0;
    }

    public Product() {
        this.reservedStock = 0;
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

    public int getOnHandStock() {
        return onHandStock;
    }

    public void setOnHandStock(int onHandStock) {
        this.onHandStock = onHandStock;
    }

    public int getReservedStock() {
        return reservedStock;
    }

    public void setReservedStock(int reservedStock) {
        this.reservedStock = reservedStock;
    }
    
    /**
     * Gets available stock (on-hand minus reserved).
     * This is the stock available for new orders.
     */
    public int getAvailableStock() {
        return onHandStock - reservedStock;
    }
    
    /**
     * Reserves stock for an order.
     * Increases reserved, decreases available, on-hand UNCHANGED.
     * 
     * @param quantity Quantity to reserve
     * @return true if reservation successful, false if insufficient stock
     */
    public boolean reserveStock(int quantity) {
        if (getAvailableStock() >= quantity) {
            reservedStock += quantity;
            // On-hand remains unchanged
            return true;
        }
        return false;
    }
    
    /**
     * Releases reserved stock (e.g., when PENDING order is cancelled).
     * Decreases reserved, increases available, on-hand UNCHANGED.
     * 
     * @param quantity Quantity to release
     */
    public void releaseReservedStock(int quantity) {
        reservedStock = Math.max(0, reservedStock - quantity);
        // On-hand remains unchanged
    }
    
    /**
     * Commits reserved stock (e.g., when order is confirmed).
     * Decreases on-hand, decreases reserved.
     * 
     * @param quantity Quantity to commit
     */
    public void commitReservedStock(int quantity) {
        int toCommit = Math.min(quantity, reservedStock);
        onHandStock -= toCommit;      // Decrease on-hand
        reservedStock -= toCommit;     // Decrease reserved
    }
    
    /**
     * Restocks inventory (e.g., when CONFIRMED order is cancelled).
     * Increases on-hand only.
     * 
     * @param quantity Quantity to restock
     */
    public void restock(int quantity) {
        onHandStock += quantity;       // Increase on-hand
        // Reserved remains unchanged
    }
    
    // Deprecated: Use specific stock methods instead
    @Deprecated
    public int getStock() {
        return getAvailableStock();
    }
    
    @Deprecated
    public void setStock(int stock) {
        this.onHandStock = stock;
        this.reservedStock = 0;
    }
}

