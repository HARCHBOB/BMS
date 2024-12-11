package Contents.MaterializedViews;

import Contents.ShowContents;
import java.sql.*;

/**
 * Handles fetching and displaying all records from the Parking_Usage materialized view.
 */
public class ParkingUsage extends ShowContents {

    public ParkingUsage(Connection con) {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Parking_Usage";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Parking Usage:");
            while (rs.next()) {
                String parkingPlace = rs.getString("Parking_Place");
                int usageCount = rs.getInt("Usage_Count");
                System.out.printf(
                        "Parking Place: %s, Usage Count: %d%n",
                        parkingPlace, usageCount
                );
            }
        }
    }

    public void searchByParkingPlace(String parkingPlace) throws SQLException {
        String sql = "SELECT * FROM bms.Parking_Usage WHERE Parking_Place ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Parking Place: " + parkingPlace);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Parking Place.");
                }

                while (rs.next()) {
                    int usageCount = rs.getInt("Usage_Count");

                    System.out.printf(
                            "Parking Place: %s, Usage Count: %d%n",
                            parkingPlace, usageCount
                    );
                }
            }
        }
    }

    public void searchByUsageCount(int usageCount) throws SQLException {
        String sql = "SELECT * FROM bms.Parking_Usage WHERE Usage_Count = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usageCount);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Parking Usage: " + usageCount);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Parking Usage.");
                }

                while (rs.next()) {
                    String parkingPlace = rs.getString("Parking_Place");

                    System.out.printf(
                            "Parking Place: %s, Usage Count: %d%n",
                            parkingPlace, usageCount
                    );
                }
            }
        }
    }
}