# **SELF-LEARNING GUIDE: OOP ADVANCED — MINI PROGRAM**

**Target Audience:** Intern Developer/Data Engineer

**Format:** Self-learning (Independent Research) & Workshop

**Duration:** 5 days (self-learning + assignment + workshop)

**Design Patterns Focus:** Singleton, Builder, Prototype, Adapter, Decorator, Proxy

## **I. LEARNING ROADMAP & KNOWLEDGE SCOPE**

### **DAY 1+2: ADVANCED OOP CONCEPTS**

**Goal:** Understand advanced object-oriented concepts and principles to design robust systems.

#### **1. Scope (Key Knowledge Areas)**

- **Association & Aggregation & Composition:** Understanding the "has-a" relationships and ownership levels.
- **Coupling & Cohesion:** Aiming for loose coupling and high cohesion.
- **Composition over Inheritance:** Why favors object composition over class inheritance.
- **Interface over Abstract Class:** Preferring interfaces to define types.
- **SOLID Principles:**
  - **S**ingle Responsibility Principle (SRP)
  - **O**pen/Closed Principle (OCP)
  - **L**iskov Substitution Principle (LSP)
  - **I**nterface Segregation Principle (ISP)
  - **D**ependency Inversion Principle (DIP)

#### **2. Instruction: How to leverage AI/Web**

Sample prompts:

- **SOLID:** "Explain SOLID principles using a 'Coffee Shop' analogy. Why is a 'RobotBarista' handling payments a violation of SRP?"
- **Composition vs Inheritance:** "Why is 'Composition over Inheritance' preferred? Give a Java example where Inheritance causes specific problems (fragile base class)."
- **Interface vs Abstract Class:** "In Java 17+, interfaces can have private and default methods. When should I ever use an Abstract Class now?"
- **Coupling:** "What is Tight Coupling? Rewrite this tightly coupled Java code [paste code] to use Dependency Injection."
- **LSP:** "Explain Liskov Substitution Principle. Why does 'Square extends Rectangle' break this principle?"

_Action:_ Complete **Assignment 01** in [ASSIGNMENTS-MINI.md](./ASSIGNMENTS-MINI.md).

### **DAY 3: CREATIONAL DESIGN PATTERNS**

**Goal:** Learn patterns for object creation mechanisms.

#### **1. Scope (Key Knowledge Areas)**

- **Singleton:** Ensure a class has only one instance and provide a global point of access to it.
- **Builder:** Separate the construction of a complex object from its representation.
- **Prototype:** Specify the kinds of objects to create using a prototypical instance, and create new objects by copying this prototype.

#### **2. Instruction: How to leverage AI/Web**

Sample prompts:

- **Singleton:** "Show me 3 ways to implement Singleton in Java. Which one is thread-safe and why is `enum` the best?"
- **Builder:** "Refactor this class with a constructor having 10 parameters to use the Builder Pattern."
- **Prototype:** "When is the Prototype pattern useful? How does `Cloneable` interface in Java relate to this?"
- **Real-world:** "Give examples of Builder pattern usage in standard Java libraries (e.g., `StringBuilder`, `Stream.Builder`)."

_Action:_ Complete **Assignment 02** in [ASSIGNMENTS-MINI.md](./ASSIGNMENTS-MINI.md).

### **DAY 4: STRUCTURAL DESIGN PATTERNS**

**Goal:** Learn patterns for object composition to form new structures with enhanced capabilities.

#### **1. Scope (Key Knowledge Areas)**

- **Adapter:** Convert the interface of a class into another interface clients expect.
- **Decorator:** Attach additional responsibilities to an object dynamically.
- **Proxy:** Provide a surrogate or placeholder for another object to control access to it.

#### **2. Instruction: How to leverage AI/Web**

Sample prompts:

- **Adapter vs Decorator:** "Both Adapter and Decorator wrap an object. What is the difference in INTENT? Give a code example comparing them."
- **Decorator:** "How does the Decorator Pattern prevent class explosion when adding features? Show a Java example with I/O streams."
- **Proxy:** "What is the difference between specific Proxy types: Remote Proxy vs Virtual Proxy vs Protection Proxy?"
- **Real-world:** "How does Spring Framework use Proxy pattern for AOP and `@Transactional`?"

_Action:_ Complete **Assignment 03** in [ASSIGNMENTS-MINI.md](./ASSIGNMENTS-MINI.md).

### **DAY 5: WORKSHOP**

**Activity:** Present and review Assignment 02 & 03 solutions. Discuss pattern trade-offs and real-world applications.
