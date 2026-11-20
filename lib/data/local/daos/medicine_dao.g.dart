// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'medicine_dao.dart';

// ignore_for_file: type=lint
mixin _$MedicineDaoMixin on DatabaseAccessor<AppDatabase> {
  $ProfilesTable get profiles => attachedDatabase.profiles;
  $MedicinesTable get medicines => attachedDatabase.medicines;
  MedicineDaoManager get managers => MedicineDaoManager(this);
}

class MedicineDaoManager {
  final _$MedicineDaoMixin _db;
  MedicineDaoManager(this._db);
  $$ProfilesTableTableManager get profiles =>
      $$ProfilesTableTableManager(_db.attachedDatabase, _db.profiles);
  $$MedicinesTableTableManager get medicines =>
      $$MedicinesTableTableManager(_db.attachedDatabase, _db.medicines);
}
