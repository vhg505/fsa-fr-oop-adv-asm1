# E-Commerce Order Processing System - Refactoring Assignment

## Overview

This assignment focuses on transforming a legacy order processing system from a monolithic "God Class" architecture into a well-designed, maintainable system that adheres to SOLID principles. The goal is to develop your understanding of software design principles and learn how to identify architectural problems and systematically address them through thoughtful refactoring.

## Current System Architecture

The existing system consists of two classes located in `src/main/java/com/ecommerce/`:

- **Main.java**: Entry point that instantiates and calls the OrderService
- **OrderService.java**: A monolithic class that handles all order processing responsibilities

### Current Project Structure

```
assignment01/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── ecommerce/
│                   ├── Main.java
│                   └── OrderService.java
└── assignment01.iml
```

### The God Class Problem

A "God Class" is an anti-pattern where a single class assumes too many responsibilities, becoming a central point of control that knows and does too much. The OrderService class currently handles:

- Order validation (checking product availability, customer information)
- Pricing calculations (base prices, tax computations)
- Discount logic (category-based discounts, promotional rules)
- Payment processing (credit card, PayPal, bank transfer)
- Stock management (inventory checks, stock updates)
- Notification delivery (email and SMS notifications)
- Logging (recording system events and errors)
- Analytics (tracking order metrics and statistics)

### Why This Design Is Problematic

**Violation of Separation of Concerns**

Each responsibility listed above represents a distinct concern that should be isolated. When all concerns are mixed together, it becomes impossible to understand, modify, or test any single aspect without considering all the others.

**High Coupling**

Every part of the system is tightly bound to every other part. Changing how discounts work might accidentally break payment processing. Adding a new notification channel requires understanding the entire order flow.

**Low Cohesion**

The class lacks focus. Methods and data that serve different purposes are forced to coexist, making the class difficult to understand and reason about.

**Rigidity**

Adding new features requires modifying the existing class, increasing the risk of introducing bugs into working functionality. Every change ripples through the entire system.

**Fragility**

Changes in one area can unexpectedly break seemingly unrelated functionality because everything is interconnected.

**Immobility**

Code cannot be reused in other contexts. You cannot extract the discount logic for use in a different system without bringing along all the other responsibilities.

**Poor Testability**

Testing individual behaviors requires setting up the entire OrderService with all its dependencies, making unit tests complex and brittle.

**Use of Primitive Data Structures**

The system uses Object arrays instead of proper domain models, eliminating type safety and making the code error-prone and difficult to understand.

## Understanding SOLID Principles

SOLID is an acronym representing five fundamental principles of object-oriented design that, when applied together, create systems that are easier to maintain, extend, and understand.

### Single Responsibility Principle (SRP)

**Definition**: A class should have one, and only one, reason to change.

**Core Concept**: Each class should focus on a single responsibility or concern. When a class has multiple responsibilities, changes to one responsibility can affect or break the others.

**Why It Matters**

When a class handles multiple responsibilities, it becomes a magnet for changes. Every time any of those responsibilities needs modification, you must open and edit that class. This increases the risk of introducing bugs and makes the class harder to understand.

**Identifying Responsibilities**

Ask yourself: "What are the different reasons this class might need to change?" Each distinct answer represents a separate responsibility that should potentially be extracted into its own class.

In the OrderService:
- Business rule changes (discounts, pricing) → one reason to change
- Payment provider integration changes → another reason to change
- Notification service changes → yet another reason to change
- Logging framework changes → another reason to change

**Benefits of SRP**

- Classes become smaller and more focused
- Code is easier to understand and navigate
- Changes are localized and less risky
- Testing becomes simpler and more targeted
- Code reuse becomes possible

### Open/Closed Principle (OCP)

**Definition**: Software entities should be open for extension but closed for modification.

**Core Concept**: You should be able to add new functionality without changing existing code. This is achieved through abstraction and polymorphism.

**Why It Matters**

Every time you modify existing code, you risk breaking functionality that already works. If your design requires editing existing classes to add new features, your system is fragile and risky to evolve.

**The Problem in OrderService**

