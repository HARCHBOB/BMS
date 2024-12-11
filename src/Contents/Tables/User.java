package Contents.Tables;

import Contents.ShowContents;
import java.sql.*;

public class User extends ShowContents {

    public User(Connection con) throws SQLException {
        super(con);
    }

    @Override
    public void showAll() throws SQLException {
        String sql = "SELECT * FROM bms.User";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Users:");
            outputInfo(rs);
        }
    }

    /**
     * Search for users by a partial or full match of their name.
     * Uses ILIKE for case-insensitive search (PostgreSQL specific).
     *
     * @param name The name or partial name to search for.
     */
    public void searchUserByName(String name) throws SQLException {
        String sql = "SELECT * FROM bms.User WHERE Name_Surname ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // For partial matching, you can wrap the input with '%' wildcards.
            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Search results for: " + name);
                outputInfo(rs);
            }
        }
    }

    private void outputInfo(ResultSet rs) throws SQLException {
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
    }

    /**
     * Create a new user record in the database.
     * Make sure to validate the input outside of this method (e.g., correct date format, etc.)
     *
     * @param nameSurname    The user's full name
     * @param dob            The user's date of birth as a java.sql.Date
     * @param passportNumber The user's unique passport number
     * @param street         The street where user lives
     * @param house          The house number
     * @param apartment      The apartment number (can be null or N/A if not applicable)
     */
    public void createUser(String nameSurname, Date dob, String passportNumber, String street, String house, String apartment) throws SQLException {
        String insertSql = "INSERT INTO bms.User (Name_Surname, Date_of_birth, Passport_Number, Street, House, Apartment) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertSql)) {
            ps.setString(1, nameSurname);
            ps.setDate(2, dob);
            ps.setString(3, passportNumber);
            ps.setString(4, street != null ? street : "N/A");
            ps.setString(5, house != null ? house : "N/A");
            ps.setString(6, apartment != null ? apartment : "N/A");
            ps.executeUpdate();
        }
    }

    public void deleteUserById(int userId) throws SQLException {
        String sql = "DELETE FROM bms.User WHERE User_ID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted User with ID: " + userId);
            } else {
                System.out.println("No User found with ID: " + userId);
            }
        }
    }

    public void deleteUserByName(String name) throws SQLException {
        String sql = "DELETE FROM bms.User WHERE Name_Surname ILIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted User with Name: " + name);
            } else {
                System.out.println("No User found with Name: " + name);
            }
        }
    }

}
