# PillRem — Project Specification (PROJECT_SPEC.md)

## 1. Product Overview & Vision
PillRem is a 100% offline-first, private, clinical-grade medication reminder and tracking application built natively for Android using **Kotlin, Jetpack Compose, Room (SQLite), and AlarmManager (Exact Alarms)**.

### Core Tenets
- **Native Android & High Reliability:** Zero cross-platform framework overhead. Android `BroadcastReceiver` handles lock-screen `[Take]` and `[Snooze]` actions instantly in background with zero UI launch penalty.
- **100% Offline & Private:** Zero accounts, zero authentication, zero telemetry, zero cloud network dependencies. All clinical data resides exclusively in SQLite (Room) on the physical device.
- **Multi-Profile Native:** First-class data isolation for multiple household members on a single device.
- **Clinical & Accessible Design:** Crisp clinical blue-and-white aesthetic (with custom light, dark, and system themes) meeting WCAG AAA contrast guidelines.
- **No Gimmicks:** Strictly no AI, ads, third-party trackers, unverified drug-interaction checkers, or bloated web views.

---

## 2. Visual Design & Color Palette System (Jetpack Compose)

| Role | Light Theme Hex | Dark Theme Hex | Purpose |
|---|---|---|---|
| **Primary** | `#2B6CB0` (Clinical Steel Blue) | `#7AB2E8` (Soft Sky Blue) | Key buttons, active tabs, dose status accents |
| **Background** | `#F0F4F8` (Soft Clinical Tint) | `#0F1722` (Deep Slate Navy) | Scaffold background |
| **Surface** | `#FFFFFF` (Pure Crisp White) | `#1E293B` (Elevated Card Slate) | Cards, modals, lists, input fields |
| **Text Primary** | `#1A283B` (Deep Navy Charcoal) | `#EBF3FA` (Soft Icy White) | Headings, primary labels, values |
| **Success** | `#388E67` (Clinical Forest Green) | `#68C496` (Mint Emerald) | 'Taken' status, adherence achievements |
| **Warning / Snooze** | `#D97706` (Amber Ochre) | `#F59E0B` (Vibrant Amber) | 'Snoozed' doses, low stock warnings |
| **Missed / Alert** | `#DC2626` (Clinical Crimson) | `#F87171` (Coral Red) | 'Missed' doses, urgent reminders |
| **Skipped / Neutral**| `#64748B` (Slate Grey) | `#94A3B8` (Cool Grey) | 'Skipped' doses, secondary captions |

---

## 3. Architecture & Clean Separation of Concerns

```
app/src/main/kotlin/com/pillrem/app/
├── PillRemApplication.kt        # Channel initialization & first-boot database seeding
├── MainActivity.kt              # Jetpack Compose UI (Theme, Profile Switcher, Today List)
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt       # Room Database
│   │   ├── dao/                 # ProfileDao, MedicineDao, ScheduleDao, DoseRecordDao
│   │   └── entities/            # Room Entity definitions with ForeignKeys & Indices
│   └── models/                  # DoseStatus, ScheduleType, DoseWithMedicine
├── domain/
│   ├── DoseEngine.kt            # Pre-generation, Take, Snooze, Skip, Missed transitions
│   └── AdherenceCalculator.kt   # Taken / (Taken + Missed) * 100% (Skipped excluded)
├── receivers/
│   ├── AlarmReceiver.kt         # Fires notification when AlarmManager wakes up
│   ├── DoseActionReceiver.kt    # Instant headless handler for [Take] and [Snooze]
│   └── BootReceiver.kt          # Reschedules alarms on BOOT_COMPLETED & timezone shifts
├── services/
│   ├── NotificationHelper.kt    # NotificationChannel & NotificationCompat builder
│   └── AlarmScheduler.kt        # Exact Alarm scheduling via AlarmManager
└── ui/
    └── theme/                   # Color.kt, Theme.kt, Type.kt (Clinical Blue palette)
```

---

## 4. SQLite Database Schema (Room)

- **`profiles`**: `id`, `name`, `avatarColor`, `avatarIcon`, `createdAt`, `isActive`
- **`profile_settings`**: `profileId` (FK CASCADE), `gracePeriodMinutes`, `defaultSnoozeMinutes`, `notificationSound`, `vibrate`
- **`app_settings`**: `key`, `value`
- **`medicines`**: `id`, `profileId` (FK CASCADE), `name`, `dosageAmount`, `dosageUnit`, `form`, `instructions`, `colorTag`, `stockCount`, `lowStockThreshold`, `isArchived`, `createdAt`
- **`schedules`**: `id`, `medicineId` (FK CASCADE), `profileId` (FK CASCADE), `scheduleType`, `timeHour`, `timeMinute`, `daysOfWeek`, `intervalDays`, `startDate`, `endDate`, `isActive`, `createdAt`
- **`dose_records`**: `id`, `profileId` (FK CASCADE), `medicineId` (FK CASCADE), `scheduleId` (FK SET_NULL), `scheduledAt`, `reminderAt`, `status`, `takenAt`, `skippedAt`, `skipReason`, `snoozeCount`, `gracePeriodDeadline`, `notificationId`, `createdAt`, `updatedAt`
  - *Unique Index*: `(scheduleId, scheduledAt)`
  - *Query Index*: `(profileId, scheduledAt, status)`
