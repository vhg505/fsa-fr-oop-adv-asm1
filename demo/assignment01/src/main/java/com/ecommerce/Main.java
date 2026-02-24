package com.ecommerce;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // PROBLEM: Direct instantiation of OrderService violates Dependency Inversion Principle
        // Main is tightly coupled to the concrete OrderService class
        // Cannot easily swap implementations or inject mock objects for testing
        OrderService service = new OrderService();
        
        // PROBLEM: Using primitive strings for payment method instead of type-safe enum or object
        // "CREDIT_CARD" could be misspelled as "CREDIT-CARD" or "CreditCard" causing runtime errors
        // No compile-time validation of valid payment methods
        String orderId = service.createOrder(
            "CUST-001",
            "john@example.com",
            Arrays.asList("P001", "P002"),
            "CREDIT_CARD",
            "123 Main St, City"
        );
        
        if (orderId != null) {
            System.out.println("\n=== Order Created: " + orderId + " ===\n");
            service.shipOrder(orderId, "TRACK-12345");
        }
        
        System.out.println("\n=== All Orders ===");
        service.printAllOrders();
    }
}
