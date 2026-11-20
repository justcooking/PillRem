import 'package:drift/drift.dart';

@DataClassName('ProfileEntry')
class Profiles extends Table {
  IntColumn get id => integer().autoIncrement()();
  TextColumn get name => text().withLength(min: 1, max: 60)();
  IntColumn get avatarColor => integer().withDefault(const Constant(0xFF2B6CB0))();
  TextColumn get avatarIcon => text().withDefault(const Constant('person'))();
  DateTimeColumn get createdAt => dateTime().withDefault(currentDateAndTime)();
  BoolColumn get isActive => boolean().withDefault(const Constant(false))();
}