The discount logic uses conditional chains that check product categories. Adding a new discount rule requires:
- Opening the OrderService class
- Finding the discount calculation method
- Adding new conditional branches
- Testing all existing discount logic to ensure nothing broke

Similarly, adding a new payment method requires editing the payment processing method and adding more conditional logic.

**The Solution Mindset**

Design your system so that new behaviors can be added by creating new classes, not by modifying existing ones. Use abstractions (interfaces or abstract classes) to define contracts, and let concrete implementations provide specific behaviors.

**Benefits of OCP**

- Existing code remains stable and tested
- New features are added through new code
- Reduces regression risk
- Enables parallel development
- Promotes plugin-style architectures

### Liskov Substitution Principle (LSP)

**Definition**: Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

**Core Concept**: Subtypes must be substitutable for their base types. If you have a reference to a base class or interface, you should be able to use any implementation without knowing which specific implementation you're using.

**Why It Matters**

This principle ensures that your abstractions are meaningful and that polymorphism works correctly. Violations of LSP lead to code that must check types and handle special cases, defeating the purpose of abstraction.

**Application to This Assignment**

When you design abstractions for payment processors, discount calculators, or notification services, each concrete implementation must fulfill the contract established by the abstraction. A credit card payment processor and a PayPal payment processor should both be usable through the same interface without the calling code needing to know which one it's using.

**Warning Signs of LSP Violations**

- Code that checks the specific type of an object before using it
- Implementations that throw "not supported" exceptions for interface methods
- Subclasses that require special handling or knowledge by clients
- Documentation that says "don't use this implementation in case X"

**Benefits of LSP**

- True polymorphism becomes possible
- Code becomes more flexible and extensible
- Abstractions remain meaningful and trustworthy
- Reduces the need for type checking and special cases

### Interface Segregation Principle (ISP)

**Definition**: Clients should not be forced to depend on interfaces they do not use.

**Core Concept**: Large, monolithic interfaces should be split into smaller, more specific ones so that clients only need to know about the methods that are relevant to them.

**Why It Matters**

When interfaces are too large, classes that implement them are forced to provide implementations for methods they don't need. Clients that depend on these interfaces are exposed to changes in methods they don't even use.

**The Problem Pattern**

Imagine creating a single "OrderProcessor" interface that includes methods for validation, pricing, payment, notification, and logging. Any class implementing this interface would need to implement all these methods, even if it only cares about one aspect.

**The Solution Mindset**

Create focused, role-specific interfaces. A payment processor should only need to implement payment-related methods. A notification service should only implement notification methods. A class that needs both can depend on both interfaces separately.

**Benefits of ISP**

- Interfaces become more cohesive and focused
- Implementations are simpler and more targeted
- Changes to one interface don't affect unrelated clients
- Promotes composition over inheritance
- Reduces coupling between components

### Dependency Inversion Principle (DIP)

**Definition**: High-level modules should not depend on low-level modules. Both should depend on abstractions. Abstractions should not depend on details. Details should depend on abstractions.

**Core Concept**: Your business logic should not be tightly coupled to specific implementations of supporting services. Instead, both should depend on abstract contracts (interfaces).

**Why It Matters**

When high-level business logic directly instantiates and uses concrete classes, it becomes impossible to change those dependencies without modifying the business logic. This creates rigid, hard-to-test systems.

**The Problem in OrderService**

The OrderService directly creates and uses specific implementations of payment processors, notification services, and other dependencies. It "knows" about concrete classes and is tightly coupled to them.

**The Solution Mindset**

The OrderService should depend on abstractions (interfaces) that define what it needs, not on concrete implementations. The specific implementations should be provided to it from the outside (dependency injection). This inverts the dependency: instead of the high-level module depending on low-level modules, both depend on abstractions.

**Benefits of DIP**

- Business logic becomes independent of implementation details
- Components can be easily swapped or mocked
- Testing becomes straightforward
- Parallel development is enabled
- System becomes more flexible and adaptable

## Conceptual Refactoring Strategy

### Step 1: Identify and Extract Responsibilities

**Analyze the God Class**

Read through the OrderService carefully and list every distinct responsibility. Group related operations together. Ask yourself: "If this aspect of the system needed to change, what code would be affected?"

**Create a Responsibility Map**

