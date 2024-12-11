package Contents.MaterializedViews;

import Contents.ShowContents;
import java.sql.*;

/**
 * Handles fetching and displaying all records from the Bicycle_Status materialized view.
 */
public class BicycleStatus extends ShowContents {

    public BicycleStatus(Connection con) {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle_Status";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Bicycle Status:");
            while (rs.next()) {
                int bicycleId = rs.getInt("Bicycle_ID");
                String brand = rs.getString("Brand");
                String color = rs.getString("Color");
                String status = rs.getString("Status");

                System.out.printf(
                        "Bicycle ID: %d, Brand: %s, Color: %s, Status: %s%n",
                        bicycleId, brand, color, status
                );
            }
        }
    }

    public void searchByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle_Status WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Bicycle ID.");
                }

                while (rs.next()) {
                    String brand = rs.getString("Brand");
                    String color = rs.getString("Color");
                    String status = rs.getString("Status");

                    System.out.printf(
                            "Bicycle ID: %d, Brand: %s, Color: %s, Status: %s%n",
                            bicycleId, brand, color, status
                    );
                }
            }
        }
    }

    public void searchByBicycleStatus(String status) throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle_Status WHERE Status ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Status: " + status);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Status.");
                }

                while (rs.next()) {
                    int bicycleId = rs.getInt("Bicycle_ID");
                    String brand = rs.getString("Brand");
                    String color = rs.getString("Color");

                    System.out.printf(
                            "Bicycle ID: %d, Brand: %s, Color: %s, Status: %s%n",
                            bicycleId, brand, color, status
                    );
                }
            }
        }
    }
}