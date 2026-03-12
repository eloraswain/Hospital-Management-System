package hospitalmanagement;

import jdk.jshell.spi.ExecutionControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class patient {
    private Connection connection;
    private Scanner scanner;
    public  patient (Connection connection ,Scanner scanner ){
        this.connection=connection;
        this.scanner=scanner;
    }
   public void addpatient(){
        System.out.println("Enter patient name");
        String name=scanner.next() ;
       System.out.println("Enter patient age");
       int age=scanner.nextInt() ;
       System.out.println("Enter patient gender");
       String gender=scanner.next();

       try{
           String query = "INSERT INTO patient ( p_name,p_age, p_gender) VALUES (?, ?, ?)";

           PreparedStatement preparedStatement =connection.prepareStatement(query);
           preparedStatement.setString(1,name);
           preparedStatement.setInt(2,age);
           preparedStatement.setString(3,gender);
           int affectedRows = preparedStatement.executeUpdate();
           if (affectedRows > 0) {
               System.out.println("Patient added successfully!!");
           } else {
               System.out.println("Patient is not added.");
           }
       } catch (SQLException e) {
           System.out.println("Error inserting patient: " + e.getMessage());
       }
   }
   public void viewpatient(){
        String query="SELECT * FROM patient";
        try{
            PreparedStatement preparedStatement =connection.prepareStatement(query);
            ResultSet rs=preparedStatement.executeQuery();
            System.out.println("patients:");
            System.out.println("+-----------+-------------------+---------+--------------+");
            System.out.println("|patient id |Name                |Age      |Gender        |");
            while(rs.next()) {
                int id = rs.getInt("p_id");
                String name = rs.getString("p_name");
                int age = rs.getInt("p_age");
                String gender = rs.getString("p_gender");
                System.out.printf("| %-11d | %-18s | %-7d | %-12s |\n", id, name, age, gender);

            }
                System.out.println("+-----------+-------------------+---------+--------------+");


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

   }
   public boolean getpatientbyid(int id) {
        String query = "SELECT * FROM patient WHERE p_id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
