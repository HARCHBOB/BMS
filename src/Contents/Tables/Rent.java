package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Rent extends ShowContents {

    public Rent(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Rent";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Rents:");
            while (rs.next()) {
                int rentId = rs.getInt("Rent_ID");
                int userId = rs.getInt("User_ID");
                int bicycleId = rs.getInt("Bicycle_ID");
                String parkingPlace = rs.getString("Parking_Place");
                double deposit = rs.getDouble("Deposit");
                Timestamp issueTime = rs.getTimestamp("Issue_Time");
                Timestamp returnTime = rs.getTimestamp("Return_Time");

                System.out.printf(
                        "Rent ID: %d, User ID: %d, Bicycle ID: %d, Parking: %s, Deposit: %.2f, Issue: %s, Return: %s%n",
                        rentId, userId, bicycleId, parkingPlace, deposit, issueTime, returnTime
                );
            }
        }
    }

    /**
     * Search for rents by User ID.
     *
     * @param userId The User ID to search for.
     */
    public void searchRentByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM bms.Rent WHERE User_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Rents for User ID: " + userId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No rents found for this User ID.");
                }

                while (rs.next()) {
                    int rentId = rs.getInt("Rent_ID");
                    int bicycleId = rs.getInt("Bicycle_ID");
                    String parkingPlace = rs.getString("Parking_Place");
                    double deposit = rs.getDouble("Deposit");
                    Timestamp issueTime = rs.getTimestamp("Issue_Time");
                    Timestamp returnTime = rs.getTimestamp("Return_Time");

                    System.out.printf(
                            "Rent ID: %d, Bicycle ID: %d, Parking: %s, Deposit: %.2f, Issue: %s, Return: %s%n",
                            rentId, bicycleId, parkingPlace, deposit, issueTime, returnTime
                    );
                }
            }
        }
    }

    /**
     * Search for rents by Bicycle ID.
     *
     * @param bicycleId The Bicycle ID to search for.
     */
    public void searchRentByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Rent WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Rents for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No rents found for this Bicycle ID.");
                }

                while (rs.next()) {
                    int rentId = rs.getInt("Rent_ID");
                    int userId = rs.getInt("User_ID");
                    String parkingPlace = rs.getString("Parking_Place");
                    double deposit = rs.getDouble("Deposit");
                    Timestamp issueTime = rs.getTimestamp("Issue_Time");
                    Timestamp returnTime = rs.getTimestamp("Return_Time");

                    System.out.printf(
                            "Rent ID: %d, User ID: %d, Parking: %s, Deposit: %.2f, Issue: %s, Return: %s%n",
                            rentId, userId, parkingPlace, deposit, issueTime, returnTime
                    );
                }
            }
        }
    }

    /**
     * Add a new rent record.
     *
     * @param userId       The ID of the user renting the bicycle.
     * @param bicycleId    The ID of the bicycle being rented.
     * @param parkingPlace The parking place associated with the rent.
     * @param deposit      The deposit amount for the rent.
     * @param issueTime    The timestamp when the rent was issued.
     */
    public void rentBicycle(int userId, int bicycleId, String parkingPlace, double deposit, Timestamp issueTime) throws SQLException {
        String addParkingSql = "INSERT INTO bms.Parking (Parking_Place, Bicycle_ID, Parking_Beginning, Parking_Ending) VALUES (?, NULL, ?, NULL) ON CONFLICT (Parking_Place) DO NOTHING";
        String addRentSql = "INSERT INTO bms.Rent (User_ID, Bicycle_ID, Parking_Place, Deposit, Issue_Time) VALUES (?, ?, ?, ?, ?)";
        String updateStandingSql = "INSERT INTO bms.Standing (Bicycle_ID, Parking_Place) VALUES (?, ?)";

        try {
            con.setAutoCommit(false); // Start transaction

            // Ensure parking place exists
            try (PreparedStatement addParkingPs = con.prepareStatement(addParkingSql)) {
                addParkingPs.setString(1, parkingPlace);
                addParkingPs.setTimestamp(2, issueTime);
                addParkingPs.executeUpdate();
            }

            // Add rent
            try (PreparedStatement addRentPs = con.prepareStatement(addRentSql)) {
                addRentPs.setInt(1, userId);
                addRentPs.setInt(2, bicycleId);
                addRentPs.setString(3, parkingPlace);
                addRentPs.setDouble(4, deposit);
                addRentPs.setTimestamp(5, issueTime);
                addRentPs.executeUpdate();
            }

            // Update standing
            try (PreparedStatement updateStandingPs = con.prepareStatement(updateStandingSql)) {
                updateStandingPs.setInt(1, bicycleId);
                updateStandingPs.setString(2, parkingPlace);
                updateStandingPs.executeUpdate();
            }

            con.commit(); // Commit transaction
            System.out.println("Bicycle rented, parking added if necessary, and status updated successfully.");
        } catch (SQLException e) {
            con.rollback(); // Rollback transaction on error
            throw e;
        } finally {
            con.setAutoCommit(true); // Reset auto-commit
        }
    }

    public void deleteRent(int rentId) throws SQLException {
        String sql = "DELETE FROM bms.Rent WHERE Rent_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rentId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted Rent record with ID: " + rentId);
            } else {
                System.out.println("No Rent record found with ID: " + rentId);
            }
        }
    }
}
