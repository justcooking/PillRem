// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'schedule_dao.dart';

// ignore_for_file: type=lint
mixin _$ScheduleDaoMixin on DatabaseAccessor<AppDatabase> {
  $ProfilesTable get profiles => attachedDatabase.profiles;
  $MedicinesTable get medicines => attachedDatabase.medicines;
  $SchedulesTable get schedules => attachedDatabase.schedules;
  ScheduleDaoManager get managers => ScheduleDaoManager(this);
}

class ScheduleDaoManager {
  final _$ScheduleDaoMixin _db;
  ScheduleDaoManager(this._db);
  $$ProfilesTableTableManager get profiles =>
      $$ProfilesTableTableManager(_db.attachedDatabase, _db.profiles);
  $$MedicinesTableTableManager get medicines =>
      $$MedicinesTableTableManager(_db.attachedDatabase, _db.medicines);
  $$SchedulesTableTableManager get schedules =>
      $$SchedulesTableTableManager(_db.attachedDatabase, _db.schedules);
}
