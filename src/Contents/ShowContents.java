package Contents;

import java.sql.*;

public abstract class ShowContents {
    protected Connection con;
    protected Statement stmt;

    public ShowContents(Connection con) throws SQLException {
        this.con = con;
        stmt = con.createStatement();
    }

    public void showAll() throws SQLException{}

}
