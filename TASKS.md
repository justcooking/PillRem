# PillRem — Streamlined Milestone Roadmap (TASKS.md)

## Status Legend
- [ ] Not Started
- [/] In Progress
- [x] Completed
- [-] Blocked / Deferred

---

## Milestone 1: Core Architecture, Database & Business Logic Engine
*Goal: Complete, tested, offline-first data layer, multi-profile isolation, and dose state machine.*
- [x] Flutter project setup & dependencies (Drift, Riverpod, GoRouter, Notifications)
- [x] Clinical theme definition (Light & Dark mode from approved palette)
- [x] Drift SQLite schema (Profiles, ProfileSettings, AppSettings, Medicines, Schedules, DoseRecords)
- [x] DAOs with cascade deletes, transactions, and reactive streams
- [x] Multi-profile isolation and switching logic
- [x] Dose generation engine (idempotent rolling 7-day window)
- [x] Dose state machine logic (Take, Snooze with grace-period invariance, Skip with reason, Auto-Missed, Missed Correction)
- [x] Unit tests for database, state transitions, and adherence formulas (100% passing)

## Milestone 2: Android Notification & Background Reliability Engine
*Goal: Rock-solid alarms and notifications that trigger across reboots, app kills, and Doze mode.*
- [ ] Android permission orchestration (`POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`, battery optimization guide)
- [ ] Notification Service (high-importance channel, clinical sound, custom payload)
- [ ] Headless background action handler (`@pragma('vm:entry-point')`) for lock-screen `[Take]` and `[Snooze]` actions
- [ ] Alarm rescheduling on device boot (`RECEIVE_BOOT_COMPLETED`) and timezone/clock changes
- [ ] Verification on Android device / emulator

## Milestone 3: Presentation Layer & Complete User Flow
*Goal: Intuitive, accessible clinical UI with zero friction.*
- [ ] Top Bar with active profile avatar and quick profile switcher
- [ ] **Today Screen:** Daily medication checklist (Morning / Afternoon / Evening / Night) with instant Take/Snooze/Skip controls
- [ ] **Medicine Cabinet Screen:** Medicine list, dosage form badges, inventory/stock tracking, and schedule builder
- [ ] **History & Adherence Screen:** Calendar view, accurate adherence rate metric, and missed-dose correction modal
- [ ] **Settings Screen:** Theme selector (Light / Dark / System), profile-specific grace periods and snooze intervals
- [ ] End-to-end integration testing and final polish
