# PillRem — Streamlined Milestone Roadmap (TASKS.md)

## Status Legend
- [ ] Not Started
- [/] In Progress
- [x] Completed
- [-] Blocked / Deferred

---

## Milestone 1: Native Android Core Engine & SQLite Database (Room)
*Goal: Native Kotlin setup, Room database schema, multi-profile isolation, and dose state machine.*
- [x] Native Android Kotlin project scaffold with Jetpack Compose & Gradle KTS
- [x] Clinical Blue Theme definition in Jetpack Compose (`Color.kt`, `Theme.kt`)
- [x] Room SQLite Entities (Profiles, ProfileSettings, AppSettings, Medicines, Schedules, DoseRecords)
- [x] Room DAOs with Foreign Key CASCADE deletions and reactive Kotlin `Flow`s
- [x] Multi-profile isolation and atomic profile switching
- [x] Dose generation engine (idempotent rolling 7-day window)
- [x] Dose state machine (Take, Snooze with grace-period invariance, Skip with reason, Auto-Missed, Correct Missed)
- [x] Unit test suite for Room database, dose state machine, and adherence math

## Milestone 2: Native Android Exact Alarms & Notification Reliability
*Goal: Rock-solid alarms and notifications that trigger across reboots, app kills, and Doze mode.*
- [x] Android permissions orchestration (`POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`, `USE_EXACT_ALARM`)
- [x] Notification Service & High-Importance Channel with action buttons (`[Take]`, `[Snooze 15m]`)
- [x] Direct `BroadcastReceiver` (`DoseActionReceiver`) for instant headless lock-screen actions
- [x] Boot & Timezone Receiver (`BootReceiver`) to automatically reschedule alarms on device restart
- [x] Verification on physical Android device (`A059`)

## Milestone 3: Presentation Layer & Complete User Flow
*Goal: Intuitive, accessible clinical UI with zero friction.*
- [x] Top App Bar with active profile avatar and quick profile switcher
- [x] **Today Screen:** Daily medication checklist with instant Take/Snooze/Skip controls
- [x] **Adherence Banner:** Adherence score (% taken vs missed, excluding skipped)
- [x] Light / Dark theme toggle
- [x] **Medicine Cabinet & Schedule Builder screens:** Active/archived tabs, stock counts, and "+ Add Medication" wizard
- [x] **History & Analytics screen:** Filter chips (All, Taken, Missed, Skipped) and tap-to-correct for missed doses
- [x] **Settings & Profiles screen:** Grace period and default snooze sliders, multi-profile manager, zero-cloud privacy guarantee
