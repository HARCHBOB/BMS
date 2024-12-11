package Contents.Views;

import Contents.ShowContents;
import java.sql.*;

public class ActiveRentals extends ShowContents {

    public ActiveRentals(Connection con) {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Active_Rentals";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Active Rentals:");
            while (rs.next()) {
                int rentId = rs.getInt("Rent_ID");
                int userId = rs.getInt("User_ID");
                int bicycleId = rs.getInt("Bicycle_ID");
                String parkingPlace = rs.getString("Parking_Place");
                double deposit = rs.getDouble("Deposit");
                Timestamp issueDate = rs.getTimestamp("Issue_Time");
                Timestamp returnDate = rs.getTimestamp("Return_Time");

                System.out.printf(
                        "Rent ID: %d, User ID: %d, Bicycle ID: %d, Parking Place: %s, Deposit: %f",
                        rentId, userId, bicycleId, parkingPlace, deposit
                );
            }
        }
    }
}
