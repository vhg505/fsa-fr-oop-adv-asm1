package com.ecommerce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderTemplate {
    private String templateName;
    private String shippingMethod;
    private String paymentMethod;
    private boolean giftWrap;
    private boolean expressShipping;
    private boolean insuranceIncluded;
    private double discountPercent;
    private List<String> defaultProductIds;
    private Map<String, String> metadata;

    public OrderTemplate(String templateName, String shippingMethod, String paymentMethod,
                         boolean giftWrap, boolean expressShipping, boolean insuranceIncluded,
                         double discountPercent) {
        this.templateName = templateName;
        this.shippingMethod = shippingMethod;
        this.paymentMethod = paymentMethod;
        this.giftWrap = giftWrap;
        this.expressShipping = expressShipping;
        this.insuranceIncluded = insuranceIncluded;
        this.discountPercent = discountPercent;
        this.defaultProductIds = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public void addProduct(String productId) {
        defaultProductIds.add(productId);
    }

    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }

    // Problem: Creating a new order from a template requires manually copying ALL fields.
    // If we add a new field to OrderTemplate, we must update every place that copies templates.
    // Also, modifying the returned object accidentally modifies the template itself (shared references).
    public OrderTemplate createFromTemplate() {
        OrderTemplate copy = new OrderTemplate(
            this.templateName, this.shippingMethod, this.paymentMethod,
            this.giftWrap, this.expressShipping, this.insuranceIncluded,
            this.discountPercent
        );
        // Bug: This shares the same list/map references!
        // Modifying the copy's products or metadata will affect the original template.
        copy.defaultProductIds = this.defaultProductIds;
        copy.metadata = this.metadata;
        return copy;
    }

    @Override
    public String toString() {
        return "OrderTemplate{name='" + templateName + "', shipping='" + shippingMethod + 
               "', products=" + defaultProductIds.size() + ", metadata=" + metadata + "}";
    }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String name) { this.templateName = name; }
    public List<String> getDefaultProductIds() { return defaultProductIds; }
    public Map<String, String> getMetadata() { return metadata; }
}
