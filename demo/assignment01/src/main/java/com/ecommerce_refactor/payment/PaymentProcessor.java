package com.ecommerce_refactor.payment;

import com.ecommerce_refactor.model.Order;

public interface PaymentProcessor {
    boolean processPayment(double amount);
    double calculateFee(double amount);
}
