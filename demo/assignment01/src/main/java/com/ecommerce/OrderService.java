package com.ecommerce;

import java.util.ArrayList;
import java.util.List;

// ============================================================================
// GOD CLASS ANTI-PATTERN: This class violates ALL five SOLID principles
// ============================================================================
// This class has TOO MANY RESPONSIBILITIES:
// 1. Product management (finding products, managing inventory)
// 2. Order validation (checking stock, validating products)
// 3. Pricing calculation (base prices, discounts, fees)
// 4. Discount logic (category-based discount rules)
// 5. Payment processing (credit card, PayPal, bank transfer)
// 6. Stock management (updating inventory)
// 7. Order lifecycle management (create, cancel, ship)
// 8. Notification delivery (email, SMS)
// 9. Logging (system events)
// 10. Analytics (tracking metrics)
//
// SOLID VIOLATIONS:
// - SRP: Has 10+ reasons to change (each responsibility above)
// - OCP: Must modify this class to add new payment methods or discount rules
// - LSP: Cannot substitute this class (it's concrete, not polymorphic)
// - ISP: Any interface for this would be huge and force implementations to handle everything
// - DIP: Depends on concrete implementations (no abstractions for dependencies)
// ============================================================================

public class OrderService {
    // PROBLEM: Using Object[] arrays instead of proper domain models
    // - No type safety: product[0] could be anything, compiler won't catch errors
    // - Magic indices: What does product[3] mean? You have to remember or look it up
    // - No encapsulation: Anyone can modify array elements directly
    // - No validation: Can't enforce business rules on the data
    // - Poor readability: Code is cryptic and hard to understand
    // VIOLATES: Single Responsibility (data structure + business logic mixed)
    private List<Object[]> orders = new ArrayList<>();
    private List<Object[]> products = new ArrayList<>();

    public OrderService() {
        // PROBLEM: Hardcoded test data in the service class
        // - Violates Single Responsibility: Service shouldn't manage test data
        // - Makes testing difficult: Can't easily inject different product catalogs
        // - Not production-ready: Real systems load data from databases
        // VIOLATES: Dependency Inversion (should depend on a IProductRepository abstraction)
        
        // Initialize sample products: [id, name, price, stock, category]
        // PROBLEM: Magic array indices - what does each position mean?
        // [0]=id, [1]=name, [2]=price, [3]=stock, [4]=category
        // Easy to mix up the order and cause bugs
        products.add(new Object[]{"P001", "Laptop", 999.99, 50, "ELECTRONICS"});
        products.add(new Object[]{"P002", "T-Shirt", 29.99, 200, "CLOTHING"});
        products.add(new Object[]{"P003", "Coffee Beans", 15.99, 100, "FOOD"});
        products.add(new Object[]{"P004", "Headphones", 149.99, 75, "ELECTRONICS"});
    }

    // ============================================================================
    // MASSIVE METHOD: This single method does EVERYTHING
    // ============================================================================
    // This method handles:
    // - Product validation, stock checking, price calculation, discount application
    // - Payment processing, stock updates, order creation, notifications, logging, analytics
    // Over 80 lines of code doing 10+ different things!
    // VIOLATES: Single Responsibility Principle (has many reasons to change)
    // ============================================================================
    
