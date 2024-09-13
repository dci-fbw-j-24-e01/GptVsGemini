package dci.j24e1.group1;

import java.sql.*;
import java.util.Random;
//Created using Gemini
public class RandomNumberGenerator {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/random_numbers_db";
    private static final String USER = "root"; // change if necessary
    private static final String PASS = "dudeWTF?";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Generate a random number
            Random rand = new Random();
            int randomNumber = rand.nextInt(1000) + 1;

            // Insert the number into the database
            String insertSql = "INSERT INTO random_numbers (number) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            pstmt.setInt(1, randomNumber);
            pstmt.executeUpdate();

            // Retrieve and print previous numbers and their counts
            String selectSql = "SELECT number, COUNT(*) AS count FROM random_numbers GROUP BY number";
            pstmt = conn.prepareStatement(selectSql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int number = rs.getInt("number");
                int count = rs.getInt("count");
                System.out.println("Number: " + number + ", Count: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}