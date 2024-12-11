package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Client extends ShowContents {

    public Client(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
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

    /**
     * Search clients by User_ID.
     * This might be useful if you want to find the client entry for a particular user.
     *
     * @param userId The User_ID to search for.
     */
    public void searchClientsByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM bms.Client WHERE User_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Client search results for User_ID: " + userId);
                while (rs.next()) {
                    int clientId = rs.getInt("Client_ID");
                    double deposit = rs.getDouble("Deposit");
                    Timestamp registrationDate = rs.getTimestamp("Registration_Date");

                    System.out.printf(
                            "Client ID: %d, User ID: %d, Deposit: %.2f, Registration Date: %s%n",
                            clientId, userId, deposit, registrationDate
                    );
                }
            }
        }
    }

    /**
     * Create a new client.
     *
     * @param userId The User_ID foreign key (must exist in bms.User)
     * @param deposit The client's deposit (must be within constraints)
     */
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
}
