package com.ecommerce_refactor.discount;

import com.ecommerce_refactor.model.Product;
import com.ecommerce_refactor.model.enumeration.ProductCategory;

/**
 * Discount strategy for clothing products.
 * Applies 10% discount on all clothing items.
 */
public class ClothingDiscountStrategy implements DiscountStrategy {
    
    private static final double DISCOUNT_PERCENTAGE = 0.10; // 10%
    
    @Override
    public boolean isApplicable(Product product) {
        return product.getCategory() == ProductCategory.CLOTHING;
    }
    
    @Override
    public double applyDiscount(Product product, double originalPrice) {
        if (!isApplicable(product)) {
            return originalPrice;
        }
        return originalPrice * (1 - DISCOUNT_PERCENTAGE);
    }
    
    @Override
    public String getDescription() {
        return "10% off all clothing";
    }
}
