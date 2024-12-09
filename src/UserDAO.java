import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT User_ID, Vardas_Pavarde, Gimimo_data, Paso_numeris, Gatve, Namas, Butas, Sukurimo_data " +
                "FROM bms.User";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("User_ID"));
                u.setVardasPavarde(rs.getString("Vardas_Pavarde"));
                u.setGimimoData(rs.getDate("Gimimo_data"));
                u.setPasoNumeris(rs.getString("Paso_numeris"));
                u.setGatve(rs.getString("Gatve"));
                u.setNamas(rs.getString("Namas"));
                u.setButas(rs.getString("Butas"));
                u.setSukurimoData(rs.getTimestamp("Sukurimo_data"));
                users.add(u);
            }
        }
        return users;
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO bms.User (Vardas_Pavarde, Gimimo_data, Paso_numeris, Gatve, Namas, Butas) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getVardasPavarde());
            stmt.setDate(2, user.getGimimoData());  // Ensure user.getGimimoData() returns a java.sql.Date
            stmt.setString(3, user.getPasoNumeris());
            stmt.setString(4, user.getGatve());
            stmt.setString(5, user.getNamas());
            stmt.setString(6, user.getButas());

            stmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE bms.User SET Vardas_Pavarde = ?, Gimimo_data = ?, Paso_numeris = ?, Gatve = ?, " +
                "Namas = ?, Butas = ? WHERE User_ID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getVardasPavarde());
            stmt.setDate(2, user.getGimimoData());
            stmt.setString(3, user.getPasoNumeris());
            stmt.setString(4, user.getGatve());
            stmt.setString(5, user.getNamas());
            stmt.setString(6, user.getButas());
            stmt.setInt(7, user.getUserId());

            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM bms.User WHERE User_ID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public User findUserById(int userId) throws SQLException {
        User u = null;
        String sql = "SELECT User_ID, Vardas_Pavarde, Gimimo_data, Paso_numeris, Gatve, Namas, Butas, Sukurimo_data " +
                "FROM bms.User WHERE User_ID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u = new User();
                    u.setUserId(rs.getInt("User_ID"));
                    u.setVardasPavarde(rs.getString("Vardas_Pavarde"));
                    u.setGimimoData(rs.getDate("Gimimo_data"));
                    u.setPasoNumeris(rs.getString("Paso_numeris"));
                    u.setGatve(rs.getString("Gatve"));
                    u.setNamas(rs.getString("Namas"));
                    u.setButas(rs.getString("Butas"));
                    u.setSukurimoData(rs.getTimestamp("Sukurimo_data"));
                }
            }
        }
        return u;
    }
}
