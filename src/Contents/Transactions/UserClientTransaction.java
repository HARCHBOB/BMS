package Contents.Transactions;

import java.sql.*;

public class UserClientTransaction {
    private Connection con;

    public UserClientTransaction(Connection con) {
        this.con = con;
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

    /**
     * Deletes a user and their associated client in a single transaction.
     */
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
}
