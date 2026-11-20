import 'package:drift/drift.dart';
import 'medicines_table.dart';
import 'profiles_table.dart';
import 'schedules_table.dart';

@DataClassName('DoseRecordEntry')
@TableIndex(name: 'idx_dose_schedule_scheduled', columns: {#scheduleId, #scheduledAt}, unique: true)
@TableIndex(name: 'idx_dose_profile_time_status', columns: {#profileId, #scheduledAt, #status})
class DoseRecords extends Table {
  IntColumn get id => integer().autoIncrement()();
  IntColumn get profileId => integer().references(Profiles, #id, onDelete: KeyAction.cascade)();
  IntColumn get medicineId => integer().references(Medicines, #id, onDelete: KeyAction.cascade)();
  IntColumn get scheduleId => integer().nullable().references(Schedules, #id, onDelete: KeyAction.setNull)();
  
  DateTimeColumn get scheduledAt => dateTime()();
  DateTimeColumn get reminderAt => dateTime()();
  TextColumn get status => text().withLength(min: 1, max: 20).withDefault(const Constant('pending'))();
  
  DateTimeColumn get takenAt => dateTime().nullable()();
  DateTimeColumn get skippedAt => dateTime().nullable()();
  TextColumn get skipReason => text().nullable()();
  
  IntColumn get snoozeCount => integer().withDefault(const Constant(0))();
  DateTimeColumn get gracePeriodDeadline => dateTime()();
  IntColumn get notificationId => integer()();
  
  DateTimeColumn get createdAt => dateTime().withDefault(currentDateAndTime)();
  DateTimeColumn get updatedAt => dateTime().withDefault(currentDateAndTime)();
}
