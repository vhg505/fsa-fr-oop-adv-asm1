package com.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private static AppConfig instance;
    private Map<String, String> settings;

    public AppConfig() {
        System.out.println("Loading configuration from database...");
        settings = new HashMap<>();
        settings.put("currency", "USD");
        settings.put("taxRate", "0.10");
        settings.put("maxCartItems", "50");
        settings.put("emailProvider", "sendgrid");
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String get(String key) {
        return settings.get(key);
    }
}
