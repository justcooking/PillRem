// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'app_database.dart';

// ignore_for_file: type=lint
class $ProfilesTable extends Profiles
    with TableInfo<$ProfilesTable, ProfileEntry> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $ProfilesTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _idMeta = const VerificationMeta('id');
  @override
  late final GeneratedColumn<int> id = GeneratedColumn<int>(
    'id',
    aliasedName,
    false,
    hasAutoIncrement: true,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'PRIMARY KEY AUTOINCREMENT',
    ),
  );
  static const VerificationMeta _nameMeta = const VerificationMeta('name');
  @override
  late final GeneratedColumn<String> name = GeneratedColumn<String>(
    'name',
    aliasedName,
    false,
    additionalChecks: GeneratedColumn.checkTextLength(
      minTextLength: 1,
      maxTextLength: 60,
    ),
    type: DriftSqlType.string,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _avatarColorMeta = const VerificationMeta(
    'avatarColor',
  );
  @override
  late final GeneratedColumn<int> avatarColor = GeneratedColumn<int>(
    'avatar_color',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultValue: const Constant(0xFF2B6CB0),
  );
  static const VerificationMeta _avatarIconMeta = const VerificationMeta(
    'avatarIcon',
  );
  @override
  late final GeneratedColumn<String> avatarIcon = GeneratedColumn<String>(
    'avatar_icon',
    aliasedName,
    false,
    type: DriftSqlType.string,
    requiredDuringInsert: false,
    defaultValue: const Constant('person'),
  );
  static const VerificationMeta _createdAtMeta = const VerificationMeta(
    'createdAt',
  );
  @override
  late final GeneratedColumn<DateTime> createdAt = GeneratedColumn<DateTime>(
    'created_at',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
    defaultValue: currentDateAndTime,
  );
  static const VerificationMeta _isActiveMeta = const VerificationMeta(
    'isActive',
  );
  @override
  late final GeneratedColumn<bool> isActive = GeneratedColumn<bool>(
    'is_active',
    aliasedName,
    false,
    type: DriftSqlType.bool,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'CHECK ("is_active" IN (0, 1))',
    ),
    defaultValue: const Constant(false),
  );
  @override
  List<GeneratedColumn> get $columns => [
    id,
    name,
    avatarColor,
    avatarIcon,
    createdAt,
    isActive,
  ];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'profiles';
  @override
  VerificationContext validateIntegrity(
    Insertable<ProfileEntry> instance, {
    bool isInserting = false,
  }) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('id')) {
      context.handle(_idMeta, id.isAcceptableOrUnknown(data['id']!, _idMeta));
    }
    if (data.containsKey('name')) {
      context.handle(
        _nameMeta,
        name.isAcceptableOrUnknown(data['name']!, _nameMeta),
      );
    } else if (isInserting) {
      context.missing(_nameMeta);
    }
    if (data.containsKey('avatar_color')) {
      context.handle(
        _avatarColorMeta,
        avatarColor.isAcceptableOrUnknown(
          data['avatar_color']!,
          _avatarColorMeta,
        ),
      );
    }
    if (data.containsKey('avatar_icon')) {
      context.handle(
        _avatarIconMeta,
        avatarIcon.isAcceptableOrUnknown(data['avatar_icon']!, _avatarIconMeta),
      );
    }
    if (data.containsKey('created_at')) {
      context.handle(
        _createdAtMeta,
        createdAt.isAcceptableOrUnknown(data['created_at']!, _createdAtMeta),
      );
    }
    if (data.containsKey('is_active')) {
      context.handle(
        _isActiveMeta,
        isActive.isAcceptableOrUnknown(data['is_active']!, _isActiveMeta),
      );
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {id};
  @override
  ProfileEntry map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return ProfileEntry(
      id: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}id'],
      )!,
      name: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}name'],
      )!,
      avatarColor: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}avatar_color'],
      )!,
      avatarIcon: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}avatar_icon'],
      )!,
      createdAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}created_at'],
      )!,
      isActive: attachedDatabase.typeMapping.read(
        DriftSqlType.bool,
        data['${effectivePrefix}is_active'],
      )!,
    );
  }

  @override
  $ProfilesTable createAlias(String alias) {
    return $ProfilesTable(attachedDatabase, alias);
  }
}

class ProfileEntry extends DataClass implements Insertable<ProfileEntry> {
  final int id;
  final String name;
  final int avatarColor;
  final String avatarIcon;
  final DateTime createdAt;
  final bool isActive;
  const ProfileEntry({
    required this.id,
    required this.name,
    required this.avatarColor,
    required this.avatarIcon,
    required this.createdAt,
    required this.isActive,
  });
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['id'] = Variable<int>(id);
    map['name'] = Variable<String>(name);
    map['avatar_color'] = Variable<int>(avatarColor);
    map['avatar_icon'] = Variable<String>(avatarIcon);
    map['created_at'] = Variable<DateTime>(createdAt);
    map['is_active'] = Variable<bool>(isActive);
    return map;
  }

  ProfilesCompanion toCompanion(bool nullToAbsent) {
    return ProfilesCompanion(
      id: Value(id),
      name: Value(name),
      avatarColor: Value(avatarColor),
      avatarIcon: Value(avatarIcon),
      createdAt: Value(createdAt),
      isActive: Value(isActive),
    );
  }

  factory ProfileEntry.fromJson(
    Map<String, dynamic> json, {
    ValueSerializer? serializer,
  }) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return ProfileEntry(
      id: serializer.fromJson<int>(json['id']),
      name: serializer.fromJson<String>(json['name']),
      avatarColor: serializer.fromJson<int>(json['avatarColor']),
      avatarIcon: serializer.fromJson<String>(json['avatarIcon']),
      createdAt: serializer.fromJson<DateTime>(json['createdAt']),
      isActive: serializer.fromJson<bool>(json['isActive']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'id': serializer.toJson<int>(id),
      'name': serializer.toJson<String>(name),
      'avatarColor': serializer.toJson<int>(avatarColor),
      'avatarIcon': serializer.toJson<String>(avatarIcon),
      'createdAt': serializer.toJson<DateTime>(createdAt),
      'isActive': serializer.toJson<bool>(isActive),
    };
  }

  ProfileEntry copyWith({
    int? id,
    String? name,
    int? avatarColor,
    String? avatarIcon,
    DateTime? createdAt,
    bool? isActive,
  }) => ProfileEntry(
    id: id ?? this.id,
    name: name ?? this.name,
    avatarColor: avatarColor ?? this.avatarColor,
    avatarIcon: avatarIcon ?? this.avatarIcon,
    createdAt: createdAt ?? this.createdAt,
    isActive: isActive ?? this.isActive,
  );
  ProfileEntry copyWithCompanion(ProfilesCompanion data) {
    return ProfileEntry(
      id: data.id.present ? data.id.value : this.id,
      name: data.name.present ? data.name.value : this.name,
      avatarColor: data.avatarColor.present
          ? data.avatarColor.value
          : this.avatarColor,
      avatarIcon: data.avatarIcon.present
          ? data.avatarIcon.value
          : this.avatarIcon,
      createdAt: data.createdAt.present ? data.createdAt.value : this.createdAt,
      isActive: data.isActive.present ? data.isActive.value : this.isActive,
    );
  }

  @override
  String toString() {
    return (StringBuffer('ProfileEntry(')
          ..write('id: $id, ')
          ..write('name: $name, ')
          ..write('avatarColor: $avatarColor, ')
          ..write('avatarIcon: $avatarIcon, ')
          ..write('createdAt: $createdAt, ')
          ..write('isActive: $isActive')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode =>
      Object.hash(id, name, avatarColor, avatarIcon, createdAt, isActive);
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is ProfileEntry &&
          other.id == this.id &&
          other.name == this.name &&
          other.avatarColor == this.avatarColor &&
          other.avatarIcon == this.avatarIcon &&
          other.createdAt == this.createdAt &&
          other.isActive == this.isActive);
}

class ProfilesCompanion extends UpdateCompanion<ProfileEntry> {
  final Value<int> id;
  final Value<String> name;
  final Value<int> avatarColor;
  final Value<String> avatarIcon;
  final Value<DateTime> createdAt;
  final Value<bool> isActive;
  const ProfilesCompanion({
    this.id = const Value.absent(),
    this.name = const Value.absent(),
    this.avatarColor = const Value.absent(),
    this.avatarIcon = const Value.absent(),
    this.createdAt = const Value.absent(),
    this.isActive = const Value.absent(),
  });
  ProfilesCompanion.insert({
    this.id = const Value.absent(),
    required String name,
    this.avatarColor = const Value.absent(),
    this.avatarIcon = const Value.absent(),
    this.createdAt = const Value.absent(),
    this.isActive = const Value.absent(),
  }) : name = Value(name);
  static Insertable<ProfileEntry> custom({
    Expression<int>? id,
    Expression<String>? name,
    Expression<int>? avatarColor,
    Expression<String>? avatarIcon,
    Expression<DateTime>? createdAt,
    Expression<bool>? isActive,
  }) {
    return RawValuesInsertable({
      if (id != null) 'id': id,
      if (name != null) 'name': name,
      if (avatarColor != null) 'avatar_color': avatarColor,
      if (avatarIcon != null) 'avatar_icon': avatarIcon,
      if (createdAt != null) 'created_at': createdAt,
      if (isActive != null) 'is_active': isActive,
    });
  }

  ProfilesCompanion copyWith({
    Value<int>? id,
    Value<String>? name,
    Value<int>? avatarColor,
    Value<String>? avatarIcon,
    Value<DateTime>? createdAt,
    Value<bool>? isActive,
  }) {
    return ProfilesCompanion(
      id: id ?? this.id,
      name: name ?? this.name,
      avatarColor: avatarColor ?? this.avatarColor,
      avatarIcon: avatarIcon ?? this.avatarIcon,
      createdAt: createdAt ?? this.createdAt,
      isActive: isActive ?? this.isActive,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (id.present) {
      map['id'] = Variable<int>(id.value);
    }
    if (name.present) {
      map['name'] = Variable<String>(name.value);
    }
    if (avatarColor.present) {
      map['avatar_color'] = Variable<int>(avatarColor.value);
    }
    if (avatarIcon.present) {
      map['avatar_icon'] = Variable<String>(avatarIcon.value);
    }
    if (createdAt.present) {
      map['created_at'] = Variable<DateTime>(createdAt.value);
    }
    if (isActive.present) {
      map['is_active'] = Variable<bool>(isActive.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('ProfilesCompanion(')
          ..write('id: $id, ')
          ..write('name: $name, ')
          ..write('avatarColor: $avatarColor, ')
          ..write('avatarIcon: $avatarIcon, ')
          ..write('createdAt: $createdAt, ')
          ..write('isActive: $isActive')
          ..write(')'))
        .toString();
  }
}

class $ProfileSettingsTable extends ProfileSettings
    with TableInfo<$ProfileSettingsTable, ProfileSettingsEntry> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $ProfileSettingsTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _profileIdMeta = const VerificationMeta(
    'profileId',
  );
  @override
  late final GeneratedColumn<int> profileId = GeneratedColumn<int>(
    'profile_id',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'REFERENCES profiles (id) ON DELETE CASCADE',
    ),
  );
  static const VerificationMeta _gracePeriodMinutesMeta =
      const VerificationMeta('gracePeriodMinutes');
  @override
  late final GeneratedColumn<int> gracePeriodMinutes = GeneratedColumn<int>(
    'grace_period_minutes',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultValue: const Constant(60),
  );
  static const VerificationMeta _defaultSnoozeMinutesMeta =
      const VerificationMeta('defaultSnoozeMinutes');
  @override
  late final GeneratedColumn<int> defaultSnoozeMinutes = GeneratedColumn<int>(
    'default_snooze_minutes',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultValue: const Constant(15),
  );
  static const VerificationMeta _notificationSoundMeta = const VerificationMeta(
    'notificationSound',
  );
  @override
  late final GeneratedColumn<String> notificationSound =
      GeneratedColumn<String>(
        'notification_sound',
        aliasedName,
        false,
        type: DriftSqlType.string,
        requiredDuringInsert: false,
        defaultValue: const Constant('clinical_chime'),
      );
  static const VerificationMeta _vibrateMeta = const VerificationMeta(
    'vibrate',
  );
  @override
  late final GeneratedColumn<bool> vibrate = GeneratedColumn<bool>(
    'vibrate',
    aliasedName,
    false,
    type: DriftSqlType.bool,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'CHECK ("vibrate" IN (0, 1))',
    ),
    defaultValue: const Constant(true),
  );
  @override
  List<GeneratedColumn> get $columns => [
    profileId,
    gracePeriodMinutes,
    defaultSnoozeMinutes,
    notificationSound,
    vibrate,
  ];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'profile_settings';
  @override
  VerificationContext validateIntegrity(
    Insertable<ProfileSettingsEntry> instance, {
    bool isInserting = false,
  }) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('profile_id')) {
      context.handle(
        _profileIdMeta,
        profileId.isAcceptableOrUnknown(data['profile_id']!, _profileIdMeta),
      );
    }
    if (data.containsKey('grace_period_minutes')) {
      context.handle(
        _gracePeriodMinutesMeta,
        gracePeriodMinutes.isAcceptableOrUnknown(
          data['grace_period_minutes']!,
          _gracePeriodMinutesMeta,
        ),
      );
    }
    if (data.containsKey('default_snooze_minutes')) {
      context.handle(
        _defaultSnoozeMinutesMeta,
        defaultSnoozeMinutes.isAcceptableOrUnknown(
          data['default_snooze_minutes']!,
          _defaultSnoozeMinutesMeta,
        ),
      );
    }
    if (data.containsKey('notification_sound')) {
      context.handle(
        _notificationSoundMeta,
        notificationSound.isAcceptableOrUnknown(
          data['notification_sound']!,
          _notificationSoundMeta,
        ),
      );
    }
    if (data.containsKey('vibrate')) {
      context.handle(
        _vibrateMeta,
        vibrate.isAcceptableOrUnknown(data['vibrate']!, _vibrateMeta),
      );
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {profileId};
  @override
  ProfileSettingsEntry map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return ProfileSettingsEntry(
      profileId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}profile_id'],
      )!,
      gracePeriodMinutes: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}grace_period_minutes'],
      )!,
      defaultSnoozeMinutes: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}default_snooze_minutes'],
      )!,
      notificationSound: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}notification_sound'],
      )!,
      vibrate: attachedDatabase.typeMapping.read(
        DriftSqlType.bool,
        data['${effectivePrefix}vibrate'],
      )!,
    );
  }

  @override
  $ProfileSettingsTable createAlias(String alias) {
    return $ProfileSettingsTable(attachedDatabase, alias);
  }
}

class ProfileSettingsEntry extends DataClass
    implements Insertable<ProfileSettingsEntry> {
  final int profileId;
  final int gracePeriodMinutes;
  final int defaultSnoozeMinutes;
  final String notificationSound;
  final bool vibrate;
  const ProfileSettingsEntry({
    required this.profileId,
    required this.gracePeriodMinutes,
    required this.defaultSnoozeMinutes,
    required this.notificationSound,
    required this.vibrate,
  });
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['profile_id'] = Variable<int>(profileId);
    map['grace_period_minutes'] = Variable<int>(gracePeriodMinutes);
    map['default_snooze_minutes'] = Variable<int>(defaultSnoozeMinutes);
    map['notification_sound'] = Variable<String>(notificationSound);
    map['vibrate'] = Variable<bool>(vibrate);
    return map;
  }

  ProfileSettingsCompanion toCompanion(bool nullToAbsent) {
    return ProfileSettingsCompanion(
      profileId: Value(profileId),
      gracePeriodMinutes: Value(gracePeriodMinutes),
      defaultSnoozeMinutes: Value(defaultSnoozeMinutes),
      notificationSound: Value(notificationSound),
      vibrate: Value(vibrate),
    );
  }

  factory ProfileSettingsEntry.fromJson(
    Map<String, dynamic> json, {
    ValueSerializer? serializer,
  }) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return ProfileSettingsEntry(
      profileId: serializer.fromJson<int>(json['profileId']),
      gracePeriodMinutes: serializer.fromJson<int>(json['gracePeriodMinutes']),
      defaultSnoozeMinutes: serializer.fromJson<int>(
        json['defaultSnoozeMinutes'],
      ),
      notificationSound: serializer.fromJson<String>(json['notificationSound']),
      vibrate: serializer.fromJson<bool>(json['vibrate']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'profileId': serializer.toJson<int>(profileId),
      'gracePeriodMinutes': serializer.toJson<int>(gracePeriodMinutes),
      'defaultSnoozeMinutes': serializer.toJson<int>(defaultSnoozeMinutes),
      'notificationSound': serializer.toJson<String>(notificationSound),
      'vibrate': serializer.toJson<bool>(vibrate),
    };
  }

  ProfileSettingsEntry copyWith({
    int? profileId,
    int? gracePeriodMinutes,
    int? defaultSnoozeMinutes,
    String? notificationSound,
    bool? vibrate,
  }) => ProfileSettingsEntry(
    profileId: profileId ?? this.profileId,
    gracePeriodMinutes: gracePeriodMinutes ?? this.gracePeriodMinutes,
    defaultSnoozeMinutes: defaultSnoozeMinutes ?? this.defaultSnoozeMinutes,
    notificationSound: notificationSound ?? this.notificationSound,
    vibrate: vibrate ?? this.vibrate,
  );
  ProfileSettingsEntry copyWithCompanion(ProfileSettingsCompanion data) {
    return ProfileSettingsEntry(
      profileId: data.profileId.present ? data.profileId.value : this.profileId,
      gracePeriodMinutes: data.gracePeriodMinutes.present
          ? data.gracePeriodMinutes.value
          : this.gracePeriodMinutes,
      defaultSnoozeMinutes: data.defaultSnoozeMinutes.present
          ? data.defaultSnoozeMinutes.value
          : this.defaultSnoozeMinutes,
      notificationSound: data.notificationSound.present
          ? data.notificationSound.value
          : this.notificationSound,
      vibrate: data.vibrate.present ? data.vibrate.value : this.vibrate,
    );
  }

  @override
  String toString() {
    return (StringBuffer('ProfileSettingsEntry(')
          ..write('profileId: $profileId, ')
          ..write('gracePeriodMinutes: $gracePeriodMinutes, ')
          ..write('defaultSnoozeMinutes: $defaultSnoozeMinutes, ')
          ..write('notificationSound: $notificationSound, ')
          ..write('vibrate: $vibrate')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode => Object.hash(
    profileId,
    gracePeriodMinutes,
    defaultSnoozeMinutes,
    notificationSound,
    vibrate,
  );
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is ProfileSettingsEntry &&
          other.profileId == this.profileId &&
          other.gracePeriodMinutes == this.gracePeriodMinutes &&
          other.defaultSnoozeMinutes == this.defaultSnoozeMinutes &&
          other.notificationSound == this.notificationSound &&
          other.vibrate == this.vibrate);
}

class ProfileSettingsCompanion extends UpdateCompanion<ProfileSettingsEntry> {
  final Value<int> profileId;
  final Value<int> gracePeriodMinutes;
  final Value<int> defaultSnoozeMinutes;
  final Value<String> notificationSound;
  final Value<bool> vibrate;
  const ProfileSettingsCompanion({
    this.profileId = const Value.absent(),
    this.gracePeriodMinutes = const Value.absent(),
    this.defaultSnoozeMinutes = const Value.absent(),
    this.notificationSound = const Value.absent(),
    this.vibrate = const Value.absent(),
  });
  ProfileSettingsCompanion.insert({
    this.profileId = const Value.absent(),
    this.gracePeriodMinutes = const Value.absent(),
    this.defaultSnoozeMinutes = const Value.absent(),
    this.notificationSound = const Value.absent(),
    this.vibrate = const Value.absent(),
  });
  static Insertable<ProfileSettingsEntry> custom({
    Expression<int>? profileId,
    Expression<int>? gracePeriodMinutes,
    Expression<int>? defaultSnoozeMinutes,
    Expression<String>? notificationSound,
    Expression<bool>? vibrate,
  }) {
    return RawValuesInsertable({
      if (profileId != null) 'profile_id': profileId,
      if (gracePeriodMinutes != null)
        'grace_period_minutes': gracePeriodMinutes,
      if (defaultSnoozeMinutes != null)
        'default_snooze_minutes': defaultSnoozeMinutes,
      if (notificationSound != null) 'notification_sound': notificationSound,
      if (vibrate != null) 'vibrate': vibrate,
    });
  }

