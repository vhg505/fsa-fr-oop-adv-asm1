package com.ecommerce_refactor.notification;

public class MailgunEmailProvider implements EmailProvider{
    @Override
    public void sendEmail(String to, String subject, String body) {
        System.out.println("[Mailgun] Sending email to " + to + " | " + subject + " | " + body);
    }
}
