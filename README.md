# Gym Log

An Android app for tracking workouts with a clean MVVM architecture, Room persistence, and a modern Android 14 (SDK 34) target. For sample I/O traces and additional information, visit the project page:  
https://www.rsiddiq.com/software-design.html

## Highlights
- **Android SDK 34** (`minSdk=34`, `targetSdk=34`, `compileSdk=34`)
- **Language:** Java
- **Architecture:** MVVM (Activities → ViewModel → Repository → Room)
- **Persistence:** Room Database with entities for `User` and `GymLog`
- **Reactive UI:** LiveData-backed queries and a RecyclerView adapter
- **Type safety:** `LocalDateTime` persisted via a custom `TypeConverter`
- **Build:** Gradle Kotlin DSL

---

## Features
- Create accounts and log in (basic username/password model with optional admin flag).
- Add and view workout entries (exercise, weight, reps, timestamp).
- Per-user filtering of logs with queries ordered by most recent first.
- Lifecycle-aware data observation via LiveData.
- RecyclerView-based list rendering with a compact `ViewHolder`/`Adapter` layer.

> Note: The database is configured with `fallbackToDestructiveMigration` (no auto-migrations defined yet). This simplifies local development but will drop data on schema changes during upgrades.

---

## Project Structure

```
app/
  src/main/java/com/gymlog/
    LoginActivity.java
    MainActivity.java
    viewHolders/
      GymLogAdapter.java
      GymLogViewHolder.java
      GymLogViewModel.java
    database/
      GymLogDatabase.java        # Room database, builder with fallbackToDestructiveMigration
      GymLogRepository.java      # App data access facade, wraps DAOs
      GymLogDAO.java             # Queries for GymLog
      UserDAO.java               # Queries for User
      entities/
        GymLog.java              # @Entity(id, exercise, weight, reps, date, userId)
        User.java                # @Entity(id, username, password, isAdmin)
      typeConverters/
        LocalDateTypeConverter.java   # Map LocalDateTime <-> Long/String
  src/main/res/                  # Layouts, strings, colors, themes
  src/androidTest/...            # Instrumented test scaffold
  src/test/...                   # Unit test scaffold
build.gradle.kts                 # Top-level build config
app/build.gradle.kts            # Android app module (Room, AppCompat, Material, etc.)
settings.gradle.kts             # Repos, plugin management
gradle/libs.versions.toml       # Centralized dependency versions
```

---

## Data Model

### `User`
| Field    | Type     | Notes                |
|----------|----------|----------------------|
| id       | int (PK) | Auto-generated       |
| username | String   | Unique handle        |
| password | String   | Plain-string storage (demo only) |
| isAdmin  | boolean  | Optional privilege   |

### `GymLog`
| Field    | Type          | Notes                         |
|----------|---------------|-------------------------------|
| id       | int (PK)      | Auto-generated                |
| exercise | String        | Exercise name                 |
| weight   | double        | Working weight                |
| reps     | int           | Repetitions                   |
| date     | LocalDateTime | Stored via `LocalDateTypeConverter` |
| userId   | int (FK)      | Owner (User.id)               |

**Key DAO methods**
- `GymLogDAO.getRecordsByUserId(...)` / `getRecordsByUserIdLiveData(...)` – latest-first ordering.
- `UserDAO.getUserByUserName(username)` and `getUserByUserId(id)` for auth/lookup.

---

## Build & Run

### Prerequisites
- Android Studio Giraffe or later
- Android SDK Platform 34
- JDK 17+

### Steps
1. Clone or unzip the project.
2. Open the root folder in **Android Studio**.
3. Let Gradle sync dependencies.
4. Use the **app** run configuration to build & deploy to an emulator or device (Android 14 recommended).

**Gradle (Kotlin DSL) dependencies include:**
- AppCompat, Material, Activity, ConstraintLayout
- Room (`runtime` + `compiler` via `annotationProcessor`)
- JUnit / AndroidX Test / Espresso

---

## UI Flow (High Level)
- **LoginActivity**: Captures credentials and resolves the active `User` via `UserDAO`/`Repository`.
- **MainActivity**: Displays logs for the active user with a `RecyclerView` (`GymLogAdapter`/`GymLogViewHolder`), plus input fields for adding an entry. `GymLogViewModel` exposes LiveData lists to keep the UI in sync.

---

## Quality & Security Notes
- Passwords are stored as plain strings for simplicity. Consider adding hashing (e.g., BCrypt) and input validation for production scenarios.
- The database uses `fallbackToDestructiveMigration`. For persistence across app upgrades, define Room migrations and remove the fallback.
- Ensure null/empty input handling for weight/reps; current code logs and continues on parse errors.