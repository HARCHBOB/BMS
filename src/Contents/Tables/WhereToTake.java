package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class WhereToTake extends ShowContents {

    public WhereToTake(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Where_To_Take";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Where To Take:");
            while (rs.next()) {
                String parkingPlace = rs.getString("Parking_Place");
                int rentId = rs.getInt("Rent_ID");

                System.out.printf(
                        "Parking Place: %s, Rent ID: %d%n",
                        parkingPlace, rentId
                );
            }
        }
    }

    /**
     * Search for entries by Parking Place.
     *
     * @param parkingPlace The parking place to search for.
     */
    public void searchByParkingPlace(String parkingPlace) throws SQLException {
        String sql = "SELECT * FROM bms.Where_To_Take WHERE Parking_Place = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Parking Place: " + parkingPlace);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Parking Place.");
                }

                while (rs.next()) {
                    int rentId = rs.getInt("Rent_ID");

                    System.out.printf(
                            "Parking Place: %s, Rent ID: %d%n",
                            parkingPlace, rentId
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
        String sql = "SELECT * FROM bms.Where_To_Take WHERE Rent_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rentId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Entries for Rent ID: " + rentId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No entries found for this Rent ID.");
                }

                while (rs.next()) {
                    String parkingPlace = rs.getString("Parking_Place");

                    System.out.printf(
                            "Parking Place: %s, Rent ID: %d%n",
                            parkingPlace, rentId
                    );
                }
            }
        }
    }

    /**
     * Add a new entry to the Where_To_Take table.
     *
     * @param parkingPlace The parking place associated with the rent.
     * @param rentId       The Rent ID associated with the parking place.
     */
    public void addWhereToTake(String parkingPlace, int rentId) throws SQLException {
        String sql = "INSERT INTO bms.Where_To_Take (Parking_Place, Rent_ID) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, parkingPlace);
            ps.setInt(2, rentId);

            ps.executeUpdate();
            System.out.println("New entry added successfully.");
        }
    }

    public void deleteWhereToTake(String parkingPlace, int rentId) throws SQLException {
        String sql = "DELETE FROM bms.Where_To_Take WHERE Parking_Place ILIKE ? AND Rent_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + parkingPlace + "%");
            ps.setInt(2, rentId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.printf("Successfully deleted Where to Take record with Parking Place %s and Rent ID %d", parkingPlace, rentId);
            } else {
                System.out.printf("No Where to Take record found with Parking Place %s and Rent ID %d", parkingPlace, rentId);
            }
        }
    }
}
