package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Logs extends ShowContents {

    public Logs(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Logs");
        System.out.println("Logs:");

        while (rs.next()) {
            int logId = rs.getInt("Log_ID");
            String event = rs.getString("Event");
            Timestamp timestamp = rs.getTimestamp("Timestamp");

            System.out.printf(
                    "Log ID: %d, Event: %s, Timestamp: %s%n",
                    logId, event, timestamp
            );
        }

        rs.close();
    }
}
