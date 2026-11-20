import 'package:drift/drift.dart';
import 'profiles_table.dart';

@DataClassName('MedicineEntry')
class Medicines extends Table {
  IntColumn get id => integer().autoIncrement()();
  IntColumn get profileId => integer().references(Profiles, #id, onDelete: KeyAction.cascade)();
  TextColumn get name => text().withLength(min: 1, max: 100)();
  RealColumn get dosageAmount => real().withDefault(const Constant(1.0))();
  TextColumn get dosageUnit => text().withLength(min: 1, max: 20).withDefault(const Constant('tablet'))();
  TextColumn get form => text().withLength(min: 1, max: 30).withDefault(const Constant('tablet'))();
  TextColumn get instructions => text().nullable()(); // 'Before food', etc.
  IntColumn get colorTag => integer().withDefault(const Constant(0xFF2B6CB0))();
  IntColumn get stockCount => integer().withDefault(const Constant(0))();
  IntColumn get lowStockThreshold => integer().nullable()();
  BoolColumn get isArchived => boolean().withDefault(const Constant(false))();
  DateTimeColumn get createdAt => dateTime().withDefault(currentDateAndTime)();
}
