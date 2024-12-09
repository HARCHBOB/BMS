package Contents.Tables;

import Contents.ShowContents;

import java.sql.*;

public class User extends ShowContents {

    public User(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM bms.User");
        System.out.println("Users:");

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String nameSurname = rs.getString("Name_Surname");
            Date dob = rs.getDate("Date_of_birth");
            String passportNumber = rs.getString("Passport_Number");
            String street = rs.getString("Street");
            String house = rs.getString("House");
            String apartment = rs.getString("Apartment");
            Timestamp creationDate = rs.getTimestamp("Creation_date");

            System.out.printf(
                    "ID: %d, Name: %s, DOB: %s, Passport: %s, Address: %s %s %s, Created: %s%n",
                    userId, nameSurname, dob, passportNumber, street, house, apartment, creationDate
            );
        }

        rs.close();
    }
}
