// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'dose_record_dao.dart';

// ignore_for_file: type=lint
mixin _$DoseRecordDaoMixin on DatabaseAccessor<AppDatabase> {
  $ProfilesTable get profiles => attachedDatabase.profiles;
  $MedicinesTable get medicines => attachedDatabase.medicines;
  $SchedulesTable get schedules => attachedDatabase.schedules;
  $DoseRecordsTable get doseRecords => attachedDatabase.doseRecords;
  DoseRecordDaoManager get managers => DoseRecordDaoManager(this);
}

class DoseRecordDaoManager {
  final _$DoseRecordDaoMixin _db;
  DoseRecordDaoManager(this._db);
  $$ProfilesTableTableManager get profiles =>
      $$ProfilesTableTableManager(_db.attachedDatabase, _db.profiles);
  $$MedicinesTableTableManager get medicines =>
      $$MedicinesTableTableManager(_db.attachedDatabase, _db.medicines);
  $$SchedulesTableTableManager get schedules =>
      $$SchedulesTableTableManager(_db.attachedDatabase, _db.schedules);
  $$DoseRecordsTableTableManager get doseRecords =>
      $$DoseRecordsTableTableManager(_db.attachedDatabase, _db.doseRecords);
}
