package com.ecommerce_refactor.payment;

public class CreditCardProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment...");
        return Math.random() > 0.1; // 90% success rate simulation
    }

    @Override
    public double calculateFee(double amount) {
        return amount * 0.03; // 3% credit card fee
    }
}
