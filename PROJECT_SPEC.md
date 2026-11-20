# PillRem — Project Specification (PROJECT_SPEC.md)

## 1. Product Overview & Vision
PillRem is an offline-first, private, clinical-grade medication reminder and tracking application built with Flutter, Dart, SQLite (Drift), Riverpod, and Local Notifications. 

### Core Tenets
- **100% Offline & Private:** Zero cloud accounts, zero authentication, zero telemetry, zero external network dependencies. All patient data resides exclusively on the physical device.
- **Reliable & Resilient:** Notifications and alarms must reliably trigger even across device reboots, time-zone shifts, daylight saving changes, and app termination.
- **Multi-Profile Native:** First-class isolation for multiple household members, dependents, or care recipients on a single device.
- **Clinical & Accessible Design:** Crisp clinical blue-and-white aesthetic (with custom light, dark, and system themes) meeting WCAG AAA contrast guidelines for high legibility among elderly or visually impaired users.
- **No Gimmicks:** Strictly no AI, ads, third-party trackers, unverified drug-interaction checkers, or bloated web views.

---

## 2. Visual Design & Color Palette System
Based on the approved Clinical Blue design system:

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

## 3. Architecture & Separation of Concerns

PillRem follows **Clean Architecture** with strict layer decoupling to ensure platform independence (facilitating Android-first release while ensuring seamless iOS and macOS porting):

```
lib/
├── app/
│   ├── app.dart                   # MaterialApp.router configuration
│   ├── router/                    # GoRouter configuration & route guards
│   └── theme/                     # AppTheme (Light & Dark color palettes)
├── core/
│   ├── constants/                 # Notification channels, default timings
│   ├── errors/                    # Domain failures and exceptions
│   ├── utils/                     # Date, time, formatting helpers
│   └── platform/                  # Exact alarm checks, battery optimization intent interfaces
├── data/
│   ├── local/
│   │   ├── database/              # Drift database definition, tables, migrations
│   │   │   ├── tables/            # profiles, medicines, schedules, dose_records, settings
│   │   │   └── app_database.dart  # NativeDatabase setup & query helpers
│   │   └── daos/                  # Data Access Objects per aggregate
│   └── repositories/              # Repository implementations translating Drift models to Entities
├── domain/
│   ├── entities/                  # Pure Dart immutable models (Profile, Medicine, Schedule, DoseRecord)
│   ├── value_objects/             # DoseStatus, ScheduleFrequency, DosageUnit, GracePeriod
│   └── repositories/              # Abstract repository interfaces (pure Dart contracts)
├── services/
│   ├── notification/              # NotificationService interface & local_notifications implementation
│   │   ├── notification_scheduler.dart
│   │   ├── notification_payload.dart
│   │   └── background_action_handler.dart  # @pragma('vm:entry-point') headless runner
│   ├── dose_engine/               # Dose window rolling generator & status evaluation logic
│   └── adherence/                 # Adherence calculation & statistics engine
└── presentation/
    ├── common_widgets/            # Clinical cards, dose badges, confirm dialogs, buttons
    ├── providers/                 # Riverpod providers & Notifiers
    ├── screens/
    │   ├── today/                 # Daily medication checklist & timeline
    │   ├── medicines/             # Medicine cabinet, dosage, stock tracking
    │   ├── history/               # History log, adherence chart, filters
    │   ├── profiles/              # Profile switcher and management
    │   └── settings/              # Grace period, snooze interval, theme, permissions
```

---

## 4. Comprehensive Database Schema (Drift / SQLite)

### 4.1 Profiles Table (`profiles`)
- `id`: `Int` (Auto-increment, Primary Key)
- `name`: `Text` (Max 60 chars, Not Null)
- `avatar_color`: `Int` (ARGB integer for profile badge)
- `avatar_icon`: `Text` (Identifier for medical/user icon)
- `created_at`: `DateTime` (Not Null)
- `is_active`: `Bool` (Default false; exactly one profile active at a time)

### 4.2 ProfileSettings Table (`profile_settings`)
- `profile_id`: `Int` (Primary Key, Foreign Key -> `profiles.id` ON DELETE CASCADE)
- `grace_period_minutes`: `Int` (Default 60, min 15, max 360)
- `default_snooze_minutes`: `Int` (Default 15, min 5, max 60)
- `notification_sound`: `Text` (Default 'clinical_chime')
- `vibrate`: `Bool` (Default true)

### 4.3 AppSettings Table (`app_settings`)
- `key`: `Text` (Primary Key)
- `value`: `Text` (Key-value store for app-level prefs, e.g. `theme_mode`: 'system'|'light'|'dark')

### 4.4 Medicines Table (`medicines`)
- `id`: `Int` (Auto-increment, Primary Key)
- `profile_id`: `Int` (Foreign Key -> `profiles.id` ON DELETE CASCADE)
- `name`: `Text` (e.g., 'Metformin', Not Null)
- `dosage_amount`: `Real` (e.g., 500.0)
- `dosage_unit`: `Text` (e.g., 'mg', 'ml', 'tablet', 'drops', 'puffs')
- `form`: `Text` (e.g., 'tablet', 'capsule', 'liquid', 'injection', 'inhaler')
- `instructions`: `Text` (e.g., 'Before meals', 'With plenty of water')
- `color_tag`: `Int` (Visual tag)
- `stock_count`: `Int` (Default 0, optional inventory tracking)
- `low_stock_threshold`: `Int` (Optional alert point)
- `is_archived`: `Bool` (Default false; soft delete to preserve historical dose accuracy)
- `created_at`: `DateTime` (Not Null)