    public String createOrder(String customerId, String customerEmail, List<String> productIds, 
                               String paymentMethod, String shippingAddress) {
        // RESPONSIBILITY #1: Product Validation and Price Calculation
        // PROBLEM: This logic should be in a separate validator/calculator class
        // VIOLATES: Single Responsibility Principle
        double total = 0;
        List<Object[]> orderItems = new ArrayList<>();
        
        for (String productId : productIds) {
            Object[] product = findProduct(productId);
            if (product == null) {
                System.out.println("ERROR: Product not found: " + productId);
                return null;
            }
            
            // PROBLEM: Unsafe type casting from Object[] - runtime errors waiting to happen
            // If someone accidentally puts a String in position [3], this crashes
            int stock = (Integer) product[3];
            if (stock <= 0) {
                System.out.println("ERROR: Out of stock: " + product[1]);
                return null;
            }
            
            double price = (Double) product[2];
            String category = (String) product[4];
            
            // ============================================================================
            // RESPONSIBILITY #2: Discount Calculation
            // PROBLEM: Hardcoded discount rules using if-else chains
            // VIOLATES: Open/Closed Principle
            // ============================================================================
            // To add a new discount rule (e.g., BOOKS category), you must:
            // 1. Open this class
            // 2. Add another if-else branch
            // 3. Risk breaking existing discount logic
            // 4. Retest all discount scenarios
            //
            // BETTER APPROACH: Use Strategy pattern with DiscountStrategy interface
            // Each discount rule becomes a separate class implementing the interface
            // New rules can be added without modifying existing code
            // ============================================================================
            
            // Apply category-specific discounts
            if (category.equals("ELECTRONICS") && price > 500) {
                price = price * 0.95; // 5% off expensive electronics
            } else if (category.equals("CLOTHING")) {
                price = price * 0.90; // 10% off all clothing
            } else if (category.equals("FOOD")) {
                // No discount for food
            }
            // What if we need seasonal discounts? Loyalty discounts? Bulk discounts?
            // This if-else chain will grow and grow...
            
            total += price;
            orderItems.add(new Object[]{productId, product[1], price});
            
            // RESPONSIBILITY #3: Stock Management
            // PROBLEM: Directly modifying product data in the middle of order processing
            // VIOLATES: Single Responsibility Principle
            // Should be handled by a separate InventoryService
            product[3] = stock - 1;
        }
        
        // ============================================================================
        // RESPONSIBILITY #4: Payment Fee Calculation
        // PROBLEM: Another if-else chain for payment-specific logic
        // VIOLATES: Open/Closed Principle
        // ============================================================================
        // To add cryptocurrency payment, you must modify this method
        // Same problem as discount logic - not extensible
        // ============================================================================
        
        // Apply payment method fee
        if (paymentMethod.equals("CREDIT_CARD")) {
            total = total * 1.03; // 3% credit card fee
        } else if (paymentMethod.equals("PAYPAL")) {
            total = total * 1.025; // 2.5% PayPal fee
        } else if (paymentMethod.equals("BANK_TRANSFER")) {
            // No fee for bank transfer
        }
        // PROBLEM: Using string comparison for payment methods
        // Typos like "CREDIT-CARD" or "credit_card" will silently fail
        // No compile-time safety
        
        // ============================================================================
        // RESPONSIBILITY #5: Payment Processing
        // PROBLEM: Payment logic embedded directly in order creation
        // VIOLATES: Single Responsibility, Open/Closed, Dependency Inversion
        // ============================================================================
        // Issues:
        // 1. Cannot test payment processing independently
        // 2. Cannot reuse payment logic elsewhere
        // 3. Adding new payment methods requires modifying this method
        // 4. No abstraction - tightly coupled to specific payment implementations
        // 5. Cannot mock payment processing for testing
        //
        // BETTER APPROACH: Create PaymentProcessor interface with implementations:
        // - CreditCardPaymentProcessor
        // - PayPalPaymentProcessor
        // - BankTransferPaymentProcessor
        // Inject the appropriate processor based on payment method
        // ============================================================================
        
        // Process payment
        boolean paymentSuccess = false;
        if (paymentMethod.equals("CREDIT_CARD")) {
            System.out.println("Processing credit card payment...");
            paymentSuccess = Math.random() > 0.1; // 90% success rate simulation
        } else if (paymentMethod.equals("PAYPAL")) {
            System.out.println("Redirecting to PayPal...");
            paymentSuccess = Math.random() > 0.05; // 95% success rate
        } else if (paymentMethod.equals("BANK_TRANSFER")) {
            System.out.println("Waiting for bank transfer confirmation...");
            paymentSuccess = true; // Always succeeds (manual verification later)
        }
        // PROBLEM: What if paymentMethod is "CRYPTO" or something unexpected?
        // paymentSuccess stays false, but no error message - silent failure!
        
        if (!paymentSuccess) {
            System.out.println("ERROR: Payment failed!");
            // PROBLEM: Stock rollback logic duplicated here
            // Same code appears in cancelOrder() - violates DRY principle
            for (String productId : productIds) {
                Object[] product = findProduct(productId);
                product[3] = (Integer) product[3] + 1;
            }
            return null;
        }
        
        // RESPONSIBILITY #6: Order Creation
        // PROBLEM: Order stored as Object[] array - same issues as products
        // [0]=orderId, [1]=customerId, [2]=orderItems, [3]=total, [4]=status, [5]=address
        String orderId = "ORD-" + System.currentTimeMillis();
        orders.add(new Object[]{orderId, customerId, orderItems, total, "CONFIRMED", shippingAddress});
        
        // ============================================================================
        // RESPONSIBILITY #7: Notification Delivery
        // PROBLEM: Notification logic scattered and hardcoded
        // VIOLATES: Single Responsibility, Open/Closed
        // ============================================================================
        // Issues:
        // 1. Email logic embedded in order processing
        // 2. Cannot switch email providers without modifying this class
        // 3. Cannot add SMS or push notifications without modifying this class
        // 4. Cannot test notifications independently
        // 5. No way to disable notifications for testing
        // 6. Notification formatting mixed with business logic
        //
        // BETTER APPROACH: Create NotificationService interface
        // Inject implementations (EmailNotifier, SMSNotifier, etc.)
        // Use Observer pattern or event-driven architecture
        // ============================================================================
        
        // Send notifications
        System.out.println("Sending email to " + customerEmail + "...");
        System.out.println("Subject: Order Confirmed - " + orderId);
        System.out.println("Body: Your order total is $" + String.format("%.2f", total));
        
        // ============================================================================
        // RESPONSIBILITY #8: Logging
        // PROBLEM: Logging mixed with business logic
        // VIOLATES: Single Responsibility, Dependency Inversion
        // ============================================================================
        // Should use a proper logging framework (injected dependency)
        // Business logic shouldn't know about logging implementation
        // ============================================================================
        
        // Log to file (simulated)
        System.out.println("[LOG] Order created: " + orderId + " for customer " + customerId);
        
        // ============================================================================
        // RESPONSIBILITY #9: Analytics
        // PROBLEM: Analytics tracking embedded in business logic
        // VIOLATES: Single Responsibility
        // ============================================================================
        // Should be handled by a separate AnalyticsService
        // Could use event-driven approach to decouple
        // ============================================================================
        
        // Update analytics
        System.out.println("[ANALYTICS] New order: $" + total + " via " + paymentMethod);
        
        return orderId;
    }

