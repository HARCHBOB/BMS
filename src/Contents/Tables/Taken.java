package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Taken extends ShowContents {

    public Taken(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Taken");
        System.out.println("Taken:");

        while (rs.next()) {
            int bicycleId = rs.getInt("Bicycle_ID");
            int rentId = rs.getInt("Rent_ID");

            System.out.printf(
                    "Bicycle ID: %d, Rent ID: %d%n",
                    bicycleId, rentId
            );
        }

        rs.close();
    }
}
