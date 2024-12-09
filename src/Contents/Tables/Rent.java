package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Rent extends ShowContents {

    public Rent(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Rent");
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

        rs.close();
    }
}
