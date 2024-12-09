package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Employee extends ShowContents {

    public Employee(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Employee");
        System.out.println("Employees:");

        while (rs.next()) {
            int adminId = rs.getInt("Admin_ID");
            int userId = rs.getInt("User_ID");
            double salary = rs.getDouble("Salary");

            System.out.printf(
                    "Admin ID: %d, User ID: %d, Salary: %.2f%n",
                    adminId, userId, salary
            );
        }

        rs.close();
    }
}
