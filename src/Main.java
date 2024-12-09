import Contents.Tables.*;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "1234");

            Bicycle bicycle = new Bicycle(con);
            Client client = new Client(con);
            Defects defects = new Defects(con);
            Employee employee = new Employee(con);
            Logs logs = new Logs(con);
            Parking parking = new Parking(con);
            Rent rent = new Rent(con);
            Standing standing = new Standing(con);
            Taken taken = new Taken(con);
            User user = new User(con);
            WhereToTake whereToTake = new WhereToTake(con);

            bicycle.showAll();
            client.showAll();
            defects.showAll();
            employee.showAll();
            logs.showAll();
            parking.showAll();
            rent.showAll();
            standing.showAll();
            taken.showAll();
            user.showAll();
            whereToTake.showAll();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