Document each responsibility you identify:
- What does it do?
- What data does it work with?
- What other responsibilities does it interact with?
- Why might it need to change in the future?

**Prioritize Extraction**

Some responsibilities are easier to extract than others. Start with those that have clear boundaries and minimal dependencies on other parts of the system.

### Step 2: Design Domain Models

**Replace Primitive Obsession**

The current system uses Object arrays to represent domain concepts. This is a code smell called "primitive obsession." Design proper domain models that represent the concepts in your business domain.

**Think About Entities**

What are the key nouns in your domain? Orders, Products, Customers, Payments, Discounts, Notifications? Each of these deserves its own class with appropriate properties and behaviors.

**Consider Value Objects**

Some concepts are defined by their values rather than identity. Email addresses and phone numbers are examples. These could be simple String fields in your entities, or you could create value objects if you need validation and behavior.

For this assignment, keeping prices as primitive double values is perfectly acceptable. Only create additional value object classes if they add clear value through encapsulation of validation or business logic.

**Benefits of Domain Models**

- Type safety: The compiler catches errors at compile time
- Encapsulation: Validation and business rules live with the data
- Clarity: Code becomes self-documenting
- Maintainability: Changes to a concept are localized to one class

### Step 3: Design Abstractions for Variable Behaviors

**Identify Extension Points**

Where does the system need to support multiple implementations or future variations? Discount strategies, payment methods, and notification channels are clear examples.

**Define Contracts Through Interfaces**

For each extension point, design an interface that captures the essential contract. What must any implementation be able to do? What inputs does it need? What outputs does it produce?

**Think About Naming**

Interface names should clearly communicate their purpose. Use role-based names that describe what the interface does, not how it does it.

### Step 4: Redesign Discount Handling

**The Current Problem**

Discount logic is embedded in the OrderService using conditional chains. Each product category has hardcoded discount rules. Adding a new category or changing discount rules requires modifying the OrderService.

**The Conceptual Solution**

Think of discounts as strategies or policies that can be applied to orders or products. Each discount rule becomes its own independent component that knows how to calculate a discount for a given context.

**Design Considerations**

- How will discount strategies receive the information they need?
- How will they return their results?
- Can multiple discounts be combined?
- How will the system select which discount to apply?
- How can new discount rules be added without changing existing code?

**Architectural Pattern**

Consider using the Strategy pattern, where each discount rule is a separate strategy that implements a common interface. The system can then select and apply the appropriate strategy at runtime.

### Step 5: Redesign Payment Processing

**The Current Problem**

Payment logic is embedded directly in the order creation method. Different payment methods are handled through conditional logic. Adding a new payment method requires editing the core order processing flow.

**The Conceptual Solution**

Payment processing should be abstracted behind an interface. Each payment method becomes a separate implementation of that interface. The order processing logic works with the abstraction, not with specific payment methods.

**Design Considerations**

- What operations must all payment processors support?
- How will payment processors communicate success or failure?
- How will they handle different types of payment information?
- How will the system select the appropriate payment processor?
- What happens if a payment fails?

**Architectural Pattern**

Use the Strategy pattern combined with Dependency Inversion. Define a payment processor interface, create concrete implementations for each payment method, and inject the appropriate processor into the order service.

### Step 6: Centralize Notification Behavior

**The Current Problem**

Notification logic is scattered throughout the OrderService. Email and SMS notifications are sent at various points in the order processing flow. The logic for formatting messages and calling notification services is mixed with business logic.

**The Conceptual Solution**

Extract all notification concerns into a dedicated component. This component should handle the details of sending notifications through various channels while the business logic simply requests that notifications be sent.

**Design Considerations**

- Should the system support multiple notification channels simultaneously?
- How will notification templates be managed?
- How will the system handle notification failures?
- Should notifications be synchronous or asynchronous?
- How can new notification channels be added?

**Architectural Pattern**

Consider using the Observer pattern or a notification service abstraction. The business logic publishes events or requests notifications, and the notification system handles the details of delivery.

### Step 7: Separate Cross-Cutting Concerns

**Identify Cross-Cutting Concerns**

Logging and analytics are examples of cross-cutting concerns—they affect multiple parts of the system but aren't core business logic.

