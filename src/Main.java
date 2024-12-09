import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        try {
            // Create a new user
            User newUser = new User();
            newUser.setVardasPavarde("Jonas Jonaitis");
            newUser.setGimimoData(Date.valueOf("1990-01-01"));
            newUser.setPasoNumeris("AB1234567");
            newUser.setGatve("Main St");
            newUser.setNamas("10");
            newUser.setButas("2A");
            userDAO.createUser(newUser);

            // Fetch and print all users
            for (User u : userDAO.getAllUsers()) {
                System.out.println(u.getUserId() + " " + u.getVardasPavarde());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
