package hospitalmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

 public class  doctor {
    private Connection connection;
    private Scanner scanner;
    public doctor(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void viewDoctor() {
        String query = "SELECT * FROM doctor";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("Doctors:");
            System.out.println("+------------+------------------------+------------------------+");
            System.out.println("| Doctor ID  | Name                   | Specialization         |");
            System.out.println("+------------+------------------------+------------------------+");

            while (rs.next()) {
                int id = rs.getInt("d_id");
                String name = rs.getString("d_name");
                String specialization = rs.getString("specialization");

                System.out.printf("| %-10d | %-22s | %-22s |\n", id, name, specialization);
            }
            System.out.println("+------------+------------------------+------------------------+");
        } catch (SQLException e) {
            System.out.println("Error viewing doctors: " + e.getMessage());
        }
    }
    public boolean getDoctorById(int id) {
        String query = "SELECT * FROM doctor WHERE d_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error fetching doctor by ID: " + e.getMessage());
            return false;
        }
    }
}
