package com.ecommerce_refactor.model.enumeration;

public enum OrderStatus {
    PENDING,      // Order created, stock reserved, awaiting confirmation
    CONFIRMED,    // Order confirmed, stock committed
    CANCELLED,    // Order cancelled
    SHIPPED       // Order shipped
}