  ProfileSettingsCompanion copyWith({
    Value<int>? profileId,
    Value<int>? gracePeriodMinutes,
    Value<int>? defaultSnoozeMinutes,
    Value<String>? notificationSound,
    Value<bool>? vibrate,
  }) {
    return ProfileSettingsCompanion(
      profileId: profileId ?? this.profileId,
      gracePeriodMinutes: gracePeriodMinutes ?? this.gracePeriodMinutes,
      defaultSnoozeMinutes: defaultSnoozeMinutes ?? this.defaultSnoozeMinutes,
      notificationSound: notificationSound ?? this.notificationSound,
      vibrate: vibrate ?? this.vibrate,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (profileId.present) {
      map['profile_id'] = Variable<int>(profileId.value);
    }
    if (gracePeriodMinutes.present) {
      map['grace_period_minutes'] = Variable<int>(gracePeriodMinutes.value);
    }
    if (defaultSnoozeMinutes.present) {
      map['default_snooze_minutes'] = Variable<int>(defaultSnoozeMinutes.value);
    }
    if (notificationSound.present) {
      map['notification_sound'] = Variable<String>(notificationSound.value);
    }
    if (vibrate.present) {
      map['vibrate'] = Variable<bool>(vibrate.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('ProfileSettingsCompanion(')
          ..write('profileId: $profileId, ')
          ..write('gracePeriodMinutes: $gracePeriodMinutes, ')
          ..write('defaultSnoozeMinutes: $defaultSnoozeMinutes, ')
          ..write('notificationSound: $notificationSound, ')
          ..write('vibrate: $vibrate')
          ..write(')'))
        .toString();
  }
}

class $AppSettingsTable extends AppSettings
    with TableInfo<$AppSettingsTable, AppSettingsEntry> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $AppSettingsTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _keyMeta = const VerificationMeta('key');
  @override
  late final GeneratedColumn<String> key = GeneratedColumn<String>(
    'key',
    aliasedName,
    false,
    type: DriftSqlType.string,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _valueMeta = const VerificationMeta('value');
  @override
  late final GeneratedColumn<String> value = GeneratedColumn<String>(
    'value',
    aliasedName,
    false,
    type: DriftSqlType.string,
    requiredDuringInsert: true,
  );
  @override
  List<GeneratedColumn> get $columns => [key, value];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'app_settings';
  @override
  VerificationContext validateIntegrity(
    Insertable<AppSettingsEntry> instance, {
    bool isInserting = false,
  }) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('key')) {
      context.handle(
        _keyMeta,
        key.isAcceptableOrUnknown(data['key']!, _keyMeta),
      );
    } else if (isInserting) {
      context.missing(_keyMeta);
    }
    if (data.containsKey('value')) {
      context.handle(
        _valueMeta,
        value.isAcceptableOrUnknown(data['value']!, _valueMeta),
      );
    } else if (isInserting) {
      context.missing(_valueMeta);
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {key};
  @override
  AppSettingsEntry map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return AppSettingsEntry(
      key: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}key'],
      )!,
      value: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}value'],
      )!,
    );
  }

  @override
  $AppSettingsTable createAlias(String alias) {
    return $AppSettingsTable(attachedDatabase, alias);
  }
}

class AppSettingsEntry extends DataClass
    implements Insertable<AppSettingsEntry> {
  final String key;
  final String value;
  const AppSettingsEntry({required this.key, required this.value});
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['key'] = Variable<String>(key);
    map['value'] = Variable<String>(value);
    return map;
  }

  AppSettingsCompanion toCompanion(bool nullToAbsent) {
    return AppSettingsCompanion(key: Value(key), value: Value(value));
  }

  factory AppSettingsEntry.fromJson(
    Map<String, dynamic> json, {
    ValueSerializer? serializer,
  }) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return AppSettingsEntry(
      key: serializer.fromJson<String>(json['key']),
      value: serializer.fromJson<String>(json['value']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'key': serializer.toJson<String>(key),
      'value': serializer.toJson<String>(value),
    };
  }

  AppSettingsEntry copyWith({String? key, String? value}) =>
      AppSettingsEntry(key: key ?? this.key, value: value ?? this.value);
  AppSettingsEntry copyWithCompanion(AppSettingsCompanion data) {
    return AppSettingsEntry(
      key: data.key.present ? data.key.value : this.key,
      value: data.value.present ? data.value.value : this.value,
    );
  }

  @override
  String toString() {
    return (StringBuffer('AppSettingsEntry(')
          ..write('key: $key, ')
          ..write('value: $value')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode => Object.hash(key, value);
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is AppSettingsEntry &&
          other.key == this.key &&
          other.value == this.value);
}

class AppSettingsCompanion extends UpdateCompanion<AppSettingsEntry> {
  final Value<String> key;
  final Value<String> value;
  final Value<int> rowid;
  const AppSettingsCompanion({
    this.key = const Value.absent(),
    this.value = const Value.absent(),
    this.rowid = const Value.absent(),
  });
  AppSettingsCompanion.insert({
    required String key,
    required String value,
    this.rowid = const Value.absent(),
  }) : key = Value(key),
       value = Value(value);
  static Insertable<AppSettingsEntry> custom({
    Expression<String>? key,
    Expression<String>? value,
    Expression<int>? rowid,
  }) {
    return RawValuesInsertable({
      if (key != null) 'key': key,
      if (value != null) 'value': value,
      if (rowid != null) 'rowid': rowid,
    });
  }

  AppSettingsCompanion copyWith({
    Value<String>? key,
    Value<String>? value,
    Value<int>? rowid,
  }) {
    return AppSettingsCompanion(
      key: key ?? this.key,
      value: value ?? this.value,
      rowid: rowid ?? this.rowid,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (key.present) {
      map['key'] = Variable<String>(key.value);
    }
    if (value.present) {
      map['value'] = Variable<String>(value.value);
    }
    if (rowid.present) {
      map['rowid'] = Variable<int>(rowid.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('AppSettingsCompanion(')
          ..write('key: $key, ')
          ..write('value: $value, ')
          ..write('rowid: $rowid')
          ..write(')'))
        .toString();
  }
}

class $MedicinesTable extends Medicines
    with TableInfo<$MedicinesTable, MedicineEntry> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $MedicinesTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _idMeta = const VerificationMeta('id');
  @override
  late final GeneratedColumn<int> id = GeneratedColumn<int>(
    'id',
    aliasedName,
    false,
    hasAutoIncrement: true,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'PRIMARY KEY AUTOINCREMENT',
    ),
  );
  static const VerificationMeta _profileIdMeta = const VerificationMeta(
    'profileId',
  );
  @override
  late final GeneratedColumn<int> profileId = GeneratedColumn<int>(
    'profile_id',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'REFERENCES profiles (id) ON DELETE CASCADE',
    ),
  );
  static const VerificationMeta _nameMeta = const VerificationMeta('name');
  @override
  late final GeneratedColumn<String> name = GeneratedColumn<String>(
    'name',
    aliasedName,
    false,
    additionalChecks: GeneratedColumn.checkTextLength(
      minTextLength: 1,
      maxTextLength: 100,
    ),
    type: DriftSqlType.string,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _dosageAmountMeta = const VerificationMeta(
    'dosageAmount',
  );
  @override
  late final GeneratedColumn<double> dosageAmount = GeneratedColumn<double>(
    'dosage_amount',
    aliasedName,
    false,
    type: DriftSqlType.double,
    requiredDuringInsert: false,
    defaultValue: const Constant(1.0),
  );
  static const VerificationMeta _dosageUnitMeta = const VerificationMeta(
    'dosageUnit',
  );
  @override
  late final GeneratedColumn<String> dosageUnit = GeneratedColumn<String>(
    'dosage_unit',
    aliasedName,
    false,
    additionalChecks: GeneratedColumn.checkTextLength(
      minTextLength: 1,
      maxTextLength: 20,
    ),
    type: DriftSqlType.string,
    requiredDuringInsert: false,
    defaultValue: const Constant('tablet'),
  );
  static const VerificationMeta _formMeta = const VerificationMeta('form');
  @override
  late final GeneratedColumn<String> form = GeneratedColumn<String>(
    'form',
    aliasedName,
    false,
    additionalChecks: GeneratedColumn.checkTextLength(
      minTextLength: 1,
      maxTextLength: 30,
    ),
    type: DriftSqlType.string,
    requiredDuringInsert: false,
    defaultValue: const Constant('tablet'),
  );
  static const VerificationMeta _instructionsMeta = const VerificationMeta(
    'instructions',
  );
  @override
  late final GeneratedColumn<String> instructions = GeneratedColumn<String>(
    'instructions',
    aliasedName,
    true,
    type: DriftSqlType.string,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _colorTagMeta = const VerificationMeta(
    'colorTag',
  );
  @override
  late final GeneratedColumn<int> colorTag = GeneratedColumn<int>(
    'color_tag',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultValue: const Constant(0xFF2B6CB0),
  );
  static const VerificationMeta _stockCountMeta = const VerificationMeta(
    'stockCount',
  );
  @override
  late final GeneratedColumn<int> stockCount = GeneratedColumn<int>(
    'stock_count',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultValue: const Constant(0),
  );
  static const VerificationMeta _lowStockThresholdMeta = const VerificationMeta(
    'lowStockThreshold',
  );
  @override
  late final GeneratedColumn<int> lowStockThreshold = GeneratedColumn<int>(
    'low_stock_threshold',
    aliasedName,
    true,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _isArchivedMeta = const VerificationMeta(
    'isArchived',
  );
  @override
  late final GeneratedColumn<bool> isArchived = GeneratedColumn<bool>(
    'is_archived',
    aliasedName,
    false,
    type: DriftSqlType.bool,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'CHECK ("is_archived" IN (0, 1))',
    ),
    defaultValue: const Constant(false),
  );
  static const VerificationMeta _createdAtMeta = const VerificationMeta(
    'createdAt',
  );
  @override
  late final GeneratedColumn<DateTime> createdAt = GeneratedColumn<DateTime>(
    'created_at',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
    defaultValue: currentDateAndTime,
  );
  @override
  List<GeneratedColumn> get $columns => [
    id,
    profileId,
    name,
    dosageAmount,
    dosageUnit,
    form,
    instructions,
    colorTag,
    stockCount,
    lowStockThreshold,
    isArchived,
    createdAt,
  ];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'medicines';
  @override
  VerificationContext validateIntegrity(
    Insertable<MedicineEntry> instance, {
    bool isInserting = false,
  }) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('id')) {
      context.handle(_idMeta, id.isAcceptableOrUnknown(data['id']!, _idMeta));
    }
    if (data.containsKey('profile_id')) {
      context.handle(
        _profileIdMeta,
        profileId.isAcceptableOrUnknown(data['profile_id']!, _profileIdMeta),
      );
    } else if (isInserting) {
      context.missing(_profileIdMeta);
    }
    if (data.containsKey('name')) {
      context.handle(
        _nameMeta,
        name.isAcceptableOrUnknown(data['name']!, _nameMeta),
      );
    } else if (isInserting) {
      context.missing(_nameMeta);
    }
    if (data.containsKey('dosage_amount')) {
      context.handle(
        _dosageAmountMeta,
        dosageAmount.isAcceptableOrUnknown(
          data['dosage_amount']!,
          _dosageAmountMeta,
        ),
      );
    }
    if (data.containsKey('dosage_unit')) {
      context.handle(
        _dosageUnitMeta,
        dosageUnit.isAcceptableOrUnknown(data['dosage_unit']!, _dosageUnitMeta),
      );
    }
    if (data.containsKey('form')) {
      context.handle(
        _formMeta,
        form.isAcceptableOrUnknown(data['form']!, _formMeta),
      );
    }
    if (data.containsKey('instructions')) {
      context.handle(
        _instructionsMeta,
        instructions.isAcceptableOrUnknown(
          data['instructions']!,
          _instructionsMeta,
        ),
      );
    }
    if (data.containsKey('color_tag')) {
      context.handle(
        _colorTagMeta,
        colorTag.isAcceptableOrUnknown(data['color_tag']!, _colorTagMeta),
      );
    }
    if (data.containsKey('stock_count')) {
      context.handle(
        _stockCountMeta,
        stockCount.isAcceptableOrUnknown(data['stock_count']!, _stockCountMeta),
      );
    }
    if (data.containsKey('low_stock_threshold')) {
      context.handle(
        _lowStockThresholdMeta,
        lowStockThreshold.isAcceptableOrUnknown(
          data['low_stock_threshold']!,
          _lowStockThresholdMeta,
        ),
      );
    }
    if (data.containsKey('is_archived')) {
      context.handle(
        _isArchivedMeta,
        isArchived.isAcceptableOrUnknown(data['is_archived']!, _isArchivedMeta),
      );
    }
    if (data.containsKey('created_at')) {
      context.handle(
        _createdAtMeta,
        createdAt.isAcceptableOrUnknown(data['created_at']!, _createdAtMeta),
      );
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {id};
  @override
  MedicineEntry map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return MedicineEntry(
      id: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}id'],
      )!,
      profileId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}profile_id'],
      )!,
      name: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}name'],
      )!,
      dosageAmount: attachedDatabase.typeMapping.read(
        DriftSqlType.double,
        data['${effectivePrefix}dosage_amount'],
      )!,
      dosageUnit: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}dosage_unit'],
      )!,
      form: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}form'],
      )!,
      instructions: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}instructions'],
      ),
      colorTag: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}color_tag'],
      )!,
      stockCount: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}stock_count'],
      )!,
      lowStockThreshold: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}low_stock_threshold'],
      ),
      isArchived: attachedDatabase.typeMapping.read(
        DriftSqlType.bool,
        data['${effectivePrefix}is_archived'],
      )!,
      createdAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}created_at'],
      )!,
    );
  }

  @override
  $MedicinesTable createAlias(String alias) {
    return $MedicinesTable(attachedDatabase, alias);
  }
}

class MedicineEntry extends DataClass implements Insertable<MedicineEntry> {
  final int id;
  final int profileId;
  final String name;
  final double dosageAmount;
  final String dosageUnit;
  final String form;
  final String? instructions;
  final int colorTag;
  final int stockCount;
  final int? lowStockThreshold;
  final bool isArchived;
  final DateTime createdAt;
  const MedicineEntry({
    required this.id,
    required this.profileId,
    required this.name,
    required this.dosageAmount,
    required this.dosageUnit,
    required this.form,
    this.instructions,
    required this.colorTag,
    required this.stockCount,
    this.lowStockThreshold,
    required this.isArchived,
    required this.createdAt,
  });
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['id'] = Variable<int>(id);
    map['profile_id'] = Variable<int>(profileId);
    map['name'] = Variable<String>(name);
    map['dosage_amount'] = Variable<double>(dosageAmount);
    map['dosage_unit'] = Variable<String>(dosageUnit);
    map['form'] = Variable<String>(form);
    if (!nullToAbsent || instructions != null) {
      map['instructions'] = Variable<String>(instructions);
    }
    map['color_tag'] = Variable<int>(colorTag);
    map['stock_count'] = Variable<int>(stockCount);
    if (!nullToAbsent || lowStockThreshold != null) {
      map['low_stock_threshold'] = Variable<int>(lowStockThreshold);
    }
    map['is_archived'] = Variable<bool>(isArchived);
    map['created_at'] = Variable<DateTime>(createdAt);
    return map;
  }

  MedicinesCompanion toCompanion(bool nullToAbsent) {
    return MedicinesCompanion(
      id: Value(id),
      profileId: Value(profileId),
      name: Value(name),
      dosageAmount: Value(dosageAmount),
      dosageUnit: Value(dosageUnit),
      form: Value(form),
      instructions: instructions == null && nullToAbsent
          ? const Value.absent()
          : Value(instructions),
      colorTag: Value(colorTag),
      stockCount: Value(stockCount),
      lowStockThreshold: lowStockThreshold == null && nullToAbsent
          ? const Value.absent()
          : Value(lowStockThreshold),
      isArchived: Value(isArchived),
      createdAt: Value(createdAt),
    );
  }

  factory MedicineEntry.fromJson(
    Map<String, dynamic> json, {
    ValueSerializer? serializer,
  }) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return MedicineEntry(
      id: serializer.fromJson<int>(json['id']),
      profileId: serializer.fromJson<int>(json['profileId']),
      name: serializer.fromJson<String>(json['name']),
      dosageAmount: serializer.fromJson<double>(json['dosageAmount']),
      dosageUnit: serializer.fromJson<String>(json['dosageUnit']),
      form: serializer.fromJson<String>(json['form']),
      instructions: serializer.fromJson<String?>(json['instructions']),
      colorTag: serializer.fromJson<int>(json['colorTag']),
      stockCount: serializer.fromJson<int>(json['stockCount']),
      lowStockThreshold: serializer.fromJson<int?>(json['lowStockThreshold']),
      isArchived: serializer.fromJson<bool>(json['isArchived']),
      createdAt: serializer.fromJson<DateTime>(json['createdAt']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'id': serializer.toJson<int>(id),
      'profileId': serializer.toJson<int>(profileId),
      'name': serializer.toJson<String>(name),
      'dosageAmount': serializer.toJson<double>(dosageAmount),
      'dosageUnit': serializer.toJson<String>(dosageUnit),
      'form': serializer.toJson<String>(form),
      'instructions': serializer.toJson<String?>(instructions),
      'colorTag': serializer.toJson<int>(colorTag),
      'stockCount': serializer.toJson<int>(stockCount),
      'lowStockThreshold': serializer.toJson<int?>(lowStockThreshold),
      'isArchived': serializer.toJson<bool>(isArchived),
      'createdAt': serializer.toJson<DateTime>(createdAt),
    };
  }

  MedicineEntry copyWith({
    int? id,
    int? profileId,
    String? name,
    double? dosageAmount,
    String? dosageUnit,
    String? form,
    Value<String?> instructions = const Value.absent(),
    int? colorTag,
    int? stockCount,
    Value<int?> lowStockThreshold = const Value.absent(),
    bool? isArchived,
    DateTime? createdAt,
  }) => MedicineEntry(
    id: id ?? this.id,
    profileId: profileId ?? this.profileId,
    name: name ?? this.name,
    dosageAmount: dosageAmount ?? this.dosageAmount,
    dosageUnit: dosageUnit ?? this.dosageUnit,
    form: form ?? this.form,
    instructions: instructions.present ? instructions.value : this.instructions,
    colorTag: colorTag ?? this.colorTag,
    stockCount: stockCount ?? this.stockCount,
    lowStockThreshold: lowStockThreshold.present
        ? lowStockThreshold.value
        : this.lowStockThreshold,
    isArchived: isArchived ?? this.isArchived,
    createdAt: createdAt ?? this.createdAt,
  );
  MedicineEntry copyWithCompanion(MedicinesCompanion data) {
    return MedicineEntry(
      id: data.id.present ? data.id.value : this.id,
      profileId: data.profileId.present ? data.profileId.value : this.profileId,
      name: data.name.present ? data.name.value : this.name,
      dosageAmount: data.dosageAmount.present
          ? data.dosageAmount.value
          : this.dosageAmount,
      dosageUnit: data.dosageUnit.present
          ? data.dosageUnit.value
          : this.dosageUnit,
      form: data.form.present ? data.form.value : this.form,
      instructions: data.instructions.present
          ? data.instructions.value
          : this.instructions,
      colorTag: data.colorTag.present ? data.colorTag.value : this.colorTag,
      stockCount: data.stockCount.present
          ? data.stockCount.value
          : this.stockCount,
      lowStockThreshold: data.lowStockThreshold.present
          ? data.lowStockThreshold.value
          : this.lowStockThreshold,
      isArchived: data.isArchived.present
          ? data.isArchived.value
          : this.isArchived,
      createdAt: data.createdAt.present ? data.createdAt.value : this.createdAt,
    );
  }

  @override
  String toString() {
    return (StringBuffer('MedicineEntry(')
          ..write('id: $id, ')
          ..write('profileId: $profileId, ')
          ..write('name: $name, ')
          ..write('dosageAmount: $dosageAmount, ')
          ..write('dosageUnit: $dosageUnit, ')
          ..write('form: $form, ')
          ..write('instructions: $instructions, ')
          ..write('colorTag: $colorTag, ')
          ..write('stockCount: $stockCount, ')
          ..write('lowStockThreshold: $lowStockThreshold, ')
          ..write('isArchived: $isArchived, ')
          ..write('createdAt: $createdAt')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode => Object.hash(
    id,
    profileId,
    name,
    dosageAmount,
    dosageUnit,
    form,
    instructions,
    colorTag,
    stockCount,
    lowStockThreshold,
    isArchived,
    createdAt,
  );
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is MedicineEntry &&
          other.id == this.id &&
          other.profileId == this.profileId &&
          other.name == this.name &&
          other.dosageAmount == this.dosageAmount &&
          other.dosageUnit == this.dosageUnit &&
          other.form == this.form &&
          other.instructions == this.instructions &&
          other.colorTag == this.colorTag &&
          other.stockCount == this.stockCount &&
          other.lowStockThreshold == this.lowStockThreshold &&
          other.isArchived == this.isArchived &&
          other.createdAt == this.createdAt);
}

