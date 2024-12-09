package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Bicycle extends ShowContents {

    public Bicycle(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.bicycle");
        System.out.println("Bicycles:");

        while (rs.next()) {
            int bicycleId = rs.getInt("Bicycle_ID");
            double price = rs.getDouble("Price");
            String color = rs.getString("Color");
            String brand = rs.getString("Brand");
            int releaseYear = rs.getInt("Release_Date");

            System.out.printf(
                    "ID: %d, Price: %.2f, Color: %s, Brand: %s, Release Year: %d%n",
                    bicycleId, price, color, brand, releaseYear
            );
        }

        rs.close();
    }
}

