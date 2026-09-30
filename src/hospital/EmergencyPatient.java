package hospital;

public class EmergencyPatient implements Comparable<EmergencyPatient> {
    private final Patient patient;
    private final int severity; // 1 = Critical, 2 = Serious, 3 = Normal
    private final long arrivalOrder;

    public EmergencyPatient(Patient patient, int severity, long arrivalOrder) {
        this.patient = patient;
        this.severity = severity;
        this.arrivalOrder = arrivalOrder;
    }

    public Patient getPatient() { return patient; }
    public int getSeverity() { return severity; }

    @Override
    public int compareTo(EmergencyPatient other) {
        int bySeverity = Integer.compare(this.severity, other.severity);
        if (bySeverity != 0) return bySeverity;
        return Long.compare(this.arrivalOrder, other.arrivalOrder);
    }

    public String severityText() {
        return switch (severity) {
            case 1 -> "CRITICAL";
            case 2 -> "SERIOUS";
            default -> "NORMAL";
        };
    }

    @Override
    public String toString() {
        return patient + " | Priority: " + severityText();
    }
}