class MedicinesCompanion extends UpdateCompanion<MedicineEntry> {
  final Value<int> id;
  final Value<int> profileId;
  final Value<String> name;
  final Value<double> dosageAmount;
  final Value<String> dosageUnit;
  final Value<String> form;
  final Value<String?> instructions;
  final Value<int> colorTag;
  final Value<int> stockCount;
  final Value<int?> lowStockThreshold;
  final Value<bool> isArchived;
  final Value<DateTime> createdAt;
  const MedicinesCompanion({
    this.id = const Value.absent(),
    this.profileId = const Value.absent(),
    this.name = const Value.absent(),
    this.dosageAmount = const Value.absent(),
    this.dosageUnit = const Value.absent(),
    this.form = const Value.absent(),
    this.instructions = const Value.absent(),
    this.colorTag = const Value.absent(),
    this.stockCount = const Value.absent(),
    this.lowStockThreshold = const Value.absent(),
    this.isArchived = const Value.absent(),
    this.createdAt = const Value.absent(),
  });
  MedicinesCompanion.insert({
    this.id = const Value.absent(),
    required int profileId,
    required String name,
    this.dosageAmount = const Value.absent(),
    this.dosageUnit = const Value.absent(),
    this.form = const Value.absent(),
    this.instructions = const Value.absent(),
    this.colorTag = const Value.absent(),
    this.stockCount = const Value.absent(),
    this.lowStockThreshold = const Value.absent(),
    this.isArchived = const Value.absent(),
    this.createdAt = const Value.absent(),
  }) : profileId = Value(profileId),
       name = Value(name);
  static Insertable<MedicineEntry> custom({
    Expression<int>? id,
    Expression<int>? profileId,
    Expression<String>? name,
    Expression<double>? dosageAmount,
    Expression<String>? dosageUnit,
    Expression<String>? form,
    Expression<String>? instructions,
    Expression<int>? colorTag,
    Expression<int>? stockCount,
    Expression<int>? lowStockThreshold,
    Expression<bool>? isArchived,
    Expression<DateTime>? createdAt,
  }) {
    return RawValuesInsertable({
      if (id != null) 'id': id,
      if (profileId != null) 'profile_id': profileId,
      if (name != null) 'name': name,
      if (dosageAmount != null) 'dosage_amount': dosageAmount,
      if (dosageUnit != null) 'dosage_unit': dosageUnit,
      if (form != null) 'form': form,
      if (instructions != null) 'instructions': instructions,
      if (colorTag != null) 'color_tag': colorTag,
      if (stockCount != null) 'stock_count': stockCount,
      if (lowStockThreshold != null) 'low_stock_threshold': lowStockThreshold,
      if (isArchived != null) 'is_archived': isArchived,
      if (createdAt != null) 'created_at': createdAt,
    });
  }

  MedicinesCompanion copyWith({
    Value<int>? id,
    Value<int>? profileId,
    Value<String>? name,
    Value<double>? dosageAmount,
    Value<String>? dosageUnit,
    Value<String>? form,
    Value<String?>? instructions,
    Value<int>? colorTag,
    Value<int>? stockCount,
    Value<int?>? lowStockThreshold,
    Value<bool>? isArchived,
    Value<DateTime>? createdAt,
  }) {
    return MedicinesCompanion(
      id: id ?? this.id,
      profileId: profileId ?? this.profileId,
      name: name ?? this.name,
      dosageAmount: dosageAmount ?? this.dosageAmount,
      dosageUnit: dosageUnit ?? this.dosageUnit,
      form: form ?? this.form,
      instructions: instructions ?? this.instructions,
      colorTag: colorTag ?? this.colorTag,
      stockCount: stockCount ?? this.stockCount,
      lowStockThreshold: lowStockThreshold ?? this.lowStockThreshold,
      isArchived: isArchived ?? this.isArchived,
      createdAt: createdAt ?? this.createdAt,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (id.present) {
      map['id'] = Variable<int>(id.value);
    }
    if (profileId.present) {
      map['profile_id'] = Variable<int>(profileId.value);
    }
    if (name.present) {
      map['name'] = Variable<String>(name.value);
    }
    if (dosageAmount.present) {
      map['dosage_amount'] = Variable<double>(dosageAmount.value);
    }
    if (dosageUnit.present) {
      map['dosage_unit'] = Variable<String>(dosageUnit.value);
    }
    if (form.present) {
      map['form'] = Variable<String>(form.value);
    }
    if (instructions.present) {
      map['instructions'] = Variable<String>(instructions.value);
    }
    if (colorTag.present) {
      map['color_tag'] = Variable<int>(colorTag.value);
    }
    if (stockCount.present) {
      map['stock_count'] = Variable<int>(stockCount.value);
    }
    if (lowStockThreshold.present) {
      map['low_stock_threshold'] = Variable<int>(lowStockThreshold.value);
    }
    if (isArchived.present) {
      map['is_archived'] = Variable<bool>(isArchived.value);
    }
    if (createdAt.present) {
      map['created_at'] = Variable<DateTime>(createdAt.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('MedicinesCompanion(')
          ..write('id: $id, ')
          ..write('profileId: $profileId, ')
          ..write('name: $name, ')
          ..write('dosageAmount: $dosageAmount, ')
          ..write('dosageUnit: $dosageUnit, ')
          ..write('form: $form, ')
          ..write('instructions: $instructions, ')
          ..write('colorTag: $colorTag, ')
          ..write('stockCount: $stockCount, ')
          ..write('lowStockThreshold: $lowStockThreshold, ')
          ..write('isArchived: $isArchived, ')
          ..write('createdAt: $createdAt')
          ..write(')'))
        .toString();
  }
}

class $SchedulesTable extends Schedules
    with TableInfo<$SchedulesTable, ScheduleEntry> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $SchedulesTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _idMeta = const VerificationMeta('id');
  @override
  late final GeneratedColumn<int> id = GeneratedColumn<int>(
    'id',
    aliasedName,
    false,
    hasAutoIncrement: true,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'PRIMARY KEY AUTOINCREMENT',
    ),
  );
  static const VerificationMeta _medicineIdMeta = const VerificationMeta(
    'medicineId',
  );
  @override
  late final GeneratedColumn<int> medicineId = GeneratedColumn<int>(
    'medicine_id',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'REFERENCES medicines (id) ON DELETE CASCADE',
    ),
  );
  static const VerificationMeta _profileIdMeta = const VerificationMeta(
    'profileId',
  );
  @override
  late final GeneratedColumn<int> profileId = GeneratedColumn<int>(
    'profile_id',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'REFERENCES profiles (id) ON DELETE CASCADE',
    ),
  );
  static const VerificationMeta _scheduleTypeMeta = const VerificationMeta(
    'scheduleType',
  );
  @override
  late final GeneratedColumn<String> scheduleType = GeneratedColumn<String>(
    'schedule_type',
    aliasedName,
    false,
    additionalChecks: GeneratedColumn.checkTextLength(
      minTextLength: 1,
      maxTextLength: 30,
    ),
    type: DriftSqlType.string,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _timeHourMeta = const VerificationMeta(
    'timeHour',
  );
  @override
  late final GeneratedColumn<int> timeHour = GeneratedColumn<int>(
    'time_hour',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _timeMinuteMeta = const VerificationMeta(
    'timeMinute',
  );
  @override
  late final GeneratedColumn<int> timeMinute = GeneratedColumn<int>(
    'time_minute',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _daysOfWeekMeta = const VerificationMeta(
    'daysOfWeek',
  );
  @override
  late final GeneratedColumn<String> daysOfWeek = GeneratedColumn<String>(
    'days_of_week',
    aliasedName,
    true,
    type: DriftSqlType.string,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _intervalDaysMeta = const VerificationMeta(
    'intervalDays',
  );
  @override
  late final GeneratedColumn<int> intervalDays = GeneratedColumn<int>(
    'interval_days',
    aliasedName,
    true,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _startDateMeta = const VerificationMeta(
    'startDate',
  );
  @override
  late final GeneratedColumn<DateTime> startDate = GeneratedColumn<DateTime>(
    'start_date',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _endDateMeta = const VerificationMeta(
    'endDate',
  );
  @override
  late final GeneratedColumn<DateTime> endDate = GeneratedColumn<DateTime>(
    'end_date',
    aliasedName,
    true,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _isActiveMeta = const VerificationMeta(
    'isActive',
  );
  @override
  late final GeneratedColumn<bool> isActive = GeneratedColumn<bool>(
    'is_active',
    aliasedName,
    false,
    type: DriftSqlType.bool,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'CHECK ("is_active" IN (0, 1))',
    ),
    defaultValue: const Constant(true),
  );
  static const VerificationMeta _createdAtMeta = const VerificationMeta(
    'createdAt',
  );
  @override
  late final GeneratedColumn<DateTime> createdAt = GeneratedColumn<DateTime>(
    'created_at',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
    defaultValue: currentDateAndTime,
  );
  @override
  List<GeneratedColumn> get $columns => [
    id,
    medicineId,
    profileId,
    scheduleType,
    timeHour,
    timeMinute,
    daysOfWeek,
    intervalDays,
    startDate,
    endDate,
    isActive,
    createdAt,
  ];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'schedules';
  @override
  VerificationContext validateIntegrity(
    Insertable<ScheduleEntry> instance, {
    bool isInserting = false,
  }) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('id')) {
      context.handle(_idMeta, id.isAcceptableOrUnknown(data['id']!, _idMeta));
    }
    if (data.containsKey('medicine_id')) {
      context.handle(
        _medicineIdMeta,
        medicineId.isAcceptableOrUnknown(data['medicine_id']!, _medicineIdMeta),
      );
    } else if (isInserting) {
      context.missing(_medicineIdMeta);
    }
    if (data.containsKey('profile_id')) {
      context.handle(
        _profileIdMeta,
        profileId.isAcceptableOrUnknown(data['profile_id']!, _profileIdMeta),
      );
    } else if (isInserting) {
      context.missing(_profileIdMeta);
    }
    if (data.containsKey('schedule_type')) {
      context.handle(
        _scheduleTypeMeta,
        scheduleType.isAcceptableOrUnknown(
          data['schedule_type']!,
          _scheduleTypeMeta,
        ),
      );
    } else if (isInserting) {
      context.missing(_scheduleTypeMeta);
    }
    if (data.containsKey('time_hour')) {
      context.handle(
        _timeHourMeta,
        timeHour.isAcceptableOrUnknown(data['time_hour']!, _timeHourMeta),
      );
    } else if (isInserting) {
      context.missing(_timeHourMeta);
    }
    if (data.containsKey('time_minute')) {
      context.handle(
        _timeMinuteMeta,
        timeMinute.isAcceptableOrUnknown(data['time_minute']!, _timeMinuteMeta),
      );
    } else if (isInserting) {
      context.missing(_timeMinuteMeta);
    }
    if (data.containsKey('days_of_week')) {
      context.handle(
        _daysOfWeekMeta,
        daysOfWeek.isAcceptableOrUnknown(
          data['days_of_week']!,
          _daysOfWeekMeta,
        ),
      );
    }
    if (data.containsKey('interval_days')) {
      context.handle(
        _intervalDaysMeta,
        intervalDays.isAcceptableOrUnknown(
          data['interval_days']!,
          _intervalDaysMeta,
        ),
      );
    }
    if (data.containsKey('start_date')) {
      context.handle(
        _startDateMeta,
        startDate.isAcceptableOrUnknown(data['start_date']!, _startDateMeta),
      );
    } else if (isInserting) {
      context.missing(_startDateMeta);
    }
    if (data.containsKey('end_date')) {
      context.handle(
        _endDateMeta,
        endDate.isAcceptableOrUnknown(data['end_date']!, _endDateMeta),
      );
    }
    if (data.containsKey('is_active')) {
      context.handle(
        _isActiveMeta,
        isActive.isAcceptableOrUnknown(data['is_active']!, _isActiveMeta),
      );
    }
    if (data.containsKey('created_at')) {
      context.handle(
        _createdAtMeta,
        createdAt.isAcceptableOrUnknown(data['created_at']!, _createdAtMeta),
      );
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {id};
  @override
  ScheduleEntry map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return ScheduleEntry(
      id: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}id'],
      )!,
      medicineId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}medicine_id'],
      )!,
      profileId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}profile_id'],
      )!,
      scheduleType: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}schedule_type'],
      )!,
      timeHour: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}time_hour'],
      )!,
      timeMinute: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}time_minute'],
      )!,
      daysOfWeek: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}days_of_week'],
      ),
      intervalDays: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}interval_days'],
      ),
      startDate: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}start_date'],
      )!,
      endDate: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}end_date'],
      ),
      isActive: attachedDatabase.typeMapping.read(
        DriftSqlType.bool,
        data['${effectivePrefix}is_active'],
      )!,
      createdAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}created_at'],
      )!,
    );
  }

  @override
  $SchedulesTable createAlias(String alias) {
    return $SchedulesTable(attachedDatabase, alias);
  }
}

class ScheduleEntry extends DataClass implements Insertable<ScheduleEntry> {
  final int id;
  final int medicineId;
  final int profileId;
  final String scheduleType;
  final int timeHour;
  final int timeMinute;
  final String? daysOfWeek;
  final int? intervalDays;
  final DateTime startDate;
  final DateTime? endDate;
  final bool isActive;
  final DateTime createdAt;
  const ScheduleEntry({
    required this.id,
    required this.medicineId,
    required this.profileId,
    required this.scheduleType,
    required this.timeHour,
    required this.timeMinute,
    this.daysOfWeek,
    this.intervalDays,
    required this.startDate,
    this.endDate,
    required this.isActive,
    required this.createdAt,
  });
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['id'] = Variable<int>(id);
    map['medicine_id'] = Variable<int>(medicineId);
    map['profile_id'] = Variable<int>(profileId);
    map['schedule_type'] = Variable<String>(scheduleType);
    map['time_hour'] = Variable<int>(timeHour);
    map['time_minute'] = Variable<int>(timeMinute);
    if (!nullToAbsent || daysOfWeek != null) {
      map['days_of_week'] = Variable<String>(daysOfWeek);
    }
    if (!nullToAbsent || intervalDays != null) {
      map['interval_days'] = Variable<int>(intervalDays);
    }
    map['start_date'] = Variable<DateTime>(startDate);
    if (!nullToAbsent || endDate != null) {
      map['end_date'] = Variable<DateTime>(endDate);
    }
    map['is_active'] = Variable<bool>(isActive);
    map['created_at'] = Variable<DateTime>(createdAt);
    return map;
  }

  SchedulesCompanion toCompanion(bool nullToAbsent) {
    return SchedulesCompanion(
      id: Value(id),
      medicineId: Value(medicineId),
      profileId: Value(profileId),
      scheduleType: Value(scheduleType),
      timeHour: Value(timeHour),
      timeMinute: Value(timeMinute),
      daysOfWeek: daysOfWeek == null && nullToAbsent
          ? const Value.absent()
          : Value(daysOfWeek),
      intervalDays: intervalDays == null && nullToAbsent
          ? const Value.absent()
          : Value(intervalDays),
      startDate: Value(startDate),
      endDate: endDate == null && nullToAbsent
          ? const Value.absent()
          : Value(endDate),
      isActive: Value(isActive),
      createdAt: Value(createdAt),
    );
  }

  factory ScheduleEntry.fromJson(
    Map<String, dynamic> json, {
    ValueSerializer? serializer,
  }) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return ScheduleEntry(
      id: serializer.fromJson<int>(json['id']),
      medicineId: serializer.fromJson<int>(json['medicineId']),
      profileId: serializer.fromJson<int>(json['profileId']),
      scheduleType: serializer.fromJson<String>(json['scheduleType']),
      timeHour: serializer.fromJson<int>(json['timeHour']),
      timeMinute: serializer.fromJson<int>(json['timeMinute']),
      daysOfWeek: serializer.fromJson<String?>(json['daysOfWeek']),
      intervalDays: serializer.fromJson<int?>(json['intervalDays']),
      startDate: serializer.fromJson<DateTime>(json['startDate']),
      endDate: serializer.fromJson<DateTime?>(json['endDate']),
      isActive: serializer.fromJson<bool>(json['isActive']),
      createdAt: serializer.fromJson<DateTime>(json['createdAt']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'id': serializer.toJson<int>(id),
      'medicineId': serializer.toJson<int>(medicineId),
      'profileId': serializer.toJson<int>(profileId),
      'scheduleType': serializer.toJson<String>(scheduleType),
      'timeHour': serializer.toJson<int>(timeHour),
      'timeMinute': serializer.toJson<int>(timeMinute),
      'daysOfWeek': serializer.toJson<String?>(daysOfWeek),
      'intervalDays': serializer.toJson<int?>(intervalDays),
      'startDate': serializer.toJson<DateTime>(startDate),
      'endDate': serializer.toJson<DateTime?>(endDate),
      'isActive': serializer.toJson<bool>(isActive),
      'createdAt': serializer.toJson<DateTime>(createdAt),
    };
  }

  ScheduleEntry copyWith({
    int? id,
    int? medicineId,
    int? profileId,
    String? scheduleType,
    int? timeHour,
    int? timeMinute,
    Value<String?> daysOfWeek = const Value.absent(),
    Value<int?> intervalDays = const Value.absent(),
    DateTime? startDate,
    Value<DateTime?> endDate = const Value.absent(),
    bool? isActive,
    DateTime? createdAt,
  }) => ScheduleEntry(
    id: id ?? this.id,
    medicineId: medicineId ?? this.medicineId,
    profileId: profileId ?? this.profileId,
    scheduleType: scheduleType ?? this.scheduleType,
    timeHour: timeHour ?? this.timeHour,
    timeMinute: timeMinute ?? this.timeMinute,
    daysOfWeek: daysOfWeek.present ? daysOfWeek.value : this.daysOfWeek,
    intervalDays: intervalDays.present ? intervalDays.value : this.intervalDays,
    startDate: startDate ?? this.startDate,
    endDate: endDate.present ? endDate.value : this.endDate,
    isActive: isActive ?? this.isActive,
    createdAt: createdAt ?? this.createdAt,
  );
  ScheduleEntry copyWithCompanion(SchedulesCompanion data) {
    return ScheduleEntry(
      id: data.id.present ? data.id.value : this.id,
      medicineId: data.medicineId.present
          ? data.medicineId.value
          : this.medicineId,
      profileId: data.profileId.present ? data.profileId.value : this.profileId,
      scheduleType: data.scheduleType.present
          ? data.scheduleType.value
          : this.scheduleType,
      timeHour: data.timeHour.present ? data.timeHour.value : this.timeHour,
      timeMinute: data.timeMinute.present
          ? data.timeMinute.value
          : this.timeMinute,
      daysOfWeek: data.daysOfWeek.present
          ? data.daysOfWeek.value
          : this.daysOfWeek,
      intervalDays: data.intervalDays.present
          ? data.intervalDays.value
          : this.intervalDays,
      startDate: data.startDate.present ? data.startDate.value : this.startDate,
      endDate: data.endDate.present ? data.endDate.value : this.endDate,
      isActive: data.isActive.present ? data.isActive.value : this.isActive,
      createdAt: data.createdAt.present ? data.createdAt.value : this.createdAt,
    );
  }

  @override
  String toString() {
    return (StringBuffer('ScheduleEntry(')
          ..write('id: $id, ')
          ..write('medicineId: $medicineId, ')
          ..write('profileId: $profileId, ')
          ..write('scheduleType: $scheduleType, ')
          ..write('timeHour: $timeHour, ')
          ..write('timeMinute: $timeMinute, ')
          ..write('daysOfWeek: $daysOfWeek, ')
          ..write('intervalDays: $intervalDays, ')
          ..write('startDate: $startDate, ')
          ..write('endDate: $endDate, ')
          ..write('isActive: $isActive, ')
          ..write('createdAt: $createdAt')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode => Object.hash(
    id,
    medicineId,
    profileId,
    scheduleType,
    timeHour,
    timeMinute,
    daysOfWeek,
    intervalDays,
    startDate,
    endDate,
    isActive,
    createdAt,
  );
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is ScheduleEntry &&
          other.id == this.id &&
          other.medicineId == this.medicineId &&
          other.profileId == this.profileId &&
          other.scheduleType == this.scheduleType &&
          other.timeHour == this.timeHour &&
          other.timeMinute == this.timeMinute &&
          other.daysOfWeek == this.daysOfWeek &&
          other.intervalDays == this.intervalDays &&
          other.startDate == this.startDate &&
          other.endDate == this.endDate &&
          other.isActive == this.isActive &&
          other.createdAt == this.createdAt);
}

