package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class Client extends ShowContents {

    public Client(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.Client");
        System.out.println("Clients:");

        while (rs.next()) {
            int clientId = rs.getInt("Client_ID");
            int userId = rs.getInt("User_ID");
            double deposit = rs.getDouble("Deposit");
            Timestamp registrationDate = rs.getTimestamp("Registration_Date");

            System.out.printf(
                    "Client ID: %d, User ID: %d, Deposit: %.2f, Registration Date: %s%n",
                    clientId, userId, deposit, registrationDate
            );
        }

        rs.close();
    }
}
