import 'package:drift/drift.dart';
import '../database/app_database.dart';
import '../database/tables/profiles_table.dart';
import '../database/tables/profile_settings_table.dart';

part 'profile_dao.g.dart';

@DriftAccessor(tables: [Profiles, ProfileSettings])
class ProfileDao extends DatabaseAccessor<AppDatabase> with _$ProfileDaoMixin {
  ProfileDao(super.db);

  Stream<List<ProfileEntry>> watchAllProfiles() {
    return (select(profiles)..orderBy([(t) => OrderingTerm(expression: t.createdAt)])).watch();
  }

  Future<List<ProfileEntry>> getAllProfiles() {
    return (select(profiles)..orderBy([(t) => OrderingTerm(expression: t.createdAt)])).get();
  }

  Stream<ProfileEntry?> watchActiveProfile() {
    return (select(profiles)..where((t) => t.isActive.equals(true))).watchSingleOrNull();
  }

  Future<ProfileEntry?> getActiveProfile() {
    return (select(profiles)..where((t) => t.isActive.equals(true))).getSingleOrNull();
  }

  Future<ProfileSettingsEntry?> getSettingsForProfile(int profileId) {
    return (select(profileSettings)..where((t) => t.profileId.equals(profileId))).getSingleOrNull();
  }

  Stream<ProfileSettingsEntry?> watchSettingsForProfile(int profileId) {
    return (select(profileSettings)..where((t) => t.profileId.equals(profileId))).watchSingleOrNull();
  }

  /// Creates a profile and automatically initializes its default ProfileSettings.
  /// If it is the first profile, it is marked active.
  Future<int> createProfile({
    required String name,
    required int avatarColor,
    required String avatarIcon,
    int gracePeriodMinutes = 60,
    int defaultSnoozeMinutes = 15,
  }) {
    return transaction(() async {
      final existingCount = await (select(profiles)..limit(1)).get();
      final isFirst = existingCount.isEmpty;

      final profileId = await into(profiles).insert(
        ProfilesCompanion.insert(
          name: name,
          avatarColor: Value(avatarColor),
          avatarIcon: Value(avatarIcon),
          isActive: Value(isFirst),
        ),
      );

      await into(profileSettings).insert(
        ProfileSettingsCompanion.insert(
          profileId: Value(profileId),
          gracePeriodMinutes: Value(gracePeriodMinutes),
          defaultSnoozeMinutes: Value(defaultSnoozeMinutes),
        ),
      );

      return profileId;
    });
  }

  /// Atomically switch the active profile
  Future<void> setActiveProfile(int profileId) {
    return transaction(() async {
      // Deactivate all
      await (update(profiles)).write(const ProfilesCompanion(isActive: Value(false)));
      // Activate selected
      await (update(profiles)..where((t) => t.id.equals(profileId)))
          .write(const ProfilesCompanion(isActive: Value(true)));
    });
  }

  Future<void> updateProfile({
    required int id,
    required String name,
    required int avatarColor,
    required String avatarIcon,
  }) {
    return (update(profiles)..where((t) => t.id.equals(id))).write(
      ProfilesCompanion(
        name: Value(name),
        avatarColor: Value(avatarColor),
        avatarIcon: Value(avatarIcon),
      ),
    );
  }

  Future<void> updateProfileSettings({
    required int profileId,
    required int gracePeriodMinutes,
    required int defaultSnoozeMinutes,
    String? notificationSound,
    bool? vibrate,
  }) {
    return (update(profileSettings)..where((t) => t.profileId.equals(profileId))).write(
      ProfileSettingsCompanion(
        gracePeriodMinutes: Value(gracePeriodMinutes),
        defaultSnoozeMinutes: Value(defaultSnoozeMinutes),
        notificationSound: notificationSound != null ? Value(notificationSound) : const Value.absent(),
        vibrate: vibrate != null ? Value(vibrate) : const Value.absent(),
      ),
    );
  }

  /// Deletes a profile. Thanks to ON DELETE CASCADE, its settings, medicines,
  /// schedules and dose records are cleaned up automatically.
  /// If the deleted profile was active, the next available profile is activated.
  Future<void> deleteProfile(int profileId) {
    return transaction(() async {
      final profile = await (select(profiles)..where((t) => t.id.equals(profileId))).getSingleOrNull();
      final wasActive = profile?.isActive ?? false;

      await (delete(profiles)..where((t) => t.id.equals(profileId))).go();

      if (wasActive) {
        final remaining = await (select(profiles)..limit(1)).getSingleOrNull();
        if (remaining != null) {
          await (update(profiles)..where((t) => t.id.equals(remaining.id)))
              .write(const ProfilesCompanion(isActive: Value(true)));
        }
      }
    });
  }
}
