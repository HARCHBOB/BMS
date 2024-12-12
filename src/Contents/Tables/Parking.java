package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Parking extends ShowContents {

    public Parking(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT Parking_Place, Capacity, LocationDetails FROM bms.Parking";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Parking:");
            while (rs.next()) {
                String parkingPlace = rs.getString("Parking_Place");
                int capacity = rs.getInt("Capacity");
                String locationDetails = rs.getString("LocationDetails");

                System.out.printf(
                        "Parking Place: %s, Capacity: %d, Location: %s%n",
                        parkingPlace, capacity, locationDetails
                );
            }
        }
    }

    public void searchByParkingPlace(String parkingPlace) throws SQLException {
        String sql = "SELECT Parking_Place, Capacity, LocationDetails FROM bms.Parking WHERE Parking_Place = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Parking Place: " + parkingPlace);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Parking Place.");
                }

                while (rs.next()) {
                    int capacity = rs.getInt("Capacity");
                    String locationDetails = rs.getString("LocationDetails");

                    System.out.printf(
                            "Parking Place: %s, Capacity: %d, Location: %s%n",
                            parkingPlace, capacity, locationDetails
                    );
                }
            }
        }
    }

    /**
     * Adds a new parking place record.
     * @param parkingPlace The name of the parking place.
     * @param capacity The capacity of this parking place.
     * @param locationDetails Additional details about the location.
     */
    public void addParking(String parkingPlace, int capacity, String locationDetails) throws SQLException {
        String sql = "INSERT INTO bms.Parking (Parking_Place, Capacity, LocationDetails) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);
            ps.setInt(2, capacity);
            ps.setString(3, locationDetails);

            ps.executeUpdate();
            System.out.println("New parking entry added successfully.");
        }
    }

    /**
     * Deletes parking records matching the given place pattern.
     * @param parkingPlace A partial or full name of the parking place.
     */
    public void deleteParking(String parkingPlace) throws SQLException {
        String sql = "DELETE FROM bms.Parking WHERE Parking_Place ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + parkingPlace + "%");
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted Parking record(s) for place: " + parkingPlace);
            } else {
                System.out.println("No Parking record found for place: " + parkingPlace);
            }
        }
    }
}
