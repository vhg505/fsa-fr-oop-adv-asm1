package com.ecommerce;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CREATIONAL PATTERNS DEMO ===\n");

        // Problem 1: Singleton - Multiple instances possible
        System.out.println("--- Singleton Problem ---");
        AppConfig config1 = new AppConfig(); // Public constructor!
        AppConfig config2 = new AppConfig(); // Another instance created!
        System.out.println("config1 == config2? " + (config1 == config2)); // false!
        System.out.println();

        // Problem 2: Builder - Telescoping constructors
        System.out.println("--- Builder Problem ---");
        Order order = new Order("ORD-001", "CUST-1", "john@email.com", "0901234567",
                Arrays.asList("P001", "P002"), "123 Main St", "456 Billing Ave",
                "CREDIT_CARD", "SAVE10", "Happy Birthday!", true, true, "Handle with care");
        // What does true, true, null mean? Unreadable!
        System.out.println("Created: " + order);
        System.out.println();

        // Problem 3: Prototype - Shallow copy bug
        System.out.println("--- Prototype Problem ---");
        OrderTemplate template = new OrderTemplate("Express International", "DHL", "CREDIT_CARD",
                false, true, true, 5.0);
        template.addProduct("P001");
        template.addProduct("P002");
        template.setMetadata("region", "US");

        OrderTemplate copy = template.createFromTemplate();
        copy.setTemplateName("Custom from Express");
        copy.getDefaultProductIds().add("P003"); // Modifying copy...

        System.out.println("Original: " + template);
        System.out.println("Copy:     " + copy);
        System.out.println("Bug! Original now has P003 too because of shared reference!");
    }
}