    public void cancelOrder(String orderId) {
        // PROBLEM: Linear search through orders - inefficient for large datasets
        // Should use a Map<String, Order> for O(1) lookup
        for (Object[] order : orders) {
            if (order[0].equals(orderId)) {
                // PROBLEM: String comparison for order status - same issues as payment method
                // Should use an enum: OrderStatus.CONFIRMED
                if (order[4].equals("CONFIRMED")) {
                    order[4] = "CANCELLED";
                    
                    // PROBLEM: Duplicated stock restoration logic (also in createOrder)
                    // Violates DRY principle - should be extracted to a method
                    // Restore stock
                    List<Object[]> items = (List<Object[]>) order[2];
                    for (Object[] item : items) {
                        Object[] product = findProduct((String) item[0]);
                        product[3] = (Integer) product[3] + 1;
                    }
                    
                    // PROBLEM: Notification logic scattered again
                    // Different notification format than in createOrder - inconsistent
                    System.out.println("[EMAIL] Your order " + orderId + " has been cancelled.");
                    System.out.println("[LOG] Order cancelled: " + orderId);
                } else {
                    System.out.println("ERROR: Cannot cancel order in status: " + order[4]);
                }
                return;
            }
        }
        System.out.println("ERROR: Order not found: " + orderId);
    }

