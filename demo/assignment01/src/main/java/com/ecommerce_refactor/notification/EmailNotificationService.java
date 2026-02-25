package com.ecommerce_refactor.notification;

import com.ecommerce_refactor.model.Order;

public class EmailNotificationService implements NotificationService {
    private final EmailProvider emailProvider;

    public EmailNotificationService(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    @Override
    public void sendOrderConfirmation(String email, Order order) {
        emailProvider.sendEmail(email, "Order Confirmation - " + order.getOrderId(),
                "Thank you for your order! Total: $" + String.format("%.2f", order.getTotal()));
    }

    @Override
    public void sendOrderShipped(Order order, String trackingNumber) {
        emailProvider.sendEmail(order.getCustomerEmail(), "Order Shipped - " + order.getOrderId(),
                "Your order has been shipped. Tracking number: " + trackingNumber);
    }

    @Override
    public void sendOrderCancelled(Order order) {
        emailProvider.sendEmail(order.getCustomerEmail(), "Order cancelled - " + order.getOrderId(),
                "Your order has been cancelled.");
    }
}
