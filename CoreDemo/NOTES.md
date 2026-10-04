# CoreDemo — Learning Notes

> What I learned today: **Loose Coupling, Dependency Injection (DI), and Inversion of Control (IOC)** — the core ideas Spring Framework is built on.

---

## 1. Proper definitions

### Inversion of Control (IoC) — the *principle*
> **IoC is a design principle in which the flow of control of a program is reversed:** instead of an object creating or looking up its own collaborators, an *external component* (a container, a framework, a caller) creates them and **passes (inverts) the control of object creation and lifecycle to it**.

In plain words:
- **Normal control:** "My class builds what it needs inside itself."
- **Inverted control:** "My class only *declares* what it needs; someone else (outside) decides which concrete object to give it."

What gets "inverted"? Three things move **out of my class and into the container**:
1. **Creation** — who calls `new`
2. **Selection** — which implementation is used
3. **Lifecycle** — when objects are created and destroyed

> Example: a web app where you never call `new Request()` — the *server framework* creates the request and *calls your* code. Your control is inverted. Spring does the same thing for regular objects.

**The IoC container** is the external component that holds and manages objects (called **beans**): it creates them, wires their dependencies, and manages their lifecycle.

### Dependency Injection (DI) — the *technique*
> **DI is a pattern that *implements* the IoC principle:** an object receives ("is injected with") the other objects (its **dependencies**) it needs to function, instead of building them itself.

- **Dependency** = an object that another object needs to do its work (here: `OrderService` needs a `NotificationService`).
- **Injection** = the dependency is *given* from outside, typically through a constructor, setter, or field.

### The relationship (most important line of the day)
> **IoC is the "what" (principle). DI is the "how" (one way to achieve it).**
> IoC is a broad principle — DI, Service Locator, and event-driven hooks are all *ways* to achieve IoC. DI is the most common one, and the one Spring uses.

| | IoC | DI |
|---|-----|-----|
| Is | a **principle / idea** | a **pattern / technique** |
| Answers | *who should control object creation?* → the outside | *how does the outside give the object what it needs?* → by injecting it |
| In Spring | the Spring **container** (IoC container) | `@Autowired` / constructor wiring |

---

## 2. Tight coupling vs loose coupling

### The problem — tight coupling
A class is **tightly coupled** when it is hardwired to one specific class.

```java
// Tightly coupled: OrderService is locked to SmsService
OrderService order = new OrderService(new SmsService());
```

- If I want email instead, I have to **change the code** inside `OrderService`.
- One class knows and depends on *one exact* implementation → fragile, hard to test.

### The fix — loose coupling with an interface
Code against an **interface (abstraction)**, not a concrete class.

```java
public interface NotificationService {
    void sendNotification();
}
```

Now `OrderService` depends only on the *abstraction*:

```java
public class OrderService {
    NotificationService notification;   // type is the INTERFACE, not a concrete class
    ...
}
```

- `SmsService`, `EmailService`, `PopUpNotification` all **implement** the same interface.
- `OrderService` doesn't care *which* one it gets — it only knows `sendNotification()` exists.
- **Loose coupling achieved**: I can swap implementations without touching `OrderService`.

---

## 3. Dependency Injection (DI)

The dependency (`NotificationService`) is **passed in from outside** — that is dependency injection.

```java
public class OrderService {
    private final NotificationService notification;

    // CONSTRUCTOR INJECTION — the dependency is "injected" here
    public OrderService(NotificationService notification){
        this.notification = notification;
    }

    public void placeholder(){
        System.out.println("Order Placed");
        notification.sendNotification();   // uses whichever impl was injected
    }
}
```

### Types of Dependency Injection
There are **three** ways to inject a dependency. All achieve the same loose coupling; they differ in *where* the dependency is passed.

