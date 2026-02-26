package com.ecommerce_refactor;

import com.ecommerce_refactor.discount.BlackFridayDiscountStrategy;
import com.ecommerce_refactor.discount.DiscountService;
import com.ecommerce_refactor.model.Product;
import com.ecommerce_refactor.notification.ConsoleNotificationService;
import com.ecommerce_refactor.notification.NotificationService;
import com.ecommerce_refactor.payment.CryptoProcessor;
import com.ecommerce_refactor.payment.PayPalProcessor;
import com.ecommerce_refactor.payment.PaymentProcessor;
import com.ecommerce_refactor.repository.InMemoryOrderRepository;
import com.ecommerce_refactor.repository.InMemoryProductRepository;
import com.ecommerce_refactor.repository.OrderRepository;
import com.ecommerce_refactor.repository.ProductRepository;
import com.ecommerce_refactor.service.OrderService;
import com.ecommerce_refactor.service.OrderServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Main class demonstrating the refactored e-commerce system.
 * This demonstrates:
 * - Dependency Injection: All dependencies are created and wired in Main
 * - SOLID Principles: Clean separation of concerns
 * - Extensibility: Easy to swap implementations
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("E-COMMERCE ORDER PROCESSING SYSTEM - REFACTORED");
        System.out.println("=".repeat(80));
        
        // Initialize repositories
        ProductRepository productRepository = new InMemoryProductRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();
        
        // Initialize notification service
        NotificationService notificationService = new ConsoleNotificationService();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("INITIAL PRODUCT INVENTORY");
        System.out.println("=".repeat(80));
        displayInventory(productRepository);
        
        // Test Scenario 1: Regular discounts with Crypto payment
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 1: Regular Discounts (Electronics + Clothing + Food)");
        System.out.println("Payment Method: Crypto");
        System.out.println("=".repeat(80));
        
        DiscountService regularDiscountService = new DiscountService();
        PaymentProcessor cryptoProcessor = new CryptoProcessor();
        OrderService orderService1 = new OrderServiceImpl(
            productRepository, 
            orderRepository, 
            cryptoProcessor, 
            notificationService, 
            regularDiscountService
        );
        
        String order1Id = orderService1.createOrder(
            "hungbeo1@fpt.com",
            Arrays.asList("P001", "P002", "P003"), // Laptop (electronics), T-Shirt (clothing), Coffee (food)
            "CRYPTO",
            "Hola Park"
        );
        
        if (order1Id != null) {
            System.out.println("\nOrder 1 Created Successfully: " + order1Id);
            displayInventory(productRepository);
        } else {
            System.out.println("\n✗ Order 1 Failed");
        }
        
        // Test Scenario 2: Black Friday discount with PayPal payment
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 2: Black Friday Sale (20% off everything!)");
        System.out.println("Payment Method: PayPal");
        System.out.println("=".repeat(80));
        
        DiscountService blackFridayDiscountService = new DiscountService();
        blackFridayDiscountService.addDiscountStrategy(new BlackFridayDiscountStrategy());
        PaymentProcessor paypalProcessor = new PayPalProcessor();
        OrderService orderService2 = new OrderServiceImpl(
            productRepository, 
            orderRepository, 
            paypalProcessor, 
            notificationService, 
            blackFridayDiscountService
        );
        
        String order2Id = orderService2.createOrder(
            "hungbeo2@fpt.com",
            Arrays.asList("P004", "P003"), // Headphones (electronics), Coffee (food)
            "PAYPAL",
                "Hola Park"
        );
        
        if (order2Id != null) {
            System.out.println("\nOrder 2 Created Successfully: " + order2Id);
            displayInventory(productRepository);
        } else {
            System.out.println("\n✗ Order 2 Failed");
        }
        
        // Test Scenario 3: Order lifecycle - Create, Confirm, Cancel
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 3: Order Lifecycle Test (Create -> Confirm -> Cancel)");
        System.out.println("Testing: PENDING order cancellation (releases reserved stock)");
        System.out.println("Payment Method: Crypto");
        System.out.println("=".repeat(80));
        
        OrderService orderService3 = new OrderServiceImpl(
            productRepository, 
            orderRepository, 
            cryptoProcessor, 
            notificationService, 
            regularDiscountService
        );
        
        System.out.println("\n--- Step 1: Create Order (Stock Reserved) ---");
        String order3Id = orderService3.createOrder(
            "hungbeo3@fpt.com",
            Arrays.asList("P001", "P004"), // Laptop + Headphones
            "CRYPTO",
                "Hola Park"
        );
        
        if (order3Id != null) {
            System.out.println("\nOrder 3 Created: " + order3Id + " (Status: PENDING)");
            displayInventory(productRepository);
            
            System.out.println("\n--- Step 2: Confirm Order (Commit Stock) ---");
            boolean confirmed = orderService3.confirmOrder(order3Id);
            if (confirmed) {
                System.out.println("Order 3 Confirmed (Status: CONFIRMED)");
                displayInventory(productRepository);
            }
            
            System.out.println("\n--- Step 3: Cancel Confirmed Order (Restock) ---");
            boolean cancelled = orderService3.cancelOrder(order3Id);
            if (cancelled) {
                System.out.println("Order 3 Cancelled - Stock Restored");
                displayInventory(productRepository);
            }
        } else {
            System.out.println("\n✗ Order 3 Failed");
        }
        
        // Test Scenario 4: Cancel PENDING order (release reserved stock)
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 4: Cancel PENDING Order (Release Reserved Stock)");
        System.out.println("=".repeat(80));
        
        System.out.println("\n--- Step 1: Create Order (Stock Reserved) ---");
        String order4Id = orderService3.createOrder(
            "hungbeo4@fpt.com",
            Arrays.asList("P002", "P003"), // T-Shirt + Coffee
            "CRYPTO",
                "Hola Park"
        );
        
        if (order4Id != null) {
            System.out.println("\nOrder 4 Created: " + order4Id + " (Status: PENDING)");
            displayInventory(productRepository);
            
            System.out.println("\n--- Step 2: Cancel PENDING Order (Release Reserved) ---");
            boolean cancelled = orderService3.cancelOrder(order4Id);
            if (cancelled) {
                System.out.println("Order 4 Cancelled - Reserved Stock Released");
                displayInventory(productRepository);
            }
        } else {
            System.out.println("\n✗ Order 4 Failed");
        }
        
        // Test Scenario 5: Ship an order
        if (order1Id != null) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("SCENARIO 5: Shipping Order 1");
            System.out.println("=".repeat(80));
            
            boolean shipped = orderService1.shipOrder(order1Id, "TRACK-12345");
            if (shipped) {
                System.out.println("\n✓ Order 1 Shipped Successfully");
            }
        }
        
        // Test Scenario 6: Try to cancel a shipped order (should fail)
        if (order1Id != null) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("SCENARIO 6: Attempt to Cancel SHIPPED Order (Should Fail)");
            System.out.println("=".repeat(80));
            
            System.out.println("\n--- Attempting to cancel shipped order " + order1Id + " ---");
            boolean cancelled = orderService1.cancelOrder(order1Id);
            if (!cancelled) {
                System.out.println("\n✓ PASS: Correctly prevented cancellation of shipped order");
                System.out.println("  Reason: Orders cannot be cancelled once they have been shipped");
            } else {
                System.out.println("\n✗ FAIL: Shipped order should not be cancellable!");
            }
            displayInventory(productRepository);
        }
        
        // Test Scenario 7: Complete order lifecycle with shipping and cancellation attempt
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 7: Full Lifecycle - Create -> Confirm -> Ship -> Cancel Attempt");
        System.out.println("Payment Method: PayPal");
        System.out.println("=".repeat(80));
        
        OrderService orderService7 = new OrderServiceImpl(
            productRepository, 
            orderRepository, 
            paypalProcessor, 
            notificationService, 
            regularDiscountService
        );
        
        System.out.println("\n--- Step 1: Create Order ---");
        String order7Id = orderService7.createOrder(
            "customer7@example.com",
            Arrays.asList("P003"), // Coffee Beans
            "PAYPAL",
            "999 Test Ave, Demo City"
        );
        
        if (order7Id != null) {
            System.out.println("\n✓ Order 7 Created: " + order7Id + " (Status: PENDING)");
            displayInventory(productRepository);
            
            System.out.println("\n--- Step 2: Confirm Order ---");
            boolean confirmed = orderService7.confirmOrder(order7Id);
            if (confirmed) {
                System.out.println("✓ Order 7 Confirmed (Status: CONFIRMED)");
                displayInventory(productRepository);
            }
            
            System.out.println("\n--- Step 3: Ship Order ---");
            boolean shipped = orderService7.shipOrder(order7Id, "TRACK-99999");
            if (shipped) {
                System.out.println("✓ Order 7 Shipped (Status: SHIPPED)");
            }
            
            System.out.println("\n--- Step 4: Attempt to Cancel Shipped Order ---");
            boolean cancelled = orderService7.cancelOrder(order7Id);
            if (!cancelled) {
                System.out.println("\n✓ PASS: System correctly rejected cancellation");
                System.out.println("  Protection: Shipped orders are locked and cannot be cancelled");
            } else {
                System.out.println("\n✗ FAIL: Should not allow cancellation of shipped order!");
            }
            displayInventory(productRepository);
        } else {
            System.out.println("\n✗ Order 7 Failed");
        }
        
        // Display all orders
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ALL ORDERS SUMMARY");
        System.out.println("=".repeat(80));
        List<String> allOrders = orderService1.getAllOrders();
        for (String orderSummary : allOrders) {
            System.out.println(orderSummary);
        }
        
        // Final inventory
        System.out.println("\n" + "=".repeat(80));
        System.out.println("FINAL PRODUCT INVENTORY");
        System.out.println("=".repeat(80));
        displayInventory(productRepository);
        
        // Summary of SOLID principles demonstrated
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SOLID PRINCIPLES DEMONSTRATED");
        System.out.println("=".repeat(80));
        System.out.println("Single Responsibility: Each class has one clear purpose");
        System.out.println("Open/Closed: Added BlackFridayDiscount without modifying existing code");
        System.out.println("Liskov Substitution: All implementations are interchangeable");
        System.out.println("Interface Segregation: Focused interfaces (PaymentProcessor, NotificationService, etc.)");
        System.out.println("Dependency Inversion: All dependencies injected as interfaces");
        System.out.println("=".repeat(80));
        
        // Summary of stock management
        System.out.println("\n" + "=".repeat(80));
        System.out.println("STOCK MANAGEMENT SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("createOrder  -> Reserve stock (increase Reserved, decrease Available)");
        System.out.println("confirmOrder -> Commit stock (decrease On-hand, decrease Reserved)");
        System.out.println("cancelOrder  -> Depends on state:");
        System.out.println("    - PENDING order   -> Release reserve (decrease Reserved, increase Available)");
        System.out.println("    - CONFIRMED order -> Restock (increase On-hand)");
        System.out.println("    - SHIPPED order   -> Cannot be cancelled (prevented)");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Helper method to display current inventory status.
     */
    private static void displayInventory(ProductRepository productRepository) {
        System.out.println("\nCurrent Inventory:");
        System.out.println("-".repeat(95));
        System.out.printf("%-12s %-20s %-12s %-15s %-10s %-10s %-10s%n", 
                         "Product ID", "Name", "Price", "Category", "On-Hand", "Reserved", "Available");
        System.out.println("-".repeat(95));
        
        List<Product> products = productRepository.getAllProducts();
        for (Product product : products) {
            System.out.printf("%-12s %-20s $%-11.2f %-15s %-10d %-10d %-10d%n",
                            product.getProductId(),
                            product.getName(),
                            product.getPrice(),
                            product.getCategory(),
                            product.getOnHandStock(),
                            product.getReservedStock(),
                            product.getAvailableStock());
        }
        System.out.println("-".repeat(95));
    }
}

