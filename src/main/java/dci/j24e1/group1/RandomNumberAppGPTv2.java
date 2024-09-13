package dci.j24e1.group1;

import java.sql.*;
import java.util.Random;

public class RandomNumberAppGPTv2 {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/random_numbers_db";
    private static final String USER = "root"; // change if necessary
    private static final String PASSWORD = "dudeWTF?"; // change if necessary

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            int randomNumber = generateRandomNumber();
            System.out.println("Generated Random Number: " + randomNumber);

            // Insert the number into the database
            insertNumber(connection, randomNumber);

            // List all numbers previously generated
            listNumbers(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }

    private static void insertNumber(Connection connection, int number) throws SQLException {
        String query = "INSERT IGNORE INTO numbers2 (number) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, number);
            stmt.executeUpdate();
        }
    }

    private static void listNumbers(Connection connection) throws SQLException {
        String query = "SELECT number FROM numbers2 ORDER BY number";
        System.out.println("\nPreviously Generated Numbers:");
        System.out.println("---------------------------");

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int number = rs.getInt("number");
                System.out.printf("| Number: %-4d %n", number);
            }
        }
        System.out.println("---------------------------");
    }
}