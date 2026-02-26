package com.ecommerce_refactor.discount;

import com.ecommerce_refactor.model.Product;

/**
 * Special promotional discount strategy for Black Friday.
 * Applies 20% discount on ALL products regardless of category.
 * 
 * This demonstrates the Open/Closed Principle:
 * - We added a new discount rule by creating a NEW class
 * - We did NOT modify any existing discount classes
 * - We did NOT modify the DiscountStrategy interface
 * - We did NOT modify the service layer (if properly designed)
 */
public class BlackFridayDiscountStrategy implements DiscountStrategy {
    
    private static final double DISCOUNT_PERCENTAGE = 0.20; // 20%
    
    @Override
    public boolean isApplicable(Product product) {
        return true; // Applies to all products during Black Friday
    }
    
    @Override
    public double applyDiscount(Product product, double originalPrice) {
        return originalPrice * (1 - DISCOUNT_PERCENTAGE);
    }
    
    @Override
    public String getDescription() {
        return "Black Friday: 20% off everything!";
    }
}
