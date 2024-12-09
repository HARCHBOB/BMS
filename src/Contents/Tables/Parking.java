package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Parking extends ShowContents {

    public Parking(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Parking");
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

        rs.close();
    }
}
