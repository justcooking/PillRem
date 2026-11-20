import '../../data/local/database/app_database.dart';
import '../../domain/value_objects/dose_status.dart';

class AdherenceStats {
  final int totalScheduled;
  final int takenCount;
  final int missedCount;
  final int skippedCount;
  final int pendingCount;
  final double adherenceRate; // 0.0 to 100.0

  const AdherenceStats({
    required this.totalScheduled,
    required this.takenCount,
    required this.missedCount,
    required this.skippedCount,
    required this.pendingCount,
    required this.adherenceRate,
  });

  factory AdherenceStats.fromDoses(List<DoseRecordEntry> doses) {
    int taken = 0;
    int missed = 0;
    int skipped = 0;
    int pending = 0;

    for (final d in doses) {
      final status = DoseStatus.fromString(d.status);
      switch (status) {
        case DoseStatus.taken:
          taken++;
          break;
        case DoseStatus.missed:
          missed++;
          break;
        case DoseStatus.skipped:
          skipped++;
          break;
        case DoseStatus.pending:
        case DoseStatus.snoozed:
          pending++;
          break;
      }
    }

    final evaluableDoses = taken + missed;
    final rate = evaluableDoses > 0 ? (taken / evaluableDoses) * 100.0 : 100.0;

    return AdherenceStats(
      totalScheduled: doses.length,
      takenCount: taken,
      missedCount: missed,
      skippedCount: skipped,
      pendingCount: pending,
      adherenceRate: rate,
    );
  }
}

class AdherenceService {
  final AppDatabase db;

  AdherenceService(this.db);

  /// Compute adherence stats for a profile across a given time window
  Future<AdherenceStats> getAdherenceStats(int profileId, {DateTime? since}) async {
    final doses = await db.doseRecordDao.getDoseHistoryForProfile(profileId, since: since);
    return AdherenceStats.fromDoses(doses);
  }
}
