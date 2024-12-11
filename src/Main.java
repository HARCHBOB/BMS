import Contents.MaterializedViews.BicycleAvailability;
import Contents.MaterializedViews.BicycleStatus;
import Contents.Tables.*;
import Contents.Views.ActiveRentals;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "1234");

            Bicycle bicycleDAO = new Bicycle(con);
            User userDAO = new User(con);
            Rent rentDAO = new Rent(con);
            Parking parkingDAO = new Parking(con);

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                int choice = showOptions(scanner);
                switch (choice) {
                    case 1 -> showActiveRents(rentDAO);
                    case 2 -> showAvailableBikes(bicycleDAO);
                    case 3 -> bikesSubMenu(scanner, bicycleDAO);
                    case 4 -> usersSubMenu(scanner, userDAO);
                    case 5 -> rentsSubMenu(scanner, rentDAO, bicycleDAO, userDAO, parkingDAO);
                    case 6 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }




           /* {Bicycle bicycle = new Bicycle(con);
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
            whereToTake.showAll();}*/

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

            System.out.println("Exiting system.");
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
                Choose what you want to do:
                1. Show active rents.
                2. Show available bikes.
                3. Bikes.
                4. Users.
                5. Rents.
                6. Exit.
                """
        );
        System.out.print("Enter choice: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private static void showActiveRents(Rent rentDAO) {
        // Implement logic to show active rents, e.g., using a view like Active_Rentals
        try {
            System.out.println("Active Rents:");
            rentDAO.showAll(); // Implement a method in Rent DAO to show active rents if not exist.
        } catch (SQLException e) {
            System.out.println("Error fetching active rents: " + e.getMessage());
        }
    }

    private static void showAvailableBikes(Bicycle bicycleDAO) {
        // Implement logic to show available bikes, e.g., using a Bicycle_Availability materialized view
        try {
            System.out.println("Available Bikes:");
            bicycleDAO.showAll(); // Implement a method in Bicycle DAO to show available bikes if not exist.
        } catch (SQLException e) {
            System.out.println("Error fetching available bikes: " + e.getMessage());
        }
    }

    private static void bikesSubMenu(Scanner scanner, Bicycle bicycleDAO) {
        boolean goBack = false;
        while (!goBack) {
            System.out.println(
                    """
                    Bikes Menu:
                    1. Show all bicycles
                    2. Add a new bicycle
                    3. Search bicycle by ID
                    4. Search bicycle by brand
                    5. Go Back
                    """
            );
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    try { bicycleDAO.showAll(); } catch (SQLException e) { e.printStackTrace(); }
                }
                case "2" -> addBicycleInteractive(scanner, bicycleDAO);
                case "3" -> searchBicycleByIdInteractive(scanner, bicycleDAO);
                case "4" -> searchBicycleByBrandInteractive(scanner, bicycleDAO);
                case "5" -> goBack = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addBicycleInteractive(Scanner scanner, Bicycle bicycleDAO) {
        try {
            System.out.print("Enter Price: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter Color: ");
            String color = scanner.nextLine();

            System.out.print("Enter Brand: ");
            String brand = scanner.nextLine();

            System.out.print("Enter Release Year: ");
            int releaseYear = Integer.parseInt(scanner.nextLine());

            bicycleDAO.addBicycle(price, color, brand, releaseYear);
            System.out.println("Bicycle added successfully.");
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error adding bicycle: " + e.getMessage());
        }
    }

    private static void searchBicycleByIdInteractive(Scanner scanner, Bicycle bicycleDAO) {
        try {
            System.out.print("Enter Bicycle_ID: ");
            int bicycleId = Integer.parseInt(scanner.nextLine());
            bicycleDAO.searchBicycleById(bicycleId);
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error searching bicycle: " + e.getMessage());
        }
    }

    private static void searchBicycleByBrandInteractive(Scanner scanner, Bicycle bicycleDAO) {
        try {
            System.out.print("Enter brand (partial/complete): ");
            String brand = scanner.nextLine();
            bicycleDAO.searchBicycleByBrand(brand);
        } catch (SQLException e) {
            System.out.println("Error searching bicycle: " + e.getMessage());
        }
    }

    private static void usersSubMenu(Scanner scanner, User userDAO) {
        boolean goBack = false;
        while (!goBack) {
            System.out.println(
                    """
                    Users Menu:
                    1. Show
                    2. Create
                    3. Search (by name)
                    4. Delete
                    5. Go Back
                    """
            );
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showUsersSubMenu(scanner, userDAO);
                case "2" -> createUsersSubMenu(scanner, userDAO);
                case "3" -> searchUserByNameInteractive(scanner, userDAO);
                case "4" -> deleteUsersSubMenu(scanner, userDAO);
                case "5" -> goBack = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showUsersSubMenu(Scanner scanner, User userDAO) {
        boolean goBack = false;
        while (!goBack) {
            System.out.println(
                    """
                    Show Menu:
                    1. Show Users
                    2. Show Clients
                    3. Show Employees
                    4. Go Back
                    """
            );
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    try { userDAO.showAll(); } catch (SQLException e) { e.printStackTrace(); }
                }
                case "2" -> {
                    try { userDAO.showClients(); } catch (SQLException e) { e.printStackTrace(); }
                }
                case "3" -> {
                    try { userDAO.showEmployee(); } catch (SQLException e) { e.printStackTrace(); }
                }
                case "4" -> goBack = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createUsersSubMenu(Scanner scanner, User userDAO) {
        boolean goBack = false;
        while (!goBack) {
            System.out.println(
                    """
                    Create Menu:
                    1. Add User and Client
                    2. Add User
                    3. Add User and Employee
                    4. Go Back
                    """
            );
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addUserAndClientInteractive(scanner, userDAO);
                case "2" -> addUserInteractive(scanner, userDAO);
                case "3" -> addUserAndEmployeeInteractive(scanner, userDAO);
                case "4" -> goBack = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void deleteUsersSubMenu(Scanner scanner, User userDAO) {
        boolean goBack = false;
        while (!goBack) {
            System.out.println(
                    """
                    Delete Menu:
                    1. Delete User by ID
                    2. Delete User by Name
                    3. Delete User and Client by User_ID
                    4. Delete Client by Client_ID
                    5. Delete Employee by Admin_ID
                    6. Go Back
                    """
            );
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> deleteUserByIdInteractive(scanner, userDAO);
                case "2" -> deleteUserByNameInteractive(scanner, userDAO);
                case "3" -> deleteUserAndClientInteractive(scanner, userDAO);
                case "4" -> deleteClientInteractive(scanner, userDAO);
                case "5" -> deleteEmployeeInteractive(scanner, userDAO);
                case "6" -> goBack = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void searchUserByNameInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter name (partial/complete): ");
            String name = scanner.nextLine();
            userDAO.searchUserByName(name);
        } catch (SQLException e) {
            System.out.println("Error searching user: " + e.getMessage());
        }
    }

// === Interactive Methods for Create Sub-Menu ===

    private static void addUserInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter Name_Surname: ");
            String name = scanner.nextLine();

            System.out.print("Enter Date_of_birth (yyyy-MM-dd): ");
            String dobStr = scanner.nextLine();

            System.out.print("Enter Passport_Number: ");
            String passport = scanner.nextLine();

            System.out.print("Enter Street: ");
            String street = scanner.nextLine();

            System.out.print("Enter House: ");
            String house = scanner.nextLine();

            System.out.print("Enter Apartment: ");
            String apartment = scanner.nextLine();

            userDAO.createUser(name, Date.valueOf(dobStr), passport, street, house, apartment);
            System.out.println("User added successfully.");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void addUserAndClientInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter Name_Surname: ");
            String name = scanner.nextLine();

            System.out.print("Enter Date_of_birth (yyyy-MM-dd): ");
            String dobStr = scanner.nextLine();
            Date dob = Date.valueOf(dobStr);

            System.out.print("Enter Passport_Number: ");
            String passport = scanner.nextLine();

            System.out.print("Enter Street: ");
            String street = scanner.nextLine();

            System.out.print("Enter House: ");
            String house = scanner.nextLine();

            System.out.print("Enter Apartment: ");
            String apartment = scanner.nextLine();

            System.out.print("Enter Deposit: ");
            double deposit = Double.parseDouble(scanner.nextLine());

            userDAO.addUserAndClient(name, dob, passport, street, house, apartment, deposit);
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error adding user and client: " + e.getMessage());
        }
    }

    private static void addUserAndEmployeeInteractive(Scanner scanner, User userDAO) {
        try {
            // Add the user first
            System.out.print("Enter Name_Surname: ");
            String name = scanner.nextLine();

            System.out.print("Enter Date_of_birth (yyyy-MM-dd): ");
            Date dob = Date.valueOf(scanner.nextLine());

            System.out.print("Enter Passport_Number: ");
            String passport = scanner.nextLine();

            System.out.print("Enter Street: ");
            String street = scanner.nextLine();

            System.out.print("Enter House: ");
            String house = scanner.nextLine();

            System.out.print("Enter Apartment: ");
            String apartment = scanner.nextLine();

            userDAO.createUser(name, dob, passport, street, house, apartment);
            System.out.println("User added. Now let's add an employee record for this user.");

            // We need the user_id we just created, so you'd likely implement a method to get the last inserted user.
            // For simplicity, let's ask the user for user_id or add a method userDAO.getUserIdByPassport(passport);
            System.out.print("Enter the User_ID of the user just created: ");
            int userId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Salary: ");
            double salary = Double.parseDouble(scanner.nextLine());

            userDAO.addEmployee(userId, salary);
            System.out.println("User and Employee added successfully.");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error adding user and employee: " + e.getMessage());
        }
    }

// === Interactive Methods for Delete Sub-Menu ===

    private static void deleteUserByIdInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter User_ID to delete: ");
            int userId = Integer.parseInt(scanner.nextLine());
            userDAO.deleteUserById(userId);
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void deleteUserByNameInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter name (partial/complete) to delete: ");
            String name = scanner.nextLine();
            userDAO.deleteUserByName(name);
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void deleteUserAndClientInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter User_ID to delete user and client: ");
            int userId = Integer.parseInt(scanner.nextLine());
            userDAO.deleteUserAndClient(userId);
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error deleting user and client: " + e.getMessage());
        }
    }

    private static void deleteClientInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter Client_ID to delete: ");
            int clientId = Integer.parseInt(scanner.nextLine());
            userDAO.deleteClient(clientId);
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error deleting client: " + e.getMessage());
        }
    }

    private static void deleteEmployeeInteractive(Scanner scanner, User userDAO) {
        try {
            System.out.print("Enter Admin_ID to delete employee: ");
            int adminId = Integer.parseInt(scanner.nextLine());
            userDAO.deleteEmployee(adminId);
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }

    private static void rentsSubMenu(Scanner scanner, Rent rentDAO, Bicycle bicycleDAO, User userDAO, Parking parkingDAO) {
        boolean goBack = false;
        while (!goBack) {
            System.out.println(
                    """
                    Rents Menu:
                    1. Show all rents
                    2. Rent a bicycle (interactive)
                    3. Search rent by User_ID
                    4. Search rent by Bicycle_ID
                    5. Go Back
                    """
            );
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    try { rentDAO.showAll(); } catch (SQLException e) { e.printStackTrace(); }
                }
                case "2" -> rentBicycleInteractive(scanner, rentDAO, bicycleDAO, userDAO, parkingDAO);
                case "3" -> searchRentByUserIdInteractive(scanner, rentDAO);
                case "4" -> searchRentByBicycleIdInteractive(scanner, rentDAO);
                case "5" -> goBack = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void rentBicycleInteractive(Scanner scanner, Rent rentDAO, Bicycle bicycleDAO, User userDAO, Parking parkingDAO) {
        try {
            // Show all users and prompt User_ID
            System.out.println("Available Users:");
            userDAO.showAll();
            System.out.print("Enter User_ID: ");
            int userId = Integer.parseInt(scanner.nextLine());

            // Show all bicycles and prompt Bicycle_ID
            System.out.println("Available Bicycles:");
            bicycleDAO.showAll();
            System.out.print("Enter Bicycle_ID: ");
            int bicycleId = Integer.parseInt(scanner.nextLine());

            System.out.println("Existing Parking Places:");
            parkingDAO.showAll();
            System.out.print("Enter Existing or New Parking Place: ");
            String parkingPlace = scanner.nextLine();

            System.out.print("Enter Deposit: ");
            double deposit = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter Issue Time (yyyy-MM-dd HH:mm:ss) or press Enter for current time: ");
            String issueTimeStr = scanner.nextLine();
            Timestamp issueTime = issueTimeStr.isEmpty() ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(issueTimeStr);

            // Call rentBicycle from transaction class or directly from Rent if integrated there.
            // If this logic is in BicycleTransaction, you'd have an instance here. For now, assuming rentDAO handles it:
            rentDAO.rentBicycle(userId, bicycleId, parkingPlace, deposit, issueTime);

            System.out.println("Bicycle rented successfully.");
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Error renting bicycle: " + e.getMessage());
        }
    }

    private static void searchRentByUserIdInteractive(Scanner scanner, Rent rentDAO) {
        try {
            System.out.print("Enter User_ID: ");
            int userId = Integer.parseInt(scanner.nextLine());
            rentDAO.searchRentByUserId(userId);
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error searching rent: " + e.getMessage());
        }
    }

    private static void searchRentByBicycleIdInteractive(Scanner scanner, Rent rentDAO) {
        try {
            System.out.print("Enter Bicycle_ID: ");
            int bicycleId = Integer.parseInt(scanner.nextLine());
            rentDAO.searchRentByBicycleId(bicycleId);
        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error searching rent: " + e.getMessage());
        }
    }
}
