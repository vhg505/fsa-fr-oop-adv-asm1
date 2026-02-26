package com.ecommerce_refactor.discount;

import com.ecommerce_refactor.model.Product;

/**
 * Strategy interface for discount calculation.
 * Follows the Open/Closed Principle: open for extension, closed for modification.
 * 
 * To add a new discount rule, simply create a new class implementing this interface.
 * No need to modify existing discount classes or the service layer.
 */
public interface DiscountStrategy {
    
    /**
     * Checks if this discount strategy is applicable to the given product.
     * 
     * @param product The product to check
     * @return true if this discount applies, false otherwise
     */
    boolean isApplicable(Product product);
    
    /**
     * Calculates the discounted price for the given product.
     * 
     * @param product The product to apply discount to
     * @param originalPrice The original price before discount
     * @return The discounted price
     */
    double applyDiscount(Product product, double originalPrice);
    
    /**
     * Gets a description of this discount for logging/display purposes.
     * 
     * @return A human-readable description of the discount
     */
    String getDescription();
}
