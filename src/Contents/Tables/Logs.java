package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class Logs extends ShowContents {

    public Logs(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.Logs";
        try (ResultSet rs = executeQuery(sql)) {
            System.out.println("Logs:");
            while (rs.next()) {
                int logId = rs.getInt("Log_ID");
                String event = rs.getString("Event");
                Timestamp timestamp = rs.getTimestamp("Timestamp");

                System.out.printf("Log ID: %d, Event: %s, Timestamp: %s%n", logId, event, timestamp);
            }
        }
    }

    /**
     * Add a new log entry to the bms.Logs table.
     *
     * @param event The event description to log.
     */
    public void addLog(String event) throws SQLException {
        String sql = "INSERT INTO bms.Logs (Event) VALUES (?)";
        executeUpdate(sql, event);
        System.out.println("Log entry added successfully: " + event);
    }

    /**
     * Search logs by event description.
     *
     * @param eventPartial Partial or full event description to search for.
     */
    public void searchLogsByEvent(String eventPartial) throws SQLException {
        String sql = "SELECT * FROM bms.Logs WHERE Event ILIKE ?";
        try (ResultSet rs = executeQuery(sql, "%" + eventPartial + "%")) {
            System.out.println("Search results for event: " + eventPartial);
            if (!rs.isBeforeFirst()) {
                System.out.println("No logs found matching the event.");
                return;
            }
            while (rs.next()) {
                int logId = rs.getInt("Log_ID");
                String event = rs.getString("Event");
                Timestamp timestamp = rs.getTimestamp("Timestamp");

                System.out.printf("Log ID: %d, Event: %s, Timestamp: %s%n", logId, event, timestamp);
            }
        }
    }
}
