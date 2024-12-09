package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class WhereToTake extends ShowContents {

    public WhereToTake(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Where_To_Take");
        System.out.println("Where To Take:");

        while (rs.next()) {
            String parkingPlace = rs.getString("Parking_Place");
            int rentId = rs.getInt("Rent_ID");

            System.out.printf(
                    "Parking Place: %s, Rent ID: %d%n",
                    parkingPlace, rentId
            );
        }

        rs.close();
    }
}
