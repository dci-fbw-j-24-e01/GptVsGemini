package dci.j24e1.group1;

import java.sql.*;
import java.util.Random;
//Created using ChatGPT
public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/random_numbers_db";
    private static final String USER = "root"; // change if necessary
    private static final String PASSWORD = "dudeWTF?"; // change if necessary

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            int randomNumber = generateRandomNumber();
            System.out.println("Generated Random Number: " + randomNumber);

            // Insert or update the number in the database
            insertOrUpdateNumber(connection, randomNumber);

            // List all numbers and their occurrences
            listNumbers(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }

    private static void insertOrUpdateNumber(Connection connection, int number) throws SQLException {
        String selectQuery = "SELECT occurrences FROM numbers WHERE number = ?";
        String insertQuery = "INSERT INTO numbers (number) VALUES (?)";
        String updateQuery = "UPDATE numbers SET occurrences = occurrences + 1 WHERE number = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, number);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    // Number exists, update the occurrences
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, number);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Number does not exist, insert it
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, number);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    }

    private static void listNumbers(Connection connection) throws SQLException {
        String query = "SELECT number, occurrences FROM numbers ORDER BY number";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int number = rs.getInt("number");
                int occurrences = rs.getInt("occurrences");
                System.out.println("Number: " + number + ", Occurrences: " + occurrences);
            }
        }
    }
}
