package Contents.Transactions;

import java.sql.*;

public class BicycleTransaction {
    private final Connection con;

    public BicycleTransaction(Connection con) {
        this.con = con;
    }

    /**
     * Rents a bicycle and updates its status in a single transaction.
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



    /**
     * Updates a bicycle's price and related defects in a single transaction.
     */
    public void updateBicycleAndDefects(int bicycleId, double newPrice, String defectUpdate) throws SQLException {
        String updateBicycleSql = "UPDATE bms.Bicycle SET Price = ? WHERE Bicycle_ID = ?";
        String updateDefectSql = "UPDATE bms.Defects SET Defect = ? WHERE Bicycle_ID = ?";

        try {
            con.setAutoCommit(false); // Start transaction

            // Update bicycle
            try (PreparedStatement updateBicyclePs = con.prepareStatement(updateBicycleSql)) {
                updateBicyclePs.setDouble(1, newPrice);
                updateBicyclePs.setInt(2, bicycleId);
                updateBicyclePs.executeUpdate();
            }

            // Update defects
            try (PreparedStatement updateDefectPs = con.prepareStatement(updateDefectSql)) {
                updateDefectPs.setString(1, defectUpdate);
                updateDefectPs.setInt(2, bicycleId);
                updateDefectPs.executeUpdate();
            }

            con.commit(); // Commit transaction
            System.out.println("Bicycle and related defects updated successfully.");
        } catch (SQLException e) {
            con.rollback(); // Rollback transaction on error
            throw e;
        } finally {
            con.setAutoCommit(true); // Reset auto-commit
        }
    }
}