**1. Constructor Injection** ✅ *(the one used in this demo — and the preferred one)*
```java
public class OrderService {
    private final NotificationService notification;

    public OrderService(NotificationService notification){
        this.notification = notification;   // injected here, at object creation
    }
}
// usage:  new OrderService(new SmsService())
```
- Dependency **cannot be null** → no `NullPointerException` surprises.
- Field can be `final` → object is immutable & thread-safe.
- All dependencies are visible in one place (the constructor signature) → easy to read & unit-test (just pass mocks).
- **When:** required dependencies — almost always.

**2. Setter Injection**
```java
public class OrderService {
    private NotificationService notification;

    public void setNotification(NotificationService notification){
        this.notification = notification;   // injected after creation
    }
}
// usage:
// OrderService order = new OrderService();
// order.setNotification(new EmailService());
```
- Object can be created first, then wired → handy when a dependency is **optional** or may change over time.
- Downside: object can exist in an *invalid, half-configured* state (null until the setter is called).
- **When:** optional/reconfigurable dependencies.

**3. Field Injection**
```java
public class OrderService {
    @Autowired
    private NotificationService notification;   // Spring sets the field directly
}
```
- Least boilerplate, but **least recommended**:
  - can't be `final`, harder to unit-test (needs reflection or the container),
  - hides the dependency (no constructor signature to read),
  - circular dependencies get silently allowed.
- **When:** basically avoid it; only for quick legacy/prototype code.

**Quick comparison**

| Type | Can be `final`? | Null-safe? | Easy to test? | Best for |
|------|:---:|:---:|:---:|----------|
| **Constructor** ✅ | Yes | Yes | Yes | Required deps (default choice) |
| **Setter** | No | No | Moderate | Optional / reconfigurable deps |
| **Field** | No | No | Hard | Avoid |

### Wiring it in `Main`
```java
NotificationService notification = new SmsService();        // choose the implementation HERE
OrderService order = new OrderService(notification);        // inject it (constructor injection)
order.placeholder();                                        // → "Order Placed" + "Sms is send"
```

To switch to email, I change **one line** in `Main` — `OrderService` is untouched:
```java
NotificationService notification = new EmailService();
```

---

## 4. IOC — Inversion of Control

- **Without IOC:** `OrderService` is in control — it creates and chooses its own `SmsService` internally.
- **With IOC:** control is **inverted**. `OrderService` no longer decides *which* notification it uses. `Main` (later: the **Spring container**) decides and hands it in.

> The object no longer controls its dependencies — the *outside* does. That inversion is the whole point.

---

## 5. Where the Spring Framework fits

In this demo, `Main` is doing the wiring by hand.
**Spring** does this wiring **automatically** using the container:

1. I mark the classes: `@Component` / `@Service` for the implementations, `@Service` for `OrderService`.
2. The dependency is injected by the container (constructor injection).
3. The **Spring container** finds the right implementation of `NotificationService` and **injects it** — no `new` needed.

```java
@Service
public class OrderService {
    private final NotificationService notification;

    // Spring finds a bean that implements NotificationService and injects it
    public OrderService(NotificationService notification){
        this.notification = notification;
    }
}
```

**Benefit:** swap SMS → email by changing which bean Spring picks, without editing `OrderService`. That's loose coupling + DI + IOC, managed by the framework.

---

## 6. Today's class map

```
            NotificationService   (interface)
             /        |        \
            /         |         \
   SmsService   EmailService  PopUpNotification
   (impl)        (impl)         (impl)

OrderService  ──depends on──▶  NotificationService   (injected via constructor)
```

**Key takeaways**
1. Interface = contract; implementations = options.
2. Depend on the abstraction, not the concrete class → loose coupling.
3. Let the dependency be given to you (constructor injection) → DI.
4. Hand the "who decides" power to the outside / Spring container → IOC.
5. Spring automates all of it with the container + annotations.

---

*Files referenced: `Main.java`, `OrderService.java`, `notification/NotificationService.java`, `notification/SmsService.java`, `notification/EmailService.java`, `notification/PopUpNotification.java`*