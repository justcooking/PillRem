import 'package:drift/drift.dart';
import '../database/app_database.dart';
import '../database/tables/dose_records_table.dart';
import '../database/tables/medicines_table.dart';

part 'dose_record_dao.g.dart';

class DoseWithMedicine {
  final DoseRecordEntry dose;
  final MedicineEntry medicine;

  DoseWithMedicine({
    required this.dose,
    required this.medicine,
  });
}

@DriftAccessor(tables: [DoseRecords, Medicines])
class DoseRecordDao extends DatabaseAccessor<AppDatabase> with _$DoseRecordDaoMixin {
  DoseRecordDao(super.db);

  /// Watch doses with their medicine info for a specific profile within a date range
  Stream<List<DoseWithMedicine>> watchDosesForProfileRange(
    int profileId,
    DateTime start,
    DateTime end,
  ) {
    final query = select(doseRecords).join([
      innerJoin(medicines, medicines.id.equalsExp(doseRecords.medicineId)),
    ])
      ..where(
        doseRecords.profileId.equals(profileId) &
            doseRecords.scheduledAt.isBiggerOrEqualValue(start) &
            doseRecords.scheduledAt.isSmallerOrEqualValue(end),
      )
      ..orderBy([OrderingTerm.asc(doseRecords.scheduledAt)]);

    return query.watch().map((rows) {
      return rows.map((row) {
        return DoseWithMedicine(
          dose: row.readTable(doseRecords),
          medicine: row.readTable(medicines),
        );
      }).toList();
    });
  }

  /// Get doses for a specific profile within a date range
  Future<List<DoseWithMedicine>> getDosesForProfileRange(
    int profileId,
    DateTime start,
    DateTime end,
  ) {
    final query = select(doseRecords).join([
      innerJoin(medicines, medicines.id.equalsExp(doseRecords.medicineId)),
    ])
      ..where(
        doseRecords.profileId.equals(profileId) &
            doseRecords.scheduledAt.isBiggerOrEqualValue(start) &
            doseRecords.scheduledAt.isSmallerOrEqualValue(end),
      )
      ..orderBy([OrderingTerm.asc(doseRecords.scheduledAt)]);

    return query.get().then((rows) {
      return rows.map((row) {
        return DoseWithMedicine(
          dose: row.readTable(doseRecords),
          medicine: row.readTable(medicines),
        );
      }).toList();
    });
  }

  Future<DoseWithMedicine?> getDoseWithMedicine(int doseId) async {
    final query = select(doseRecords).join([
      innerJoin(medicines, medicines.id.equalsExp(doseRecords.medicineId)),
    ])..where(doseRecords.id.equals(doseId));

    final row = await query.getSingleOrNull();
    if (row == null) return null;
    return DoseWithMedicine(
      dose: row.readTable(doseRecords),
      medicine: row.readTable(medicines),
    );
  }

  /// Insert a dose idempotently (ignoring duplicates if scheduleId & scheduledAt exist)
  Future<int> insertDoseIdempotent(DoseRecordsCompanion entry) {
    return into(doseRecords).insert(entry, mode: InsertMode.insertOrIgnore);
  }

  /// Update dose status atomically
  Future<void> updateDoseStatus({
    required int doseId,
    required String status,
    DateTime? reminderAt,
    DateTime? takenAt,
    DateTime? skippedAt,
    String? skipReason,
    int? snoozeCount,
  }) {
    return (update(doseRecords)..where((t) => t.id.equals(doseId))).write(
      DoseRecordsCompanion(
        status: Value(status),
        reminderAt: reminderAt != null ? Value(reminderAt) : const Value.absent(),
        takenAt: takenAt != null ? Value(takenAt) : const Value.absent(),
        skippedAt: skippedAt != null ? Value(skippedAt) : const Value.absent(),
        skipReason: skipReason != null ? Value(skipReason) : const Value.absent(),
        snoozeCount: snoozeCount != null ? Value(snoozeCount) : const Value.absent(),
        updatedAt: Value(DateTime.now()),
      ),
    );
  }

  /// Find all pending or snoozed doses where grace_period_deadline has passed
  Future<List<DoseRecordEntry>> getUnresolvedExpiredDoses(DateTime cutoffTime) {
    return (select(doseRecords)
          ..where((t) =>
              (t.status.equals('pending') | t.status.equals('snoozed')) &
              t.gracePeriodDeadline.isSmallerOrEqualValue(cutoffTime)))
        .get();
  }

  /// All history doses for adherence calculations
  Future<List<DoseRecordEntry>> getDoseHistoryForProfile(int profileId, {DateTime? since}) {
    var query = select(doseRecords)..where((t) => t.profileId.equals(profileId));
    if (since != null) {
      query = query..where((t) => t.scheduledAt.isBiggerOrEqualValue(since));
    }
    return (query..orderBy([(t) => OrderingTerm.desc(t.scheduledAt)])).get();
  }
}
