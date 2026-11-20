// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'profile_dao.dart';

// ignore_for_file: type=lint
mixin _$ProfileDaoMixin on DatabaseAccessor<AppDatabase> {
  $ProfilesTable get profiles => attachedDatabase.profiles;
  $ProfileSettingsTable get profileSettings => attachedDatabase.profileSettings;
  ProfileDaoManager get managers => ProfileDaoManager(this);
}

class ProfileDaoManager {
  final _$ProfileDaoMixin _db;
  ProfileDaoManager(this._db);
  $$ProfilesTableTableManager get profiles =>
      $$ProfilesTableTableManager(_db.attachedDatabase, _db.profiles);
  $$ProfileSettingsTableTableManager get profileSettings =>
      $$ProfileSettingsTableTableManager(
        _db.attachedDatabase,
        _db.profileSettings,
      );
}
