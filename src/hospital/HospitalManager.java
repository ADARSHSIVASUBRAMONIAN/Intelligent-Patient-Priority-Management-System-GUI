package hospital;

import java.util.*;

public class HospitalManager {
    private final ArrayList<Patient> patients = new ArrayList<>();
    private final ArrayList<Doctor> doctors = new ArrayList<>();
    private final Queue<Appointment> appointments = new LinkedList<>();
    private final PriorityQueue<EmergencyPatient> emergencyQueue = new PriorityQueue<>();
    private final HashMap<Integer, Patient> patientIndex = new HashMap<>();
    private final Stack<String> undoStack = new Stack<>();
    private long arrivalCounter = 0;

    public boolean addPatient(Patient p) {
        if (patientIndex.containsKey(p.getId())) return false;
        patients.add(p);
        patientIndex.put(p.getId(), p);
        undoStack.push("Registered patient " + p.getName() + " (ID " + p.getId() + ")");
        return true;
    }

    public boolean addDoctor(Doctor d) {
        for (Doctor x : doctors) {
            if (x.getId() == d.getId()) return false;
        }
        doctors.add(d);
        undoStack.push("Added doctor Dr. " + d.getName());
        return true;
    }

    public Patient searchPatient(int id) {
        return patientIndex.get(id);
    }

    public void addEmergency(Patient p, int severity) {
        emergencyQueue.offer(new EmergencyPatient(p, severity, arrivalCounter++));
        undoStack.push("Added " + p.getName() + " to emergency queue");
    }

    public EmergencyPatient treatNextEmergency() {
        EmergencyPatient e = emergencyQueue.poll();
        if (e != null) {
            e.getPatient().addHistory("Emergency treatment completed");
            undoStack.push("Treated emergency patient " + e.getPatient().getName());
        }
        return e;
    }

    public void addAppointment(Appointment a) {
        appointments.offer(a);
        undoStack.push("Booked appointment for " + a.getPatient().getName());
    }

    public Appointment serveNextAppointment() {
        Appointment a = appointments.poll();
        if (a != null) {
            a.getPatient().addHistory("Appointment with Dr. " + a.getDoctor().getName());
        }
        return a;
    }

    public ArrayList<Patient> getPatients() { return patients; }
    public ArrayList<Doctor> getDoctors() { return doctors; }
    public Queue<Appointment> getAppointments() { return appointments; }
    public PriorityQueue<EmergencyPatient> getEmergencyQueue() { return emergencyQueue; }

    public String undoLastAction() {
        if (undoStack.empty()) return "Nothing to undo.";
        return "Last action: " + undoStack.pop();
    }

    public int totalPatients() { return patients.size(); }
    public int totalDoctors() { return doctors.size(); }
    public int emergencyCount() { return emergencyQueue.size(); }
    public int appointmentCount() { return appointments.size(); }
}
