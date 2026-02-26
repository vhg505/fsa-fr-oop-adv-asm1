package com.ecommerce_refactor.service;

import java.util.List;

/**
 * Service interface for order management operations.
 * 
 * This interface demonstrates:
 * - Interface Segregation Principle: Focused on order-related operations only
 * - Dependency Inversion Principle: High-level modules depend on this abstraction
 * - Single Responsibility Principle: Only handles order business logic
 */
public interface OrderService {
    
    /**
     * Creates a new order with the specified details.
     * 
     * @param customerEmail Customer's email address
     * @param productIds List of product IDs to order
     * @param paymentMethod Payment method to use
     * @param shippingAddress Shipping address for the order
     * @return Order ID if successful, null if failed
     */
    String createOrder(String customerEmail, List<String> productIds, 
                      String paymentMethod, String shippingAddress);
    
    /**
     * Confirms an order (alternative flow if orders start in pending state).
     * 
     * @param orderId The order ID to confirm
     * @return true if confirmation successful, false otherwise
     */
    boolean confirmOrder(String orderId);
    
    /**
     * Cancels an existing order.
     * 
     * @param orderId The order ID to cancel
     * @return true if cancellation successful, false otherwise
     */
    boolean cancelOrder(String orderId);
    
    /**
     * Ships an order with tracking information.
     * 
     * @param orderId The order ID to ship
     * @param trackingNumber Tracking number for the shipment
     * @return true if shipping successful, false otherwise
     */
    boolean shipOrder(String orderId, String trackingNumber);
    
    /**
     * Retrieves all orders in the system.
     * 
     * @return List of all orders
     */
    List<String> getAllOrders();
}
