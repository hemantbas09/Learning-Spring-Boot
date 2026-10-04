# CoreDemo — Learning Notes

> What I learned today: **Loose Coupling, Dependency Injection (DI), and Inversion of Control (IOC)** — the core ideas Spring Framework is built on.

---

## 1. The one-line rule

- **IOC** is the *principle* (the idea).
- **DI** is the *technique* used to achieve IOC.

> Inversion of Control = instead of a class **making** its own dependencies, the dependency is **given (injected) to it** from outside.

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
| Type | How | Notes |
|------|-----|-------|
| **Constructor injection** | passed in the constructor ✅ *(used in this demo)* | Preferred — dependency can't be null, easy to test, final field |
| **Setter injection** | passed via a `setNotification(...)` method | Optional dependencies |
| **Field injection** | `@Autowired` directly on a field | Least preferred — hard to test, hides the dependency |

### Wiring it in `Main`
```java
NotificationService notification = new SmsService();        // choose the implementation HERE
OrderService order = new OrderService(notification);        // inject it
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