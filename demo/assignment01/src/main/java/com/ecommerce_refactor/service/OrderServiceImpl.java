package com.ecommerce_refactor.service;

import com.ecommerce_refactor.discount.DiscountService;
import com.ecommerce_refactor.model.Order;
import com.ecommerce_refactor.model.OrderItem;
import com.ecommerce_refactor.model.Product;
import com.ecommerce_refactor.model.enumeration.OrderStatus;
import com.ecommerce_refactor.notification.NotificationService;
import com.ecommerce_refactor.payment.PaymentProcessor;
import com.ecommerce_refactor.repository.OrderRepository;
import com.ecommerce_refactor.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of OrderService interface.
 * 
 * This class demonstrates SOLID principles:
 * - Single Responsibility: Orchestrates order processing workflow
 * - Open/Closed: Extensible through injected dependencies
 * - Liskov Substitution: Can be substituted wherever OrderService is used
 * - Interface Segregation: Depends only on focused interfaces
 * - Dependency Inversion: Depends on abstractions (interfaces), not concrete classes
 */
public class OrderServiceImpl implements OrderService {
    
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentProcessor paymentProcessor;
    private final NotificationService notificationService;
    private final DiscountService discountService;
    
    /**
     * Constructor with dependency injection.
     * All dependencies are injected as interfaces, following Dependency Inversion Principle.
     * 
     * @param productRepository Repository for product data access
     * @param orderRepository Repository for order data access
     * @param paymentProcessor Payment processing service
     * @param notificationService Notification delivery service
     * @param discountService Discount calculation service
     */
    public OrderServiceImpl(ProductRepository productRepository,
                           OrderRepository orderRepository,
                           PaymentProcessor paymentProcessor,
                           NotificationService notificationService,
                           DiscountService discountService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.paymentProcessor = paymentProcessor;
        this.notificationService = notificationService;
        this.discountService = discountService;
    }
    
    @Override
    public String createOrder(String customerEmail, List<String> productIds, 
                             String paymentMethod, String shippingAddress) {
        
        // Step 1: Validate products and reserve stock
        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> productsToReserve = new ArrayList<>();
        double subtotal = 0;
        int itemId = 1;
        
        for (String productId : productIds) {
            Optional<Product> productOpt = productRepository.getProductById(productId);
            
            if (productOpt.isEmpty()) {
                System.out.println("ERROR: Product not found: " + productId);
                return null;
            }
            
            Product product = productOpt.get();
            
            // Check available stock (on-hand - reserved)
            if (product.getAvailableStock() <= 0) {
                System.out.println("ERROR: Out of stock: " + product.getName());
                System.out.println("  On-hand: " + product.getOnHandStock() 
                                 + ", Reserved: " + product.getReservedStock() 
                                 + ", Available: " + product.getAvailableStock());
                // Release any already reserved stock
                releaseReservedStock(productsToReserve);
                return null;
            }
            
            // Apply discount using DiscountService
            double originalPrice = product.getPrice();
            double discountedPrice = discountService.calculateDiscountedPrice(product);
            
            // Create order item
            OrderItem orderItem = new OrderItem(itemId++, productId, 1, 
                                               discountedPrice, discountedPrice);
            orderItems.add(orderItem);
            subtotal += discountedPrice;
            
            // Reserve stock (increase Reserved, decrease Available, On-hand UNCHANGED)
            if (product.reserveStock(1)) {
                productsToReserve.add(product);
                System.out.println("[STOCK] Reserved 1x " + product.getName() 
                                 + " (On-hand: " + product.getOnHandStock()
                                 + ", Reserved: " + product.getReservedStock()
                                 + ", Available: " + product.getAvailableStock() + ")");
            } else {
                System.out.println("ERROR: Failed to reserve stock for: " + product.getName());
                // Release any already reserved stock
                releaseReservedStock(productsToReserve);
                return null;
            }
        }
        
        // Step 2: Calculate payment fee
        double paymentFee = paymentProcessor.calculateFee(subtotal);
        double total = subtotal + paymentFee;
        
        // Step 3: Process payment
        boolean paymentSuccess = paymentProcessor.processPayment(total);
        
        if (!paymentSuccess) {
            System.out.println("ERROR: Payment failed!");
            // Release reserved stock
            releaseReservedStock(productsToReserve);
            return null;
        }
        
        // Step 4: Create and save order with PENDING status
        String orderId = "ORD-" + System.currentTimeMillis();
        Order order = new Order(orderId, customerEmail, orderItems, 
                               total, OrderStatus.PENDING, shippingAddress);
        orderRepository.save(order);
        
        // Step 5: Send notification
        notificationService.sendOrderConfirmation(customerEmail, order);
        
        // Step 6: Log order creation
        System.out.println("[LOG] Order created: " + orderId + " for customer " + customerEmail);
        System.out.println("[LOG] Order status: PENDING (stock reserved, awaiting confirmation)");
        System.out.println("[ANALYTICS] New order: $" + String.format("%.2f", total) 
                         + " via " + paymentMethod);
        
        return orderId;
    }
    
