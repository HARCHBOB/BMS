package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Defects extends ShowContents {

    public Defects(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Defects");
        System.out.println("Defects:");

        while (rs.next()) {
            int defectId = rs.getInt("Defect_ID");
            int bicycleId = rs.getInt("Bicycle_ID");
            String defect = rs.getString("Defect");
            Date date = rs.getDate("Date");
            String zone = rs.getString("Zone");

            System.out.printf(
                    "Defect ID: %d, Bicycle ID: %d, Defect: %s, Date: %s, Zone: %s%n",
                    defectId, bicycleId, defect, date, zone
            );
        }

        rs.close();
    }
}
