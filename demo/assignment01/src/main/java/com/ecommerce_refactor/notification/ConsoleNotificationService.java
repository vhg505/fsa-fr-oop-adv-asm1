package com.ecommerce_refactor.notification;

import com.ecommerce_refactor.model.Order;

public class ConsoleNotificationService implements NotificationService{
    @Override
    public void sendOrderConfirmation(String email, Order order) {
        System.out.println("Sending order confirmation email to " + email + " ...");
        System.out.println("Subject: Order Confirmation - " + order.getOrderId());
        System.out.println("Body: Thank you for your order! Total: $" + String.format("%.2f", order.getTotal()));
    }

    @Override
    public void sendOrderShipped(Order order, String trackingNumber) {
        System.out.println("[Email] Your order - " + order.getOrderId() + " has been shipped!");
        System.out.println("Tracking number: " + trackingNumber);
    }

    @Override
    public void sendOrderCancelled(Order order) {
        System.out.println("[Email] Your order - " + order.getOrderId() + " has been cancelled.");
    }
}
