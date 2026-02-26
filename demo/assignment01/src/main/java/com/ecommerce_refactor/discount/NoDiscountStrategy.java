package com.ecommerce_refactor.discount;

import com.ecommerce_refactor.model.Product;

/**
 * Default discount strategy that applies no discount.
 * Used as a fallback when no other discount applies.
 */
public class NoDiscountStrategy implements DiscountStrategy {
    
    @Override
    public boolean isApplicable(Product product) {
        return true; // Always applicable as a fallback
    }
    
    @Override
    public double applyDiscount(Product product, double originalPrice) {
        return originalPrice; // No discount applied
    }
    
    @Override
    public String getDescription() {
        return "No discount";
    }
}