    public void shipOrder(String orderId, String trackingNumber) {
        // PROBLEM: Same linear search inefficiency
        for (Object[] order : orders) {
            if (order[0].equals(orderId)) {
                if (order[4].equals("CONFIRMED")) {
                    order[4] = "SHIPPED";
                    
                    // PROBLEM: Notification logic scattered across three methods
                    // Each method has different notification formats and channels
                    // - createOrder: sends email only
                    // - cancelOrder: sends email only
                    // - shipOrder: sends email AND SMS
                    // Inconsistent behavior - should be centralized
                    System.out.println("[EMAIL] Your order " + orderId + " has been shipped!");
                    System.out.println("Tracking: " + trackingNumber);
                    System.out.println("[SMS] Order shipped: " + trackingNumber);
                    System.out.println("[LOG] Order shipped: " + orderId);
                }
                return;
            }
        }
        // PROBLEM: Silent failure if order not found - no error message
        // Inconsistent with cancelOrder which prints an error
    }

    private Object[] findProduct(String productId) {
        // PROBLEM: Linear search - O(n) complexity
        // For large product catalogs, this becomes a performance bottleneck
        // Should use Map<String, Product> for O(1) lookup
        for (Object[] product : products) {
            if (product[0].equals(productId)) {
                return product;
            }
        }
        return null;
        // PROBLEM: Returns null instead of using Optional<Product>
        // Callers must remember to check for null or risk NullPointerException
    }

    public void printAllOrders() {
        // PROBLEM: Presentation logic in the service layer
        // VIOLATES: Single Responsibility, Separation of Concerns
        // Service classes should not handle UI/output formatting
        // This should be in a separate view or presenter class
        for (Object[] order : orders) {
            // PROBLEM: Magic indices again - order[0], order[4], order[3]
            // What if someone changes the order array structure?
            // All these indices would need to be updated
            System.out.println("Order: " + order[0] + " - Status: " + order[4] + " - Total: $" + order[3]);
        }
    }
}

// ============================================================================
// SUMMARY OF PROBLEMS IN THIS CLASS
// ============================================================================
//
// SINGLE RESPONSIBILITY PRINCIPLE VIOLATIONS:
// - Handles 10+ distinct responsibilities in one class
// - Every change to any responsibility requires modifying this class
// - Impossible to understand or test individual concerns in isolation
//
// OPEN/CLOSED PRINCIPLE VIOLATIONS:
// - Must modify class to add new discount rules (if-else chains)
// - Must modify class to add new payment methods (if-else chains)
// - Must modify class to add new notification channels
// - Cannot extend behavior without changing existing code
//
// LISKOV SUBSTITUTION PRINCIPLE VIOLATIONS:
// - Class is concrete, not polymorphic - cannot be substituted
// - No interfaces or abstractions to enable substitutability
//
// INTERFACE SEGREGATION PRINCIPLE VIOLATIONS:
// - If this were behind an interface, it would be massive
// - Clients would depend on methods they don't use
// - No focused, role-specific interfaces
//
// DEPENDENCY INVERSION PRINCIPLE VIOLATIONS:
// - No abstractions - everything is concrete
// - Cannot inject dependencies for testing or flexibility
// - Tightly coupled to specific implementations
// - High-level business logic mixed with low-level details
//
// ADDITIONAL PROBLEMS:
// - Primitive Obsession: Object[] arrays instead of domain models
// - No type safety: Runtime errors from casting
// - Magic numbers/strings: Array indices, status strings, payment methods
// - Code duplication: Stock restoration logic repeated
// - Poor performance: Linear searches instead of hash lookups
// - Inconsistent error handling: Some methods print errors, others fail silently
// - Mixed concerns: Business logic, persistence, notifications, logging all mixed
// - Hard to test: Cannot mock dependencies or test components in isolation
// - Hard to maintain: Changes ripple through the entire class
// - Hard to extend: Adding features requires modifying existing code
//
// ============================================================================
