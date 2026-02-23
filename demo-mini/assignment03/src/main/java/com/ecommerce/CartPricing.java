package com.ecommerce;

import java.util.ArrayList;
import java.util.List;

public class CartPricing {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(String productName, double price, int quantity) {
        items.add(new CartItem(productName, price, quantity));
    }

    // Problem: Adding fees using inheritance causes class explosion:
    //   CartWithExpressShipping extends CartPricing
    //   CartWithGiftWrap extends CartPricing
    //   CartWithExpressShippingAndGiftWrap extends CartPricing
    //   CartWithInsurance extends CartPricing
    //   CartWithExpressShippingAndInsurance extends CartPricing
    //   ... and so on for every combination
    //
    // Current approach: hardcoded fee logic inside calculateTotal()
    public double calculateTotal(boolean expressShipping, boolean giftWrap, boolean insurance) {
        double total = 0;
        for (CartItem item : items) {
            total += item.price * item.quantity;
        }

        if (expressShipping) {
            total += 15.00; // Express shipping fee
        }
        if (giftWrap) {
            total += 5.00;  // Gift wrapping fee per order
        }
        if (insurance) {
            total += total * 0.02; // 2% insurance fee
        }

        // Problem: Every new fee type requires adding another boolean parameter
        // and another if-block. This violates Open/Closed Principle.
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cart:\n");
        for (CartItem item : items) {
            sb.append("  - ").append(item.productName)
              .append(" x").append(item.quantity)
              .append(" @ $").append(item.price).append("\n");
        }
        return sb.toString();
    }
}

class CartItem {
    String productName;
    double price;
    int quantity;

    CartItem(String productName, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
