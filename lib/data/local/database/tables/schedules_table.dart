import 'package:drift/drift.dart';
import 'medicines_table.dart';
import 'profiles_table.dart';

@DataClassName('ScheduleEntry')
class Schedules extends Table {
  IntColumn get id => integer().autoIncrement()();
  IntColumn get medicineId => integer().references(Medicines, #id, onDelete: KeyAction.cascade)();
  IntColumn get profileId => integer().references(Profiles, #id, onDelete: KeyAction.cascade)();
  TextColumn get scheduleType => text().withLength(min: 1, max: 30)(); // 'daily', 'specific_days', etc.
  IntColumn get timeHour => integer()(); // 0-23
  IntColumn get timeMinute => integer()(); // 0-59
  TextColumn get daysOfWeek => text().nullable()(); // '1,2,3,4,5,6,7'
  IntColumn get intervalDays => integer().nullable()(); // e.g. every 2 days
  DateTimeColumn get startDate => dateTime()();
  DateTimeColumn get endDate => dateTime().nullable()();
  BoolColumn get isActive => boolean().withDefault(const Constant(true))();
  DateTimeColumn get createdAt => dateTime().withDefault(currentDateAndTime)();
}
