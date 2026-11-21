# PillRem (Android Native)

<div align="center">

[![Latest Release](https://img.shields.io/github/v/release/justcooking/PillRem?style=for-the-badge&color=2B6CB0&label=Latest%20Release)](https://github.com/justcooking/PillRem/releases/latest)
[![Download APK](https://img.shields.io/badge/Download-PillRem.apk-388E67?style=for-the-badge&logo=android&logoColor=white)](https://github.com/justcooking/PillRem/releases/latest/download/PillRem-latest.apk)
[![License: MIT](https://img.shields.io/badge/License-MIT-gray?style=for-the-badge)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B-blue?style=for-the-badge&logo=android)](https://developer.android.com)

**Clinical-grade, offline-first medication reminder and adherence tracking Android application.**

*100% Offline • Zero Cloud • Zero Telemetry • Zero Ads • WCAG AAA Accessible*

</div>

---

## 📥 Where to Get the APK

### 1. Download Latest Release (Recommended for Users)
You can download the pre-compiled APK directly from GitHub Releases:
* **Direct APK Download:** [Download PillRem-latest.apk](https://github.com/justcooking/PillRem/releases/latest/download/PillRem-latest.apk)
* **All Releases & Changelogs:** [GitHub Releases Page](https://github.com/justcooking/PillRem/releases)

#### How to Install on Android:
1. Download `PillRem-latest.apk` onto your phone.
2. Tap the downloaded `.apk` notification or open it from your file manager.
3. If prompted by Android, enable **"Allow from this source"** for your browser/file manager (standard Android security check for direct APKs).
4. Tap **Install** and launch PillRem!

---

### 2. Autonomous In-App Over-The-Air (OTA) Updates
You do **not** need to manually download APKs for subsequent updates:
* PillRem features a built-in privacy-preserving update engine (`UpdateManager.kt`).
* When a new version is published to GitHub, an **"Update Available"** banner appears in the app.
* Tap **"Download & Install"** to view what's new and update with a single tap.
* **All patient data, history, and schedules are 100% preserved across updates.**

---

### 3. Local Build Output Paths (For Developers)
When compiling locally from source using Gradle:
* **Debug APK** *(Pre-signed with Android debug key, ready to sideload via ADB)*:
  ```
  app/build/outputs/apk/debug/app-debug.apk
  ```
* **Release APK** *(Optimized, shrunk release binary)*:
  ```
  app/build/outputs/apk/release/app-release-unsigned.apk
  ```

---

## Key Architecture & Features

### 1. Reliable Exact Reminders (`AlarmManager`)
* **Exact Waking Alarms**: Uses `AlarmManager.setExactAndAllowWhileIdle()` to reliably wake the device even in Android Doze mode.
* **Lock Screen Actions**: High-importance notifications with immediate headless **Take** and **Snooze** actions.
* **Unified Alarm Reconciliation**: Single reconciliation path (`DoseEngine.reconcileAndScheduleAlarms`) ensures newly added medicines, restored backups, app launches, reboots, and background workers always schedule upcoming alarms.
* **Persistent Reboot Recovery**: `BootReceiver` automatically reconciles the 7-day rolling dose schedule upon phone reboot or timezone shift.
* **Configurable Grace Periods & Snoozes**: Per-profile settings for grace periods (15–180 minutes) and dynamic snooze intervals (5–60 minutes).
* **Auto-Missed Evaluation**: Background WorkManager (`AutoMissedWorker`) marks expired unacknowledged doses as missed and replenishes future rolling doses.

### 2. Clinical Dose State Machine & Adherence
* **Invariant Dose Engine**: Rolling 7-day idempotent dose pre-generation based on schedules (`daily`, `specific_days`, `interval_days`).
* **Concurrency-Guarded Atomic Transitions**: SQLite transactions (`db.withTransaction`) preventing duplicate stock decrements or invalid re-takes.
* **Clinical Adherence Formula**:
  $$\text{Adherence Rate} = \frac{\text{Taken}}{\text{Taken} + \text{Missed}} \times 100\%$$
  *(Note: Medically skipped doses are explicitly excluded from penalties).*
* **Fractional Dosage Support**: Full decimal dosage support (`0.5 tablet`, `1.5 ml`, `0.25 mg`) with strict form validation.
* **Tap-to-Correct**: Patients can correct mistakenly marked missed doses with audit logging.
* **Archive Lifecycle**: Archiving a medicine safely cancels future alarms, clears pending dose slots, and excludes archived records from the active schedule engine.

### 3. Multi-Profile Isolation
* Independent medication schedules, history, and adherence statistics per profile (e.g., family members or dependents).
* One-tap profile switching with distinct clinical avatar colors.
* Foreign-key cascade safety preventing orphaned records.

### 4. Offline JSON Backup & Restore
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

### Install onto Connected Device via ADB
```powershell
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

### Build Release APK
```powershell
.\gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## Publishing a Release

To release a new version to users:
```powershell
git tag v1.0.0
git push origin v1.0.0
```
The automated GitHub Actions CI/CD workflow (`.github/workflows/release.yml`) will:
1. Compile the optimized Release APK.
2. Publish a GitHub Release under that tag.
3. Attach `PillRem-v1.0.0.apk` and `PillRem-latest.apk`.
4. Trigger in-app update notifications for all active PillRem users.
