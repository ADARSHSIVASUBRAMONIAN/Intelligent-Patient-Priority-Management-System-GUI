package hospital;

public class Appointment {
    private final Patient patient;
    private final Doctor doctor;
    private final String time;

    public Appointment(Patient patient, Doctor doctor, String time) {
        this.patient = patient;
        this.doctor = doctor;
        this.time = time;
    }

    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }

    @Override
    public String toString() {
        return "Patient: " + patient.getName()
                + " | Doctor: Dr. " + doctor.getName()
                + " | Department: " + doctor.getDepartment()
                + " | Time: " + time;
    }
}
