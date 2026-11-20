import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'core/theme/app_theme.dart';
import 'data/local/database/app_database.dart';
import 'data/local/daos/dose_record_dao.dart';
import 'domain/value_objects/medication_enums.dart';
import 'services/dose_engine/dose_engine_service.dart';
import 'services/adherence/adherence_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final database = AppDatabase();
  final doseEngine = DoseEngineService(database);
  final adherenceService = AdherenceService(database);

  runApp(
    ProviderScope(
      child: PillRemApp(
        database: database,
        doseEngine: doseEngine,
        adherenceService: adherenceService,
      ),
    ),
  );
}

class PillRemApp extends StatefulWidget {
  final AppDatabase database;
  final DoseEngineService doseEngine;
  final AdherenceService adherenceService;

  const PillRemApp({
    super.key,
    required this.database,
    required this.doseEngine,
    required this.adherenceService,
  });

  @override
  State<PillRemApp> createState() => _PillRemAppState();
}

class _PillRemAppState extends State<PillRemApp> {
  ThemeMode _themeMode = ThemeMode.light;

  void _toggleTheme() {
    setState(() {
      _themeMode = _themeMode == ThemeMode.light ? ThemeMode.dark : ThemeMode.light;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'PillRem',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: _themeMode,
      home: TestBenchScreen(
        database: widget.database,
        doseEngine: widget.doseEngine,
        adherenceService: widget.adherenceService,
        onToggleTheme: _toggleTheme,
        currentThemeMode: _themeMode,
      ),
    );
  }
}

class TestBenchScreen extends StatefulWidget {
  final AppDatabase database;
  final DoseEngineService doseEngine;
  final AdherenceService adherenceService;
  final VoidCallback onToggleTheme;
  final ThemeMode currentThemeMode;

  const TestBenchScreen({
    super.key,
    required this.database,
    required this.doseEngine,
    required this.adherenceService,
    required this.onToggleTheme,
    required this.currentThemeMode,
  });

  @override
  State<TestBenchScreen> createState() => _TestBenchScreenState();
}

class _TestBenchScreenState extends State<TestBenchScreen> {
  ProfileEntry? _activeProfile;
  List<ProfileEntry> _profiles = [];
  List<DoseWithMedicine> _doses = [];
  AdherenceStats? _adherence;
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _refreshData();
  }

  Future<void> _refreshData() async {
    setState(() => _isLoading = true);

    // Auto-seed if database is empty on first boot
    final profiles = await widget.database.profileDao.getAllProfiles();
    if (profiles.isEmpty) {
      await _seedInitialData();
    }

    final active = await widget.database.profileDao.getActiveProfile();
    final allProfiles = await widget.database.profileDao.getAllProfiles();

    List<DoseWithMedicine> doses = [];
    AdherenceStats? stats;

    if (active != null) {
      final now = DateTime.now();
      final start = DateTime(now.year, now.month, now.day, 0, 0);
      final end = DateTime(now.year, now.month, now.day, 23, 59);

      // Pre-generate rolling doses for today if empty
      await widget.doseEngine.generateRollingDoses(daysAhead: 3);

      doses = await widget.database.doseRecordDao.getDosesForProfileRange(active.id, start, end);
      stats = await widget.adherenceService.getAdherenceStats(active.id);
    }

    setState(() {
      _activeProfile = active;
      _profiles = allProfiles;
      _doses = doses;
      _adherence = stats;
      _isLoading = false;
    });
  }

  Future<void> _seedInitialData() async {
    // Profile 1: Sarah
    final sarahId = await widget.database.profileDao.createProfile(
      name: 'Sarah Connor',
      avatarColor: 0xFF2B6CB0,
      avatarIcon: 'person',
      gracePeriodMinutes: 60,
      defaultSnoozeMinutes: 15,
    );

    // Medicine 1: Metformin
    final metId = await widget.database.medicineDao.insertMedicine(
      MedicinesCompanion.insert(
        profileId: sarahId,
        name: 'Metformin',
        dosageAmount: const Value(500.0),
        dosageUnit: const Value('mg'),
        form: const Value('tablet'),
        instructions: const Value('With dinner'),
        colorTag: const Value(0xFF2B6CB0),
        stockCount: const Value(28),
      ),
    );

    // Medicine 2: Lisinopril
    final lisId = await widget.database.medicineDao.insertMedicine(
      MedicinesCompanion.insert(
        profileId: sarahId,
        name: 'Lisinopril',
        dosageAmount: const Value(10.0),
        dosageUnit: const Value('mg'),
        form: const Value('tablet'),
        instructions: const Value('In the morning'),
        colorTag: const Value(0xFF388E67),
        stockCount: const Value(14),
      ),
    );

    // Schedules
    final now = DateTime.now();
    await widget.database.scheduleDao.insertSchedule(
      SchedulesCompanion.insert(
        profileId: sarahId,
        medicineId: lisId,
        scheduleType: ScheduleType.daily.value,
        timeHour: 8,
        timeMinute: 0,
        startDate: now,
      ),
    );

    await widget.database.scheduleDao.insertSchedule(
      SchedulesCompanion.insert(
        profileId: sarahId,
        medicineId: metId,
        scheduleType: ScheduleType.daily.value,
        timeHour: 20,
        timeMinute: 0,
        startDate: now,
      ),
    );

    // Profile 2: Grandpa Joe
    await widget.database.profileDao.createProfile(
      name: 'Grandpa Joe',
      avatarColor: 0xFF388E67,
      avatarIcon: 'elderly',
      gracePeriodMinutes: 90,
      defaultSnoozeMinutes: 20,
    );
  }

