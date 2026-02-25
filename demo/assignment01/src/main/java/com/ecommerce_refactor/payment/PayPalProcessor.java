package com.ecommerce_refactor.payment;

public class PayPalProcessor implements PaymentProcessor{
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Redirecting to PayPal...");
        return Math.random() > 0.05; // 95% success rate
    }

    @Override
    public double calculateFee(double amount) {
        return amount * 0.025; // 2.5% PayPal fee
    }
}
