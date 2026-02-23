package com.ecommerce;

import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private String customerEmail;
    private String customerPhone;
    private List<String> productIds;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private String couponCode;
    private String giftMessage;
    private boolean giftWrap;
    private boolean expressShipping;
    private String notes;

    public Order(String id, String customerId, String customerEmail, List<String> productIds, 
                 String shippingAddress, String paymentMethod) {
        this(id, customerId, customerEmail, null, productIds, shippingAddress, 
             shippingAddress, paymentMethod, null, null, false, false, null);
    }

    public Order(String id, String customerId, String customerEmail, String customerPhone,
                 List<String> productIds, String shippingAddress, String billingAddress,
                 String paymentMethod, String couponCode) {
        this(id, customerId, customerEmail, customerPhone, productIds, shippingAddress, 
             billingAddress, paymentMethod, couponCode, null, false, false, null);
    }

    public Order(String id, String customerId, String customerEmail, String customerPhone,
                 List<String> productIds, String shippingAddress, String billingAddress,
                 String paymentMethod, String couponCode, String giftMessage, 
                 boolean giftWrap, boolean expressShipping, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.productIds = productIds;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.couponCode = couponCode;
        this.giftMessage = giftMessage;
        this.giftWrap = giftWrap;
        this.expressShipping = expressShipping;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Order{id='" + id + "', customer='" + customerId + "'}";
    }
}