class SchedulesCompanion extends UpdateCompanion<ScheduleEntry> {
  final Value<int> id;
  final Value<int> medicineId;
  final Value<int> profileId;
  final Value<String> scheduleType;
  final Value<int> timeHour;
  final Value<int> timeMinute;
  final Value<String?> daysOfWeek;
  final Value<int?> intervalDays;
  final Value<DateTime> startDate;
  final Value<DateTime?> endDate;
  final Value<bool> isActive;
  final Value<DateTime> createdAt;
  const SchedulesCompanion({
    this.id = const Value.absent(),
    this.medicineId = const Value.absent(),
    this.profileId = const Value.absent(),
    this.scheduleType = const Value.absent(),
    this.timeHour = const Value.absent(),
    this.timeMinute = const Value.absent(),
    this.daysOfWeek = const Value.absent(),
    this.intervalDays = const Value.absent(),
    this.startDate = const Value.absent(),
    this.endDate = const Value.absent(),
    this.isActive = const Value.absent(),
    this.createdAt = const Value.absent(),
  });
  SchedulesCompanion.insert({
    this.id = const Value.absent(),
    required int medicineId,
    required int profileId,
    required String scheduleType,
    required int timeHour,
    required int timeMinute,
    this.daysOfWeek = const Value.absent(),
    this.intervalDays = const Value.absent(),
    required DateTime startDate,
    this.endDate = const Value.absent(),
    this.isActive = const Value.absent(),
    this.createdAt = const Value.absent(),
  }) : medicineId = Value(medicineId),
       profileId = Value(profileId),
       scheduleType = Value(scheduleType),
       timeHour = Value(timeHour),
       timeMinute = Value(timeMinute),
       startDate = Value(startDate);
  static Insertable<ScheduleEntry> custom({
    Expression<int>? id,
    Expression<int>? medicineId,
    Expression<int>? profileId,
    Expression<String>? scheduleType,
    Expression<int>? timeHour,
    Expression<int>? timeMinute,
    Expression<String>? daysOfWeek,
    Expression<int>? intervalDays,
    Expression<DateTime>? startDate,
    Expression<DateTime>? endDate,
    Expression<bool>? isActive,
    Expression<DateTime>? createdAt,
  }) {
    return RawValuesInsertable({
      if (id != null) 'id': id,
      if (medicineId != null) 'medicine_id': medicineId,
      if (profileId != null) 'profile_id': profileId,
      if (scheduleType != null) 'schedule_type': scheduleType,
      if (timeHour != null) 'time_hour': timeHour,
      if (timeMinute != null) 'time_minute': timeMinute,
      if (daysOfWeek != null) 'days_of_week': daysOfWeek,
      if (intervalDays != null) 'interval_days': intervalDays,
      if (startDate != null) 'start_date': startDate,
      if (endDate != null) 'end_date': endDate,
      if (isActive != null) 'is_active': isActive,
      if (createdAt != null) 'created_at': createdAt,
    });
  }

  SchedulesCompanion copyWith({
    Value<int>? id,
    Value<int>? medicineId,
    Value<int>? profileId,
    Value<String>? scheduleType,
    Value<int>? timeHour,
    Value<int>? timeMinute,
    Value<String?>? daysOfWeek,
    Value<int?>? intervalDays,
    Value<DateTime>? startDate,
    Value<DateTime?>? endDate,
    Value<bool>? isActive,
    Value<DateTime>? createdAt,
  }) {
    return SchedulesCompanion(
      id: id ?? this.id,
      medicineId: medicineId ?? this.medicineId,
      profileId: profileId ?? this.profileId,
      scheduleType: scheduleType ?? this.scheduleType,
      timeHour: timeHour ?? this.timeHour,
      timeMinute: timeMinute ?? this.timeMinute,
      daysOfWeek: daysOfWeek ?? this.daysOfWeek,
      intervalDays: intervalDays ?? this.intervalDays,
      startDate: startDate ?? this.startDate,
      endDate: endDate ?? this.endDate,
      isActive: isActive ?? this.isActive,
      createdAt: createdAt ?? this.createdAt,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (id.present) {
      map['id'] = Variable<int>(id.value);
    }
    if (medicineId.present) {
      map['medicine_id'] = Variable<int>(medicineId.value);
    }
    if (profileId.present) {
      map['profile_id'] = Variable<int>(profileId.value);
    }
    if (scheduleType.present) {
      map['schedule_type'] = Variable<String>(scheduleType.value);
    }
    if (timeHour.present) {
      map['time_hour'] = Variable<int>(timeHour.value);
    }
    if (timeMinute.present) {
      map['time_minute'] = Variable<int>(timeMinute.value);
    }
    if (daysOfWeek.present) {
      map['days_of_week'] = Variable<String>(daysOfWeek.value);
    }
    if (intervalDays.present) {
      map['interval_days'] = Variable<int>(intervalDays.value);
    }
    if (startDate.present) {
      map['start_date'] = Variable<DateTime>(startDate.value);
    }
    if (endDate.present) {
      map['end_date'] = Variable<DateTime>(endDate.value);
    }
    if (isActive.present) {
      map['is_active'] = Variable<bool>(isActive.value);
    }
    if (createdAt.present) {
      map['created_at'] = Variable<DateTime>(createdAt.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('SchedulesCompanion(')
          ..write('id: $id, ')
          ..write('medicineId: $medicineId, ')
          ..write('profileId: $profileId, ')
          ..write('scheduleType: $scheduleType, ')
          ..write('timeHour: $timeHour, ')
          ..write('timeMinute: $timeMinute, ')
          ..write('daysOfWeek: $daysOfWeek, ')
          ..write('intervalDays: $intervalDays, ')
          ..write('startDate: $startDate, ')
          ..write('endDate: $endDate, ')
          ..write('isActive: $isActive, ')
          ..write('createdAt: $createdAt')
          ..write(')'))
        .toString();
  }
}

class $DoseRecordsTable extends DoseRecords
    with TableInfo<$DoseRecordsTable, DoseRecordEntry> {
  @override
  final GeneratedDatabase attachedDatabase;
  final String? _alias;
  $DoseRecordsTable(this.attachedDatabase, [this._alias]);
  static const VerificationMeta _idMeta = const VerificationMeta('id');
  @override
  late final GeneratedColumn<int> id = GeneratedColumn<int>(
    'id',
    aliasedName,
    false,
    hasAutoIncrement: true,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'PRIMARY KEY AUTOINCREMENT',
    ),
  );
  static const VerificationMeta _profileIdMeta = const VerificationMeta(
    'profileId',
  );
  @override
  late final GeneratedColumn<int> profileId = GeneratedColumn<int>(
    'profile_id',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'REFERENCES profiles (id) ON DELETE CASCADE',
    ),
  );
  static const VerificationMeta _medicineIdMeta = const VerificationMeta(
    'medicineId',
  );
  @override
  late final GeneratedColumn<int> medicineId = GeneratedColumn<int>(
    'medicine_id',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'REFERENCES medicines (id) ON DELETE CASCADE',
    ),
  );
  static const VerificationMeta _scheduleIdMeta = const VerificationMeta(
    'scheduleId',
  );
  @override
  late final GeneratedColumn<int> scheduleId = GeneratedColumn<int>(
    'schedule_id',
    aliasedName,
    true,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultConstraints: GeneratedColumn.constraintIsAlways(
      'REFERENCES schedules (id) ON DELETE SET NULL',
    ),
  );
  static const VerificationMeta _scheduledAtMeta = const VerificationMeta(
    'scheduledAt',
  );
  @override
  late final GeneratedColumn<DateTime> scheduledAt = GeneratedColumn<DateTime>(
    'scheduled_at',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _reminderAtMeta = const VerificationMeta(
    'reminderAt',
  );
  @override
  late final GeneratedColumn<DateTime> reminderAt = GeneratedColumn<DateTime>(
    'reminder_at',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _statusMeta = const VerificationMeta('status');
  @override
  late final GeneratedColumn<String> status = GeneratedColumn<String>(
    'status',
    aliasedName,
    false,
    additionalChecks: GeneratedColumn.checkTextLength(
      minTextLength: 1,
      maxTextLength: 20,
    ),
    type: DriftSqlType.string,
    requiredDuringInsert: false,
    defaultValue: const Constant('pending'),
  );
  static const VerificationMeta _takenAtMeta = const VerificationMeta(
    'takenAt',
  );
  @override
  late final GeneratedColumn<DateTime> takenAt = GeneratedColumn<DateTime>(
    'taken_at',
    aliasedName,
    true,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _skippedAtMeta = const VerificationMeta(
    'skippedAt',
  );
  @override
  late final GeneratedColumn<DateTime> skippedAt = GeneratedColumn<DateTime>(
    'skipped_at',
    aliasedName,
    true,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _skipReasonMeta = const VerificationMeta(
    'skipReason',
  );
  @override
  late final GeneratedColumn<String> skipReason = GeneratedColumn<String>(
    'skip_reason',
    aliasedName,
    true,
    type: DriftSqlType.string,
    requiredDuringInsert: false,
  );
  static const VerificationMeta _snoozeCountMeta = const VerificationMeta(
    'snoozeCount',
  );
  @override
  late final GeneratedColumn<int> snoozeCount = GeneratedColumn<int>(
    'snooze_count',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: false,
    defaultValue: const Constant(0),
  );
  static const VerificationMeta _gracePeriodDeadlineMeta =
      const VerificationMeta('gracePeriodDeadline');
  @override
  late final GeneratedColumn<DateTime> gracePeriodDeadline =
      GeneratedColumn<DateTime>(
        'grace_period_deadline',
        aliasedName,
        false,
        type: DriftSqlType.dateTime,
        requiredDuringInsert: true,
      );
  static const VerificationMeta _notificationIdMeta = const VerificationMeta(
    'notificationId',
  );
  @override
  late final GeneratedColumn<int> notificationId = GeneratedColumn<int>(
    'notification_id',
    aliasedName,
    false,
    type: DriftSqlType.int,
    requiredDuringInsert: true,
  );
  static const VerificationMeta _createdAtMeta = const VerificationMeta(
    'createdAt',
  );
  @override
  late final GeneratedColumn<DateTime> createdAt = GeneratedColumn<DateTime>(
    'created_at',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
    defaultValue: currentDateAndTime,
  );
  static const VerificationMeta _updatedAtMeta = const VerificationMeta(
    'updatedAt',
  );
  @override
  late final GeneratedColumn<DateTime> updatedAt = GeneratedColumn<DateTime>(
    'updated_at',
    aliasedName,
    false,
    type: DriftSqlType.dateTime,
    requiredDuringInsert: false,
    defaultValue: currentDateAndTime,
  );
  @override
  List<GeneratedColumn> get $columns => [
    id,
    profileId,
    medicineId,
    scheduleId,
    scheduledAt,
    reminderAt,
    status,
    takenAt,
    skippedAt,
    skipReason,
    snoozeCount,
    gracePeriodDeadline,
    notificationId,
    createdAt,
    updatedAt,
  ];
  @override
  String get aliasedName => _alias ?? actualTableName;
  @override
  String get actualTableName => $name;
  static const String $name = 'dose_records';
  @override
  VerificationContext validateIntegrity(
    Insertable<DoseRecordEntry> instance, {
    bool isInserting = false,
  }) {
    final context = VerificationContext();
    final data = instance.toColumns(true);
    if (data.containsKey('id')) {
      context.handle(_idMeta, id.isAcceptableOrUnknown(data['id']!, _idMeta));
    }
    if (data.containsKey('profile_id')) {
      context.handle(
        _profileIdMeta,
        profileId.isAcceptableOrUnknown(data['profile_id']!, _profileIdMeta),
      );
    } else if (isInserting) {
      context.missing(_profileIdMeta);
    }
    if (data.containsKey('medicine_id')) {
      context.handle(
        _medicineIdMeta,
        medicineId.isAcceptableOrUnknown(data['medicine_id']!, _medicineIdMeta),
      );
    } else if (isInserting) {
      context.missing(_medicineIdMeta);
    }
    if (data.containsKey('schedule_id')) {
      context.handle(
        _scheduleIdMeta,
        scheduleId.isAcceptableOrUnknown(data['schedule_id']!, _scheduleIdMeta),
      );
    }
    if (data.containsKey('scheduled_at')) {
      context.handle(
        _scheduledAtMeta,
        scheduledAt.isAcceptableOrUnknown(
          data['scheduled_at']!,
          _scheduledAtMeta,
        ),
      );
    } else if (isInserting) {
      context.missing(_scheduledAtMeta);
    }
    if (data.containsKey('reminder_at')) {
      context.handle(
        _reminderAtMeta,
        reminderAt.isAcceptableOrUnknown(data['reminder_at']!, _reminderAtMeta),
      );
    } else if (isInserting) {
      context.missing(_reminderAtMeta);
    }
    if (data.containsKey('status')) {
      context.handle(
        _statusMeta,
        status.isAcceptableOrUnknown(data['status']!, _statusMeta),
      );
    }
    if (data.containsKey('taken_at')) {
      context.handle(
        _takenAtMeta,
        takenAt.isAcceptableOrUnknown(data['taken_at']!, _takenAtMeta),
      );
    }
    if (data.containsKey('skipped_at')) {
      context.handle(
        _skippedAtMeta,
        skippedAt.isAcceptableOrUnknown(data['skipped_at']!, _skippedAtMeta),
      );
    }
    if (data.containsKey('skip_reason')) {
      context.handle(
        _skipReasonMeta,
        skipReason.isAcceptableOrUnknown(data['skip_reason']!, _skipReasonMeta),
      );
    }
    if (data.containsKey('snooze_count')) {
      context.handle(
        _snoozeCountMeta,
        snoozeCount.isAcceptableOrUnknown(
          data['snooze_count']!,
          _snoozeCountMeta,
        ),
      );
    }
    if (data.containsKey('grace_period_deadline')) {
      context.handle(
        _gracePeriodDeadlineMeta,
        gracePeriodDeadline.isAcceptableOrUnknown(
          data['grace_period_deadline']!,
          _gracePeriodDeadlineMeta,
        ),
      );
    } else if (isInserting) {
      context.missing(_gracePeriodDeadlineMeta);
    }
    if (data.containsKey('notification_id')) {
      context.handle(
        _notificationIdMeta,
        notificationId.isAcceptableOrUnknown(
          data['notification_id']!,
          _notificationIdMeta,
        ),
      );
    } else if (isInserting) {
      context.missing(_notificationIdMeta);
    }
    if (data.containsKey('created_at')) {
      context.handle(
        _createdAtMeta,
        createdAt.isAcceptableOrUnknown(data['created_at']!, _createdAtMeta),
      );
    }
    if (data.containsKey('updated_at')) {
      context.handle(
        _updatedAtMeta,
        updatedAt.isAcceptableOrUnknown(data['updated_at']!, _updatedAtMeta),
      );
    }
    return context;
  }

  @override
  Set<GeneratedColumn> get $primaryKey => {id};
  @override
  DoseRecordEntry map(Map<String, dynamic> data, {String? tablePrefix}) {
    final effectivePrefix = tablePrefix != null ? '$tablePrefix.' : '';
    return DoseRecordEntry(
      id: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}id'],
      )!,
      profileId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}profile_id'],
      )!,
      medicineId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}medicine_id'],
      )!,
      scheduleId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}schedule_id'],
      ),
      scheduledAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}scheduled_at'],
      )!,
      reminderAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}reminder_at'],
      )!,
      status: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}status'],
      )!,
      takenAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}taken_at'],
      ),
      skippedAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}skipped_at'],
      ),
      skipReason: attachedDatabase.typeMapping.read(
        DriftSqlType.string,
        data['${effectivePrefix}skip_reason'],
      ),
      snoozeCount: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}snooze_count'],
      )!,
      gracePeriodDeadline: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}grace_period_deadline'],
      )!,
      notificationId: attachedDatabase.typeMapping.read(
        DriftSqlType.int,
        data['${effectivePrefix}notification_id'],
      )!,
      createdAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}created_at'],
      )!,
      updatedAt: attachedDatabase.typeMapping.read(
        DriftSqlType.dateTime,
        data['${effectivePrefix}updated_at'],
      )!,
    );
  }

  @override
  $DoseRecordsTable createAlias(String alias) {
    return $DoseRecordsTable(attachedDatabase, alias);
  }
}

