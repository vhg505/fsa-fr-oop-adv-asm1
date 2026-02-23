package com.ecommerce.legacy;

// Third-party legacy code from old system - CANNOT MODIFY
public class LegacyPaymentGateway {
    
    public int processPaymentVND(long amountInVND, String cardNumber) {
        System.out.println("Legacy gateway processing " + amountInVND + " VND");
        return 1; // 1 = success, 0 = failure
    }
    
    public int refundVND(long amountInVND, String transactionId) {
        System.out.println("Legacy gateway refunding " + amountInVND + " VND");
        return 1;
    }
}
