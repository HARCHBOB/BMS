package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class User extends ShowContents {

    public User(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.User";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Users:");
            outputInfo(rs);
        }
    }

    private void outputInfo(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String nameSurname = rs.getString("Name_Surname");
            Date dob = rs.getDate("Date_of_birth");
            String passportNumber = rs.getString("Passport_Number");
            String street = rs.getString("Street");
            String house = rs.getString("House");
            String apartment = rs.getString("Apartment");
            Timestamp creationDate = rs.getTimestamp("Creation_date");

            System.out.printf(
                    "ID: %d, Name: %s, DOB: %s, Passport: %s, Address: %s %s %s, Created: %s%n",
                    userId, nameSurname, dob, passportNumber, street, house, apartment, creationDate
            );
        }
    }



    /**
     * Search for users by a partial or full match of their name.
     * Uses ILIKE for case-insensitive search (PostgreSQL specific).
     *
     * @param name The name or partial name to search for.
     */
    public void searchUserByName(String name) throws SQLException {
        String sql = "SELECT * FROM bms.User WHERE Name_Surname ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // For partial matching, you can wrap the input with '%' wildcards.
            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Search results for: " + name);
                outputInfo(rs);
            }
        }
    }


    /**
     * Adds a user and a client in a single transaction.
     */
    public void addUserAndClient(String name, Date dob, String passport, String street, String house, String apartment, double deposit) throws SQLException {
        String addUserSql = "INSERT INTO bms.User (Name_Surname, Date_of_birth, Passport_Number, Street, House, Apartment) VALUES (?, ?, ?, ?, ?, ?)";
        String addClientSql = "INSERT INTO bms.Client (User_ID, Deposit) VALUES (?, ?)";

        try {
            con.setAutoCommit(false); // Start transaction

            // Add user
            int userId;
            try (PreparedStatement addUserPs = con.prepareStatement(addUserSql, Statement.RETURN_GENERATED_KEYS)) {
                addUserPs.setString(1, name);
                addUserPs.setDate(2, dob);
                addUserPs.setString(3, passport);
                addUserPs.setString(4, street);
                addUserPs.setString(5, house);
                addUserPs.setString(6, apartment);
                addUserPs.executeUpdate();

                try (ResultSet rs = addUserPs.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve User_ID.");
                    }
                }
            }

            // Add client
            try (PreparedStatement addClientPs = con.prepareStatement(addClientSql)) {
                addClientPs.setInt(1, userId);
                addClientPs.setDouble(2, deposit);
                addClientPs.executeUpdate();
            }

            con.commit(); // Commit transaction
            System.out.println("User and client added successfully.");
        } catch (SQLException e) {
            con.rollback(); // Rollback transaction on error
            throw e;
        } finally {
            con.setAutoCommit(true); // Reset auto-commit
        }
    }


    public void createUser(String nameSurname, Date dob, String passportNumber, String street, String house, String apartment) throws SQLException {
        String insertSql = "INSERT INTO bms.User (Name_Surname, Date_of_birth, Passport_Number, Street, House, Apartment) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertSql)) {
            ps.setString(1, nameSurname);
            ps.setDate(2, dob);
            ps.setString(3, passportNumber);
            ps.setString(4, street != null ? street : "N/A");
            ps.setString(5, house != null ? house : "N/A");
            ps.setString(6, apartment != null ? apartment : "N/A");
            ps.executeUpdate();
        }
    }

    public void deleteUserById(int userId) throws SQLException {
        String sql = "DELETE FROM bms.User WHERE User_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted User with ID: " + userId);
            } else {
                System.out.println("No User found with ID: " + userId);
            }
        }
    }

    public void deleteUserByName(String name) throws SQLException {
        String sql = "DELETE FROM bms.User WHERE Name_Surname ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted User with Name: " + name);
            } else {
                System.out.println("No User found with Name: " + name);
            }
        }
    }

    public void deleteUserAndClient(int userId) throws SQLException {
        String deleteClientSql = "DELETE FROM bms.Client WHERE User_ID = ?";
        String deleteUserSql = "DELETE FROM bms.User WHERE User_ID = ?";

        try {
            con.setAutoCommit(false); // Start transaction

            // Delete client
            try (PreparedStatement deleteClientPs = con.prepareStatement(deleteClientSql)) {
                deleteClientPs.setInt(1, userId);
                deleteClientPs.executeUpdate();
            }

            // Delete user
            try (PreparedStatement deleteUserPs = con.prepareStatement(deleteUserSql)) {
                deleteUserPs.setInt(1, userId);
                deleteUserPs.executeUpdate();
            }

            con.commit(); // Commit transaction
            System.out.println("User and client deleted successfully.");
        } catch (SQLException e) {
            con.rollback(); // Rollback transaction on error
            throw e;
        } finally {
            con.setAutoCommit(true); // Reset auto-commit
        }
    }

    public void showClients() throws SQLException {
        String sql = "SELECT * FROM bms.Client";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Clients:");
            while (rs.next()) {
                int clientId = rs.getInt("Client_ID");
                int userId = rs.getInt("User_ID");
                double deposit = rs.getDouble("Deposit");
                Timestamp registrationDate = rs.getTimestamp("Registration_Date");

                System.out.printf(
                        "Client ID: %d, User ID: %d, Deposit: %.2f, Registration Date: %s%n",
                        clientId, userId, deposit, registrationDate
                );
            }
        }
    }

    public void createClient(int userId, double deposit) throws SQLException {
        String sql = "INSERT INTO bms.Client (User_ID, Deposit) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDouble(2, deposit);
            ps.executeUpdate();
        }
    }

    public void deleteClient(int clientId) throws SQLException {
        String sql = "DELETE FROM bms.Client WHERE Client_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted Client with ID: " + clientId);
            } else {
                System.out.println("No Client found with ID: " + clientId);
            }
        }
    }


    public void showEmployee() throws SQLException {
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

    public void addEmployee(int userId, double salary) throws SQLException {
        String sql = "INSERT INTO bms.Employee (User_ID, Salary) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDouble(2, salary);

            ps.executeUpdate();
            System.out.println("New employee added successfully.");
        }
    }

    public void deleteEmployee(int adminId) throws SQLException {
        String sql = "DELETE FROM bms.Employee WHERE Admin_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted Employee with Admin ID: " + adminId);
            } else {
                System.out.println("No Employee found with Admin ID: " + adminId);
            }
        }
    }

}
