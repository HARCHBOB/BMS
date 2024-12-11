package Contents;

import java.sql.*;

public abstract class ShowContents {
    protected Connection con;

    public ShowContents(Connection con) {
        this.con = con;
    }


    public abstract void showAll() throws SQLException;


    protected ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);
        setParameters(ps, params);
        return ps.executeQuery();
    }

    protected int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        }
    }

    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
    }

    protected void closeResources(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
