import 'package:drift/drift.dart';
import 'profiles_table.dart';

@DataClassName('ProfileSettingsEntry')
class ProfileSettings extends Table {
  IntColumn get profileId => integer().references(Profiles, #id, onDelete: KeyAction.cascade)();
  IntColumn get gracePeriodMinutes => integer().withDefault(const Constant(60))();
  IntColumn get defaultSnoozeMinutes => integer().withDefault(const Constant(15))();
  TextColumn get notificationSound => text().withDefault(const Constant('clinical_chime'))();
  BoolColumn get vibrate => boolean().withDefault(const Constant(true))();

  @override
  Set<Column> get primaryKey => {profileId};
}
