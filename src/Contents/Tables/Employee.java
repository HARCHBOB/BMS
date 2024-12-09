package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Employee extends ShowContents {

    public Employee(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Employee";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        }
    }

    /**
     * Search for an employee by User ID.
     *
     * @param userId The User ID to search for.
     */
    public void searchEmployeeByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM bms.Employee WHERE User_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Search results for User ID: " + userId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No employees found with this User ID.");
                }

                while (rs.next()) {
                    int adminId = rs.getInt("Admin_ID");
                    double salary = rs.getDouble("Salary");

                    System.out.printf(
                            "Admin ID: %d, User ID: %d, Salary: %.2f%n",
                            adminId, userId, salary
                    );
                }
            }
        }
    }

    /**
     * Add a new employee.
     *
     * @param userId The User ID of the employee (must exist in bms.User).
     * @param salary The employee's salary.
     */
    public void addEmployee(int userId, double salary) throws SQLException {
        String sql = "INSERT INTO bms.Employee (User_ID, Salary) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDouble(2, salary);

            ps.executeUpdate();
            System.out.println("New employee added successfully.");
        }
    }
}
