import 'package:drift/drift.dart';

@DataClassName('AppSettingsEntry')
class AppSettings extends Table {
  TextColumn get key => text()();
  TextColumn get value => text()();

  @override
  Set<Column> get primaryKey => {key};
}
