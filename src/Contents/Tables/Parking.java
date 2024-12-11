package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Parking extends ShowContents {

    public Parking(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Parking";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Parking:");
            while (rs.next()) {
                String parkingPlace = rs.getString("Parking_Place");
                int bicycleId = rs.getInt("Bicycle_ID");
                Timestamp parkingBeginning = rs.getTimestamp("Parking_Beginning");
                Timestamp parkingEnding = rs.getTimestamp("Parking_Ending");

                System.out.printf(
                        "Place: %s, Bicycle ID: %d, Start: %s, End: %s%n",
                        parkingPlace, bicycleId, parkingBeginning, parkingEnding
                );
            }
        }
    }

    public void searchByParkingPlace(String parkingPlace) throws SQLException {
        String sql = "SELECT * FROM bms.Parking WHERE Parking_Place = ?";
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
                            "Place: %s, Bicycle ID: %d, Start: %s, End: %s%n",
                            parkingPlace, bicycleId, parkingBeginning, parkingEnding
                    );
                }
            }
        }
    }

    public void searchByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Parking WHERE Bicycle_ID = ?";
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
                            "Place: %s, Bicycle ID: %d, Start: %s, End: %s%n",
                            parkingPlace, bicycleId, parkingBeginning, parkingEnding
                    );
                }
            }
        }
    }

    public void addParking(String parkingPlace, int bicycleId, Timestamp parkingBeginning, Timestamp parkingEnding) throws SQLException {
        String sql = "INSERT INTO bms.Parking (Parking_Place, Bicycle_ID, Parking_Beginning, Parking_Ending) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);
            ps.setInt(2, bicycleId);
            ps.setTimestamp(3, parkingBeginning);
            ps.setTimestamp(4, parkingEnding);

            ps.executeUpdate();
            System.out.println("New parking entry added successfully.");
        }
    }

    public void deleteParking(String parkingPlace) throws SQLException {
        String sql = "DELETE FROM bms.Parking WHERE Parking_Place ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + parkingPlace + "%");
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted Parking record for place: " + parkingPlace);
            } else {
                System.out.println("No Parking record found for place: " + parkingPlace);
            }
        }
    }
}
