# Learning Log

## 2026-10-31 – Java domain model

### What I learned

**Java package structure:**

- The directory path must exactly match the package name
- `package ch.golders.backend.domain;` → file is in `ch/golders/backend/domain/`
- Without a package declaration, a class is placed in the default package
  (which cannot be imported)

**Enums:**

- Use enums for small, known sets of values that control the system (statuses,
  roles, types)
- Do NOT use enums for data that changes or is entered by a user (makes, models)
- Example: `FuelType` as an enum ✅, `make` as a string ✅

**Immutability with `final`:**

- Declare fields that never change as `final`
- Prevents accidental changes
- Makes code easier to understand and thread-safe
- Example: `private final String id;` – can only be set in the constructor

**Encapsulation:**

- Always make fields `private`
- Access them only through getters
- Do not add setters for fields that should not change
- Later: use meaningful methods instead of setters (for example,
  `rent()` instead of `setStatus()`)

**Avoiding bidirectional relationships:**

- `Employee` does NOT have a `List<Reservation>`
- Relationships are represented through repositories
- Simpler, more flexible, and less error-prone

**Modern date API:**

- Use `java.time.LocalDate` instead of the outdated `java.util.Date`
- Date only, without a time zone – ideal for birthdays and reservation periods

**Maven:**

- `mvnw.cmd compile` – compiles the code
- The Maven Wrapper automatically downloads the correct Maven version
- Compilation is per file – only changed files are recompiled
- Bytecode (`.class` files) runs anywhere a JVM is available

**Git commit messages:**

- Conventional Commits: `feat:`, `fix:`, `docs:`, `chore:`, etc.
- Keep the first line short (50–72 characters maximum)
- Put details in the body

## 2026-11-09 – Repository interfaces in Java

### What I learned

**Interfaces as contracts:**

- An interface defines method signatures, not implementations
- Methods are implicitly `public` – the modifier can be omitted
- Business logic depends only on the interface, not the implementation
  (Dependency Inversion) → a Dataverse implementation can be swapped in later

**Default package pitfall:**

- Without a `package` declaration, a class is placed in the unnamed default
  package
- Classes in named packages cannot import classes from the default package
- Symptom: "cannot find symbol" for classes that actually exist

## 2026-11-09 – Repository pattern: interface vs. implementation

### What I learned

**Separating the interface (`domain`) from the class (`infrastructure`):**

- The interface in `domain` is the **contract** (socket) – signatures only, no
  logic
- The class in `infrastructure` is the **implementation** (plug) – it contains
  the actual logic (for example, `HashMap`)
- `implements` promises the compiler that all interface methods will be
  provided
- `@Override` marks methods from the interface → the compiler warns about typos

**The benefits:**

- A second implementation can later be added as
  `DataverseVehicleRepository implements VehicleRepository`
- The service knows only the interface → switching implementations requires
  **no changes** in the service
- Dependencies point inward toward `domain` (Dependency Inversion)

**Use interfaces internally as well:**

- `Map<String, Vehicle> vehicles = new HashMap<>()` instead of
  `HashMap<...> vehicles`
- Same principle, one level deeper

## 2026-11-09 – In-memory repositories implemented

### What I learned

**HashMap as a "database":**

- `HashMap<String, Vehicle>` – the key is the ID, the value is the object
- `put(key, value)` = INSERT/UPDATE, `get(key)` = SELECT, `values()` = all entries
- `findAll()` returns `new ArrayList<>(map.values())` (defensive copy – callers
  cannot change the internal map)

**Filtering with loops:**

- Iterate over `map.values()`, filter with `if`, and collect matches in a result
  list
- Check relationships using **ID comparisons**, not object references:
  `reservation.getVehicle().getId().equals(vehicle.getId())`

**Method chaining:**

- `reservation.getVehicle().getId()` – each getter returns an object on which
  the next getter is called
- `Reservation` stores objects (`Vehicle`, `Employee`), not IDs

**Common pitfalls:**

- The method signature when implementing an interface must match it **exactly**
  (name, parameter types, return type) – otherwise the compiler reports that an
  abstract method has not been implemented
- Java is case-sensitive: `entraId` ≠ `entraID` – a "cannot find symbol" error
  often means the spelling should be checked
- Methods not declared in the interface must not have `@Override`

## 2026-11-12 – Service and overlap checks

- `domain` describes the domain model and repository contracts; `application`
  coordinates use cases; `infrastructure` implements technical access. Depending
  on responsibility, business rules can also belong in the model.
- A service does not implement the repository itself: it receives a repository
  object through its constructor and uses it through the interface type. Wiring
  is still required; an interface does not create an object.
- Each in-memory repository instance owns its own map. Its data is lost when the
  application restarts.
- Two valid time periods overlap if each starts before the other ends. Adjacent
  time periods do not overlap.
- Compare enum values as enums, not as text.

### Next learning goals

- Connect overlap checks with saving
- Define valid time periods and relevant reservation statuses
- Write unit tests with JUnit
- Implement validated status transitions
