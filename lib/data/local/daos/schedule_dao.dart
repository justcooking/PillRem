import 'package:drift/drift.dart';
import '../database/app_database.dart';
import '../database/tables/schedules_table.dart';

part 'schedule_dao.g.dart';

@DriftAccessor(tables: [Schedules])
class ScheduleDao extends DatabaseAccessor<AppDatabase> with _$ScheduleDaoMixin {
  ScheduleDao(super.db);

  Stream<List<ScheduleEntry>> watchSchedulesForProfile(int profileId) {
    return (select(schedules)
          ..where((t) => t.profileId.equals(profileId) & t.isActive.equals(true)))
        .watch();
  }

  Future<List<ScheduleEntry>> getActiveSchedulesForProfile(int profileId) {
    return (select(schedules)
          ..where((t) => t.profileId.equals(profileId) & t.isActive.equals(true)))
        .get();
  }

  Future<List<ScheduleEntry>> getAllActiveSchedules() {
    return (select(schedules)..where((t) => t.isActive.equals(true))).get();
  }

  Stream<List<ScheduleEntry>> watchSchedulesForMedicine(int medicineId) {
    return (select(schedules)
          ..where((t) => t.medicineId.equals(medicineId)))
        .watch();
  }

  Future<List<ScheduleEntry>> getSchedulesForMedicine(int medicineId) {
    return (select(schedules)
          ..where((t) => t.medicineId.equals(medicineId)))
        .get();
  }

  Future<int> insertSchedule(SchedulesCompanion entry) {
    return into(schedules).insert(entry);
  }

  Future<bool> updateSchedule(SchedulesCompanion entry) {
    return update(schedules).replace(entry);
  }

  Future<void> toggleScheduleActive(int scheduleId, bool isActive) {
    return (update(schedules)..where((t) => t.id.equals(scheduleId))).write(
      SchedulesCompanion(isActive: Value(isActive)),
    );
  }

  Future<int> deleteSchedule(int scheduleId) {
    return (delete(schedules)..where((t) => t.id.equals(scheduleId))).go();
  }
}
