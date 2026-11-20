# PillRem — Master Task Plan (TASKS.md)

## Status Legend
- [ ] Not Started
- [/] In Progress
- [x] Completed
- [-] Blocked / Deferred

---

## Phase 1: Project Setup, Design System & Architecture Foundation
- [ ] Initialize Flutter project `pillrem` with appropriate Android package identifier (`com.pillrem.app`)
- [ ] Configure `pubspec.yaml` with core dependencies (Drift, Sqlite3 Flutter Libs, Riverpod, Flutter Hooks, GoRouter, Flutter Local Notifications, Intrinsic Timezone, Google Fonts)
- [ ] Setup Clinical Blue Color Palette & Typography (ThemeData for Light & Dark mode, matching user spec palette)
- [ ] Establish Clean Architecture folder structure (`core/`, `data/`, `domain/`, `presentation/`, `services/`)
- [ ] Implement error handling and result types
- [ ] Unit test: Verify theme tokens, contrast ratios, and domain value objects

## Phase 2: Database Layer & Domain Entities (Drift + SQLite)
- [ ] Define Drift tables:
  - [ ] `profiles`
  - [ ] `profile_settings`
  - [ ] `app_settings`
  - [ ] `medicines`
  - [ ] `schedules`
  - [ ] `dose_records`
- [ ] Add foreign keys with cascade delete & unique constraints
- [ ] Build DAOs with reactive Streams and transaction methods
- [ ] Write DB unit tests covering:
  - [ ] Profile CRUD & active profile switching
  - [ ] Medicine creation, soft-delete archiving, and stock decrements
  - [ ] Schedule recurrence rules and calculations
  - [ ] Dose record creation and atomic state transitions
  - [ ] Cascade deletes on profile deletion

## Phase 3: Profile Management & Switching
- [ ] Domain repository and Riverpod Notifier for `ProfileState`
- [ ] Profile Switcher UI (Quick switch dropdown/bottom sheet in top app bar)
- [ ] Profile Management Screen (Create profile, edit name/avatar color/icon, delete profile)
- [ ] Profile Settings Screen (Configure grace period [15-360m], default snooze [5-60m], notification chime)
- [ ] Default profile auto-initialization on first app launch
- [ ] Unit & Widget tests: Multi-profile isolation and switching

## Phase 4: Medicine & Schedule Management
- [ ] Medicine Domain Models & Repositories
- [ ] Medicine List Screen ("Cabinet") with active & archived tabs, search, and stock indicators
- [ ] Medicine Creation / Edit Wizard:
  - [ ] Name, dosage amount & unit (mg, ml, tablet, etc.)
  - [ ] Medication form & clinical instructions
  - [ ] Inventory tracking (stock count & reorder reminder threshold)
  - [ ] Color tag and icon picker
- [ ] Schedule Builder:
  - [ ] Frequency (Everyday, specific days of week, day intervals, as-needed)
  - [ ] Dose times per day
  - [ ] Start date and optional end date
- [ ] Unit tests: Medicine & schedule validation logic

## Phase 5: Dose Generation Engine & State Machine
- [ ] Rolling Dose Window Generator (pre-generates doses for rolling 7-day window)
- [ ] Idempotent generation using unique constraint `(schedule_id, scheduled_at)`
- [ ] Dose State Machine:
  - [ ] `takeDose(doseId)`: records timestamp, cancels notification, decrements inventory
  - [ ] `snoozeDose(doseId, duration)`: keeps pending, updates reminder time, keeps original grace deadline
  - [ ] `skipDose(doseId, reason)`: prompts confirmation, cancels notification, logs reason
  - [ ] `evaluateMissedDoses()`: transitions pending doses past grace deadline to missed
  - [ ] `correctMissedDose(doseId, actualTime)`: allows user to correct missed to taken
- [ ] Unit tests: State machine transitions, snooze grace invariance, and adherence calculations

## Phase 6: Notification & Background Alarm Engine
- [ ] Android Permissions & Exact Alarm integration:
  - [ ] `POST_NOTIFICATIONS` runtime permission request
  - [ ] `SCHEDULE_EXACT_ALARM` check & deep link settings fallback
  - [ ] Battery optimization guidance banner
- [ ] Local Notification Service:
  - [ ] Clinical notification channels (high importance, vibration, sound)
  - [ ] Android notification payload with profile name, medicine name, dosage, and dose ID
  - [ ] Action buttons: `[Take]`, `[Snooze 15m]`
- [ ] Background Action Handler:
  - [ ] Top-level `@pragma('vm:entry-point')` callback
  - [ ] Direct Drift database update in background isolate
- [ ] Broadcast Receivers:
  - [ ] Boot receiver (`ACTION_BOOT_COMPLETED`)
  - [ ] Time/Timezone change receiver
- [ ] Integration & manual test guide for physical Android devices

## Phase 7: Presentation Layer — Today, History & Analytics
- [ ] "Today" Timeline Screen:
  - [ ] Profile banner & profile switcher
  - [ ] Day selector / date carousel
  - [ ] Morning, Afternoon, Evening, Night grouping
  - [ ] Interactive dose cards with quick swipe or tap actions (Take, Snooze, Skip)
  - [ ] Empty state when no doses scheduled
- [ ] "History & Adherence" Screen:
  - [ ] Calendar view with adherence dot indicators
  - [ ] Adherence rate metric (% taken on time vs missed, excluding skipped)
  - [ ] Filterable chronological log (All, Taken, Missed, Skipped)
  - [ ] Missed dose correction dialog
- [ ] "Settings" Screen:
  - [ ] Theme switcher (Light, Dark, System)
  - [ ] Active profile preferences (Grace period, Snooze interval)
  - [ ] Notification diagnostic & permission status tool
  - [ ] Database backup / export (JSON/CSV) for offline peace of mind

## Phase 8: Verification, Device Testing & Polish
- [ ] End-to-end multi-profile flow testing
- [ ] Snooze and grace period boundary tests (edge cases: midnight crossing, DST shift)
- [ ] Android physical device validation (background kill, lock screen actions, reboot test)
- [ ] Accessibility & contrast review (WCAG AAA verification with clinical palette)
- [ ] Technical limitations documentation and release readiness