class DoseRecordEntry extends DataClass implements Insertable<DoseRecordEntry> {
  final int id;
  final int profileId;
  final int medicineId;
  final int? scheduleId;
  final DateTime scheduledAt;
  final DateTime reminderAt;
  final String status;
  final DateTime? takenAt;
  final DateTime? skippedAt;
  final String? skipReason;
  final int snoozeCount;
  final DateTime gracePeriodDeadline;
  final int notificationId;
  final DateTime createdAt;
  final DateTime updatedAt;
  const DoseRecordEntry({
    required this.id,
    required this.profileId,
    required this.medicineId,
    this.scheduleId,
    required this.scheduledAt,
    required this.reminderAt,
    required this.status,
    this.takenAt,
    this.skippedAt,
    this.skipReason,
    required this.snoozeCount,
    required this.gracePeriodDeadline,
    required this.notificationId,
    required this.createdAt,
    required this.updatedAt,
  });
  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    map['id'] = Variable<int>(id);
    map['profile_id'] = Variable<int>(profileId);
    map['medicine_id'] = Variable<int>(medicineId);
    if (!nullToAbsent || scheduleId != null) {
      map['schedule_id'] = Variable<int>(scheduleId);
    }
    map['scheduled_at'] = Variable<DateTime>(scheduledAt);
    map['reminder_at'] = Variable<DateTime>(reminderAt);
    map['status'] = Variable<String>(status);
    if (!nullToAbsent || takenAt != null) {
      map['taken_at'] = Variable<DateTime>(takenAt);
    }
    if (!nullToAbsent || skippedAt != null) {
      map['skipped_at'] = Variable<DateTime>(skippedAt);
    }
    if (!nullToAbsent || skipReason != null) {
      map['skip_reason'] = Variable<String>(skipReason);
    }
    map['snooze_count'] = Variable<int>(snoozeCount);
    map['grace_period_deadline'] = Variable<DateTime>(gracePeriodDeadline);
    map['notification_id'] = Variable<int>(notificationId);
    map['created_at'] = Variable<DateTime>(createdAt);
    map['updated_at'] = Variable<DateTime>(updatedAt);
    return map;
  }

  DoseRecordsCompanion toCompanion(bool nullToAbsent) {
    return DoseRecordsCompanion(
      id: Value(id),
      profileId: Value(profileId),
      medicineId: Value(medicineId),
      scheduleId: scheduleId == null && nullToAbsent
          ? const Value.absent()
          : Value(scheduleId),
      scheduledAt: Value(scheduledAt),
      reminderAt: Value(reminderAt),
      status: Value(status),
      takenAt: takenAt == null && nullToAbsent
          ? const Value.absent()
          : Value(takenAt),
      skippedAt: skippedAt == null && nullToAbsent
          ? const Value.absent()
          : Value(skippedAt),
      skipReason: skipReason == null && nullToAbsent
          ? const Value.absent()
          : Value(skipReason),
      snoozeCount: Value(snoozeCount),
      gracePeriodDeadline: Value(gracePeriodDeadline),
      notificationId: Value(notificationId),
      createdAt: Value(createdAt),
      updatedAt: Value(updatedAt),
    );
  }

  factory DoseRecordEntry.fromJson(
    Map<String, dynamic> json, {
    ValueSerializer? serializer,
  }) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return DoseRecordEntry(
      id: serializer.fromJson<int>(json['id']),
      profileId: serializer.fromJson<int>(json['profileId']),
      medicineId: serializer.fromJson<int>(json['medicineId']),
      scheduleId: serializer.fromJson<int?>(json['scheduleId']),
      scheduledAt: serializer.fromJson<DateTime>(json['scheduledAt']),
      reminderAt: serializer.fromJson<DateTime>(json['reminderAt']),
      status: serializer.fromJson<String>(json['status']),
      takenAt: serializer.fromJson<DateTime?>(json['takenAt']),
      skippedAt: serializer.fromJson<DateTime?>(json['skippedAt']),
      skipReason: serializer.fromJson<String?>(json['skipReason']),
      snoozeCount: serializer.fromJson<int>(json['snoozeCount']),
      gracePeriodDeadline: serializer.fromJson<DateTime>(
        json['gracePeriodDeadline'],
      ),
      notificationId: serializer.fromJson<int>(json['notificationId']),
      createdAt: serializer.fromJson<DateTime>(json['createdAt']),
      updatedAt: serializer.fromJson<DateTime>(json['updatedAt']),
    );
  }
  @override
  Map<String, dynamic> toJson({ValueSerializer? serializer}) {
    serializer ??= driftRuntimeOptions.defaultSerializer;
    return <String, dynamic>{
      'id': serializer.toJson<int>(id),
      'profileId': serializer.toJson<int>(profileId),
      'medicineId': serializer.toJson<int>(medicineId),
      'scheduleId': serializer.toJson<int?>(scheduleId),
      'scheduledAt': serializer.toJson<DateTime>(scheduledAt),
      'reminderAt': serializer.toJson<DateTime>(reminderAt),
      'status': serializer.toJson<String>(status),
      'takenAt': serializer.toJson<DateTime?>(takenAt),
      'skippedAt': serializer.toJson<DateTime?>(skippedAt),
      'skipReason': serializer.toJson<String?>(skipReason),
      'snoozeCount': serializer.toJson<int>(snoozeCount),
      'gracePeriodDeadline': serializer.toJson<DateTime>(gracePeriodDeadline),
      'notificationId': serializer.toJson<int>(notificationId),
      'createdAt': serializer.toJson<DateTime>(createdAt),
      'updatedAt': serializer.toJson<DateTime>(updatedAt),
    };
  }

  DoseRecordEntry copyWith({
    int? id,
    int? profileId,
    int? medicineId,
    Value<int?> scheduleId = const Value.absent(),
    DateTime? scheduledAt,
    DateTime? reminderAt,
    String? status,
    Value<DateTime?> takenAt = const Value.absent(),
    Value<DateTime?> skippedAt = const Value.absent(),
    Value<String?> skipReason = const Value.absent(),
    int? snoozeCount,
    DateTime? gracePeriodDeadline,
    int? notificationId,
    DateTime? createdAt,
    DateTime? updatedAt,
  }) => DoseRecordEntry(
    id: id ?? this.id,
    profileId: profileId ?? this.profileId,
    medicineId: medicineId ?? this.medicineId,
    scheduleId: scheduleId.present ? scheduleId.value : this.scheduleId,
    scheduledAt: scheduledAt ?? this.scheduledAt,
    reminderAt: reminderAt ?? this.reminderAt,
    status: status ?? this.status,
    takenAt: takenAt.present ? takenAt.value : this.takenAt,
    skippedAt: skippedAt.present ? skippedAt.value : this.skippedAt,
    skipReason: skipReason.present ? skipReason.value : this.skipReason,
    snoozeCount: snoozeCount ?? this.snoozeCount,
    gracePeriodDeadline: gracePeriodDeadline ?? this.gracePeriodDeadline,
    notificationId: notificationId ?? this.notificationId,
    createdAt: createdAt ?? this.createdAt,
    updatedAt: updatedAt ?? this.updatedAt,
  );
  DoseRecordEntry copyWithCompanion(DoseRecordsCompanion data) {
    return DoseRecordEntry(
      id: data.id.present ? data.id.value : this.id,
      profileId: data.profileId.present ? data.profileId.value : this.profileId,
      medicineId: data.medicineId.present
          ? data.medicineId.value
          : this.medicineId,
      scheduleId: data.scheduleId.present
          ? data.scheduleId.value
          : this.scheduleId,
      scheduledAt: data.scheduledAt.present
          ? data.scheduledAt.value
          : this.scheduledAt,
      reminderAt: data.reminderAt.present
          ? data.reminderAt.value
          : this.reminderAt,
      status: data.status.present ? data.status.value : this.status,
      takenAt: data.takenAt.present ? data.takenAt.value : this.takenAt,
      skippedAt: data.skippedAt.present ? data.skippedAt.value : this.skippedAt,
      skipReason: data.skipReason.present
          ? data.skipReason.value
          : this.skipReason,
      snoozeCount: data.snoozeCount.present
          ? data.snoozeCount.value
          : this.snoozeCount,
      gracePeriodDeadline: data.gracePeriodDeadline.present
          ? data.gracePeriodDeadline.value
          : this.gracePeriodDeadline,
      notificationId: data.notificationId.present
          ? data.notificationId.value
          : this.notificationId,
      createdAt: data.createdAt.present ? data.createdAt.value : this.createdAt,
      updatedAt: data.updatedAt.present ? data.updatedAt.value : this.updatedAt,
    );
  }

  @override
  String toString() {
    return (StringBuffer('DoseRecordEntry(')
          ..write('id: $id, ')
          ..write('profileId: $profileId, ')
          ..write('medicineId: $medicineId, ')
          ..write('scheduleId: $scheduleId, ')
          ..write('scheduledAt: $scheduledAt, ')
          ..write('reminderAt: $reminderAt, ')
          ..write('status: $status, ')
          ..write('takenAt: $takenAt, ')
          ..write('skippedAt: $skippedAt, ')
          ..write('skipReason: $skipReason, ')
          ..write('snoozeCount: $snoozeCount, ')
          ..write('gracePeriodDeadline: $gracePeriodDeadline, ')
          ..write('notificationId: $notificationId, ')
          ..write('createdAt: $createdAt, ')
          ..write('updatedAt: $updatedAt')
          ..write(')'))
        .toString();
  }

  @override
  int get hashCode => Object.hash(
    id,
    profileId,
    medicineId,
    scheduleId,
    scheduledAt,
    reminderAt,
    status,
    takenAt,
    skippedAt,
    skipReason,
    snoozeCount,
    gracePeriodDeadline,
    notificationId,
    createdAt,
    updatedAt,
  );
  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is DoseRecordEntry &&
          other.id == this.id &&
          other.profileId == this.profileId &&
          other.medicineId == this.medicineId &&
          other.scheduleId == this.scheduleId &&
          other.scheduledAt == this.scheduledAt &&
          other.reminderAt == this.reminderAt &&
          other.status == this.status &&
          other.takenAt == this.takenAt &&
          other.skippedAt == this.skippedAt &&
          other.skipReason == this.skipReason &&
          other.snoozeCount == this.snoozeCount &&
          other.gracePeriodDeadline == this.gracePeriodDeadline &&
          other.notificationId == this.notificationId &&
          other.createdAt == this.createdAt &&
          other.updatedAt == this.updatedAt);
}

class DoseRecordsCompanion extends UpdateCompanion<DoseRecordEntry> {
  final Value<int> id;
  final Value<int> profileId;
  final Value<int> medicineId;
  final Value<int?> scheduleId;
  final Value<DateTime> scheduledAt;
  final Value<DateTime> reminderAt;
  final Value<String> status;
  final Value<DateTime?> takenAt;
  final Value<DateTime?> skippedAt;
  final Value<String?> skipReason;
  final Value<int> snoozeCount;
  final Value<DateTime> gracePeriodDeadline;
  final Value<int> notificationId;
  final Value<DateTime> createdAt;
  final Value<DateTime> updatedAt;
  const DoseRecordsCompanion({
    this.id = const Value.absent(),
    this.profileId = const Value.absent(),
    this.medicineId = const Value.absent(),
    this.scheduleId = const Value.absent(),
    this.scheduledAt = const Value.absent(),
    this.reminderAt = const Value.absent(),
    this.status = const Value.absent(),
    this.takenAt = const Value.absent(),
    this.skippedAt = const Value.absent(),
    this.skipReason = const Value.absent(),
    this.snoozeCount = const Value.absent(),
    this.gracePeriodDeadline = const Value.absent(),
    this.notificationId = const Value.absent(),
    this.createdAt = const Value.absent(),
    this.updatedAt = const Value.absent(),
  });
  DoseRecordsCompanion.insert({
    this.id = const Value.absent(),
    required int profileId,
    required int medicineId,
    this.scheduleId = const Value.absent(),
    required DateTime scheduledAt,
    required DateTime reminderAt,
    this.status = const Value.absent(),
    this.takenAt = const Value.absent(),
    this.skippedAt = const Value.absent(),
    this.skipReason = const Value.absent(),
    this.snoozeCount = const Value.absent(),
    required DateTime gracePeriodDeadline,
    required int notificationId,
    this.createdAt = const Value.absent(),
    this.updatedAt = const Value.absent(),
  }) : profileId = Value(profileId),
       medicineId = Value(medicineId),
       scheduledAt = Value(scheduledAt),
       reminderAt = Value(reminderAt),
       gracePeriodDeadline = Value(gracePeriodDeadline),
       notificationId = Value(notificationId);
  static Insertable<DoseRecordEntry> custom({
    Expression<int>? id,
    Expression<int>? profileId,
    Expression<int>? medicineId,
    Expression<int>? scheduleId,
    Expression<DateTime>? scheduledAt,
    Expression<DateTime>? reminderAt,
    Expression<String>? status,
    Expression<DateTime>? takenAt,
    Expression<DateTime>? skippedAt,
    Expression<String>? skipReason,
    Expression<int>? snoozeCount,
    Expression<DateTime>? gracePeriodDeadline,
    Expression<int>? notificationId,
    Expression<DateTime>? createdAt,
    Expression<DateTime>? updatedAt,
  }) {
    return RawValuesInsertable({
      if (id != null) 'id': id,
      if (profileId != null) 'profile_id': profileId,
      if (medicineId != null) 'medicine_id': medicineId,
      if (scheduleId != null) 'schedule_id': scheduleId,
      if (scheduledAt != null) 'scheduled_at': scheduledAt,
      if (reminderAt != null) 'reminder_at': reminderAt,
      if (status != null) 'status': status,
      if (takenAt != null) 'taken_at': takenAt,
      if (skippedAt != null) 'skipped_at': skippedAt,
      if (skipReason != null) 'skip_reason': skipReason,
      if (snoozeCount != null) 'snooze_count': snoozeCount,
      if (gracePeriodDeadline != null)
        'grace_period_deadline': gracePeriodDeadline,
      if (notificationId != null) 'notification_id': notificationId,
      if (createdAt != null) 'created_at': createdAt,
      if (updatedAt != null) 'updated_at': updatedAt,
    });
  }

  DoseRecordsCompanion copyWith({
    Value<int>? id,
    Value<int>? profileId,
    Value<int>? medicineId,
    Value<int?>? scheduleId,
    Value<DateTime>? scheduledAt,
    Value<DateTime>? reminderAt,
    Value<String>? status,
    Value<DateTime?>? takenAt,
    Value<DateTime?>? skippedAt,
    Value<String?>? skipReason,
    Value<int>? snoozeCount,
    Value<DateTime>? gracePeriodDeadline,
    Value<int>? notificationId,
    Value<DateTime>? createdAt,
    Value<DateTime>? updatedAt,
  }) {
    return DoseRecordsCompanion(
      id: id ?? this.id,
      profileId: profileId ?? this.profileId,
      medicineId: medicineId ?? this.medicineId,
      scheduleId: scheduleId ?? this.scheduleId,
      scheduledAt: scheduledAt ?? this.scheduledAt,
      reminderAt: reminderAt ?? this.reminderAt,
      status: status ?? this.status,
      takenAt: takenAt ?? this.takenAt,
      skippedAt: skippedAt ?? this.skippedAt,
      skipReason: skipReason ?? this.skipReason,
      snoozeCount: snoozeCount ?? this.snoozeCount,
      gracePeriodDeadline: gracePeriodDeadline ?? this.gracePeriodDeadline,
      notificationId: notificationId ?? this.notificationId,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
    );
  }

  @override
  Map<String, Expression> toColumns(bool nullToAbsent) {
    final map = <String, Expression>{};
    if (id.present) {
      map['id'] = Variable<int>(id.value);
    }
    if (profileId.present) {
      map['profile_id'] = Variable<int>(profileId.value);
    }
    if (medicineId.present) {
      map['medicine_id'] = Variable<int>(medicineId.value);
    }
    if (scheduleId.present) {
      map['schedule_id'] = Variable<int>(scheduleId.value);
    }
    if (scheduledAt.present) {
      map['scheduled_at'] = Variable<DateTime>(scheduledAt.value);
    }
    if (reminderAt.present) {
      map['reminder_at'] = Variable<DateTime>(reminderAt.value);
    }
    if (status.present) {
      map['status'] = Variable<String>(status.value);
    }
    if (takenAt.present) {
      map['taken_at'] = Variable<DateTime>(takenAt.value);
    }
    if (skippedAt.present) {
      map['skipped_at'] = Variable<DateTime>(skippedAt.value);
    }
    if (skipReason.present) {
      map['skip_reason'] = Variable<String>(skipReason.value);
    }
    if (snoozeCount.present) {
      map['snooze_count'] = Variable<int>(snoozeCount.value);
    }
    if (gracePeriodDeadline.present) {
      map['grace_period_deadline'] = Variable<DateTime>(
        gracePeriodDeadline.value,
      );
    }
    if (notificationId.present) {
      map['notification_id'] = Variable<int>(notificationId.value);
    }
    if (createdAt.present) {
      map['created_at'] = Variable<DateTime>(createdAt.value);
    }
    if (updatedAt.present) {
      map['updated_at'] = Variable<DateTime>(updatedAt.value);
    }
    return map;
  }

  @override
  String toString() {
    return (StringBuffer('DoseRecordsCompanion(')
          ..write('id: $id, ')
          ..write('profileId: $profileId, ')
          ..write('medicineId: $medicineId, ')
          ..write('scheduleId: $scheduleId, ')
          ..write('scheduledAt: $scheduledAt, ')
          ..write('reminderAt: $reminderAt, ')
          ..write('status: $status, ')
          ..write('takenAt: $takenAt, ')
          ..write('skippedAt: $skippedAt, ')
          ..write('skipReason: $skipReason, ')
          ..write('snoozeCount: $snoozeCount, ')
          ..write('gracePeriodDeadline: $gracePeriodDeadline, ')
          ..write('notificationId: $notificationId, ')
          ..write('createdAt: $createdAt, ')
          ..write('updatedAt: $updatedAt')
          ..write(')'))
        .toString();
  }
}

abstract class _$AppDatabase extends GeneratedDatabase {
  _$AppDatabase(QueryExecutor e) : super(e);
  $AppDatabaseManager get managers => $AppDatabaseManager(this);
  late final $ProfilesTable profiles = $ProfilesTable(this);
  late final $ProfileSettingsTable profileSettings = $ProfileSettingsTable(
    this,
  );
  late final $AppSettingsTable appSettings = $AppSettingsTable(this);
  late final $MedicinesTable medicines = $MedicinesTable(this);
  late final $SchedulesTable schedules = $SchedulesTable(this);
  late final $DoseRecordsTable doseRecords = $DoseRecordsTable(this);
  late final Index idxDoseScheduleScheduled = Index(
    'idx_dose_schedule_scheduled',
    'CREATE UNIQUE INDEX idx_dose_schedule_scheduled ON dose_records (schedule_id, scheduled_at)',
  );
  late final Index idxDoseProfileTimeStatus = Index(
    'idx_dose_profile_time_status',
    'CREATE INDEX idx_dose_profile_time_status ON dose_records (profile_id, scheduled_at, status)',
  );
  late final ProfileDao profileDao = ProfileDao(this as AppDatabase);
  late final MedicineDao medicineDao = MedicineDao(this as AppDatabase);
  late final ScheduleDao scheduleDao = ScheduleDao(this as AppDatabase);
  late final DoseRecordDao doseRecordDao = DoseRecordDao(this as AppDatabase);
  @override
  Iterable<TableInfo<Table, Object?>> get allTables =>
      allSchemaEntities.whereType<TableInfo<Table, Object?>>();
  @override
  List<DatabaseSchemaEntity> get allSchemaEntities => [
    profiles,
    profileSettings,
    appSettings,
    medicines,
    schedules,
    doseRecords,
    idxDoseScheduleScheduled,
    idxDoseProfileTimeStatus,
  ];
  @override
  StreamQueryUpdateRules get streamUpdateRules => const StreamQueryUpdateRules([
    WritePropagation(
      on: TableUpdateQuery.onTableName(
        'profiles',
        limitUpdateKind: UpdateKind.delete,
      ),
      result: [TableUpdate('profile_settings', kind: UpdateKind.delete)],
    ),
    WritePropagation(
      on: TableUpdateQuery.onTableName(
        'profiles',
        limitUpdateKind: UpdateKind.delete,
      ),
      result: [TableUpdate('medicines', kind: UpdateKind.delete)],
    ),
    WritePropagation(
      on: TableUpdateQuery.onTableName(
        'medicines',
        limitUpdateKind: UpdateKind.delete,
      ),
      result: [TableUpdate('schedules', kind: UpdateKind.delete)],
    ),
    WritePropagation(
      on: TableUpdateQuery.onTableName(
        'profiles',
        limitUpdateKind: UpdateKind.delete,
      ),
      result: [TableUpdate('schedules', kind: UpdateKind.delete)],
    ),
    WritePropagation(
      on: TableUpdateQuery.onTableName(
        'profiles',
        limitUpdateKind: UpdateKind.delete,
      ),
      result: [TableUpdate('dose_records', kind: UpdateKind.delete)],
    ),
    WritePropagation(
      on: TableUpdateQuery.onTableName(
        'medicines',
        limitUpdateKind: UpdateKind.delete,
      ),
      result: [TableUpdate('dose_records', kind: UpdateKind.delete)],
    ),
    WritePropagation(
      on: TableUpdateQuery.onTableName(
        'schedules',
        limitUpdateKind: UpdateKind.delete,
      ),
      result: [TableUpdate('dose_records', kind: UpdateKind.update)],
    ),
  ]);
}

typedef $$ProfilesTableCreateCompanionBuilder = ProfilesCompanion Function({
  Value<int> id,
  required String name,
  Value<int> avatarColor,
  Value<String> avatarIcon,
  Value<DateTime> createdAt,
  Value<bool> isActive,
});
typedef $$ProfilesTableUpdateCompanionBuilder = ProfilesCompanion Function({
  Value<int> id,
  Value<String> name,
  Value<int> avatarColor,
  Value<String> avatarIcon,
  Value<DateTime> createdAt,
  Value<bool> isActive,
});

final class $$ProfilesTableReferences
    extends BaseReferences<_$AppDatabase, $ProfilesTable, ProfileEntry> {
  $$ProfilesTableReferences(super.$_db, super.$_table, super.$_typedResult);

  static MultiTypedResultKey<$ProfileSettingsTable, List<ProfileSettingsEntry>>
  _profileSettingsRefsTable(_$AppDatabase db) => MultiTypedResultKey.fromTable(
    db.profileSettings,
    aliasName: 'profiles__id__profile_settings__profile_id',
  );

  $$ProfileSettingsTableProcessedTableManager get profileSettingsRefs {
    final manager = $$ProfileSettingsTableTableManager(
      $_db,
      $_db.profileSettings,
    ).filter((f) => f.profileId.id.sqlEquals($_itemColumn<int>('id')!));

    final cache = $_typedResult.readTableOrNull(
      _profileSettingsRefsTable($_db),
    );
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: cache),
    );
  }

  static MultiTypedResultKey<$MedicinesTable, List<MedicineEntry>>
  _medicinesRefsTable(_$AppDatabase db) => MultiTypedResultKey.fromTable(
    db.medicines,
    aliasName: 'profiles__id__medicines__profile_id',
  );

  $$MedicinesTableProcessedTableManager get medicinesRefs {
    final manager = $$MedicinesTableTableManager(
      $_db,
      $_db.medicines,
    ).filter((f) => f.profileId.id.sqlEquals($_itemColumn<int>('id')!));

    final cache = $_typedResult.readTableOrNull(_medicinesRefsTable($_db));
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: cache),
    );
  }

  static MultiTypedResultKey<$SchedulesTable, List<ScheduleEntry>>
  _schedulesRefsTable(_$AppDatabase db) => MultiTypedResultKey.fromTable(
    db.schedules,
    aliasName: 'profiles__id__schedules__profile_id',
  );

  $$SchedulesTableProcessedTableManager get schedulesRefs {
    final manager = $$SchedulesTableTableManager(
      $_db,
      $_db.schedules,
    ).filter((f) => f.profileId.id.sqlEquals($_itemColumn<int>('id')!));

    final cache = $_typedResult.readTableOrNull(_schedulesRefsTable($_db));
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: cache),
    );
  }

  static MultiTypedResultKey<$DoseRecordsTable, List<DoseRecordEntry>>
  _doseRecordsRefsTable(_$AppDatabase db) => MultiTypedResultKey.fromTable(
    db.doseRecords,
    aliasName: 'profiles__id__dose_records__profile_id',
  );

  $$DoseRecordsTableProcessedTableManager get doseRecordsRefs {
    final manager = $$DoseRecordsTableTableManager(
      $_db,
      $_db.doseRecords,
    ).filter((f) => f.profileId.id.sqlEquals($_itemColumn<int>('id')!));

    final cache = $_typedResult.readTableOrNull(_doseRecordsRefsTable($_db));
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: cache),
    );
  }
}

