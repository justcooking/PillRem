import 'package:drift/drift.dart';
import '../database/app_database.dart';
import '../database/tables/medicines_table.dart';

part 'medicine_dao.g.dart';

@DriftAccessor(tables: [Medicines])
class MedicineDao extends DatabaseAccessor<AppDatabase> with _$MedicineDaoMixin {
  MedicineDao(super.db);

  Stream<List<MedicineEntry>> watchActiveMedicines(int profileId) {
    return (select(medicines)
          ..where((t) => t.profileId.equals(profileId) & t.isArchived.equals(false))
          ..orderBy([(t) => OrderingTerm(expression: t.name)]))
        .watch();
  }

  Future<List<MedicineEntry>> getActiveMedicines(int profileId) {
    return (select(medicines)
          ..where((t) => t.profileId.equals(profileId) & t.isArchived.equals(false))
          ..orderBy([(t) => OrderingTerm(expression: t.name)]))
        .get();
  }

  Stream<List<MedicineEntry>> watchArchivedMedicines(int profileId) {
    return (select(medicines)
          ..where((t) => t.profileId.equals(profileId) & t.isArchived.equals(true))
          ..orderBy([(t) => OrderingTerm(expression: t.name)]))
        .watch();
  }

  Future<MedicineEntry?> getMedicineById(int id) {
    return (select(medicines)..where((t) => t.id.equals(id))).getSingleOrNull();
  }

  Future<int> insertMedicine(MedicinesCompanion entry) {
    return into(medicines).insert(entry);
  }

  Future<bool> updateMedicine(MedicinesCompanion entry) {
    return update(medicines).replace(entry);
  }

  /// Soft-delete by setting isArchived = true. Preserves historical dose record consistency.
  Future<void> archiveMedicine(int medicineId) {
    return (update(medicines)..where((t) => t.id.equals(medicineId))).write(
      const MedicinesCompanion(isArchived: Value(true)),
    );
  }

  Future<void> restoreMedicine(int medicineId) {
    return (update(medicines)..where((t) => t.id.equals(medicineId))).write(
      const MedicinesCompanion(isArchived: Value(false)),
    );
  }

  /// Decrement inventory on dose taken
  Future<void> decrementStock(int medicineId, {int amount = 1}) {
    return transaction(() async {
      final med = await getMedicineById(medicineId);
      if (med != null && med.stockCount > 0) {
        final newStock = (med.stockCount - amount).clamp(0, 999999);
        await (update(medicines)..where((t) => t.id.equals(medicineId))).write(
          MedicinesCompanion(stockCount: Value(newStock)),
        );
      }
    });
  }

  Future<void> updateStock(int medicineId, int newStock) {
    return (update(medicines)..where((t) => t.id.equals(medicineId))).write(
      MedicinesCompanion(stockCount: Value(newStock)),
    );
  }
}
