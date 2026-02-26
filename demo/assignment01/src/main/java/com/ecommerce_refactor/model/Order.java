package com.ecommerce_refactor.model;

import com.ecommerce_refactor.model.enumeration.OrderStatus;

import java.util.List;

public class Order {
    private String orderId;
    private String customerEmail;
    private List<OrderItem> orderItems;
    private double total;
    private OrderStatus status;
    private String shippingAddress;

    public Order(String orderId, String customerEmail, List<OrderItem> orderItems, double total, OrderStatus status, String shippingAddress) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.orderItems = orderItems;
        this.total = total;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
