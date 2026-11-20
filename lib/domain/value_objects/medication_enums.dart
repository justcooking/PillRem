enum ScheduleType {
  daily('daily', 'Every Day'),
  specificDays('specific_days', 'Specific Days'),
  intervalDays('interval_days', 'Day Interval'),
  asNeeded('as_needed', 'As Needed');

  final String value;
  final String label;

  const ScheduleType(this.value, this.label);

  static ScheduleType fromString(String value) {
    return ScheduleType.values.firstWhere(
      (e) => e.value == value,
      orElse: () => ScheduleType.daily,
    );
  }
}

enum MedicationForm {
  tablet('tablet', 'Tablet'),
  capsule('capsule', 'Capsule'),
  liquid('liquid', 'Liquid / Syrup'),
  injection('injection', 'Injection'),
  inhaler('inhaler', 'Inhaler'),
  drops('drops', 'Drops'),
  patch('patch', 'Patch'),
  other('other', 'Other');

  final String value;
  final String label;

  const MedicationForm(this.value, this.label);

  static MedicationForm fromString(String value) {
    return MedicationForm.values.firstWhere(
      (e) => e.value == value,
      orElse: () => MedicationForm.tablet,
    );
  }
}
