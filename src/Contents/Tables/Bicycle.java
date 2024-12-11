package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Bicycle extends ShowContents {

    public Bicycle(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Bicycles:");
            outputInfo(rs);
        }
    }

    private void outputInfo(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int bicycleId = rs.getInt("Bicycle_ID");
            double price = rs.getDouble("Price");
            String color = rs.getString("Color");
            String brand = rs.getString("Brand");
            int releaseYear = rs.getInt("Release_Date");

            System.out.printf(
                    "Bicycle ID: %d, Price: %.2f, Color: %s, Brand: %s, Release Year: %d%n",
                    bicycleId, price, color, brand, releaseYear
            );
        }
    }
    /**
     * Search a bicycle by its exact ID.
     *
     * @param bicycleId The bicycle ID to search for.
     */
    public void searchBicycleById(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Search results for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No bicycle found with the given ID.");
                }
                outputInfo(rs);
            }
        }
    }



    /**
     * Search bicycles by brand name (case-insensitive partial match).
     *
     * @param brandPartial The brand name (or part of it) to search for.
     */
    public void searchBicycleByBrand(String brandPartial) throws SQLException {
        String sql = "SELECT * FROM bms.Bicycle WHERE Brand ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + brandPartial + "%");
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Search results for brand: " + brandPartial);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No bicycles found matching the given brand search term.");
                }
                outputInfo(rs);
            }
        }
    }

    /**
     * Adds a new bicycle entry in the bms.Bicycle table.
     *
     * @param price       Bicycle price (>0 as per constraint)
     * @param color       Color of the bicycle
     * @param brand       Brand name of the bicycle (not null)
     * @param releaseYear Release year of the bicycle
     */
    public void addBicycle(double price, String color, String brand, int releaseYear) throws SQLException {
        String sql = "INSERT INTO bms.Bicycle (Price, Color, Brand, Release_Date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, price);
            ps.setString(2, color);
            ps.setString(3, brand);
            ps.setInt(4, releaseYear);
            ps.executeUpdate();
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

    /**
     * Deletes a bicycle by its exact ID.
     *
     * @param bicycleId The bicycle ID to delete.
     * @throws SQLException if an SQL exception occurs.
     */
    public void deleteBicycle(int bicycleId) throws SQLException {
        String sql = "DELETE FROM bms.Bicycle WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted bicycle with ID: " + bicycleId);
            } else {
                System.out.println("No bicycle found with ID: " + bicycleId);
            }
        }
    }
}
