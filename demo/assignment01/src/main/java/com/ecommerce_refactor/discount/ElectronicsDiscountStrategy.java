package com.ecommerce_refactor.discount;

import com.ecommerce_refactor.model.Product;
import com.ecommerce_refactor.model.enumeration.ProductCategory;

/**
 * Discount strategy for electronics products.
 * Applies 5% discount on electronics items priced over $500.
 */
public class ElectronicsDiscountStrategy implements DiscountStrategy {
    
    private static final double DISCOUNT_PERCENTAGE = 0.05; // 5%
    private static final double MINIMUM_PRICE = 500.0;
    
    @Override
    public boolean isApplicable(Product product) {
        return product.getCategory() == ProductCategory.ELECTRONICS 
               && product.getPrice() > MINIMUM_PRICE;
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
        return "5% off electronics over $500";
    }
}
