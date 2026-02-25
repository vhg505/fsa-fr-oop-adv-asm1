package com.ecommerce_refactor.notification;

public class AmazonSESEmailProvider implements EmailProvider{
    @Override
    public void sendEmail(String to, String subject, String body) {
        System.out.println("[AmazonSES] Sending email to " + to + " | " + subject + " | " + body);
    }
}