### 4.5 Schedules Table (`schedules`)
- `id`: `Int` (Auto-increment, Primary Key)
- `medicine_id`: `Int` (Foreign Key -> `medicines.id` ON DELETE CASCADE)
- `profile_id`: `Int` (Foreign Key -> `profiles.id` ON DELETE CASCADE)
- `schedule_type`: `Text` ('daily', 'specific_days', 'days_interval', 'as_needed')
- `time_hour`: `Int` (0..23)
- `time_minute`: `Int` (0..59)
- `days_of_week`: `Text` (Comma-separated 1..7 for Mon..Sun when `schedule_type` = 'specific_days')
- `interval_days`: `Int` (e.g., every 2 days, for `days_interval`)
- `start_date`: `DateTime` (Not Null)
- `end_date`: `DateTime` (Nullable; if open-ended)
- `is_active`: `Bool` (Default true)
- `created_at`: `DateTime` (Not Null)

### 4.6 DoseRecords Table (`dose_records`)
- `id`: `Int` (Auto-increment, Primary Key)
- `profile_id`: `Int` (Foreign Key -> `profiles.id` ON DELETE CASCADE)
- `medicine_id`: `Int` (Foreign Key -> `medicines.id` ON DELETE CASCADE)
- `schedule_id`: `Int` (Nullable Foreign Key -> `schedules.id` ON DELETE SET NULL)
- `scheduled_at`: `DateTime` (Exact scheduled date and time)
- `reminder_at`: `DateTime` (Current trigger time; advances on snooze)
- `status`: `Text` ('pending', 'taken', 'snoozed', 'missed', 'skipped')
- `taken_at`: `DateTime` (Nullable)
- `skipped_at`: `DateTime` (Nullable)
- `skip_reason`: `Text` (Nullable, optional user reason)
- `snooze_count`: `Int` (Default 0)
- `grace_period_deadline`: `DateTime` (`scheduled_at` + profile `grace_period_minutes`)
- `notification_id`: `Int` (Unique deterministic 32-bit ID for flutter_local_notifications)
- `created_at`: `DateTime` (Not Null)
- `updated_at`: `DateTime` (Not Null)

*Indices:*
- `idx_dose_profile_time` on (`profile_id`, `scheduled_at`, `status`)
- `idx_dose_schedule_scheduled` on (`schedule_id`, `scheduled_at`) UNIQUE (Guarantees zero duplicate doses per schedule instance)

---

## 5. Dose State Machine & Notification Rules

### State Transitions
```
                ┌───────────────┐
                │    Pending    │◄──────────────┐
                └──┬────┬─────┬─┘               │
      Take Action  │    │     │ Grace Period    │ Snooze Action
                   │    │     │ Expired         │
                   ▼    │     ▼                 │
              ┌───────┐ │   ┌────────┐          │
              │ Taken │ │   │ Missed │          │
              └───────┘ │   └───┬────┘          │
                        │       │ Correction    │
            Skip Action │       ▼               │
                        │   ┌────────┐          │
                        │   │ Taken  │          │
                        │   │(Retro) │          │
                        ▼   └────────┘          │
                   ┌─────────┐                  │
                   │ Skipped │                  │
                   └─────────┘                  │
                        ▲                       │
                        │         ┌─────────┐   │
                        └─────────┤ Snoozed ├───┘
                        Skip      └─────────┘
```

1. **Snoozing:**
   - Increments `snooze_count`.
   - Schedules a new alarm for `DateTime.now() + snooze_duration`.
   - **Critical invariant:** Does **not** push back `grace_period_deadline`.
   - Prevents duplicate alarms by cancelling the previous notification ID.
2. **Auto-Missed Resolution:**
   - Evaluated proactively:
     a) On app launch / resume lifecycle events.
     b) Inside background alarms / notification trigger events.
     c) When viewing the Today or History tab.
   - Any record with `status IN ('pending', 'snoozed')` AND `DateTime.now() > grace_period_deadline` is transition-locked to `missed`.
   - User correction is supported: allows flipping a `missed` dose to `taken` with the real timestamp.
3. **Skipping:**
   - Modals prompt for confirmation and optional reason.
   - Pending notifications for this dose record are immediately cancelled.
   - Excluded from adherence penalties (adherence = Taken / (Taken + Missed)).
4. **Taking:**
   - Atomically records `taken_at = DateTime.now()`.
   - Status updated to `taken`.
   - Decrements stock count if medicine has inventory tracking enabled.
   - Cancels all pending notifications for that dose ID.

---

## 6. Android Notification & Background Reliability Specification

1. **Exact Alarms:**
   - Declares `SCHEDULE_EXACT_ALARM` and `USE_EXACT_ALARM` in `AndroidManifest.xml`.
   - Checks `canScheduleExactAlarms()` on Android 12+ (API 31+). If false, gracefully prompts user with deep-link to system permission screen.
2. **Notification Channel:**
   - High importance (`Importance.max`), high priority (`Priority.high`), custom sound, vibration pattern.
   - Actions: `[Take]`, `[Snooze]`.
3. **Background Notification Actions:**
   - Uses `@pragma('vm:entry-point')` top-level callback handler.
   - Opens Drift database in background isolate, executes transition in a transaction, and updates notification manager without launching the main UI.
4. **Reboot & Timezone Resilience:**
   - Broadcast receiver registered for `ACTION_BOOT_COMPLETED`, `ACTION_MY_PACKAGE_REPLACED`, `ACTION_TIMEZONE_CHANGED`, `ACTION_TIME_CHANGED`.
   - Runs deterministic rescheduling of all pending doses in the active rolling window (next 7 days).
