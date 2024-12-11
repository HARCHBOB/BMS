import Contents.MaterializedViews.BicycleAvailability;
import Contents.MaterializedViews.BicycleStatus;
import Contents.Tables.*;
import Contents.Transactions.*;
import Contents.Views.ActiveRentals;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "1234");
            Scanner scanner = new Scanner(System.in);

            boolean run = true;
            do {
                int answer = showOptions(scanner);
                switch (answer) {
                    case 1:
                        ActiveRentals ar = new ActiveRentals(con);
                        ar.showAll();
                        System.in.read();
                        break;
                    case 2:
                        BicycleStatus bs = new BicycleStatus(con);
                        bs.searchByBicycleStatus("Parked");
                        System.in.read();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        run = false;
                        break;
                    default:
                        System.out.println("Unknown command. Try again.");
                }
            } while (run);

            {/*Bicycle bicycle = new Bicycle(con);
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

            System.out.println("SHOWING EXISTING RECORDS:");
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
            whereToTake.showAll();*/}

            {/*System.out.println("\n TEST TRANSACTIONS:");
            UserClientTransaction userClientTx = new UserClientTransaction(con);
            BicycleTransaction bicycleTx = new BicycleTransaction(con);

            // Add user and client in a transaction
            userClientTx.addUserAndClient("Jane Doe", Date.valueOf("1992-06-15"), "XYZ123456", "Oak Street", "12", "5B", 100.0);

            // Rent a bicycle and update its status
            bicycleTx.rentBicycle(8, 1, "Main Parking", 150.0, Timestamp.valueOf("2024-12-10 09:00:00"));

            // Update bicycle price and related defects
            bicycleTx.updateBicycleAndDefects(1, 350.0, "Brake pads replaced");

            // Delete user and related client
            userClientTx.deleteUserAndClient(8);*/}

            {/* // Testing Safety:
            System.out.println("\nTESTING INSERTS AND SEARCH");

            // Create User (required for Client, Rent, Employee)
            Date dob = Date.valueOf("1990-01-01");
            user.createUser("John Doe", dob, "ABC123456", "MainStreet", "10", "2A");
            System.out.println("New user created.");
            user.searchUserByName("John");

            // Adds Bicycle (required for Defects, Rent, Parking, Standing)
            bicycle.addBicycle(299.99, "Red", "Trek", 2022);
            System.out.println("New bicycle added.");
            bicycle.searchBicycleByBrand("Trek");

            // Create Parking (required for WhereToTake)
            Timestamp start = Timestamp.valueOf("2024-12-09 08:00:00");
            Timestamp end = Timestamp.valueOf("2024-12-09 18:00:00");
            parking.addParking("Central Parking", 1, start, end);
            System.out.println("New parking entry added.");
            parking.searchByParkingPlace("Central Parking");

            // Create Rent (required for Taken, WhereToTake)
            Timestamp issueTime = Timestamp.valueOf("2024-12-09 10:00:00");
            rent.addRent(1, 1, "Central Parking", 200.00, issueTime);
            System.out.println("New rent entry added.");
            rent.searchRentByUserId(1);

            // Create WhereToTake (requires Parking and Rent)
            whereToTake.addWhereToTake("Central Parking", 1);
            System.out.println("New WhereToTake entry added.");
            whereToTake.searchByParkingPlace("Central Parking");

            // Create Standing (requires Parking and Bicycle)
            standing.addStanding(1, "Central Parking");
            System.out.println("New Standing entry added.");
            standing.searchByParkingPlace("Central Parking");

            // Create Taken (requires Rent and Bicycle)
            taken.addTaken(1, 1);
            System.out.println("New Taken entry added.");
            taken.searchByBicycleId(1);

            // Create Client (requires User)
            client.createClient(1, 150.50);
            System.out.println("New client created.");
            client.searchClientsByUserId(1);

            // Create Employee (requires User)
            employee.addEmployee(1, 4500.50);
            System.out.println("New employee created.");
            employee.searchEmployeeByUserId(1);

            // Add Defect (requires Bicycle)
            defects.addDefect(1, "Flat tire", Date.valueOf("2024-12-09"), "Front wheel");
            System.out.println("New defect created.");
            defects.searchDefectsByBicycleId(1);*/}

            scanner.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int showOptions(Scanner scanner) throws IOException {
        System.out.println(
                """
                        Bike Management System
                        Chose what you want to do:
                        1. Show active rents.
                        2. Show available bikes.
                        3. Bikes.
                        4. Users.
                        5. Rents.
                        6. Exit.
                        """
        );

        System.out.print("Your command: ");
        return scanner.nextInt();
    }
}
