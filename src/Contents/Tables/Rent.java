package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Rent extends ShowContents {

    public Rent(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Rent";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Rents:");
            while (rs.next()) {
                int rentId = rs.getInt("Rent_ID");
                int userId = rs.getInt("User_ID");
                int bicycleId = rs.getInt("Bicycle_ID");
                String parkingPlace = rs.getString("Parking_Place");
                double deposit = rs.getDouble("Deposit");
                Timestamp issueTime = rs.getTimestamp("Issue_Time");
                Timestamp returnTime = rs.getTimestamp("Return_Time");

                System.out.printf(
                        "Rent ID: %d, User ID: %d, Bicycle ID: %d, Parking: %s, Deposit: %.2f, Issue: %s, Return: %s%n",
                        rentId, userId, bicycleId, parkingPlace, deposit, issueTime, returnTime
                );
            }
        }
    }

    /**
     * Search for rents by User ID.
     *
     * @param userId The User ID to search for.
     */
    public void searchRentByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM bms.Rent WHERE User_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Rents for User ID: " + userId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No rents found for this User ID.");
                }

                while (rs.next()) {
                    int rentId = rs.getInt("Rent_ID");
                    int bicycleId = rs.getInt("Bicycle_ID");
                    String parkingPlace = rs.getString("Parking_Place");
                    double deposit = rs.getDouble("Deposit");
                    Timestamp issueTime = rs.getTimestamp("Issue_Time");
                    Timestamp returnTime = rs.getTimestamp("Return_Time");

                    System.out.printf(
                            "Rent ID: %d, Bicycle ID: %d, Parking: %s, Deposit: %.2f, Issue: %s, Return: %s%n",
                            rentId, bicycleId, parkingPlace, deposit, issueTime, returnTime
                    );
                }
            }
        }
    }

    /**
     * Search for rents by Bicycle ID.
     *
     * @param bicycleId The Bicycle ID to search for.
     */
    public void searchRentByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Rent WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Rents for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No rents found for this Bicycle ID.");
                }

                while (rs.next()) {
                    int rentId = rs.getInt("Rent_ID");
                    int userId = rs.getInt("User_ID");
                    String parkingPlace = rs.getString("Parking_Place");
                    double deposit = rs.getDouble("Deposit");
                    Timestamp issueTime = rs.getTimestamp("Issue_Time");
                    Timestamp returnTime = rs.getTimestamp("Return_Time");

                    System.out.printf(
                            "Rent ID: %d, User ID: %d, Parking: %s, Deposit: %.2f, Issue: %s, Return: %s%n",
                            rentId, userId, parkingPlace, deposit, issueTime, returnTime
                    );
                }
            }
        }
    }

    /**
     * Add a new rent record.
     *
     * @param userId       The ID of the user renting the bicycle.
     * @param bicycleId    The ID of the bicycle being rented.
     * @param parkingPlace The parking place associated with the rent.
     * @param deposit      The deposit amount for the rent.
     * @param issueTime    The timestamp when the rent was issued.
     */
    public void addRent(int userId, int bicycleId, String parkingPlace, double deposit, Timestamp issueTime) throws SQLException {
        String sql = "INSERT INTO bms.Rent (User_ID, Bicycle_ID, Parking_Place, Deposit, Issue_Time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bicycleId);
            ps.setString(3, parkingPlace);
            ps.setDouble(4, deposit);
            ps.setTimestamp(5, issueTime);

            ps.executeUpdate();
            System.out.println("New rent added successfully.");
        }
    }
}
