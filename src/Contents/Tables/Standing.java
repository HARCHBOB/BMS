package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Standing extends ShowContents {

    public Standing(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Standing";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Standing:");
            while (rs.next()) {
                int bicycleId = rs.getInt("Bicycle_ID");
                String parkingPlace = rs.getString("Parking_Place");

                System.out.printf(
                        "Bicycle ID: %d, Parking Place: %s%n",
                        bicycleId, parkingPlace
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
        String sql = "SELECT * FROM bms.Standing WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Bicycle ID.");
                }

                while (rs.next()) {
                    String parkingPlace = rs.getString("Parking_Place");

                    System.out.printf(
                            "Bicycle ID: %d, Parking Place: %s%n",
                            bicycleId, parkingPlace
                    );
                }
            }
        }
    }

    /**
     * Search for entries by Parking Place.
     *
     * @param parkingPlace The Parking Place to search for.
     */
    public void searchByParkingPlace(String parkingPlace) throws SQLException {
        String sql = "SELECT * FROM bms.Standing WHERE Parking_Place = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Parking Place: " + parkingPlace);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Parking Place.");
                }

                while (rs.next()) {
                    int bicycleId = rs.getInt("Bicycle_ID");

                    System.out.printf(
                            "Bicycle ID: %d, Parking Place: %s%n",
                            bicycleId, parkingPlace
                    );
                }
            }
        }
    }

    /**
     * Add a new entry to the Standing table.
     *
     * @param bicycleId    The Bicycle ID associated with the parking place.
     * @param parkingPlace The Parking Place associated with the bicycle.
     */
    public void addStanding(int bicycleId, String parkingPlace) throws SQLException {
        String sql = "INSERT INTO bms.Standing (Bicycle_ID, Parking_Place) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setString(2, parkingPlace);

            ps.executeUpdate();
            System.out.println("New entry added successfully.");
        }
    }

    public void deleteStanding(int bicycleId, String parkingPlace) throws SQLException {
        String sql = "DELETE FROM bms.Standing WHERE Bicycle_ID = ? AND Parking_Place ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setString(2, "%" + parkingPlace + "%");
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.printf("Successfully deleted Standing record with Bicycle ID %d and Parking Place %s ", bicycleId, parkingPlace);
            } else {
                System.out.printf("No Standing record found with Bicycle ID %d and Parking Place %s ", bicycleId, parkingPlace);
            }
        }
    }
}
