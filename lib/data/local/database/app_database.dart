import 'package:drift/drift.dart';
import 'package:drift_flutter/drift_flutter.dart';

import 'tables/profiles_table.dart';
import 'tables/profile_settings_table.dart';
import 'tables/app_settings_table.dart';
import 'tables/medicines_table.dart';
import 'tables/schedules_table.dart';
import 'tables/dose_records_table.dart';
import '../daos/profile_dao.dart';
import '../daos/medicine_dao.dart';
import '../daos/schedule_dao.dart';
import '../daos/dose_record_dao.dart';

part 'app_database.g.dart';

@DriftDatabase(
  tables: [
    Profiles,
    ProfileSettings,
    AppSettings,
    Medicines,
    Schedules,
    DoseRecords,
  ],
  daos: [
    ProfileDao,
    MedicineDao,
    ScheduleDao,
    DoseRecordDao,
  ],
)
class AppDatabase extends _$AppDatabase {
  AppDatabase([QueryExecutor? e]) : super(e ?? _openConnection());

  @override
  int get schemaVersion => 1;

  @override
  MigrationStrategy get migration {
    return MigrationStrategy(
      beforeOpen: (details) async {
        // Enforce referential integrity & foreign keys in SQLite
        await customStatement('PRAGMA foreign_keys = ON;');
      },
    );
  }

  static QueryExecutor _openConnection() {
    return driftDatabase(
      name: 'pillrem_db',
      native: const DriftNativeOptions(
        shareAcrossIsolates: true,
      ),
    );
  }
}
