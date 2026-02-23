package com.ecommerce;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== STRUCTURAL PATTERNS DEMO ===\n");

        // Problem 1: Adapter - Legacy gateway with incompatible interface
        System.out.println("--- Adapter Problem ---");
        // Our modern system expects: boolean processPayment(double amountUSD, String card)
        // Legacy system provides: int processPaymentVND(long amountVND, String card)
        // We CANNOT modify LegacyPaymentGateway!
        com.ecommerce.legacy.LegacyPaymentGateway legacy = new com.ecommerce.legacy.LegacyPaymentGateway();
        int result = legacy.processPaymentVND(250000, "4111-1111-1111-1111");
        System.out.println("Legacy returns int: " + result + " (not boolean!)");
        System.out.println("Modern system expects USD + boolean response\n");

        // Problem 2: Decorator - Class explosion with fees
        System.out.println("--- Decorator Problem ---");
        CartPricing cart = new CartPricing();
        cart.addItem("Laptop", 999.99, 1);
        cart.addItem("Mouse", 29.99, 2);
        System.out.println(cart);

        // Every new fee type = new boolean parameter = new if-block
        double total1 = cart.calculateTotal(false, false, false);
        double total2 = cart.calculateTotal(true, true, false);
        double total3 = cart.calculateTotal(true, true, true);
        System.out.println("Base total:     $" + total1);
        System.out.println("+ Ship + Gift:  $" + total2);
        System.out.println("+ All fees:     $" + total3);
        System.out.println();

        // Problem 3: Proxy - No caching, repeated DB calls
        System.out.println("--- Proxy Problem ---");
        ProductCatalog catalog = new ProductCatalog();
        long start = System.currentTimeMillis();

        // Same product fetched 3 times = 3 slow DB queries!
        catalog.getProduct("P001");
        catalog.getProduct("P001");
        catalog.getProduct("P001");

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("3 queries for same product took " + elapsed + "ms (should be cached!)");
    }
}
