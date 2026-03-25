import java.io.*;
import java.util.*;

public class StudentRecordProcessor {
    // Поля для хранения данных
    private final List<Student> students = new ArrayList<>();

    // _____реализуйте класс Student ниже в этом же файле______

    private double averageScore;
    private Student highestStudent;


    /**
     * Task 1 + Task 2 + Task 5 + Task 6
     */
    public void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("input/students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    System.out.println("Invalid data: " + line);
                    continue;
                }
                String name = parts[0].trim();
                String scoreStr = parts[1].trim();
                try {
                    int score = Integer.parseInt(scoreStr);
                    if (score < 0 || score > 100) {
                        throw new InvalidScoreException("Score out of range: " + score);
                    }
                    students.add(new Student(name, score));
                    System.out.println(name + "," + score);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data: " + line);
                } catch (InvalidScoreException e) {
                    System.out.println("Invalid data: " + line + " (" + e.getMessage() + ")");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: input/students.txt");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Task 3 + Task 8
     */
    public void processData() {
        if (students.isEmpty()) {
            System.out.println("No valid student data to process.");
            return;
        }

        int sum = 0;
        highestStudent = students.get(0);
        for (Student s : students) {
            sum += s.getScore();
            if (s.getScore() > highestStudent.getScore()) {
                highestStudent = s;
            }
        }
        averageScore = (double) sum / students.size();

        students.sort((a, b) -> b.getScore() - a.getScore());
    }

    /**
     * Task 4 + Task 5 + Task 8
     */
    public void writeFile() {
        if (students.isEmpty()) {
            System.out.println("No data to write.");
            return;
        }

        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/report.txt"))) {
            writer.write("Average: " + averageScore);
            writer.newLine();
            writer.write("Highest: " + highestStudent.getName() + " - " + highestStudent.getScore());
            writer.newLine();
            writer.newLine();
            writer.write("Sorted Students (by score descending):");
            writer.newLine();
            for (Student s : students) {
                writer.write(s.getName() + " - " + s.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentRecordProcessor processor = new StudentRecordProcessor();

        try {
            processor.readFile();
            processor.processData();
            processor.writeFile();
            System.out.println("Processing completed. Check output/report.txt");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

// class InvalidScoreException реализуйте меня
class InvalidScoreException extends Exception {
    public InvalidScoreException(String message) {
        super(message);
    }
}

// class Student (name, score)
class Student {
    private String name;
    private int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}