  @override
  Widget build(BuildContext context) {
    final isDark = widget.currentThemeMode == ThemeMode.dark;

    return Scaffold(
      appBar: AppBar(
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'PillRem',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
            ),
            if (_activeProfile != null)
              Text(
                'Active: ${_activeProfile!.name}',
                style: TextStyle(
                  fontSize: 12,
                  color: isDark ? AppColors.darkTextSecondary : AppColors.lightTextSecondary,
                ),
              ),
          ],
        ),
        actions: [
          IconButton(
            tooltip: 'Toggle Light/Dark Theme',
            icon: Icon(isDark ? Icons.light_mode : Icons.dark_mode),
            onPressed: widget.onToggleTheme,
          ),
          PopupMenuButton<int>(
            tooltip: 'Switch Profile',
            icon: const Icon(Icons.switch_account),
            onSelected: (profileId) async {
              await widget.database.profileDao.setActiveProfile(profileId);
              await _refreshData();
            },
            itemBuilder: (context) {
              return _profiles.map((p) {
                return PopupMenuItem<int>(
                  value: p.id,
                  child: Row(
                    children: [
                      CircleAvatar(
                        radius: 12,
                        backgroundColor: Color(p.avatarColor),
                        child: Text(
                          p.name[0],
                          style: const TextStyle(color: Colors.white, fontSize: 12),
                        ),
                      ),
                      const SizedBox(width: 10),
                      Text(p.name),
                      if (p.isActive) ...[
                        const Spacer(),
                        const Icon(Icons.check, size: 16, color: AppColors.lightPrimary),
                      ],
                    ],
                  ),
                );
              }).toList();
            },
          ),
          IconButton(
            tooltip: 'Refresh',
            icon: const Icon(Icons.refresh),
            onPressed: _refreshData,
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : ListView(
              padding: const EdgeInsets.all(16),
              children: [
                _buildAdherenceCard(isDark),
                const SizedBox(height: 16),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      "Today's Medication Doses",
                      style: TextStyle(
                        fontSize: 17,
                        fontWeight: FontWeight.bold,
                        color: isDark ? AppColors.darkText : AppColors.lightText,
                      ),
                    ),
                    Text(
                      '${_doses.length} scheduled',
                      style: TextStyle(
                        fontSize: 13,
                        color: isDark ? AppColors.darkTextSecondary : AppColors.lightTextSecondary,
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 12),
                if (_doses.isEmpty)
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(24.0),
                      child: Center(
                        child: Text(
                          'No doses scheduled for today.',
                          style: TextStyle(
                            color: isDark ? AppColors.darkTextSecondary : AppColors.lightTextSecondary,
                          ),
                        ),
                      ),
                    ),
                  )
                else
                  ..._doses.map((item) => _buildDoseCard(item, isDark)),
                const SizedBox(height: 24),
                _buildDiagnosticFooter(isDark),
              ],
            ),
    );
  }

  Widget _buildAdherenceCard(bool isDark) {
    final rate = _adherence?.adherenceRate ?? 100.0;
    final taken = _adherence?.takenCount ?? 0;
    final missed = _adherence?.missedCount ?? 0;
    final skipped = _adherence?.skippedCount ?? 0;

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                const Text(
                  'Adherence Score',
                  style: TextStyle(fontWeight: FontWeight.w600, fontSize: 15),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: (rate >= 80 ? (isDark ? AppColors.darkSuccess : AppColors.lightSuccess) : AppColors.lightWarning).withOpacity(0.15),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Text(
                    '${rate.toStringAsFixed(0)}%',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      color: rate >= 80 ? (isDark ? AppColors.darkSuccess : AppColors.lightSuccess) : AppColors.lightWarning,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                _buildStatPill('Taken', taken.toString(), isDark ? AppColors.darkSuccess : AppColors.lightSuccess),
                _buildStatPill('Missed', missed.toString(), isDark ? AppColors.darkDanger : AppColors.lightDanger),
                _buildStatPill('Skipped', skipped.toString(), isDark ? AppColors.darkNeutral : AppColors.lightNeutral),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildStatPill(String label, String count, Color color) {
    return Column(
      children: [
        Text(count, style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18, color: color)),
        const SizedBox(height: 2),
        Text(label, style: const TextStyle(fontSize: 12, color: Colors.grey)),
      ],
    );
  }

  Widget _buildDoseCard(DoseWithMedicine item, bool isDark) {
    final dose = item.dose;
    final med = item.medicine;
    final isTaken = dose.status == 'taken';
    final isSkipped = dose.status == 'skipped';
    final isMissed = dose.status == 'missed';

    Color statusColor = AppColors.lightPrimary;
    if (isTaken) statusColor = isDark ? AppColors.darkSuccess : AppColors.lightSuccess;
    if (isMissed) statusColor = isDark ? AppColors.darkDanger : AppColors.lightDanger;
    if (isSkipped) statusColor = isDark ? AppColors.darkNeutral : AppColors.lightNeutral;

    final timeStr = '${dose.scheduledAt.hour.toString().padLeft(2, '0')}:${dose.scheduledAt.minute.toString().padLeft(2, '0')}';

    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                CircleAvatar(
                  radius: 18,
                  backgroundColor: statusColor.withOpacity(0.15),
                  child: Icon(Icons.medication, color: statusColor, size: 20),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        med.name,
                        style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                      ),
                      Text(
                        '${med.dosageAmount.toInt()} ${med.dosageUnit} • ${med.instructions ?? ''}',
                        style: TextStyle(
                          fontSize: 13,
                          color: isDark ? AppColors.darkTextSecondary : AppColors.lightTextSecondary,
                        ),
                      ),
                    ],
                  ),
                ),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Text(
                      timeStr,
                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                    ),
                    Text(
                      'Stock: ${med.stockCount}',
                      style: TextStyle(
                        fontSize: 11,
                        color: med.stockCount <= 7 ? AppColors.lightWarning : Colors.grey,
                      ),
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                  decoration: BoxDecoration(
                    color: statusColor.withOpacity(0.12),
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: Text(
                    dose.status.toUpperCase(),
                    style: TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: statusColor),
                  ),
                ),
                if (dose.snoozeCount > 0) ...[
                  const SizedBox(width: 8),
                  Text(
                    'Snoozed ${dose.snoozeCount}x',
                    style: const TextStyle(fontSize: 11, color: AppColors.lightWarning),
                  ),
                ],
                const Spacer(),
                if (!isTaken && !isSkipped) ...[
                  TextButton.icon(
                    style: TextButton.styleFrom(visualDensity: VisualDensity.compact),
                    icon: const Icon(Icons.snooze, size: 16),
                    label: const Text('Snooze'),
                    onPressed: () async {
                      await widget.doseEngine.snoozeDose(dose.id, snoozeMinutes: 15);
                      await _refreshData();
                    },
                  ),
                  TextButton.icon(
                    style: TextButton.styleFrom(visualDensity: VisualDensity.compact),
                    icon: const Icon(Icons.close, size: 16),
                    label: const Text('Skip'),
                    onPressed: () async {
                      await widget.doseEngine.skipDose(dose.id, reason: 'Patient skipped');
                      await _refreshData();
                    },
                  ),
                  ElevatedButton.icon(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: isDark ? AppColors.darkPrimary : AppColors.lightPrimary,
                      foregroundColor: Colors.white,
                      visualDensity: VisualDensity.compact,
                    ),
                    icon: const Icon(Icons.check, size: 16),
                    label: const Text('Take'),
                    onPressed: () async {
                      await widget.doseEngine.takeDose(dose.id);
                      await _refreshData();
                    },
                  ),
                ] else if (isTaken) ...[
                  Text(
                    'Taken at ${dose.takenAt?.hour.toString().padLeft(2, '0')}:${dose.takenAt?.minute.toString().padLeft(2, '0')}',
                    style: TextStyle(
                      fontSize: 12,
                      color: isDark ? AppColors.darkSuccess : AppColors.lightSuccess,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ] else if (isSkipped) ...[
                  Text(
                    'Skipped: ${dose.skipReason ?? 'Confirmed'}',
                    style: const TextStyle(fontSize: 12, color: Colors.grey),
                  ),
                ],
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildDiagnosticFooter(bool isDark) {
    return Center(
      child: Text(
        'PillRem v1.1 • Offline-first • SQLite Drift • Multi-Profile',
        style: TextStyle(
          fontSize: 11,
          color: isDark ? AppColors.darkTextSecondary : AppColors.lightTextSecondary,
        ),
      ),
    );
  }
}
