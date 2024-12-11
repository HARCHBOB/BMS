package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Taken extends ShowContents {

    public Taken(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Taken";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Taken:");
            while (rs.next()) {
                int bicycleId = rs.getInt("Bicycle_ID");
                int rentId = rs.getInt("Rent_ID");

                System.out.printf(
                        "Bicycle ID: %d, Rent ID: %d%n",
                        bicycleId, rentId
                );
            }
        }
    }

    /**
     * Search for entries by Bicycle ID.
     *
     * @param bicycleId The Bicycle ID to search for.
     */
    public void searchByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Taken WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Bicycle ID.");
                }

                while (rs.next()) {
                    int rentId = rs.getInt("Rent_ID");

                    System.out.printf(
                            "Bicycle ID: %d, Rent ID: %d%n",
                            bicycleId, rentId
                    );
                }
            }
        }
    }

    /**
     * Search for entries by Rent ID.
     *
     * @param rentId The Rent ID to search for.
     */
    public void searchByRentId(int rentId) throws SQLException {
        String sql = "SELECT * FROM bms.Taken WHERE Rent_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rentId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Rent ID: " + rentId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Rent ID.");
                }

                while (rs.next()) {
                    int bicycleId = rs.getInt("Bicycle_ID");

                    System.out.printf(
                            "Bicycle ID: %d, Rent ID: %d%n",
                            bicycleId, rentId
                    );
                }
            }
        }
    }

    /**
     * Add a new entry to the Taken table.
     *
     * @param bicycleId The Bicycle ID associated with the Rent ID.
     * @param rentId    The Rent ID associated with the Bicycle ID.
     */
    public void addTaken(int bicycleId, int rentId) throws SQLException {
        String sql = "INSERT INTO bms.Taken (Bicycle_ID, Rent_ID) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setInt(2, rentId);

            ps.executeUpdate();
            System.out.println("New entry added successfully.");
        }
    }

    public void deleteTaken(int bicycleId, int rentId) throws SQLException {
        String sql = "DELETE FROM bms.Taken WHERE Bicycle_ID = ? AND Rent_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setInt(2, rentId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.printf("Successfully deleted Taken record with Bicycle ID %d and Rent ID %d%n ", bicycleId, rentId);
            } else {
                System.out.printf("No Taken record found with Bicycle ID %d and Rent ID %d%n ", bicycleId, rentId);
            }
        }
    }
}