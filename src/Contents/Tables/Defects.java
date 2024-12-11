package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Defects extends ShowContents {

    public Defects(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Defects";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        }
    }

    public void searchDefectsByBicycleId(int bicycleId) throws SQLException {
        String sql = "SELECT * FROM bms.Defects WHERE Bicycle_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Defects for Bicycle ID: " + bicycleId);
                if (!rs.isBeforeFirst()) {
                    System.out.println("No defects found for this bicycle.");
                }

                while (rs.next()) {
                    int defectId = rs.getInt("Defect_ID");
                    String defect = rs.getString("Defect");
                    Date date = rs.getDate("Date");
                    String zone = rs.getString("Zone");

                    System.out.printf(
                            "Defect ID: %d, Bicycle ID: %d, Defect: %s, Date: %s, Zone: %s%n",
                            defectId, bicycleId, defect, date, zone
                    );
                }
            }
        }
    }

    public void addDefect(int bicycleId, String defect, Date date, String zone) throws SQLException {
        String sql = "INSERT INTO bms.Defects (Bicycle_ID, Defect, Date, Zone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bicycleId);
            ps.setString(2, defect);
            ps.setDate(3, date);
            ps.setString(4, zone);

            ps.executeUpdate();
            System.out.println("New defect added successfully.");
        }
    }

    public void deleteDefect(int defectId) throws SQLException {
        String sql = "DELETE FROM bms.Defects WHERE Defect_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, defectId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted Defect with ID: " + defectId);
            } else {
                System.out.println("No Defect found with ID: " + defectId);
            }
        }
    }
}
