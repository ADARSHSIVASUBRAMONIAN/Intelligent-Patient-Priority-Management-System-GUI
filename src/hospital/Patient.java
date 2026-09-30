package hospital;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private final int id;
    private final String name;
    private final int age;
    private final String phone;
    private final List<String> history = new ArrayList<>();

    public Patient(int id, String name, int age, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPhone() { return phone; }
    public List<String> getHistory() { return history; }

    public void addHistory(String record) {
        history.add(record);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Age: %d | Phone: %s",
                id, name, age, phone);
    }
}
