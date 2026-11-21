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

## 📖 User Guide: How to Use PillRem

### 1. Quick-Start Setup (First Launch)
1. **Grant Permissions**: Upon opening PillRem for the first time, tap the banner at the top of the **Today** tab to enable:
   * **Notifications**: Required to display lock screen medication reminders.
   * **Exact Alarms**: Required by Android 12+ so alarms ring at the precise scheduled minute even during battery-saver Doze mode.
   * **Battery Optimization Exemption**: Ensures Android doesn't kill alarms when your phone is left idle overnight.
2. **Profile Selection**: PillRem comes with an initial profile. You can tap the profile avatar in the top-right corner to switch profiles or create separate profiles for family members or dependents.

---

### 2. Tab-by-Tab Feature Walkthrough

#### 📅 Today Screen (Your Daily Dose Checklist)
* **Adherence Gauge**: Displays your real-time adherence rate calculated as:
  $$\text{Adherence Rate} = \frac{\text{Taken}}{\text{Taken} + \text{Missed}} \times 100\%$$
* **Dose Checklist**: Shows all medications scheduled for today in chronological order.
* **Taking a Dose (`[Take]`)**:
  * Tap **Take** when you consume your medication.
  * The dose status instantly transitions to **Taken**, the timestamp is recorded, and your inventory stock automatically decrements.
* **Skipping a Dose (`[Skip]`)**:
  * Tap **Skip** if you are advised to pause a dose (e.g. fasting before blood tests, doctor instructions, nausea, or temporary stock-out).
  * Select or type a reason.
  * **Clinical Benefit:** Skipped doses are mathematically excluded from adherence penalties.
* **Lock Screen & Notification Actions**:
  * When an alarm fires, a high-priority banner appears with sound and vibration.
  * Tap **Take** directly from the lock screen to record it without unlocking your phone.
  * Tap **Snooze** to defer the reminder by your configured snooze duration (e.g. 15 or 20 minutes) while preserving your dose's grace-period deadline.

---

#### 💊 Cabinet Screen (Medicine Inventory & Schedules)
* **Adding a New Medication**:
  * Tap the **`+`** floating action button.
  * **Medicine Name**: Enter brand or generic name (e.g. *Metformin*).
  * **Dosage & Unit**: Supports both whole numbers and fractional quantities (e.g. `0.5`, `1`, `2.5`) with units (`tablet`, `capsule`, `mg`, `ml`, `drops`).
  * **Form**: Select tablet, capsule, syrup, injection, drops, inhaler, patch, or cream.
  * **Clinical Instructions**: Choose *With Food*, *Before Food*, *After Food*, *Empty Stomach*, or *Before Bed*.
  * **Stock Count**: Enter current quantity on hand. PillRem displays warning badges when stock drops to 7 or fewer doses remaining.
  * **Scheduled Time**: Set exact daily hour and minute.
  * Tap **Save** — PillRem instantly generates a 7-day rolling schedule and arms the system alarm.
* **Active vs. Archived Medications**:
  * When a temporary prescription ends (e.g. a 7-day course of antibiotics), tap **Archive**.
  * Archiving immediately cancels future scheduled alarms and removes pending future doses while completely preserving past dose logs for doctor review.
  * Switch to the **Archived** tab at any time to restore a medication.

---

#### 📜 History Screen (Audit Log & Adherence Correction)
* **Filterable Log**: View past medication events filtered by **All**, **Taken**, **Missed**, or **Skipped**.
* **Tap-to-Correct (Missed Dose Correction)**:
  * If you took a medication but forgot to tap the notification in time, the system will have automatically marked it as Missed after the grace period expired.
  * Tap any **Missed** record in the History tab to correct it back to **Taken**.
  * The system updates the record, logs the confirmed timestamp, decrements stock, and recalculates your adherence score.

---

#### ⚙️ Settings Screen (Profiles, Invariants & Privacy)
* **Profile Management**:
  * Create profiles for yourself, children, or elderly family members.
  * Each profile maintains completely isolated medications, schedules, stock counts, and history.
* **Clinical Invariant Settings**:
  * **Grace Period Slider (15–180 minutes)**: How long a dose remains actionable before the engine marks it as missed.
  * **Snooze Interval Slider (5–60 minutes)**: How long lock screen snooze notifications wait before re-alerting you.
* **System Alarm Diagnostics Card**:
  * Provides live diagnostics verifying whether exact alarms are enabled and battery optimization is disabled.
* **100% Offline JSON Backup & Restore**:
  * **Export JSON**: Saves your complete profile, medicine, schedule, and dose history into a clean JSON file to store in your private files or transfer to a new device.
  * **Import JSON**: Restores data with atomic transactions, schema validation, and automatic alarm reconstruction.
* **App Updates**:
  * View current build version and tap **"Check for Updates"** to query GitHub Releases directly without third-party app stores.
* **Light / Dark Mode**:
  * Toggle between Clinical Slate Blue light mode and battery-saving dark mode.

---

## Tech Stack & Architecture

| Layer | Technology |
|---|---|
| **Language** | Kotlin 2.1.10 |
| **UI Toolkit** | Jetpack Compose (Material 3) |
| **Local Database** | SQLite via AndroidX Room 2.6.1 with Foreign Key CASCADE deletes |
| **Alarm System** | Android `AlarmManager.setExactAndAllowWhileIdle()` |
| **Concurrency** | Kotlin Coroutines & Flow |
| **Background Processing** | AndroidX WorkManager 2.10.0 |
| **JSON Backup** | Google Gson 2.11.0 |
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

## Building and Running from Source

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