**Design Considerations**

- Should these concerns be handled through dedicated services?
- Should they be injected as dependencies?
- Could they be handled through aspect-oriented techniques?
- How can they be added or removed without affecting business logic?

### Step 8: Apply Dependency Injection

**The Concept**

Instead of classes creating their own dependencies, dependencies should be provided from the outside. This is the practical application of the Dependency Inversion Principle.

**Design Considerations**

- What dependencies does each class need?
- How will these dependencies be provided?
- Where will the actual wiring of dependencies occur?
- How will this affect testing?

**Benefits**

- Classes become independent of specific implementations
- Testing becomes straightforward through mock objects
- Configuration becomes centralized
- Runtime flexibility increases

### Step 9: Restructure the Order Processing Flow

**Orchestration vs. Implementation**

After extracting responsibilities, the OrderService should become an orchestrator that coordinates the work of specialized components rather than doing all the work itself.

**Design Considerations**

- What is the high-level flow of order processing?
- Which specialized components need to be involved at each step?
- How should errors be handled?
- How should the flow be made testable?

**The New Role**

The refactored OrderService should be thin, focused on coordinating the order processing workflow by delegating to specialized services.

## Designing Without SOLID vs. With SOLID

### Without SOLID Principles

**Characteristics**

- Large, monolithic classes that handle multiple responsibilities
- Concrete dependencies hardcoded throughout the system
- Conditional logic to handle variations in behavior
- Primitive data structures instead of domain models
- Business logic mixed with infrastructure concerns
- Tight coupling between all components

**Consequences**

- Every change requires modifying existing classes
- High risk of breaking existing functionality
- Difficult to understand the system
- Testing requires complex setup
- Code reuse is nearly impossible
- Parallel development is challenging
- New team members struggle to contribute
- Technical debt accumulates rapidly

**Maintenance Burden**

Over time, the system becomes increasingly difficult to work with. Simple changes take longer and longer. Fear of breaking things leads to workarounds and patches rather than proper fixes. Eventually, the system may need to be completely rewritten.

### With SOLID Principles

**Characteristics**

- Small, focused classes with single responsibilities
- Dependencies on abstractions rather than concrete classes
- Polymorphism to handle variations in behavior
- Rich domain models that encapsulate business concepts
- Clear separation between business logic and infrastructure
- Loose coupling through interfaces and dependency injection

**Consequences**

- New features are added through new classes
- Existing code remains stable and tested
- System structure is clear and navigable
- Testing is straightforward with focused unit tests
- Components can be reused in different contexts
- Multiple developers can work in parallel
- New team members can contribute quickly
- Technical debt is minimized

**Maintenance Benefits**

The system remains flexible and adaptable over time. Changes are localized and low-risk. Developers can work confidently, knowing that changes won't have unexpected side effects. The system can evolve to meet new requirements without requiring rewrites.

## Trade-offs and Considerations

### Increased Number of Classes

**Trade-off**: A SOLID design will have more classes than a monolithic design.

**Consideration**: While this might seem like added complexity, each class is simpler and easier to understand. The overall system complexity is reduced because concerns are separated and relationships are explicit.

### Upfront Design Effort

**Trade-off**: Designing a SOLID system requires more thought upfront.

**Consideration**: This investment pays dividends over the lifetime of the system. The time saved in maintenance and enhancement far exceeds the initial design time.

### Learning Curve

**Trade-off**: Developers unfamiliar with SOLID principles may initially find the design harder to understand.

**Consideration**: Once the principles are understood, SOLID designs are actually easier to navigate because they follow predictable patterns and have clear structure.

### Abstraction Overhead

**Trade-off**: Interfaces and abstractions add a level of indirection.

**Consideration**: This indirection is what provides flexibility. The ability to change implementations without affecting clients is worth the slight overhead of working through interfaces.

## Benefits of SOLID Design

### Maintainability

Changes are localized to specific classes. You can modify discount logic without touching payment processing. You can add notification channels without affecting order validation.

### Testability

Small, focused classes with injected dependencies are easy to test in isolation. You can test discount calculations without setting up payment processors. You can test payment processing without triggering notifications.

### Scalability

