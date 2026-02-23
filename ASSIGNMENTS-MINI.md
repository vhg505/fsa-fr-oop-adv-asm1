# **PROJECT ASSIGNMENTS: E-COMMERCE ORDER MANAGEMENT SYSTEM (MINI)**

**Context:**
You are building the backend for an e-commerce platform. The system handles products, shopping carts, orders, payments, and notifications. The architecture must be maintainable, testable, and follow Clean Architecture principles.

**Source Code:** All initial source code is located in `demo-mini/` directory.

## **ASSIGNMENT 01: SOLID PRINCIPLES REFACTORING**

**Objective:** Refactor a legacy "God Class" order processing system to adhere to SOLID principles.

**Source Code:** `demo/assignment01/`

### **Problems**

1. The `OrderService` class handles validation, pricing, discounts, payment processing, stock management, notifications (email, SMS), logging, and analytics all in one place.
2. The discount logic uses `if-else` chains based on product category. Adding a new discount rule (e.g., "Black Friday 20% off everything") requires modifying this class.
3. The payment processing logic is embedded inside `createOrder()`. Adding a new payment method (e.g., Crypto) requires editing the same method.
4. Notification logic (email, SMS, logging) is scattered throughout multiple methods. Changing the email provider means hunting through the entire class.
5. The class uses `Object[]` arrays for data, making it impossible to use type safety or proper domain modeling.

**Action:** Analyze the problems, write down your solutions reasoning to `README.md` and refactor the code.

## **ASSIGNMENT 02: CREATIONAL DESIGN PATTERNS**

**Objective:** Apply Creational Design Patterns (Singleton, Builder, Prototype) to improve object creation in an e-commerce system.

**Source Code:** `demo-mini/assignment02/`

### **Problems**

1. The `AppConfig` allows multiple instantiations. In a multi-threaded server, two requests could create two config instances with potentially different values.
2. Creating an `Order` is confusing with 13 constructor parameters. It's unclear what `true, true, null` means without checking the signature.
3. The `OrderTemplate` stores pre-defined order configurations (e.g., "Standard Domestic", "Express International"). Currently, creating a new order from a template requires manually copying all fields. Modifying the original template accidentally affects all future orders.

**Action:** Analyze the problems, write down your solutions reasoning to `README.md` and refactor the code.

## **ASSIGNMENT 03: STRUCTURAL DESIGN PATTERNS**

**Objective:** Apply Structural Design Patterns (Adapter, Decorator, Proxy) for flexible e-commerce business logic.

**Source Code:** `demo-mini/assignment03/`

### **Problems**

1. The `LegacyPaymentGateway` uses VND currency and returns integer status codes. Your modern system uses USD and expects `boolean` responses. You cannot modify the legacy code.
2. You want to add "Express Shipping Fee" and "Gift Wrapping Fee" to cart calculations. Using inheritance leads to class explosion (CartWithShipping, CartWithGift, CartWithShippingAndGift...).
3. The `ProductCatalog` queries the database every time a product is requested, even for frequently accessed products. There is no caching layer, and adding caching logic directly into the catalog class violates Single Responsibility Principle.

**Action:** Analyze the problems, write down your solutions reasoning to `README.md` and refactor the code.
