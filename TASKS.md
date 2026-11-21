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
- [x] Concurrency-guarded atomic dose state machine (`takeDose`, `snoozeDose`, `skipDose`, `autoMarkMissedDoses`, `correctMissedDose`)
- [x] Fractional dosage support (`0.5`, `1.5`, etc.) with `DosageFormatter` and real decimal input validation
- [x] Injected Clock support in `DoseEngine` for deterministic time testing

## Milestone 2: Native Android Exact Alarms & Notification Reliability
*Goal: Rock-solid alarms and notifications that trigger across reboots, app kills, and Doze mode.*
- [x] Android permissions orchestration (`POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`, `USE_EXACT_ALARM`)
- [x] Shared reconciliation & scheduling path (`reconcileAndScheduleAlarms`) executed on medicine save, app launch, reboot, and restore
- [x] Notification Service & High-Importance Channel with dynamic snooze action buttons honoring profile settings (`[Snooze Xm]`)
- [x] Direct `BroadcastReceiver` (`DoseActionReceiver`) for instant headless lock-screen actions
- [x] Boot & Timezone Receiver (`BootReceiver`) to automatically reschedule alarms on device restart
- [x] Full archiving integration: archiving cancels pending future alarms and cleans up pending records; active schedule queries exclude archived medicines

## Milestone 3: Presentation Layer & Complete User Flow
*Goal: Intuitive, accessible clinical UI with zero friction.*
- [x] Top App Bar with active profile avatar and quick profile switcher
- [x] **Today Screen:** Daily medication checklist with instant Take/Skip controls, adherence banner, and update notifications
- [x] **Adherence Banner:** Adherence score (% taken vs missed, excluding skipped)
- [x] Light / Dark theme toggle (Clinical Blue WCAG AAA palette)
- [x] **Medicine Cabinet & Schedule Builder screens:** Active/archived tabs, stock counts, and "+ Add Medication" wizard with strict input validation
- [x] **History & Analytics screen:** Filter chips (All, Taken, Missed, Skipped) and tap-to-correct for missed doses
- [x] **Settings & Profiles screen:** Grace period and default snooze sliders, multi-profile manager, zero-cloud privacy guarantee
- [x] **Offline JSON Backup & Restore:** Transactional import with schema version validation and automatic alarm rebuild

## Milestone 4: Over-The-Air Updates & CI/CD
*Goal: Autonomous, direct app updates without third-party store dependencies.*
- [x] In-app GitHub Releases OTA update checker (`UpdateManager.kt`)
- [x] Update Dialog with changelog viewer, progress indicator, and native package installer launch
- [x] Automated GitHub Actions CI/CD workflow (`.github/workflows/release.yml`) for git tag releases

## Milestone 5: Verification & Quality Assurance
- [x] Unit test suite covering:
  - Fractional dosage formatting (`0.5`, `1.5`, `0.25`, `1`)
  - Schedule matching (`daily`, `specific_days`, `interval_days`)
  - Adherence calculation (excluding skipped doses)
  - Deterministic notification ID generation
  - GitHub Release JSON parsing and semantic version comparisons
- [x] Clean compilation of both Debug APK (`assembleDebug`) and Release APK (`assembleRelease`)
- [x] Comprehensive Native Android `README.md` with architecture, features, and build steps