The system can grow to accommodate new requirements. New payment methods, discount rules, and notification channels can be added without modifying existing code.

### Flexibility

Components can be swapped or configured differently for different contexts. You might use different payment processors in different regions or different notification services in different environments.

### Understandability

Each class has a clear purpose and limited scope. Developers can understand individual components without needing to understand the entire system.

### Reusability

Well-designed components can be used in different contexts. Your discount calculation logic might be reused in a pricing preview feature. Your notification service might be used by other parts of the application.

### Parallel Development

Multiple developers can work on different components simultaneously without conflicts. One developer can work on payment processing while another works on notifications.

### Reduced Risk

Changes are less likely to have unexpected side effects. The impact of any change is limited to a specific component and its direct dependencies.

## Recommended Project Structure After Refactoring

Organize your refactored code into logical packages that reflect the separation of concerns:

```
assignment01/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── ecommerce/
│                   ├── Main.java
│                   │
│                   ├── model/              # Domain models (entities and value objects)
│                   │   ├── Order.java
│                   │   ├── Product.java
│                   │   ├── Customer.java
│                   │   ├── OrderItem.java
│                   │   └── OrderStatus.java (enum)
│                   │
│                   ├── service/            # Business logic services
│                   │   ├── OrderService.java (refactored)
│                   │   ├── PricingService.java
│                   │   └── InventoryService.java
│                   │
│                   ├── discount/           # Discount strategies
│                   │   ├── DiscountStrategy.java (interface)
│                   │   ├── ElectronicsDiscountStrategy.java
│                   │   ├── ClothingDiscountStrategy.java
│                   │   └── NoDiscountStrategy.java
│                   │
│                   ├── payment/            # Payment processing
│                   │   ├── PaymentProcessor.java (interface)
│                   │   ├── PaymentResult.java
│                   │   ├── CreditCardPaymentProcessor.java
│                   │   ├── PayPalPaymentProcessor.java
│                   │   └── BankTransferPaymentProcessor.java
│                   │
│                   ├── notification/       # Notification services
│                   │   ├── NotificationService.java (interface)
│                   │   ├── EmailNotificationService.java
│                   │   ├── SMSNotificationService.java
│                   │   └── CompositeNotificationService.java
│                   │
│                   ├── repository/         # Data access (optional)
│                   │   ├── OrderRepository.java (interface)
│                   │   ├── ProductRepository.java (interface)
│                   │   ├── InMemoryOrderRepository.java
│                   │   └── InMemoryProductRepository.java
│                   │
│                   └── util/               # Cross-cutting concerns
│                       ├── Logger.java (interface)
│                       ├── ConsoleLogger.java
│                       ├── AnalyticsService.java (interface)
│                       └── SimpleAnalyticsService.java
└── assignment01.iml
```

### Package Organization Guidelines

