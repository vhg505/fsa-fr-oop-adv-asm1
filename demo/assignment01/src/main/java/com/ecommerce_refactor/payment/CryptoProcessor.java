package com.ecommerce_refactor.payment;

public class CryptoProcessor implements PaymentProcessor{

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing crypto payment...");
        return Math.random() > 0.05; // 95% success rate simulation
    }

    @Override
    public double calculateFee(double amount) {
        return amount * 0.05; // 5% crypto fee
    }
}
