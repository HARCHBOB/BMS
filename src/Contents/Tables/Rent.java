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


    public void rentBicycle(int userId, int bicycleId, String parkingPlace, double deposit, Timestamp issueTime) throws SQLException {
        String findActiveRentSql = "SELECT Rent_ID, Parking_Place FROM bms.Rent WHERE Bicycle_ID = ? AND Return_Time IS NULL LIMIT 1";
        String deleteOldRentSql = "DELETE FROM bms.Rent WHERE Rent_ID = ?";
        String addOrUpdateParkingSql = """
        INSERT INTO bms.Parking (Parking_Place, Bicycle_ID, Parking_Beginning, Parking_Ending)
        VALUES (?, ?, ?, NULL)
        ON CONFLICT (Parking_Place)
        DO UPDATE SET Bicycle_ID = EXCLUDED.Bicycle_ID,
                      Parking_Beginning = EXCLUDED.Parking_Beginning,
                      Parking_Ending = EXCLUDED.Parking_Ending
    """;
        String addRentSql = "INSERT INTO bms.Rent (User_ID, Bicycle_ID, Parking_Place, Deposit, Issue_Time) VALUES (?, ?, ?, ?, ?)";
        String updateStandingSql = """
        INSERT INTO bms.Standing (Bicycle_ID, Parking_Place)
        VALUES (?, ?)
        ON CONFLICT (Bicycle_ID, Parking_Place) DO NOTHING
    """;

        con.setAutoCommit(false);
        try {
            // 1. Check for existing active rent for this bicycle
            Integer oldRentId = null;
            String oldRentParkingPlace = null;
            try (PreparedStatement ps = con.prepareStatement(findActiveRentSql)) {
                ps.setInt(1, bicycleId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        oldRentId = rs.getInt("Rent_ID");
                        oldRentParkingPlace = rs.getString("Parking_Place");
                    }
                }
            }

            // If there is an active rent:
            if (oldRentId != null) {
                // 2. Compare parking places
                if (oldRentParkingPlace.equals(parkingPlace)) {
                    // Same Parking Place and Bicycle ID means do nothing
                    System.out.println("Bicycle is already rented at the same parking place. Doing nothing.");
                    con.rollback();
                    return;
                } else {
                    // Different Parking Place: Delete the old rent to allow inserting the new one
                    try (PreparedStatement ps = con.prepareStatement(deleteOldRentSql)) {
                        ps.setInt(1, oldRentId);
                        ps.executeUpdate();
                    }
                }
            }

            // 3. Insert or update Parking record
            try (PreparedStatement addParkingPs = con.prepareStatement(addOrUpdateParkingSql)) {
                addParkingPs.setString(1, parkingPlace);
                addParkingPs.setInt(2, bicycleId);
                addParkingPs.setTimestamp(3, issueTime);
                addParkingPs.executeUpdate();
            }

            // 4. Add new Rent
            try (PreparedStatement addRentPs = con.prepareStatement(addRentSql)) {
                addRentPs.setInt(1, userId);
                addRentPs.setInt(2, bicycleId);
                addRentPs.setString(3, parkingPlace);
                addRentPs.setDouble(4, deposit);
                addRentPs.setTimestamp(5, issueTime);
                addRentPs.executeUpdate();
            }

            // 5. Update Standing (if needed)
            try (PreparedStatement updateStandingPs = con.prepareStatement(updateStandingSql)) {
                updateStandingPs.setInt(1, bicycleId);
                updateStandingPs.setString(2, parkingPlace);
                updateStandingPs.executeUpdate();
            }

            con.commit();
            System.out.println("Bicycle rented successfully. Old rent removed (if different location), parking updated, and standing set.");
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
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