**model/** - Contains domain entities and value objects
- Replace Object[] arrays with proper classes
- Encapsulate validation logic within domain objects
- Use enums for fixed sets of values (OrderStatus, PaymentMethod, ProductCategory)
- Keep it simple - use primitive double for prices unless you need currency-specific behavior

**service/** - Contains business logic orchestration
- Refactored OrderService should coordinate between other services
- Each service has a single, well-defined responsibility
- Services depend on interfaces, not concrete implementations

**discount/** - Contains discount calculation strategies
- Each discount rule is a separate class
- All implement the DiscountStrategy interface
- New discount rules can be added without modifying existing code

**payment/** - Contains payment processing implementations
- Each payment method is a separate class
- All implement the PaymentProcessor interface
- Payment logic is isolated and testable

**notification/** - Contains notification delivery implementations
- Each notification channel is a separate class
- All implement the NotificationService interface
- Consider a composite pattern to support multiple channels

**repository/** - Contains data access abstractions (optional but recommended)
- Separates data storage concerns from business logic
- Makes it easy to switch from in-memory to database storage later
- Improves testability through mock repositories

**util/** - Contains cross-cutting concerns
- Logging, analytics, and other infrastructure services
- Keep these separate from core business logic

## Approach to Refactoring

### Phase 1: Understand the Current System

Before making any changes, thoroughly understand what the system currently does:

1. Run the Main.java class and observe the output
2. Read through OrderService.java with the detailed comments
3. Trace the flow of the createOrder() method
4. Identify all the responsibilities (they're marked in the comments)
5. Document the current behavior - this is your baseline

You cannot refactor what you don't understand.

### Phase 2: Create a Vision

Design the target architecture before writing code:

1. Sketch a class diagram showing the main components and their relationships
2. List the interfaces you'll need and their key methods
3. Identify which classes will depend on which interfaces
4. Plan your package structure using the recommended structure above
5. Document your design decisions and rationale

This vision will guide your refactoring steps and help you stay focused.

### Phase 3: Refactor Incrementally

Don't try to refactor everything at once. Follow this suggested order:

**Step 1: Create Domain Models (model/ package)**
- Start with Product.java - replace Object[] product representation
- Create Order.java, OrderItem.java, Customer.java
- Create enums: OrderStatus, PaymentMethod, ProductCategory
- Use simple types (String, double, int) for basic properties
- Update OrderService to use these models (this will break things temporarily)

**Step 2: Extract Discount Logic (discount/ package)**
- Create DiscountStrategy interface
- Implement ElectronicsDiscountStrategy, ClothingDiscountStrategy, NoDiscountStrategy
- Update OrderService to use discount strategies instead of if-else chains
- Test that discounts still work correctly

**Step 3: Extract Payment Processing (payment/ package)**
- Create PaymentProcessor interface and PaymentResult class
- Implement CreditCardPaymentProcessor, PayPalPaymentProcessor, BankTransferPaymentProcessor
- Update OrderService to use payment processors
- Test that payments still work correctly

**Step 4: Extract Notification Logic (notification/ package)**
- Create NotificationService interface
- Implement EmailNotificationService and SMSNotificationService
- Consider CompositeNotificationService to send multiple notifications
- Update OrderService to use notification services
- Test that notifications still work correctly

**Step 5: Extract Cross-Cutting Concerns (util/ package)**
- Create Logger interface and ConsoleLogger implementation
- Create AnalyticsService interface and implementation
- Inject these into OrderService
- Test that logging and analytics still work

**Step 6: Apply Dependency Injection**
- Modify OrderService constructor to accept all dependencies as parameters
- Update Main.java to create and wire all dependencies
- Consider using a simple factory or builder pattern for complex wiring

**Step 7: Optional - Add Repository Layer (repository/ package)**
- Create OrderRepository and ProductRepository interfaces
- Implement in-memory versions
- Move data storage concerns out of OrderService

After each step, run Main.java and verify the system still produces the same output.

### Phase 4: Verify and Validate

After completing the refactoring:

1. Run Main.java and compare output with the original system
2. Verify all functionality still works (order creation, cancellation, shipping)
3. Check that your code follows the recommended package structure
4. Review each class to ensure it has a single responsibility
5. Verify that you can add new features without modifying existing code
6. Reflect on what you've learned about each SOLID principle

## Specific Implementation Guidelines

### Creating Domain Models

When replacing Object[] arrays with domain classes:

**Before (OrderService.java):**
```
Object[] product = {"P001", "Laptop", 999.99, 50, "ELECTRONICS"};
String id = (String) product[0];           // Unsafe casting
Double price = (Double) product[2];        // Magic index
```

**After (Product.java concept):**
- Create a Product class with proper fields: id, name, price, stock, category
- Use appropriate types: String for id/name, double for price, int for stock
- Add getters and validation in the constructor
- Consider making it immutable (final fields, no setters) or provide controlled setters

### Designing Interfaces

When creating interfaces for strategies and services:

**Key Principles:**
- Keep interfaces focused and cohesive (ISP)
- Define clear contracts with meaningful method names
- Consider what parameters are needed and what should be returned
- Think about error handling (return types, exceptions)

**Example thinking process for DiscountStrategy:**
- What does a discount strategy need to know? (Product, price, quantity?)
- What should it return? (Discounted price? Discount amount? Percentage?)
- Should it be able to determine if it applies? (isApplicable method?)

### Implementing Dependency Injection

**In Main.java:**
- Create all concrete implementations (payment processors, notification services, etc.)
- Wire them together by passing them to constructors
- This is where you decide which implementations to use
- Main becomes the "composition root" of your application

**In OrderService.java:**
- Accept dependencies through constructor parameters
- Depend on interfaces, not concrete classes
- Store dependencies as private final fields
- Use the dependencies in your methods instead of creating objects directly

### Testing Your Refactoring

**Behavioral Verification:**
- The refactored system should produce the same output as the original
- Run both versions and compare results
- Test edge cases (out of stock, payment failure, etc.)

**Design Verification:**
- Can you add a new discount rule without modifying existing classes?
- Can you add a new payment method without modifying OrderService?
- Can you test discount logic without involving payment processing?
- Can you swap notification implementations easily?

If you answer "yes" to these questions, you've successfully applied SOLID principles.

## Learning Objectives

By completing this assignment, you will:

- Recognize the problems with monolithic, tightly-coupled designs
- Understand each of the SOLID principles and why they matter
- Learn to identify responsibilities and separate concerns
- Practice designing abstractions and interfaces
- Understand the benefits of dependency injection
- Experience the difference between rigid and flexible architectures
- Develop skills in incremental refactoring
- Learn to think about software design at an architectural level

## Evaluation Criteria

Your refactored system will be evaluated on:

### Code Organization (20%)
- Follows the recommended package structure
- Classes are in appropriate packages
- Clear separation between layers (model, service, strategy, etc.)
- Logical grouping of related components

### SOLID Principles Application (40%)
- **SRP**: Each class has a single, well-defined responsibility
- **OCP**: New features can be added without modifying existing code
- **LSP**: Implementations are properly substitutable for their abstractions
- **ISP**: Interfaces are focused and role-specific
- **DIP**: Dependencies on abstractions, not concrete classes

### Domain Modeling (15%)
- Object[] arrays replaced with proper domain classes
- Appropriate use of enums for fixed value sets
- Value objects for concepts like Money and Address
- Proper encapsulation and validation

### Design Quality (15%)
- Appropriate use of design patterns (Strategy, Dependency Injection, etc.)
- Clean, readable code with meaningful names
- Proper error handling
- No code duplication

### Functionality (10%)
- System produces the same output as the original
- All features work correctly (create, cancel, ship orders)
- Edge cases handled properly

### Documentation (Optional Bonus)
- Clear comments explaining design decisions
- README section describing your refactoring approach
- Justification for key architectural choices

## Submission Guidelines

Your submission should include:

1. **Complete source code** organized according to the recommended package structure
2. **Working Main.java** that demonstrates all functionality
3. **Brief design document** (optional) explaining:
   - Your refactoring approach and order of steps
   - Key design decisions and trade-offs
   - How each SOLID principle is applied
   - Challenges faced and how you solved them

## Common Pitfalls to Avoid

### Over-Engineering
- Don't create unnecessary abstractions
- Keep it simple - solve the problems at hand
- Every interface and class should have a clear purpose

### Under-Refactoring
- Don't stop at just creating new classes
- Ensure proper use of interfaces and dependency injection
- Make sure the design is actually extensible

### Breaking Functionality
- Test frequently as you refactor
- Make small, incremental changes
- Keep the system working at each step

### Ignoring the Comments
- The existing code has detailed comments pointing out problems
- Use these as a guide for what needs to be refactored
- Each comment identifies a specific SOLID violation to address

### Poor Naming
- Use clear, descriptive names for classes and methods
- Follow Java naming conventions
- Names should reveal intent and purpose

## Conclusion

Refactoring is not just about making code "prettier" or following rules for their own sake. It's about creating systems that are easier to understand, maintain, and evolve. The SOLID principles provide a framework for thinking about design that leads to more flexible, robust software.

As you work through this assignment, focus on understanding why each principle matters and how it improves the design. Think about the trade-offs and benefits. Consider how the refactored system would handle future changes compared to the original design.

The goal is not perfection but improvement. Each step you take toward better separation of concerns, clearer abstractions, and looser coupling makes the system better. Learn from the process, and carry these principles forward into your future work.

Remember: good design is not about following rules mechanically. It's about making thoughtful decisions that balance competing concerns and create systems that serve their users and maintainers well over time.
