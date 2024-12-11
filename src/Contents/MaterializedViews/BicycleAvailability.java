package Contents.MaterializedViews;

import Contents.ShowContents;
import java.sql.*;

/**
 * Handles fetching and displaying all records from the Bicycle_Availability materialized view.
 */
public class BicycleAvailability extends ShowContents {

    public BicycleAvailability(Connection con) {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle_Availability";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Bicycle Availability:");
            while (rs.next()) {
                int bicycleId = rs.getInt("Bicycle_ID");
                int availableCount = rs.getInt("Available_Count");
                System.out.printf(
                        "Bicycle ID: %d, Available Count: %d%n",
                        bicycleId, availableCount
                );
            }
        }
    }

    public void searchByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle_Availability WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Bicycle ID.");
                }

                while (rs.next()) {
                    int availableCount = rs.getInt("Available_Count");

                    System.out.printf(
                            "Bicycle ID: %d, Available Count: %d%n",
                            bicycleId, availableCount
                    );
                }
            }
        }
    }

    public void searchByBicycleAvailability(int availableCount) throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle_Availability WHERE Available_Count = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, availableCount);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Available Count: " + availableCount);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Available Count.");
                }

                while (rs.next()) {
                    int bicycleId = rs.getInt("Bicycle_ID");

                    System.out.printf(
                            "Bicycle ID: %d, Rent ID: %d%n",
                            bicycleId, availableCount
                    );
                }
            }
        }
    }
}