    @Override
    public boolean confirmOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        
        if (order == null) {
            System.out.println("ERROR: Order not found: " + orderId);
            return false;
        }
        
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            System.out.println("Order already confirmed: " + orderId);
            return true;
        }
        
        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("ERROR: Cannot confirm order in status: " + order.getStatus());
            return false;
        }
        
        // Commit reserved stock (decrease On-hand, decrease Reserved)
        for (OrderItem item : order.getOrderItems()) {
            Optional<Product> productOpt = productRepository.getProductById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                product.commitReservedStock(item.getQuantity());
                System.out.println("[STOCK] Committed " + item.getQuantity() + "x " + product.getName()
                                 + " (On-hand: " + product.getOnHandStock()
                                 + ", Reserved: " + product.getReservedStock()
                                 + ", Available: " + product.getAvailableStock() + ")");
            }
        }
        
        // Update order status
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
        
        // Send confirmation notification
        notificationService.sendOrderConfirmation(order.getCustomerEmail(), order);
        System.out.println("[LOG] Order confirmed: " + orderId);
        
        return true;
    }
    
    @Override
    public boolean cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        
        if (order == null) {
            System.out.println("ERROR: Order not found: " + orderId);
            return false;
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Order already cancelled: " + orderId);
            return true;
        }
        
        if (order.getStatus() == OrderStatus.SHIPPED) {
            System.out.println("╔" + "═".repeat(78) + "╗");
            System.out.println("║" + " ".repeat(78) + "║");
            System.out.println("║" + centerText("ERROR: CANNOT CANCEL SHIPPED ORDER", 78) + "║");
            System.out.println("║" + " ".repeat(78) + "║");
            System.out.println("║" + centerText("Order ID: " + orderId, 78) + "║");
            System.out.println("║" + centerText("Status: SHIPPED", 78) + "║");
            System.out.println("║" + " ".repeat(78) + "║");
            System.out.println("║" + centerText("Reason: Orders cannot be cancelled once shipped", 78) + "║");
            System.out.println("║" + centerText("The package is already in transit to the customer", 78) + "║");
            System.out.println("║" + " ".repeat(78) + "║");
            System.out.println("╚" + "═".repeat(78) + "╝");
            return false;
        }
        
        // Handle stock based on order status
        if (order.getStatus() == OrderStatus.PENDING) {
            // Order NOT confirmed -> release reserve (decrease Reserved, increase Available, On-hand UNCHANGED)
            System.out.println("[STOCK] Releasing reserved stock for PENDING order...");
            for (OrderItem item : order.getOrderItems()) {
                Optional<Product> productOpt = productRepository.getProductById(item.getProductId());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    product.releaseReservedStock(item.getQuantity());
                    System.out.println("[STOCK] Released " + item.getQuantity() + "x " + product.getName()
                                     + " (On-hand: " + product.getOnHandStock()
                                     + ", Reserved: " + product.getReservedStock()
                                     + ", Available: " + product.getAvailableStock() + ")");
                }
            }
        } else if (order.getStatus() == OrderStatus.CONFIRMED) {
            // Order confirmed -> restock (increase On-hand, Reserved UNCHANGED)
            System.out.println("[STOCK] Restocking for CONFIRMED order...");
            for (OrderItem item : order.getOrderItems()) {
                Optional<Product> productOpt = productRepository.getProductById(item.getProductId());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    product.restock(item.getQuantity());
                    System.out.println("[STOCK] Restocked " + item.getQuantity() + "x " + product.getName()
                                     + " (On-hand: " + product.getOnHandStock()
                                     + ", Reserved: " + product.getReservedStock()
                                     + ", Available: " + product.getAvailableStock() + ")");
                }
            }
        }
        
        // Update order status
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        
        // Send cancellation notification
        notificationService.sendOrderCancelled(order);
        System.out.println("[LOG] Order cancelled: " + orderId);
        
        return true;
    }
    
    @Override
    public boolean shipOrder(String orderId, String trackingNumber) {
        Order order = orderRepository.findById(orderId);
        
        if (order == null) {
            System.out.println("ERROR: Order not found: " + orderId);
            return false;
        }
        
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            System.out.println("ERROR: Cannot ship order in status: " + order.getStatus());
            return false;
        }
        
        // Update order status
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
        
        // Send shipping notification
        notificationService.sendOrderShipped(order, trackingNumber);
        System.out.println("[LOG] Order shipped: " + orderId + " with tracking: " + trackingNumber);
        
        return true;
    }
    
    @Override
    public List<String> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<String> orderSummaries = new ArrayList<>();
        
        for (Order order : orders) {
            String summary = "Order: " + order.getOrderId() 
                           + " - Status: " + order.getStatus() 
                           + " - Total: $" + String.format("%.2f", order.getTotal());
            orderSummaries.add(summary);
        }
        
        return orderSummaries;
    }
    
    /**
     * Helper method to release reserved stock when order creation fails.
     * 
     * @param products List of products to release reserved stock for
     */
    private void releaseReservedStock(List<Product> products) {
        for (Product product : products) {
            product.releaseReservedStock(1);
            System.out.println("[STOCK] Released reserved stock for: " + product.getName());
        }
    }
    
    /**
     * Helper method to center text for formatted error messages.
     * 
     * @param text Text to center
     * @param width Total width
     * @return Centered text with padding
     */
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int leftPadding = (width - text.length()) / 2;
        int rightPadding = width - text.length() - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }
}
