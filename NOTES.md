# Learning Spring Boot — Main Notes

> My main hub for Spring learning. Each project has its own notes file — this file is the index that links them all.

## What is this repo?
A place to learn **Spring / Spring Boot** step by step — from core concepts (IoC, DI, loose coupling) to building real Spring Boot apps.

---

## Projects & notes

### 1. CoreDemo — Spring Core: IoC, DI, Loose Coupling
Full notes: [CoreDemo/NOTES.md](CoreDemo/NOTES.md)

What I learned there (one-line summary):
- **IoC** = principle — the container (outside) controls object creation & lifecycle.
- **DI** = technique to achieve IoC — dependencies are injected, not built inside the class.
- **Loose coupling** — depend on the `NotificationService` *interface*, not on `SmsService` directly, so I can swap implementations freely.
- **3 DI types** — constructor (preferred), setter (optional deps), field (avoid).

Code location: [`CoreDemo/src/main/java/in/hemant/`](CoreDemo/src/main/java/in/hemant/)

---

### 2. (next project — placeholder)
- Spring Boot basics: `@SpringBootApplication`, `ApplicationContext`, beans
- `@Component`, `@Service`, `@Repository`, `@Autowired`
- Spring MVC: controllers, `@RestController`, `@GetMapping`

---

## Golden rules (from day 1)
1. Depend on the **abstraction**, not the concrete class.
2. Let the **outside** (container) decide which implementation to use -> IoC.
3. Prefer **constructor injection** — final, null-safe, easy to test.
4. One project = one notes file, linked from this main file.