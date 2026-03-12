import hospitalmanagement.patient;
import hospitalmanagement.doctor;
import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;
 public class hospitalmanagementsystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital_db";
     ;
    private static final String username = "root";
    private static final String password = "richa123";
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try (
                Scanner scanner = new Scanner(System.in);
                Connection connection = DriverManager.getConnection(url, username, password)
        ) {
            patient patient = new patient(connection, scanner);
            doctor doctor = new doctor(connection, scanner);
            while (true) {
                System.out.println("\nHOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add patient");
                System.out.println("2. View patient");
                System.out.println("3. View doctor");
                System.out.println("4. Book appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        patient.addpatient();
                        break;
                    case 2:
                        patient.viewpatient();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        break;
                    case 4:
                        bookAppointment(connection, scanner, patient, doctor);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


     public static void bookAppointment(Connection connection, Scanner scanner, patient patient, doctor doctor) {
         System.out.print("Enter patient ID: ");
         int patientId = scanner.nextInt();

         System.out.print("Enter doctor ID: ");
         int doctorId = scanner.nextInt();

         System.out.print("Enter appointment date (yyyy-mm-dd): ");
         String appointmentDateInput = scanner.next();

         // Convert input to LocalDate
         LocalDate appointmentDate;
         try {
             appointmentDate = LocalDate.parse(appointmentDateInput);
         } catch (Exception e) {
             System.out.println("❌ Invalid date format. Use yyyy-mm-dd.");
             return;
         }

         // Check if date is in the past
         if (appointmentDate.isBefore(LocalDate.now())) {
             System.out.println("❌ Cannot book appointment in the past!");
             return;
         }

         // Check if patient and doctor exist
         if (patient.getpatientbyid(patientId) && doctor.getDoctorById(doctorId)) {

             // Check doctor availability
             if (checkDoctorAvailability(doctorId, appointmentDateInput, connection)) {
                 String query = "INSERT INTO appointment (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                 try (PreparedStatement ps = connection.prepareStatement(query)) {
                     ps.setInt(1, patientId);
                     ps.setInt(2, doctorId);
                     ps.setString(3, appointmentDateInput);

                     int rowsAffected = ps.executeUpdate();
                     if (rowsAffected > 0) {
                         System.out.println("✅ Appointment booked successfully!");
                     } else {
                         System.out.println("❌ Failed to book appointment.");
                     }
                 } catch (SQLException e) {
                     System.out.println("Error while booking appointment: " + e.getMessage());
                 }
             } else {
                 System.out.println("❌ Doctor is not available on this date.");
             }
         } else {
             System.out.println("❌ Either patient or doctor does not exist.");
         }
     }


     public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, doctorId);
            ps.setString(2, appointmentDate);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
        return false;
    }
}
