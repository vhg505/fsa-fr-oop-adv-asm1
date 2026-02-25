package com.ecommerce_refactor.payment;

public class BankTransferProcessor implements PaymentProcessor{

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Waiting for bank transfer confirmation...");
        return true;
    }

    @Override
    public double calculateFee(double amount) {
        return 0;
    }
}
