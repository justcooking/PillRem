# PillRem (Android Native)

> **Clinical-grade, offline-first medication reminder and adherence tracking Android application.**

PillRem is built strictly adhering to the philosophy of **simplicity, clinical reliability, and privacy**. It features zero cloud dependencies, zero user accounts, zero analytics or telemetry, and zero advertising. All sensitive patient medication data stays strictly on the physical device in a secure SQLite database.

---

## Key Architecture & Features

### 1. Reliable Exact Reminders (`AlarmManager`)
* **Exact Waking Alarms**: Uses `AlarmManager.setExactAndAllowWhileIdle()` to reliably wake the device even under aggressive Android Doze mode.
* **Lock Screen Actions**: High-importance notifications with immediate headless **Take** and **Snooze** actions.
* **Persistent Reboot Recovery**: `BootReceiver` automatically reconciles the 7-day rolling dose schedule and schedules alarms upon device reboot, app update, or timezone shift.
* **Configurable Grace Periods & Snoozes**: Per-profile settings for grace periods (15–180 minutes) and snooze intervals (5–60 minutes).
* **Auto-Missed Evaluation**: Background WorkManager (`AutoMissedWorker`) marks expired unacknowledged doses as missed and replenishes future rolling doses.

### 2. Clinical Dose State Machine & Adherence
* **Invariant Dose Engine**: Rolling 7-day idempotent dose pre-generation based on schedules (`daily`, `specific_days`, `interval_days`).
* **Atomic State Transitions**: Concurrency-guarded, transactional transitions preventing duplicate stock deductions or invalid re-takes.
* **Clinical Adherence Formula**:
  $$\text{Adherence Rate} = \frac{\text{Taken}}{\text{Taken} + \text{Missed}} \times 100\%$$
  *(Note: Medically skipped doses are explicitly excluded from penalties).*
* **Fractional Dosage Support**: Full decimal dosage support (e.g., 0.5 tablets, 1.5 ml) with clean typography.
* **Tap-to-Correct**: Patients can correct mistakenly marked missed doses with audit logging.

### 3. Multi-Profile Isolation
* Independent medication schedules, history, and adherence statistics per profile (e.g., family members or dependents).
* One-tap profile switching with distinct clinical avatar colors.
* Foreign-key cascade safety preventing orphaned records.

### 4. Over-The-Air (OTA) In-App Updates
* Direct GitHub Releases update checker (`https://api.github.com/repos/justcooking/PillRem/releases/latest`).
* Complete changelog viewer, download progress indicator, and native Android `PackageInstaller` launch via `FileProvider`.
* Zero third-party app store or cloud tracking required.

### 5. Offline JSON Backup & Restore
* Complete database export and import to standard JSON format.
* Transactional atomic restores with schema version validation and automatic alarm reconstruction.

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin 2.1.10 |
| **UI Toolkit** | Jetpack Compose (Material 3) |
| **Local Database** | SQLite via AndroidX Room 2.6.1 |
| **Async & Concurrency** | Kotlin Coroutines & Flow |
| **Background Processing** | AndroidX WorkManager 2.10.0 |
| **JSON Serialization** | Google Gson 2.11.0 |
| **Testing** | JUnit 4, Kotlinx Coroutines Test |

---

## Design System: Clinical Blue (WCAG AAA)

PillRem follows strict WCAG AAA contrast standards for healthcare accessibility:

* **Light Theme**:
  * Primary: `#2B6CB0` (Clinical Slate Blue)
  * Background: `#F0F4F8`
  * Surface: `#FFFFFF`
  * Text Primary: `#1A283B`
  * Success: `#388E67` | Warning: `#D97706` | Danger: `#DC2626`
* **Dark Theme**:
  * Primary: `#7AB2E8`
  * Background: `#0F1722`
  * Surface: `#1E293B`
  * Text Primary: `#EBF3FA`
  * Success: `#68C496` | Warning: `#F59E0B` | Danger: `#F87171`

---

## Building and Running

### Prerequisites
* JDK 17 (or Android Studio JBR 17+)
* Android SDK (API 35 compile target, minimum API 26 / Android 8.0)

### Run Unit Tests
```powershell
.\gradlew testDebugUnitTest
```

### Build Debug APK
```powershell
.\gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Install onto Device via ADB
```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

### Build Release APK
```powershell
.\gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## Releasing Updates

To publish an update to users, simply create and push a git tag:
```powershell
git tag v1.1.1
git push origin v1.1.1
```
The automated GitHub Actions CI/CD workflow (`.github/workflows/release.yml`) will automatically compile the release APK and publish a GitHub Release with the APK attached. Running PillRem instances will detect the update and prompt users to update in-app.
