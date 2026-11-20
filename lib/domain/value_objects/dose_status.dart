enum DoseStatus {
  pending('pending', 'Pending'),
  taken('taken', 'Taken'),
  snoozed('snoozed', 'Snoozed'),
  missed('missed', 'Missed'),
  skipped('skipped', 'Skipped');

  final String value;
  final String label;

  const DoseStatus(this.value, this.label);

  static DoseStatus fromString(String value) {
    return DoseStatus.values.firstWhere(
      (e) => e.value == value,
      orElse: () => DoseStatus.pending,
    );
  }

  bool get isPending => this == DoseStatus.pending;
  bool get isTaken => this == DoseStatus.taken;
  bool get isSnoozed => this == DoseStatus.snoozed;
  bool get isMissed => this == DoseStatus.missed;
  bool get isSkipped => this == DoseStatus.skipped;

  /// Whether this dose has been resolved (taken, missed, or skipped)
  bool get isResolved => isTaken || isMissed || isSkipped;
}
