package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Standing extends ShowContents {

    public Standing(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT Bicycle_ID, Parking_Place, Parking_Beginning, Parking_Ending FROM bms.Standing";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Standing:");
            while (rs.next()) {
                int bicycleId = rs.getInt("Bicycle_ID");
                String parkingPlace = rs.getString("Parking_Place");
                Timestamp parkingBeginning = rs.getTimestamp("Parking_Beginning");
                Timestamp parkingEnding = rs.getTimestamp("Parking_Ending");

                System.out.printf(
                        "Bicycle ID: %d, Parking Place: %s, Start: %s, End: %s%n",
                        bicycleId, parkingPlace, parkingBeginning, parkingEnding
                );
            }
        }
    }

    public void searchByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT Bicycle_ID, Parking_Place, Parking_Beginning, Parking_Ending FROM bms.Standing WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Bicycle ID.");
                }

                while (rs.next()) {
                    String parkingPlace = rs.getString("Parking_Place");
                    Timestamp parkingBeginning = rs.getTimestamp("Parking_Beginning");
                    Timestamp parkingEnding = rs.getTimestamp("Parking_Ending");

                    System.out.printf(
                            "Bicycle ID: %d, Parking Place: %s, Start: %s, End: %s%n",
                            bicycleId, parkingPlace, parkingBeginning, parkingEnding
                    );
                }
            }
        }
    }

    public void searchByParkingPlace(String parkingPlace) throws SQLException {
        String sql = "SELECT Bicycle_ID, Parking_Place, Parking_Beginning, Parking_Ending FROM bms.Standing WHERE Parking_Place = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Parking Place: " + parkingPlace);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Parking Place.");
                }

                while (rs.next()) {
                    int bicycleId = rs.getInt("Bicycle_ID");
                    Timestamp parkingBeginning = rs.getTimestamp("Parking_Beginning");
                    Timestamp parkingEnding = rs.getTimestamp("Parking_Ending");

                    System.out.printf(
                            "Bicycle ID: %d, Parking Place: %s, Start: %s, End: %s%n",
                            bicycleId, parkingPlace, parkingBeginning, parkingEnding
                    );
                }
            }
        }
    }

    /**
     * Adds a new standing entry for a bicycle at a given parking place.
     * Parking_Beginning is set to CURRENT_TIMESTAMP by default.
     * If you need a specific start time, add a parameter and adjust the SQL accordingly.
     */
    public void addStanding(int bicycleId, String parkingPlace) throws SQLException {
        String sql = "INSERT INTO bms.Standing (Bicycle_ID, Parking_Place) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setString(2, parkingPlace);

            ps.executeUpdate();
            System.out.println("New standing entry added successfully.");
        }
    }

    /**
     * Ends a standing period by updating the Parking_Ending time to the current timestamp.
     * This is often more realistic than deleting the record if you want history.
     */
    public void endStanding(int bicycleId, String parkingPlace) throws SQLException {
        String sql = "UPDATE bms.Standing SET Parking_Ending = CURRENT_TIMESTAMP WHERE Bicycle_ID = ? AND Parking_Place = ? AND Parking_Ending IS NULL";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setString(2, parkingPlace);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.printf("Parking_Ending updated for Bicycle ID %d at %s.%n", bicycleId, parkingPlace);
            } else {
                System.out.printf("No ongoing standing record found for Bicycle ID %d at %s.%n", bicycleId, parkingPlace);
            }
        }
    }

    /**
     * Deletes a standing record. This might not be commonly needed if you prefer to end the standing by setting Parking_Ending.
     * Use with caution as it removes the historical data.
     */
    public void deleteStanding(int bicycleId, String parkingPlace) throws SQLException {
        String sql = "DELETE FROM bms.Standing WHERE Bicycle_ID = ? AND Parking_Place = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setString(2, parkingPlace);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.printf("Successfully deleted Standing record with Bicycle ID %d and Parking Place %s.%n", bicycleId, parkingPlace);
            } else {
                System.out.printf("No Standing record found with Bicycle ID %d and Parking Place %s.%n", bicycleId, parkingPlace);
            }
        }
    }
}
