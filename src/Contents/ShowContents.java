package Contents;

import java.sql.*;

public abstract class ShowContents {
    protected Connection con;

    /**
     * Constructor to initialize the database connection.
     *
     * @param con Database connection object.
     */
    public ShowContents(Connection con) {
        this.con = con;
    }

    /**
     * Abstract method to show all entries. Subclasses must implement this.
     *
     * @throws SQLException If a database access error occurs.
     */
    public abstract void showAll() throws SQLException;

    /**
     * Executes a SELECT query and returns a ResultSet.
     *
     * @param query The SQL SELECT query.
     * @param params Parameters for the query.
     * @return ResultSet containing the query results.
     * @throws SQLException If a database access error occurs.
     */
    protected ResultSet executeQuery(String query, Object... params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);
        setParameters(ps, params);
        return ps.executeQuery();
    }

    /**
     * Executes an UPDATE, INSERT, or DELETE query.
     *
     * @param query The SQL query to execute.
     * @param params Parameters for the query.
     * @return The number of rows affected.
     * @throws SQLException If a database access error occurs.
     */
    protected int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        }
    }

    /**
     * Sets parameters for a PreparedStatement.
     *
     * @param ps The PreparedStatement.
     * @param params Parameters to set.
     * @throws SQLException If a database access error occurs.
     */
    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
    }

    /**
     * Closes the resources used by the database operation.
     *
     * @param rs ResultSet to close (can be null).
     * @param stmt Statement to close (can be null).
     */
    protected void closeResources(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
