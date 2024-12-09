package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Standing extends ShowContents {

    public Standing(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Standing");
        System.out.println("Standing:");

        while (rs.next()) {
            int bicycleId = rs.getInt("Bicycle_ID");
            String parkingPlace = rs.getString("Parking_Place");

            System.out.printf(
                    "Bicycle ID: %d, Parking Place: %s%n",
                    bicycleId, parkingPlace
            );
        }

        rs.close();
    }
}
