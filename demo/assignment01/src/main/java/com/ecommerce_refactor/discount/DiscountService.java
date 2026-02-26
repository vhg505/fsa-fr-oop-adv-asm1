package com.ecommerce_refactor.discount;

import com.ecommerce_refactor.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that manages and applies discount strategies.
 * 
 * This class demonstrates several SOLID principles:
 * - Single Responsibility: Only handles discount calculation logic
 * - Open/Closed: Can add new discount strategies without modifying this class
 * - Dependency Inversion: Depends on DiscountStrategy abstraction, not concrete classes
 * 
 * Usage:
 * 1. Create a DiscountService instance
 * 2. Register discount strategies (order matters - first applicable wins)
 * 3. Call calculateDiscountedPrice() for each product
 */
public class DiscountService {
    
    private final List<DiscountStrategy> discountStrategies;
    
    /**
     * Constructor with dependency injection.
     * Allows external configuration of which discount strategies to use.
     */
    public DiscountService(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = new ArrayList<>(discountStrategies);
    }
    
    /**
     * Default constructor with standard discount strategies.
     */
    public DiscountService() {
        this.discountStrategies = new ArrayList<>();
        // Register default strategies in priority order
        this.discountStrategies.add(new ElectronicsDiscountStrategy());
        this.discountStrategies.add(new ClothingDiscountStrategy());
        this.discountStrategies.add(new NoDiscountStrategy()); // Fallback
    }
    
    /**
     * Calculates the discounted price for a product.
     * Uses the first applicable discount strategy.
     * 
     * @param product The product to calculate discount for
     * @return The discounted price
     */
    public double calculateDiscountedPrice(Product product) {
        double originalPrice = product.getPrice();
        
        // Find the first applicable discount strategy
        for (DiscountStrategy strategy : discountStrategies) {
            if (strategy.isApplicable(product)) {
                return strategy.applyDiscount(product, originalPrice);
            }
        }
        
        // Fallback: return original price if no strategy applies
        return originalPrice;
    }
    
    /**
     * Adds a new discount strategy to the service.
     * Useful for runtime configuration or promotional periods.
     * 
     * @param strategy The discount strategy to add
     */
    public void addDiscountStrategy(DiscountStrategy strategy) {
        this.discountStrategies.add(0, strategy); // Add at beginning for highest priority
    }
    
    /**
     * Removes a discount strategy from the service.
     * 
     * @param strategyClass The class of the strategy to remove
     */
    public void removeDiscountStrategy(Class<? extends DiscountStrategy> strategyClass) {
        this.discountStrategies.removeIf(strategy -> strategy.getClass().equals(strategyClass));
    }
    
    /**
     * Gets all registered discount strategies.
     * 
     * @return List of discount strategies
     */
    public List<DiscountStrategy> getDiscountStrategies() {
        return new ArrayList<>(discountStrategies);
    }
}
