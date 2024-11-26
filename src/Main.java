import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/rca";
    private static final String USER = "postgres"; // Replace with your PostgreSQL username
    private static final String PASSWORD = "buburine"; // Replace with your PostgreSQL password

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to the database!");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Add a new student");
                System.out.println("2. Search for a student by ID");
                System.out.println("3. View all students");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        // Insert a new student
                        System.out.print("Enter first name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter last name: ");
                        String lastName = scanner.nextLine();

                        String insertSQL = "INSERT INTO student (first_name, last_name) VALUES (?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
                            insertStatement.setString(1, firstName);
                            insertStatement.setString(2, lastName);
                            int rowsInserted = insertStatement.executeUpdate();
                            System.out.println(rowsInserted + " student(s) inserted.");
                        }
                        break;

                    case 2:
                        // Search for a student by ID
                        System.out.print("Enter student ID: ");
                        int studentId = scanner.nextInt();

                        String searchSQL = "SELECT * FROM student WHERE id = ?";
                        try (PreparedStatement searchStatement = connection.prepareStatement(searchSQL)) {
                            searchStatement.setInt(1, studentId);
                            ResultSet resultSet = searchStatement.executeQuery();
                            if (resultSet.next()) {
                                System.out.println("Student found:");
                                System.out.println("ID: " + resultSet.getInt("id"));
                                System.out.println("First Name: " + resultSet.getString("first_name"));
                                System.out.println("Last Name: " + resultSet.getString("last_name"));
                            } else {
                                System.out.println("No student found with ID: " + studentId);
                            }
                        }
                        break;

                    case 3:
                        // Retrieve all students
                        String selectSQL = "SELECT * FROM student";
                        try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                            ResultSet resultSet = selectStatement.executeQuery();
                            System.out.println("Students in the database:");
                            while (resultSet.next()) {
                                int id = resultSet.getInt("id");
                                String fName = resultSet.getString("first_name");
                                String lName = resultSet.getString("last_name");
                                System.out.println(id + ": " + fName + " " + lName);
                            }
                        }
                        break;

                    case 4:
                        // Exit the program
                        System.out.println("Exiting...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
