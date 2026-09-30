# Intelligent Patient Priority Management System

Java Swing GUI version of the Advanced Data Structures with Java hospital project.

## Data structures
- ArrayList: patients and doctors
- HashMap: patient lookup by ID
- PriorityQueue: emergency patient priority
- Queue (LinkedList): appointments
- Stack: recent actions

## Run on Mac/Linux
```bash
javac -d out src/hospital/*.java
java -cp out hospital.Main
```
Or double-click/run:
```bash
java -jar IntelligentPatientPrioritySystem.jar
```

Emergency priority: 1 Critical, 2 Serious, 3 Normal.