class $$ProfilesTableFilterComposer
    extends Composer<_$AppDatabase, $ProfilesTable> {
  $$ProfilesTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get name => $composableBuilder(
    column: $table.name,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get avatarColor => $composableBuilder(
    column: $table.avatarColor,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get avatarIcon => $composableBuilder(
    column: $table.avatarIcon,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<bool> get isActive => $composableBuilder(
    column: $table.isActive,
    builder: (column) => ColumnFilters(column),
  );

  Expression<bool> profileSettingsRefs(
    Expression<bool> Function($$ProfileSettingsTableFilterComposer f) f,
  ) {
    final $$ProfileSettingsTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.profileSettings,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfileSettingsTableFilterComposer(
            $db: $db,
            $table: $db.profileSettings,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<bool> medicinesRefs(
    Expression<bool> Function($$MedicinesTableFilterComposer f) f,
  ) {
    final $$MedicinesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableFilterComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<bool> schedulesRefs(
    Expression<bool> Function($$SchedulesTableFilterComposer f) f,
  ) {
    final $$SchedulesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.schedules,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$SchedulesTableFilterComposer(
            $db: $db,
            $table: $db.schedules,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<bool> doseRecordsRefs(
    Expression<bool> Function($$DoseRecordsTableFilterComposer f) f,
  ) {
    final $$DoseRecordsTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.doseRecords,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$DoseRecordsTableFilterComposer(
            $db: $db,
            $table: $db.doseRecords,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }
}

class $$ProfilesTableOrderingComposer
    extends Composer<_$AppDatabase, $ProfilesTable> {
  $$ProfilesTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get name => $composableBuilder(
    column: $table.name,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get avatarColor => $composableBuilder(
    column: $table.avatarColor,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get avatarIcon => $composableBuilder(
    column: $table.avatarIcon,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<bool> get isActive => $composableBuilder(
    column: $table.isActive,
    builder: (column) => ColumnOrderings(column),
  );
}

class $$ProfilesTableAnnotationComposer
    extends Composer<_$AppDatabase, $ProfilesTable> {
  $$ProfilesTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get id =>
      $composableBuilder(column: $table.id, builder: (column) => column);

  GeneratedColumn<String> get name =>
      $composableBuilder(column: $table.name, builder: (column) => column);

  GeneratedColumn<int> get avatarColor => $composableBuilder(
    column: $table.avatarColor,
    builder: (column) => column,
  );

  GeneratedColumn<String> get avatarIcon => $composableBuilder(
    column: $table.avatarIcon,
    builder: (column) => column,
  );

  GeneratedColumn<DateTime> get createdAt =>
      $composableBuilder(column: $table.createdAt, builder: (column) => column);

  GeneratedColumn<bool> get isActive =>
      $composableBuilder(column: $table.isActive, builder: (column) => column);

  Expression<T> profileSettingsRefs<T extends Object>(
    Expression<T> Function($$ProfileSettingsTableAnnotationComposer a) f,
  ) {
    final $$ProfileSettingsTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.profileSettings,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfileSettingsTableAnnotationComposer(
            $db: $db,
            $table: $db.profileSettings,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<T> medicinesRefs<T extends Object>(
    Expression<T> Function($$MedicinesTableAnnotationComposer a) f,
  ) {
    final $$MedicinesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableAnnotationComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<T> schedulesRefs<T extends Object>(
    Expression<T> Function($$SchedulesTableAnnotationComposer a) f,
  ) {
    final $$SchedulesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.schedules,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$SchedulesTableAnnotationComposer(
            $db: $db,
            $table: $db.schedules,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<T> doseRecordsRefs<T extends Object>(
    Expression<T> Function($$DoseRecordsTableAnnotationComposer a) f,
  ) {
    final $$DoseRecordsTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.doseRecords,
      getReferencedColumn: (t) => t.profileId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$DoseRecordsTableAnnotationComposer(
            $db: $db,
            $table: $db.doseRecords,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }
}

class $$ProfilesTableTableManager
    extends
        RootTableManager<
          _$AppDatabase,
          $ProfilesTable,
          ProfileEntry,
          $$ProfilesTableFilterComposer,
          $$ProfilesTableOrderingComposer,
          $$ProfilesTableAnnotationComposer,
          $$ProfilesTableCreateCompanionBuilder,
          $$ProfilesTableUpdateCompanionBuilder,
          (ProfileEntry, $$ProfilesTableReferences),
          ProfileEntry,
          PrefetchHooks Function({
            bool profileSettingsRefs,
            bool medicinesRefs,
            bool schedulesRefs,
            bool doseRecordsRefs,
          })
        > {
  $$ProfilesTableTableManager(_$AppDatabase db, $ProfilesTable table)
    : super(
        TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$ProfilesTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$ProfilesTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$ProfilesTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                Value<String> name = const Value.absent(),
                Value<int> avatarColor = const Value.absent(),
                Value<String> avatarIcon = const Value.absent(),
                Value<DateTime> createdAt = const Value.absent(),
                Value<bool> isActive = const Value.absent(),
              }) => ProfilesCompanion(
                id: id,
                name: name,
                avatarColor: avatarColor,
                avatarIcon: avatarIcon,
                createdAt: createdAt,
                isActive: isActive,
              ),
          createCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                required String name,
                Value<int> avatarColor = const Value.absent(),
                Value<String> avatarIcon = const Value.absent(),
                Value<DateTime> createdAt = const Value.absent(),
                Value<bool> isActive = const Value.absent(),
              }) => ProfilesCompanion.insert(
                id: id,
                name: name,
                avatarColor: avatarColor,
                avatarIcon: avatarIcon,
                createdAt: createdAt,
                isActive: isActive,
              ),
          withReferenceMapper: (p0) => p0
              .map(
                (e) => (
                  e.readTable<$ProfilesTable, ProfileEntry>(table),
                  $$ProfilesTableReferences(db, table, e),
                ),
              )
              .toList(),
          prefetchHooksCallback:
              ({
                profileSettingsRefs = false,
                medicinesRefs = false,
                schedulesRefs = false,
                doseRecordsRefs = false,
              }) {
                return PrefetchHooks(
                  db: db,
                  explicitlyWatchedTables: [
                    if (profileSettingsRefs) db.profileSettings,
                    if (medicinesRefs) db.medicines,
                    if (schedulesRefs) db.schedules,
                    if (doseRecordsRefs) db.doseRecords,
                  ],
                  addJoins: null,
                  getPrefetchedDataCallback: (items) async {
                    return [
                      if (profileSettingsRefs)
                        await $_getPrefetchedDataByColumns<
                          ProfileEntry,
                          $ProfilesTable,
                          ProfileSettingsEntry,
                          $ProfileSettingsTable
                        >(
                          currentTable: table,
                          referencedTable: $$ProfilesTableReferences
                              ._profileSettingsRefsTable(db),
                          managerFromTypedResult: (p0) =>
                              $$ProfilesTableReferences(
                                db,
                                table,
                                p0,
                              ).profileSettingsRefs,
                          currentColumn: 'id',
                          referencedColumn: 'profile_id',
                          typedResults: items,
                        ),
                      if (medicinesRefs)
                        await $_getPrefetchedDataByColumns<
                          ProfileEntry,
                          $ProfilesTable,
                          MedicineEntry,
                          $MedicinesTable
                        >(
                          currentTable: table,
                          referencedTable: $$ProfilesTableReferences
                              ._medicinesRefsTable(db),
                          managerFromTypedResult: (p0) =>
                              $$ProfilesTableReferences(
                                db,
                                table,
                                p0,
                              ).medicinesRefs,
                          currentColumn: 'id',
                          referencedColumn: 'profile_id',
                          typedResults: items,
                        ),
                      if (schedulesRefs)
                        await $_getPrefetchedDataByColumns<
                          ProfileEntry,
                          $ProfilesTable,
                          ScheduleEntry,
                          $SchedulesTable
                        >(
                          currentTable: table,
                          referencedTable: $$ProfilesTableReferences
                              ._schedulesRefsTable(db),
                          managerFromTypedResult: (p0) =>
                              $$ProfilesTableReferences(
                                db,
                                table,
                                p0,
                              ).schedulesRefs,
                          currentColumn: 'id',
                          referencedColumn: 'profile_id',
                          typedResults: items,
                        ),
                      if (doseRecordsRefs)
                        await $_getPrefetchedDataByColumns<
                          ProfileEntry,
                          $ProfilesTable,
                          DoseRecordEntry,
                          $DoseRecordsTable
                        >(
                          currentTable: table,
                          referencedTable: $$ProfilesTableReferences
                              ._doseRecordsRefsTable(db),
                          managerFromTypedResult: (p0) =>
                              $$ProfilesTableReferences(
                                db,
                                table,
                                p0,
                              ).doseRecordsRefs,
                          currentColumn: 'id',
                          referencedColumn: 'profile_id',
                          typedResults: items,
                        ),
                    ];
                  },
                );
              },
        ),
      );
}

typedef $$ProfilesTableProcessedTableManager =
    ProcessedTableManager<
      _$AppDatabase,
      $ProfilesTable,
      ProfileEntry,
      $$ProfilesTableFilterComposer,
      $$ProfilesTableOrderingComposer,
      $$ProfilesTableAnnotationComposer,
      $$ProfilesTableCreateCompanionBuilder,
      $$ProfilesTableUpdateCompanionBuilder,
      (ProfileEntry, $$ProfilesTableReferences),
      ProfileEntry,
      PrefetchHooks Function({
        bool profileSettingsRefs,
        bool medicinesRefs,
        bool schedulesRefs,
        bool doseRecordsRefs,
      })
    >;
typedef $$ProfileSettingsTableCreateCompanionBuilder =
    ProfileSettingsCompanion Function({
      Value<int> profileId,
      Value<int> gracePeriodMinutes,
      Value<int> defaultSnoozeMinutes,
      Value<String> notificationSound,
      Value<bool> vibrate,
    });
typedef $$ProfileSettingsTableUpdateCompanionBuilder =
    ProfileSettingsCompanion Function({
      Value<int> profileId,
      Value<int> gracePeriodMinutes,
      Value<int> defaultSnoozeMinutes,
      Value<String> notificationSound,
      Value<bool> vibrate,
    });

final class $$ProfileSettingsTableReferences
    extends
        BaseReferences<
          _$AppDatabase,
          $ProfileSettingsTable,
          ProfileSettingsEntry
        > {
  $$ProfileSettingsTableReferences(
    super.$_db,
    super.$_table,
    super.$_typedResult,
  );

  static $ProfilesTable _profileIdTable(_$AppDatabase db) =>
      db.profiles.createAlias('profile_settings__profile_id__profiles__id');

  $$ProfilesTableProcessedTableManager get profileId {
    final $_column = $_itemColumn<int>('profile_id')!;

    final manager = $$ProfilesTableTableManager(
      $_db,
      $_db.profiles,
    ).filter((f) => f.id.sqlEquals($_column));
    final item = $_typedResult.readTableOrNull(_profileIdTable($_db));
    if (item == null) return manager;
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: [item]),
    );
  }
}

class $$ProfileSettingsTableFilterComposer
    extends Composer<_$AppDatabase, $ProfileSettingsTable> {
  $$ProfileSettingsTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get gracePeriodMinutes => $composableBuilder(
    column: $table.gracePeriodMinutes,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get defaultSnoozeMinutes => $composableBuilder(
    column: $table.defaultSnoozeMinutes,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get notificationSound => $composableBuilder(
    column: $table.notificationSound,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<bool> get vibrate => $composableBuilder(
    column: $table.vibrate,
    builder: (column) => ColumnFilters(column),
  );

  $$ProfilesTableFilterComposer get profileId {
    final $$ProfilesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableFilterComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$ProfileSettingsTableOrderingComposer
    extends Composer<_$AppDatabase, $ProfileSettingsTable> {
  $$ProfileSettingsTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get gracePeriodMinutes => $composableBuilder(
    column: $table.gracePeriodMinutes,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get defaultSnoozeMinutes => $composableBuilder(
    column: $table.defaultSnoozeMinutes,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get notificationSound => $composableBuilder(
    column: $table.notificationSound,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<bool> get vibrate => $composableBuilder(
    column: $table.vibrate,
    builder: (column) => ColumnOrderings(column),
  );

  $$ProfilesTableOrderingComposer get profileId {
    final $$ProfilesTableOrderingComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableOrderingComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$ProfileSettingsTableAnnotationComposer
    extends Composer<_$AppDatabase, $ProfileSettingsTable> {
  $$ProfileSettingsTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get gracePeriodMinutes => $composableBuilder(
    column: $table.gracePeriodMinutes,
    builder: (column) => column,
  );

  GeneratedColumn<int> get defaultSnoozeMinutes => $composableBuilder(
    column: $table.defaultSnoozeMinutes,
    builder: (column) => column,
  );

  GeneratedColumn<String> get notificationSound => $composableBuilder(
    column: $table.notificationSound,
    builder: (column) => column,
  );

  GeneratedColumn<bool> get vibrate =>
      $composableBuilder(column: $table.vibrate, builder: (column) => column);

  $$ProfilesTableAnnotationComposer get profileId {
    final $$ProfilesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableAnnotationComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$ProfileSettingsTableTableManager
    extends
        RootTableManager<
          _$AppDatabase,
          $ProfileSettingsTable,
          ProfileSettingsEntry,
          $$ProfileSettingsTableFilterComposer,
          $$ProfileSettingsTableOrderingComposer,
          $$ProfileSettingsTableAnnotationComposer,
          $$ProfileSettingsTableCreateCompanionBuilder,
          $$ProfileSettingsTableUpdateCompanionBuilder,
          (ProfileSettingsEntry, $$ProfileSettingsTableReferences),
          ProfileSettingsEntry,
          PrefetchHooks Function({bool profileId})
        > {
  $$ProfileSettingsTableTableManager(
    _$AppDatabase db,
    $ProfileSettingsTable table,
  ) : super(
        TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$ProfileSettingsTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$ProfileSettingsTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$ProfileSettingsTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback:
              ({
                Value<int> profileId = const Value.absent(),
                Value<int> gracePeriodMinutes = const Value.absent(),
                Value<int> defaultSnoozeMinutes = const Value.absent(),
                Value<String> notificationSound = const Value.absent(),
                Value<bool> vibrate = const Value.absent(),
              }) => ProfileSettingsCompanion(
                profileId: profileId,
                gracePeriodMinutes: gracePeriodMinutes,
                defaultSnoozeMinutes: defaultSnoozeMinutes,
                notificationSound: notificationSound,
                vibrate: vibrate,
              ),
          createCompanionCallback:
              ({
                Value<int> profileId = const Value.absent(),
                Value<int> gracePeriodMinutes = const Value.absent(),
                Value<int> defaultSnoozeMinutes = const Value.absent(),
                Value<String> notificationSound = const Value.absent(),
                Value<bool> vibrate = const Value.absent(),
              }) => ProfileSettingsCompanion.insert(
                profileId: profileId,
                gracePeriodMinutes: gracePeriodMinutes,
                defaultSnoozeMinutes: defaultSnoozeMinutes,
                notificationSound: notificationSound,
                vibrate: vibrate,
              ),
          withReferenceMapper: (p0) => p0
              .map(
                (e) => (
                  e.readTable<$ProfileSettingsTable, ProfileSettingsEntry>(
                    table,
                  ),
                  $$ProfileSettingsTableReferences(db, table, e),
                ),
              )
              .toList(),
          prefetchHooksCallback: ({profileId = false}) {
            return PrefetchHooks(
              db: db,
              explicitlyWatchedTables: [],
              addJoins:
                  <
                    T extends TableManagerState<
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic,
                      dynamic
                    >
                  >(state) {
                    if (profileId) {
                      state = state.withJoin(
                        currentTable: table,
                        currentColumn: table.profileId,
                        referencedTable: $$ProfileSettingsTableReferences
                            ._profileIdTable(db),
                        referencedColumn: $$ProfileSettingsTableReferences
                            ._profileIdTable(db)
                            .id,
                      ) as T;
                    }

                    return state;
                  },
              getPrefetchedDataCallback: (items) async {
                return [];
              },
            );
          },
        ),
      );
}

typedef $$ProfileSettingsTableProcessedTableManager =
    ProcessedTableManager<
      _$AppDatabase,
      $ProfileSettingsTable,
      ProfileSettingsEntry,
      $$ProfileSettingsTableFilterComposer,
      $$ProfileSettingsTableOrderingComposer,
      $$ProfileSettingsTableAnnotationComposer,
      $$ProfileSettingsTableCreateCompanionBuilder,
      $$ProfileSettingsTableUpdateCompanionBuilder,
      (ProfileSettingsEntry, $$ProfileSettingsTableReferences),
      ProfileSettingsEntry,
      PrefetchHooks Function({bool profileId})
    >;
typedef $$AppSettingsTableCreateCompanionBuilder =
    AppSettingsCompanion Function({
      required String key,
      required String value,
      Value<int> rowid,
    });
typedef $$AppSettingsTableUpdateCompanionBuilder =
    AppSettingsCompanion Function({
      Value<String> key,
      Value<String> value,
      Value<int> rowid,
    });

class $$AppSettingsTableFilterComposer
    extends Composer<_$AppDatabase, $AppSettingsTable> {
  $$AppSettingsTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<String> get key => $composableBuilder(
    column: $table.key,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get value => $composableBuilder(
    column: $table.value,
    builder: (column) => ColumnFilters(column),
  );
}

class $$AppSettingsTableOrderingComposer
    extends Composer<_$AppDatabase, $AppSettingsTable> {
  $$AppSettingsTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<String> get key => $composableBuilder(
    column: $table.key,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get value => $composableBuilder(
    column: $table.value,
    builder: (column) => ColumnOrderings(column),
  );
}

class $$AppSettingsTableAnnotationComposer
    extends Composer<_$AppDatabase, $AppSettingsTable> {
  $$AppSettingsTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<String> get key =>
      $composableBuilder(column: $table.key, builder: (column) => column);

  GeneratedColumn<String> get value =>
      $composableBuilder(column: $table.value, builder: (column) => column);
}

class $$AppSettingsTableTableManager
    extends
        RootTableManager<
          _$AppDatabase,
          $AppSettingsTable,
          AppSettingsEntry,
          $$AppSettingsTableFilterComposer,
          $$AppSettingsTableOrderingComposer,
          $$AppSettingsTableAnnotationComposer,
          $$AppSettingsTableCreateCompanionBuilder,
          $$AppSettingsTableUpdateCompanionBuilder,
          (
            AppSettingsEntry,
            BaseReferences<_$AppDatabase, $AppSettingsTable, AppSettingsEntry>,
          ),
          AppSettingsEntry,
          PrefetchHooks Function()
        > {
  $$AppSettingsTableTableManager(_$AppDatabase db, $AppSettingsTable table)
    : super(
        TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$AppSettingsTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$AppSettingsTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$AppSettingsTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback: ({
            Value<String> key = const Value.absent(),
            Value<String> value = const Value.absent(),
            Value<int> rowid = const Value.absent(),
          }) => AppSettingsCompanion(key: key, value: value, rowid: rowid),
          createCompanionCallback:
              ({
                required String key,
                required String value,
                Value<int> rowid = const Value.absent(),
              }) => AppSettingsCompanion.insert(
                key: key,
                value: value,
                rowid: rowid,
              ),
          withReferenceMapper: (p0) => p0
              .map(
                (e) => (
                  e.readTable<$AppSettingsTable, AppSettingsEntry>(table),
                  BaseReferences<
                    _$AppDatabase,
                    $AppSettingsTable,
                    AppSettingsEntry
                  >(db, table, e),
                ),
              )
              .toList(),
          prefetchHooksCallback: null,
        ),
      );
}

typedef $$AppSettingsTableProcessedTableManager =
    ProcessedTableManager<
      _$AppDatabase,
      $AppSettingsTable,
      AppSettingsEntry,
      $$AppSettingsTableFilterComposer,
      $$AppSettingsTableOrderingComposer,
      $$AppSettingsTableAnnotationComposer,
      $$AppSettingsTableCreateCompanionBuilder,
      $$AppSettingsTableUpdateCompanionBuilder,
      (
        AppSettingsEntry,
        BaseReferences<_$AppDatabase, $AppSettingsTable, AppSettingsEntry>,
      ),
      AppSettingsEntry,
      PrefetchHooks Function()
    >;
typedef $$MedicinesTableCreateCompanionBuilder = MedicinesCompanion Function({
  Value<int> id,
  required int profileId,
  required String name,
  Value<double> dosageAmount,
  Value<String> dosageUnit,
  Value<String> form,
  Value<String?> instructions,
  Value<int> colorTag,
  Value<int> stockCount,
  Value<int?> lowStockThreshold,
  Value<bool> isArchived,
  Value<DateTime> createdAt,
});
typedef $$MedicinesTableUpdateCompanionBuilder = MedicinesCompanion Function({
  Value<int> id,
  Value<int> profileId,
  Value<String> name,
  Value<double> dosageAmount,
  Value<String> dosageUnit,
  Value<String> form,
  Value<String?> instructions,
  Value<int> colorTag,
  Value<int> stockCount,
  Value<int?> lowStockThreshold,
  Value<bool> isArchived,
  Value<DateTime> createdAt,
});

final class $$MedicinesTableReferences
    extends BaseReferences<_$AppDatabase, $MedicinesTable, MedicineEntry> {
  $$MedicinesTableReferences(super.$_db, super.$_table, super.$_typedResult);

  static $ProfilesTable _profileIdTable(_$AppDatabase db) =>
      db.profiles.createAlias('medicines__profile_id__profiles__id');

  $$ProfilesTableProcessedTableManager get profileId {
    final $_column = $_itemColumn<int>('profile_id')!;

    final manager = $$ProfilesTableTableManager(
      $_db,
      $_db.profiles,
    ).filter((f) => f.id.sqlEquals($_column));
    final item = $_typedResult.readTableOrNull(_profileIdTable($_db));
    if (item == null) return manager;
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: [item]),
    );
  }

  static MultiTypedResultKey<$SchedulesTable, List<ScheduleEntry>>
  _schedulesRefsTable(_$AppDatabase db) => MultiTypedResultKey.fromTable(
    db.schedules,
    aliasName: 'medicines__id__schedules__medicine_id',
  );

  $$SchedulesTableProcessedTableManager get schedulesRefs {
    final manager = $$SchedulesTableTableManager(
      $_db,
      $_db.schedules,
    ).filter((f) => f.medicineId.id.sqlEquals($_itemColumn<int>('id')!));

    final cache = $_typedResult.readTableOrNull(_schedulesRefsTable($_db));
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: cache),
    );
  }

  static MultiTypedResultKey<$DoseRecordsTable, List<DoseRecordEntry>>
  _doseRecordsRefsTable(_$AppDatabase db) => MultiTypedResultKey.fromTable(
    db.doseRecords,
    aliasName: 'medicines__id__dose_records__medicine_id',
  );

  $$DoseRecordsTableProcessedTableManager get doseRecordsRefs {
    final manager = $$DoseRecordsTableTableManager(
      $_db,
      $_db.doseRecords,
    ).filter((f) => f.medicineId.id.sqlEquals($_itemColumn<int>('id')!));

    final cache = $_typedResult.readTableOrNull(_doseRecordsRefsTable($_db));
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: cache),
    );
  }
}

class $$MedicinesTableFilterComposer
    extends Composer<_$AppDatabase, $MedicinesTable> {
  $$MedicinesTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get name => $composableBuilder(
    column: $table.name,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<double> get dosageAmount => $composableBuilder(
    column: $table.dosageAmount,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get dosageUnit => $composableBuilder(
    column: $table.dosageUnit,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get form => $composableBuilder(
    column: $table.form,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get instructions => $composableBuilder(
    column: $table.instructions,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get colorTag => $composableBuilder(
    column: $table.colorTag,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get stockCount => $composableBuilder(
    column: $table.stockCount,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get lowStockThreshold => $composableBuilder(
    column: $table.lowStockThreshold,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<bool> get isArchived => $composableBuilder(
    column: $table.isArchived,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnFilters(column),
  );

  $$ProfilesTableFilterComposer get profileId {
    final $$ProfilesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableFilterComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  Expression<bool> schedulesRefs(
    Expression<bool> Function($$SchedulesTableFilterComposer f) f,
  ) {
    final $$SchedulesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.schedules,
      getReferencedColumn: (t) => t.medicineId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$SchedulesTableFilterComposer(
            $db: $db,
            $table: $db.schedules,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<bool> doseRecordsRefs(
    Expression<bool> Function($$DoseRecordsTableFilterComposer f) f,
  ) {
    final $$DoseRecordsTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.doseRecords,
      getReferencedColumn: (t) => t.medicineId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$DoseRecordsTableFilterComposer(
            $db: $db,
            $table: $db.doseRecords,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }
}

class $$MedicinesTableOrderingComposer
    extends Composer<_$AppDatabase, $MedicinesTable> {
  $$MedicinesTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get name => $composableBuilder(
    column: $table.name,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<double> get dosageAmount => $composableBuilder(
    column: $table.dosageAmount,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get dosageUnit => $composableBuilder(
    column: $table.dosageUnit,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get form => $composableBuilder(
    column: $table.form,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get instructions => $composableBuilder(
    column: $table.instructions,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get colorTag => $composableBuilder(
    column: $table.colorTag,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get stockCount => $composableBuilder(
    column: $table.stockCount,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get lowStockThreshold => $composableBuilder(
    column: $table.lowStockThreshold,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<bool> get isArchived => $composableBuilder(
    column: $table.isArchived,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnOrderings(column),
  );

  $$ProfilesTableOrderingComposer get profileId {
    final $$ProfilesTableOrderingComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableOrderingComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$MedicinesTableAnnotationComposer
    extends Composer<_$AppDatabase, $MedicinesTable> {
  $$MedicinesTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get id =>
      $composableBuilder(column: $table.id, builder: (column) => column);

  GeneratedColumn<String> get name =>
      $composableBuilder(column: $table.name, builder: (column) => column);

  GeneratedColumn<double> get dosageAmount => $composableBuilder(
    column: $table.dosageAmount,
    builder: (column) => column,
  );

  GeneratedColumn<String> get dosageUnit => $composableBuilder(
    column: $table.dosageUnit,
    builder: (column) => column,
  );

  GeneratedColumn<String> get form =>
      $composableBuilder(column: $table.form, builder: (column) => column);

  GeneratedColumn<String> get instructions => $composableBuilder(
    column: $table.instructions,
    builder: (column) => column,
  );

  GeneratedColumn<int> get colorTag =>
      $composableBuilder(column: $table.colorTag, builder: (column) => column);

  GeneratedColumn<int> get stockCount => $composableBuilder(
    column: $table.stockCount,
    builder: (column) => column,
  );

  GeneratedColumn<int> get lowStockThreshold => $composableBuilder(
    column: $table.lowStockThreshold,
    builder: (column) => column,
  );

  GeneratedColumn<bool> get isArchived => $composableBuilder(
    column: $table.isArchived,
    builder: (column) => column,
  );

  GeneratedColumn<DateTime> get createdAt =>
      $composableBuilder(column: $table.createdAt, builder: (column) => column);

  $$ProfilesTableAnnotationComposer get profileId {
    final $$ProfilesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableAnnotationComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  Expression<T> schedulesRefs<T extends Object>(
    Expression<T> Function($$SchedulesTableAnnotationComposer a) f,
  ) {
    final $$SchedulesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.schedules,
      getReferencedColumn: (t) => t.medicineId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$SchedulesTableAnnotationComposer(
            $db: $db,
            $table: $db.schedules,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }

  Expression<T> doseRecordsRefs<T extends Object>(
    Expression<T> Function($$DoseRecordsTableAnnotationComposer a) f,
  ) {
    final $$DoseRecordsTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.doseRecords,
      getReferencedColumn: (t) => t.medicineId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$DoseRecordsTableAnnotationComposer(
            $db: $db,
            $table: $db.doseRecords,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }
}

class $$MedicinesTableTableManager
    extends
        RootTableManager<
          _$AppDatabase,
          $MedicinesTable,
          MedicineEntry,
          $$MedicinesTableFilterComposer,
          $$MedicinesTableOrderingComposer,
          $$MedicinesTableAnnotationComposer,
          $$MedicinesTableCreateCompanionBuilder,
          $$MedicinesTableUpdateCompanionBuilder,
          (MedicineEntry, $$MedicinesTableReferences),
          MedicineEntry,
          PrefetchHooks Function({
            bool profileId,
            bool schedulesRefs,
            bool doseRecordsRefs,
          })
        > {
  $$MedicinesTableTableManager(_$AppDatabase db, $MedicinesTable table)
    : super(
        TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$MedicinesTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$MedicinesTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$MedicinesTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                Value<int> profileId = const Value.absent(),
                Value<String> name = const Value.absent(),
                Value<double> dosageAmount = const Value.absent(),
                Value<String> dosageUnit = const Value.absent(),
                Value<String> form = const Value.absent(),
                Value<String?> instructions = const Value.absent(),
                Value<int> colorTag = const Value.absent(),
                Value<int> stockCount = const Value.absent(),
                Value<int?> lowStockThreshold = const Value.absent(),
                Value<bool> isArchived = const Value.absent(),
                Value<DateTime> createdAt = const Value.absent(),
              }) => MedicinesCompanion(
                id: id,
                profileId: profileId,
                name: name,
                dosageAmount: dosageAmount,
                dosageUnit: dosageUnit,
                form: form,
                instructions: instructions,
                colorTag: colorTag,
                stockCount: stockCount,
                lowStockThreshold: lowStockThreshold,
                isArchived: isArchived,
                createdAt: createdAt,
              ),
          createCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                required int profileId,
                required String name,
                Value<double> dosageAmount = const Value.absent(),
                Value<String> dosageUnit = const Value.absent(),
                Value<String> form = const Value.absent(),
                Value<String?> instructions = const Value.absent(),
                Value<int> colorTag = const Value.absent(),
                Value<int> stockCount = const Value.absent(),
                Value<int?> lowStockThreshold = const Value.absent(),
                Value<bool> isArchived = const Value.absent(),
                Value<DateTime> createdAt = const Value.absent(),
              }) => MedicinesCompanion.insert(
                id: id,
                profileId: profileId,
                name: name,
                dosageAmount: dosageAmount,
                dosageUnit: dosageUnit,
                form: form,
                instructions: instructions,
                colorTag: colorTag,
                stockCount: stockCount,
                lowStockThreshold: lowStockThreshold,
                isArchived: isArchived,
                createdAt: createdAt,
              ),
          withReferenceMapper: (p0) => p0
              .map(
                (e) => (
                  e.readTable<$MedicinesTable, MedicineEntry>(table),
                  $$MedicinesTableReferences(db, table, e),
                ),
              )
              .toList(),
          prefetchHooksCallback:
              ({
                profileId = false,
                schedulesRefs = false,
                doseRecordsRefs = false,
              }) {
                return PrefetchHooks(
                  db: db,
                  explicitlyWatchedTables: [
                    if (schedulesRefs) db.schedules,
                    if (doseRecordsRefs) db.doseRecords,
                  ],
                  addJoins:
                      <
                        T extends TableManagerState<
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic
                        >
                      >(state) {
                        if (profileId) {
                          state = state.withJoin(
                            currentTable: table,
                            currentColumn: table.profileId,
                            referencedTable: $$MedicinesTableReferences
                                ._profileIdTable(db),
                            referencedColumn: $$MedicinesTableReferences
                                ._profileIdTable(db)
                                .id,
                          ) as T;
                        }

                        return state;
                      },
                  getPrefetchedDataCallback: (items) async {
                    return [
                      if (schedulesRefs)
                        await $_getPrefetchedDataByColumns<
                          MedicineEntry,
                          $MedicinesTable,
                          ScheduleEntry,
                          $SchedulesTable
                        >(
                          currentTable: table,
                          referencedTable: $$MedicinesTableReferences
                              ._schedulesRefsTable(db),
                          managerFromTypedResult: (p0) =>
                              $$MedicinesTableReferences(
                                db,
                                table,
                                p0,
                              ).schedulesRefs,
                          currentColumn: 'id',
                          referencedColumn: 'medicine_id',
                          typedResults: items,
                        ),
                      if (doseRecordsRefs)
                        await $_getPrefetchedDataByColumns<
                          MedicineEntry,
                          $MedicinesTable,
                          DoseRecordEntry,
                          $DoseRecordsTable
                        >(
                          currentTable: table,
                          referencedTable: $$MedicinesTableReferences
                              ._doseRecordsRefsTable(db),
                          managerFromTypedResult: (p0) =>
                              $$MedicinesTableReferences(
                                db,
                                table,
                                p0,
                              ).doseRecordsRefs,
                          currentColumn: 'id',
                          referencedColumn: 'medicine_id',
                          typedResults: items,
                        ),
                    ];
                  },
                );
              },
        ),
      );
}

typedef $$MedicinesTableProcessedTableManager =
    ProcessedTableManager<
      _$AppDatabase,
      $MedicinesTable,
      MedicineEntry,
      $$MedicinesTableFilterComposer,
      $$MedicinesTableOrderingComposer,
      $$MedicinesTableAnnotationComposer,
      $$MedicinesTableCreateCompanionBuilder,
      $$MedicinesTableUpdateCompanionBuilder,
      (MedicineEntry, $$MedicinesTableReferences),
      MedicineEntry,
      PrefetchHooks Function({
        bool profileId,
        bool schedulesRefs,
        bool doseRecordsRefs,
      })
    >;
typedef $$SchedulesTableCreateCompanionBuilder = SchedulesCompanion Function({
  Value<int> id,
  required int medicineId,
  required int profileId,
  required String scheduleType,
  required int timeHour,
  required int timeMinute,
  Value<String?> daysOfWeek,
  Value<int?> intervalDays,
  required DateTime startDate,
  Value<DateTime?> endDate,
  Value<bool> isActive,
  Value<DateTime> createdAt,
});
typedef $$SchedulesTableUpdateCompanionBuilder = SchedulesCompanion Function({
  Value<int> id,
  Value<int> medicineId,
  Value<int> profileId,
  Value<String> scheduleType,
  Value<int> timeHour,
  Value<int> timeMinute,
  Value<String?> daysOfWeek,
  Value<int?> intervalDays,
  Value<DateTime> startDate,
  Value<DateTime?> endDate,
  Value<bool> isActive,
  Value<DateTime> createdAt,
});

final class $$SchedulesTableReferences
    extends BaseReferences<_$AppDatabase, $SchedulesTable, ScheduleEntry> {
  $$SchedulesTableReferences(super.$_db, super.$_table, super.$_typedResult);

  static $MedicinesTable _medicineIdTable(_$AppDatabase db) =>
      db.medicines.createAlias('schedules__medicine_id__medicines__id');

  $$MedicinesTableProcessedTableManager get medicineId {
    final $_column = $_itemColumn<int>('medicine_id')!;

    final manager = $$MedicinesTableTableManager(
      $_db,
      $_db.medicines,
    ).filter((f) => f.id.sqlEquals($_column));
    final item = $_typedResult.readTableOrNull(_medicineIdTable($_db));
    if (item == null) return manager;
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: [item]),
    );
  }

  static $ProfilesTable _profileIdTable(_$AppDatabase db) =>
      db.profiles.createAlias('schedules__profile_id__profiles__id');

  $$ProfilesTableProcessedTableManager get profileId {
    final $_column = $_itemColumn<int>('profile_id')!;

    final manager = $$ProfilesTableTableManager(
      $_db,
      $_db.profiles,
    ).filter((f) => f.id.sqlEquals($_column));
    final item = $_typedResult.readTableOrNull(_profileIdTable($_db));
    if (item == null) return manager;
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: [item]),
    );
  }

  static MultiTypedResultKey<$DoseRecordsTable, List<DoseRecordEntry>>
  _doseRecordsRefsTable(_$AppDatabase db) => MultiTypedResultKey.fromTable(
    db.doseRecords,
    aliasName: 'schedules__id__dose_records__schedule_id',
  );

  $$DoseRecordsTableProcessedTableManager get doseRecordsRefs {
    final manager = $$DoseRecordsTableTableManager(
      $_db,
      $_db.doseRecords,
    ).filter((f) => f.scheduleId.id.sqlEquals($_itemColumn<int>('id')!));

    final cache = $_typedResult.readTableOrNull(_doseRecordsRefsTable($_db));
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: cache),
    );
  }
}

class $$SchedulesTableFilterComposer
    extends Composer<_$AppDatabase, $SchedulesTable> {
  $$SchedulesTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get scheduleType => $composableBuilder(
    column: $table.scheduleType,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get timeHour => $composableBuilder(
    column: $table.timeHour,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get timeMinute => $composableBuilder(
    column: $table.timeMinute,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get daysOfWeek => $composableBuilder(
    column: $table.daysOfWeek,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get intervalDays => $composableBuilder(
    column: $table.intervalDays,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get startDate => $composableBuilder(
    column: $table.startDate,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get endDate => $composableBuilder(
    column: $table.endDate,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<bool> get isActive => $composableBuilder(
    column: $table.isActive,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnFilters(column),
  );

  $$MedicinesTableFilterComposer get medicineId {
    final $$MedicinesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.medicineId,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableFilterComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$ProfilesTableFilterComposer get profileId {
    final $$ProfilesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableFilterComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  Expression<bool> doseRecordsRefs(
    Expression<bool> Function($$DoseRecordsTableFilterComposer f) f,
  ) {
    final $$DoseRecordsTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.doseRecords,
      getReferencedColumn: (t) => t.scheduleId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$DoseRecordsTableFilterComposer(
            $db: $db,
            $table: $db.doseRecords,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }
}

class $$SchedulesTableOrderingComposer
    extends Composer<_$AppDatabase, $SchedulesTable> {
  $$SchedulesTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get scheduleType => $composableBuilder(
    column: $table.scheduleType,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get timeHour => $composableBuilder(
    column: $table.timeHour,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get timeMinute => $composableBuilder(
    column: $table.timeMinute,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get daysOfWeek => $composableBuilder(
    column: $table.daysOfWeek,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get intervalDays => $composableBuilder(
    column: $table.intervalDays,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get startDate => $composableBuilder(
    column: $table.startDate,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get endDate => $composableBuilder(
    column: $table.endDate,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<bool> get isActive => $composableBuilder(
    column: $table.isActive,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnOrderings(column),
  );

  $$MedicinesTableOrderingComposer get medicineId {
    final $$MedicinesTableOrderingComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.medicineId,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableOrderingComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$ProfilesTableOrderingComposer get profileId {
    final $$ProfilesTableOrderingComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableOrderingComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$SchedulesTableAnnotationComposer
    extends Composer<_$AppDatabase, $SchedulesTable> {
  $$SchedulesTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get id =>
      $composableBuilder(column: $table.id, builder: (column) => column);

  GeneratedColumn<String> get scheduleType => $composableBuilder(
    column: $table.scheduleType,
    builder: (column) => column,
  );

  GeneratedColumn<int> get timeHour =>
      $composableBuilder(column: $table.timeHour, builder: (column) => column);

  GeneratedColumn<int> get timeMinute => $composableBuilder(
    column: $table.timeMinute,
    builder: (column) => column,
  );

  GeneratedColumn<String> get daysOfWeek => $composableBuilder(
    column: $table.daysOfWeek,
    builder: (column) => column,
  );

  GeneratedColumn<int> get intervalDays => $composableBuilder(
    column: $table.intervalDays,
    builder: (column) => column,
  );

  GeneratedColumn<DateTime> get startDate =>
      $composableBuilder(column: $table.startDate, builder: (column) => column);

  GeneratedColumn<DateTime> get endDate =>
      $composableBuilder(column: $table.endDate, builder: (column) => column);

  GeneratedColumn<bool> get isActive =>
      $composableBuilder(column: $table.isActive, builder: (column) => column);

  GeneratedColumn<DateTime> get createdAt =>
      $composableBuilder(column: $table.createdAt, builder: (column) => column);

  $$MedicinesTableAnnotationComposer get medicineId {
    final $$MedicinesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.medicineId,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableAnnotationComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$ProfilesTableAnnotationComposer get profileId {
    final $$ProfilesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableAnnotationComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  Expression<T> doseRecordsRefs<T extends Object>(
    Expression<T> Function($$DoseRecordsTableAnnotationComposer a) f,
  ) {
    final $$DoseRecordsTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.id,
      referencedTable: $db.doseRecords,
      getReferencedColumn: (t) => t.scheduleId,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$DoseRecordsTableAnnotationComposer(
            $db: $db,
            $table: $db.doseRecords,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return f(composer);
  }
}

class $$SchedulesTableTableManager
    extends
        RootTableManager<
          _$AppDatabase,
          $SchedulesTable,
          ScheduleEntry,
          $$SchedulesTableFilterComposer,
          $$SchedulesTableOrderingComposer,
          $$SchedulesTableAnnotationComposer,
          $$SchedulesTableCreateCompanionBuilder,
          $$SchedulesTableUpdateCompanionBuilder,
          (ScheduleEntry, $$SchedulesTableReferences),
          ScheduleEntry,
          PrefetchHooks Function({
            bool medicineId,
            bool profileId,
            bool doseRecordsRefs,
          })
        > {
  $$SchedulesTableTableManager(_$AppDatabase db, $SchedulesTable table)
    : super(
        TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$SchedulesTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$SchedulesTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$SchedulesTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                Value<int> medicineId = const Value.absent(),
                Value<int> profileId = const Value.absent(),
                Value<String> scheduleType = const Value.absent(),
                Value<int> timeHour = const Value.absent(),
                Value<int> timeMinute = const Value.absent(),
                Value<String?> daysOfWeek = const Value.absent(),
                Value<int?> intervalDays = const Value.absent(),
                Value<DateTime> startDate = const Value.absent(),
                Value<DateTime?> endDate = const Value.absent(),
                Value<bool> isActive = const Value.absent(),
                Value<DateTime> createdAt = const Value.absent(),
              }) => SchedulesCompanion(
                id: id,
                medicineId: medicineId,
                profileId: profileId,
                scheduleType: scheduleType,
                timeHour: timeHour,
                timeMinute: timeMinute,
                daysOfWeek: daysOfWeek,
                intervalDays: intervalDays,
                startDate: startDate,
                endDate: endDate,
                isActive: isActive,
                createdAt: createdAt,
              ),
          createCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                required int medicineId,
                required int profileId,
                required String scheduleType,
                required int timeHour,
                required int timeMinute,
                Value<String?> daysOfWeek = const Value.absent(),
                Value<int?> intervalDays = const Value.absent(),
                required DateTime startDate,
                Value<DateTime?> endDate = const Value.absent(),
                Value<bool> isActive = const Value.absent(),
                Value<DateTime> createdAt = const Value.absent(),
              }) => SchedulesCompanion.insert(
                id: id,
                medicineId: medicineId,
                profileId: profileId,
                scheduleType: scheduleType,
                timeHour: timeHour,
                timeMinute: timeMinute,
                daysOfWeek: daysOfWeek,
                intervalDays: intervalDays,
                startDate: startDate,
                endDate: endDate,
                isActive: isActive,
                createdAt: createdAt,
              ),
          withReferenceMapper: (p0) => p0
              .map(
                (e) => (
                  e.readTable<$SchedulesTable, ScheduleEntry>(table),
                  $$SchedulesTableReferences(db, table, e),
                ),
              )
              .toList(),
          prefetchHooksCallback:
              ({
                medicineId = false,
                profileId = false,
                doseRecordsRefs = false,
              }) {
                return PrefetchHooks(
                  db: db,
                  explicitlyWatchedTables: [
                    if (doseRecordsRefs) db.doseRecords,
                  ],
                  addJoins:
                      <
                        T extends TableManagerState<
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic
                        >
                      >(state) {
                        if (medicineId) {
                          state = state.withJoin(
                            currentTable: table,
                            currentColumn: table.medicineId,
                            referencedTable: $$SchedulesTableReferences
                                ._medicineIdTable(db),
                            referencedColumn: $$SchedulesTableReferences
                                ._medicineIdTable(db)
                                .id,
                          ) as T;
                        }
                        if (profileId) {
                          state = state.withJoin(
                            currentTable: table,
                            currentColumn: table.profileId,
                            referencedTable: $$SchedulesTableReferences
                                ._profileIdTable(db),
                            referencedColumn: $$SchedulesTableReferences
                                ._profileIdTable(db)
                                .id,
                          ) as T;
                        }

                        return state;
                      },
                  getPrefetchedDataCallback: (items) async {
                    return [
                      if (doseRecordsRefs)
                        await $_getPrefetchedDataByColumns<
                          ScheduleEntry,
                          $SchedulesTable,
                          DoseRecordEntry,
                          $DoseRecordsTable
                        >(
                          currentTable: table,
                          referencedTable: $$SchedulesTableReferences
                              ._doseRecordsRefsTable(db),
                          managerFromTypedResult: (p0) =>
                              $$SchedulesTableReferences(
                                db,
                                table,
                                p0,
                              ).doseRecordsRefs,
                          currentColumn: 'id',
                          referencedColumn: 'schedule_id',
                          typedResults: items,
                        ),
                    ];
                  },
                );
              },
        ),
      );
}

typedef $$SchedulesTableProcessedTableManager =
    ProcessedTableManager<
      _$AppDatabase,
      $SchedulesTable,
      ScheduleEntry,
      $$SchedulesTableFilterComposer,
      $$SchedulesTableOrderingComposer,
      $$SchedulesTableAnnotationComposer,
      $$SchedulesTableCreateCompanionBuilder,
      $$SchedulesTableUpdateCompanionBuilder,
      (ScheduleEntry, $$SchedulesTableReferences),
      ScheduleEntry,
      PrefetchHooks Function({
        bool medicineId,
        bool profileId,
        bool doseRecordsRefs,
      })
    >;
typedef $$DoseRecordsTableCreateCompanionBuilder =
    DoseRecordsCompanion Function({
      Value<int> id,
      required int profileId,
      required int medicineId,
      Value<int?> scheduleId,
      required DateTime scheduledAt,
      required DateTime reminderAt,
      Value<String> status,
      Value<DateTime?> takenAt,
      Value<DateTime?> skippedAt,
      Value<String?> skipReason,
      Value<int> snoozeCount,
      required DateTime gracePeriodDeadline,
      required int notificationId,
      Value<DateTime> createdAt,
      Value<DateTime> updatedAt,
    });
typedef $$DoseRecordsTableUpdateCompanionBuilder =
    DoseRecordsCompanion Function({
      Value<int> id,
      Value<int> profileId,
      Value<int> medicineId,
      Value<int?> scheduleId,
      Value<DateTime> scheduledAt,
      Value<DateTime> reminderAt,
      Value<String> status,
      Value<DateTime?> takenAt,
      Value<DateTime?> skippedAt,
      Value<String?> skipReason,
      Value<int> snoozeCount,
      Value<DateTime> gracePeriodDeadline,
      Value<int> notificationId,
      Value<DateTime> createdAt,
      Value<DateTime> updatedAt,
    });

final class $$DoseRecordsTableReferences
    extends BaseReferences<_$AppDatabase, $DoseRecordsTable, DoseRecordEntry> {
  $$DoseRecordsTableReferences(super.$_db, super.$_table, super.$_typedResult);

  static $ProfilesTable _profileIdTable(_$AppDatabase db) =>
      db.profiles.createAlias('dose_records__profile_id__profiles__id');

  $$ProfilesTableProcessedTableManager get profileId {
    final $_column = $_itemColumn<int>('profile_id')!;

    final manager = $$ProfilesTableTableManager(
      $_db,
      $_db.profiles,
    ).filter((f) => f.id.sqlEquals($_column));
    final item = $_typedResult.readTableOrNull(_profileIdTable($_db));
    if (item == null) return manager;
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: [item]),
    );
  }

  static $MedicinesTable _medicineIdTable(_$AppDatabase db) =>
      db.medicines.createAlias('dose_records__medicine_id__medicines__id');

  $$MedicinesTableProcessedTableManager get medicineId {
    final $_column = $_itemColumn<int>('medicine_id')!;

    final manager = $$MedicinesTableTableManager(
      $_db,
      $_db.medicines,
    ).filter((f) => f.id.sqlEquals($_column));
    final item = $_typedResult.readTableOrNull(_medicineIdTable($_db));
    if (item == null) return manager;
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: [item]),
    );
  }

  static $SchedulesTable _scheduleIdTable(_$AppDatabase db) =>
      db.schedules.createAlias('dose_records__schedule_id__schedules__id');

  $$SchedulesTableProcessedTableManager? get scheduleId {
    final $_column = $_itemColumn<int>('schedule_id');
    if ($_column == null) return null;
    final manager = $$SchedulesTableTableManager(
      $_db,
      $_db.schedules,
    ).filter((f) => f.id.sqlEquals($_column));
    final item = $_typedResult.readTableOrNull(_scheduleIdTable($_db));
    if (item == null) return manager;
    return ProcessedTableManager(
      manager.$state.copyWith(prefetchedData: [item]),
    );
  }
}

class $$DoseRecordsTableFilterComposer
    extends Composer<_$AppDatabase, $DoseRecordsTable> {
  $$DoseRecordsTableFilterComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnFilters<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get scheduledAt => $composableBuilder(
    column: $table.scheduledAt,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get reminderAt => $composableBuilder(
    column: $table.reminderAt,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get status => $composableBuilder(
    column: $table.status,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get takenAt => $composableBuilder(
    column: $table.takenAt,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get skippedAt => $composableBuilder(
    column: $table.skippedAt,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<String> get skipReason => $composableBuilder(
    column: $table.skipReason,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get snoozeCount => $composableBuilder(
    column: $table.snoozeCount,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get gracePeriodDeadline => $composableBuilder(
    column: $table.gracePeriodDeadline,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<int> get notificationId => $composableBuilder(
    column: $table.notificationId,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnFilters(column),
  );

  ColumnFilters<DateTime> get updatedAt => $composableBuilder(
    column: $table.updatedAt,
    builder: (column) => ColumnFilters(column),
  );

  $$ProfilesTableFilterComposer get profileId {
    final $$ProfilesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableFilterComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$MedicinesTableFilterComposer get medicineId {
    final $$MedicinesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.medicineId,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableFilterComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$SchedulesTableFilterComposer get scheduleId {
    final $$SchedulesTableFilterComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.scheduleId,
      referencedTable: $db.schedules,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$SchedulesTableFilterComposer(
            $db: $db,
            $table: $db.schedules,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$DoseRecordsTableOrderingComposer
    extends Composer<_$AppDatabase, $DoseRecordsTable> {
  $$DoseRecordsTableOrderingComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  ColumnOrderings<int> get id => $composableBuilder(
    column: $table.id,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get scheduledAt => $composableBuilder(
    column: $table.scheduledAt,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get reminderAt => $composableBuilder(
    column: $table.reminderAt,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get status => $composableBuilder(
    column: $table.status,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get takenAt => $composableBuilder(
    column: $table.takenAt,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get skippedAt => $composableBuilder(
    column: $table.skippedAt,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<String> get skipReason => $composableBuilder(
    column: $table.skipReason,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get snoozeCount => $composableBuilder(
    column: $table.snoozeCount,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get gracePeriodDeadline => $composableBuilder(
    column: $table.gracePeriodDeadline,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<int> get notificationId => $composableBuilder(
    column: $table.notificationId,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get createdAt => $composableBuilder(
    column: $table.createdAt,
    builder: (column) => ColumnOrderings(column),
  );

  ColumnOrderings<DateTime> get updatedAt => $composableBuilder(
    column: $table.updatedAt,
    builder: (column) => ColumnOrderings(column),
  );

  $$ProfilesTableOrderingComposer get profileId {
    final $$ProfilesTableOrderingComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableOrderingComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$MedicinesTableOrderingComposer get medicineId {
    final $$MedicinesTableOrderingComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.medicineId,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableOrderingComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$SchedulesTableOrderingComposer get scheduleId {
    final $$SchedulesTableOrderingComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.scheduleId,
      referencedTable: $db.schedules,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$SchedulesTableOrderingComposer(
            $db: $db,
            $table: $db.schedules,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$DoseRecordsTableAnnotationComposer
    extends Composer<_$AppDatabase, $DoseRecordsTable> {
  $$DoseRecordsTableAnnotationComposer({
    required super.$db,
    required super.$table,
    super.joinBuilder,
    super.$addJoinBuilderToRootComposer,
    super.$removeJoinBuilderFromRootComposer,
  });
  GeneratedColumn<int> get id =>
      $composableBuilder(column: $table.id, builder: (column) => column);

  GeneratedColumn<DateTime> get scheduledAt => $composableBuilder(
    column: $table.scheduledAt,
    builder: (column) => column,
  );

  GeneratedColumn<DateTime> get reminderAt => $composableBuilder(
    column: $table.reminderAt,
    builder: (column) => column,
  );

  GeneratedColumn<String> get status =>
      $composableBuilder(column: $table.status, builder: (column) => column);

  GeneratedColumn<DateTime> get takenAt =>
      $composableBuilder(column: $table.takenAt, builder: (column) => column);

  GeneratedColumn<DateTime> get skippedAt =>
      $composableBuilder(column: $table.skippedAt, builder: (column) => column);

  GeneratedColumn<String> get skipReason => $composableBuilder(
    column: $table.skipReason,
    builder: (column) => column,
  );

  GeneratedColumn<int> get snoozeCount => $composableBuilder(
    column: $table.snoozeCount,
    builder: (column) => column,
  );

  GeneratedColumn<DateTime> get gracePeriodDeadline => $composableBuilder(
    column: $table.gracePeriodDeadline,
    builder: (column) => column,
  );

  GeneratedColumn<int> get notificationId => $composableBuilder(
    column: $table.notificationId,
    builder: (column) => column,
  );

  GeneratedColumn<DateTime> get createdAt =>
      $composableBuilder(column: $table.createdAt, builder: (column) => column);

  GeneratedColumn<DateTime> get updatedAt =>
      $composableBuilder(column: $table.updatedAt, builder: (column) => column);

  $$ProfilesTableAnnotationComposer get profileId {
    final $$ProfilesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.profileId,
      referencedTable: $db.profiles,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$ProfilesTableAnnotationComposer(
            $db: $db,
            $table: $db.profiles,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$MedicinesTableAnnotationComposer get medicineId {
    final $$MedicinesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.medicineId,
      referencedTable: $db.medicines,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$MedicinesTableAnnotationComposer(
            $db: $db,
            $table: $db.medicines,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }

  $$SchedulesTableAnnotationComposer get scheduleId {
    final $$SchedulesTableAnnotationComposer composer = $composerBuilder(
      composer: this,
      getCurrentColumn: (t) => t.scheduleId,
      referencedTable: $db.schedules,
      getReferencedColumn: (t) => t.id,
      builder:
          (
            joinBuilder, {
            $addJoinBuilderToRootComposer,
            $removeJoinBuilderFromRootComposer,
          }) => $$SchedulesTableAnnotationComposer(
            $db: $db,
            $table: $db.schedules,
            $addJoinBuilderToRootComposer: $addJoinBuilderToRootComposer,
            joinBuilder: joinBuilder,
            $removeJoinBuilderFromRootComposer:
                $removeJoinBuilderFromRootComposer,
          ),
    );
    return composer;
  }
}

class $$DoseRecordsTableTableManager
    extends
        RootTableManager<
          _$AppDatabase,
          $DoseRecordsTable,
          DoseRecordEntry,
          $$DoseRecordsTableFilterComposer,
          $$DoseRecordsTableOrderingComposer,
          $$DoseRecordsTableAnnotationComposer,
          $$DoseRecordsTableCreateCompanionBuilder,
          $$DoseRecordsTableUpdateCompanionBuilder,
          (DoseRecordEntry, $$DoseRecordsTableReferences),
          DoseRecordEntry,
          PrefetchHooks Function({
            bool profileId,
            bool medicineId,
            bool scheduleId,
          })
        > {
  $$DoseRecordsTableTableManager(_$AppDatabase db, $DoseRecordsTable table)
    : super(
        TableManagerState(
          db: db,
          table: table,
          createFilteringComposer: () =>
              $$DoseRecordsTableFilterComposer($db: db, $table: table),
          createOrderingComposer: () =>
              $$DoseRecordsTableOrderingComposer($db: db, $table: table),
          createComputedFieldComposer: () =>
              $$DoseRecordsTableAnnotationComposer($db: db, $table: table),
          updateCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                Value<int> profileId = const Value.absent(),
                Value<int> medicineId = const Value.absent(),
                Value<int?> scheduleId = const Value.absent(),
                Value<DateTime> scheduledAt = const Value.absent(),
                Value<DateTime> reminderAt = const Value.absent(),
                Value<String> status = const Value.absent(),
                Value<DateTime?> takenAt = const Value.absent(),
                Value<DateTime?> skippedAt = const Value.absent(),
                Value<String?> skipReason = const Value.absent(),
                Value<int> snoozeCount = const Value.absent(),
                Value<DateTime> gracePeriodDeadline = const Value.absent(),
                Value<int> notificationId = const Value.absent(),
                Value<DateTime> createdAt = const Value.absent(),
                Value<DateTime> updatedAt = const Value.absent(),
              }) => DoseRecordsCompanion(
                id: id,
                profileId: profileId,
                medicineId: medicineId,
                scheduleId: scheduleId,
                scheduledAt: scheduledAt,
                reminderAt: reminderAt,
                status: status,
                takenAt: takenAt,
                skippedAt: skippedAt,
                skipReason: skipReason,
                snoozeCount: snoozeCount,
                gracePeriodDeadline: gracePeriodDeadline,
                notificationId: notificationId,
                createdAt: createdAt,
                updatedAt: updatedAt,
              ),
          createCompanionCallback:
              ({
                Value<int> id = const Value.absent(),
                required int profileId,
                required int medicineId,
                Value<int?> scheduleId = const Value.absent(),
                required DateTime scheduledAt,
                required DateTime reminderAt,
                Value<String> status = const Value.absent(),
                Value<DateTime?> takenAt = const Value.absent(),
                Value<DateTime?> skippedAt = const Value.absent(),
                Value<String?> skipReason = const Value.absent(),
                Value<int> snoozeCount = const Value.absent(),
                required DateTime gracePeriodDeadline,
                required int notificationId,
                Value<DateTime> createdAt = const Value.absent(),
                Value<DateTime> updatedAt = const Value.absent(),
              }) => DoseRecordsCompanion.insert(
                id: id,
                profileId: profileId,
                medicineId: medicineId,
                scheduleId: scheduleId,
                scheduledAt: scheduledAt,
                reminderAt: reminderAt,
                status: status,
                takenAt: takenAt,
                skippedAt: skippedAt,
                skipReason: skipReason,
                snoozeCount: snoozeCount,
                gracePeriodDeadline: gracePeriodDeadline,
                notificationId: notificationId,
                createdAt: createdAt,
                updatedAt: updatedAt,
              ),
          withReferenceMapper: (p0) => p0
              .map(
                (e) => (
                  e.readTable<$DoseRecordsTable, DoseRecordEntry>(table),
                  $$DoseRecordsTableReferences(db, table, e),
                ),
              )
              .toList(),
          prefetchHooksCallback:
              ({profileId = false, medicineId = false, scheduleId = false}) {
                return PrefetchHooks(
                  db: db,
                  explicitlyWatchedTables: [],
                  addJoins:
                      <
                        T extends TableManagerState<
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic,
                          dynamic
                        >
                      >(state) {
                        if (profileId) {
                          state = state.withJoin(
                            currentTable: table,
                            currentColumn: table.profileId,
                            referencedTable: $$DoseRecordsTableReferences
                                ._profileIdTable(db),
                            referencedColumn: $$DoseRecordsTableReferences
                                ._profileIdTable(db)
                                .id,
                          ) as T;
                        }
                        if (medicineId) {
                          state = state.withJoin(
                            currentTable: table,
                            currentColumn: table.medicineId,
                            referencedTable: $$DoseRecordsTableReferences
                                ._medicineIdTable(db),
                            referencedColumn: $$DoseRecordsTableReferences
                                ._medicineIdTable(db)
                                .id,
                          ) as T;
                        }
                        if (scheduleId) {
                          state = state.withJoin(
                            currentTable: table,
                            currentColumn: table.scheduleId,
                            referencedTable: $$DoseRecordsTableReferences
                                ._scheduleIdTable(db),
                            referencedColumn: $$DoseRecordsTableReferences
                                ._scheduleIdTable(db)
                                .id,
                          ) as T;
                        }

                        return state;
                      },
                  getPrefetchedDataCallback: (items) async {
                    return [];
                  },
                );
              },
        ),
      );
}

typedef $$DoseRecordsTableProcessedTableManager =
    ProcessedTableManager<
      _$AppDatabase,
      $DoseRecordsTable,
      DoseRecordEntry,
      $$DoseRecordsTableFilterComposer,
      $$DoseRecordsTableOrderingComposer,
      $$DoseRecordsTableAnnotationComposer,
      $$DoseRecordsTableCreateCompanionBuilder,
      $$DoseRecordsTableUpdateCompanionBuilder,
      (DoseRecordEntry, $$DoseRecordsTableReferences),
      DoseRecordEntry,
      PrefetchHooks Function({bool profileId, bool medicineId, bool scheduleId})
    >;

class $AppDatabaseManager {
  final _$AppDatabase _db;
  $AppDatabaseManager(this._db);
  $$ProfilesTableTableManager get profiles =>
      $$ProfilesTableTableManager(_db, _db.profiles);
  $$ProfileSettingsTableTableManager get profileSettings =>
      $$ProfileSettingsTableTableManager(_db, _db.profileSettings);
  $$AppSettingsTableTableManager get appSettings =>
      $$AppSettingsTableTableManager(_db, _db.appSettings);
  $$MedicinesTableTableManager get medicines =>
      $$MedicinesTableTableManager(_db, _db.medicines);
  $$SchedulesTableTableManager get schedules =>
      $$SchedulesTableTableManager(_db, _db.schedules);
  $$DoseRecordsTableTableManager get doseRecords =>
      $$DoseRecordsTableTableManager(_db, _db.doseRecords);
}
