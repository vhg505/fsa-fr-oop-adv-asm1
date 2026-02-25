package com.ecommerce_refactor.notification;

public interface EmailProvider {
    void sendEmail(String to, String subject, String body);
}
