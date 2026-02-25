package com.ecommerce_refactor.notification;

import com.ecommerce_refactor.model.Order;

public interface NotificationService {
    void sendOrderConfirmation(String email, Order order);
    void sendOrderShipped(Order order, String trackingNumber);
    void sendOrderCancelled(Order order);
}
