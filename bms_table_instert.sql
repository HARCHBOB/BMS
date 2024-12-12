-- Users
INSERT INTO bms.User (Name_Surname, Date_of_birth, Passport_Number, Street, House, Apartment)
VALUES
    ('John Doe', '1990-01-01', 'PAS123456', 'Main St', '10', '2'),
    ('Jane Smith', '1985-05-15', 'PAS234567', 'Oak Ave', '5', 'B'),
    ('Alice Johnson', '1992-07-20', 'PAS345678', 'Pine Rd', '12', '1A'),
    ('Bob Brown', '1978-11-11', 'PAS456789', 'Elm St', '3', '3C');

-- Clients (assume first two users are clients)
INSERT INTO bms.Client (User_ID, Deposit)
VALUES
    (1, 100.00),
    (2, 200.00);

-- Employees (assume last two users are employees)
INSERT INTO bms.Employee (User_ID, Salary)
VALUES
    (3, 3000.00),
    (4, 2500.00);


INSERT INTO bms.Bicycle (Price, Color, Brand, Release_Date)
VALUES
    (350.00, 'Red', 'Trek', 2020),
    (400.00, 'Blue', 'Giant', 2021),
    (250.00, 'Black', 'Merida', 2019),
    (299.99, 'Red', 'Trek', 2022);


INSERT INTO bms.Parking (Parking_Place, Capacity, LocationDetails)
VALUES
    ('Garage A', 5, 'Underground garage near main street'),
    ('Garage B', 2, 'Small garage behind building'),
    ('Garage C', 10, 'Large central garage'),
    ('Central Parking', 3, 'City center parking area'),
    ('Main Parking', 4, 'Main square parking lot'),
    ('Aboba Island', 2, 'Fictional island parking');

INSERT INTO bms.Standing (Bicycle_ID, Parking_Place, Parking_Beginning)
VALUES
    (1, 'Garage A', '2024-01-01 08:00:00'),
    (2, 'Garage B', '2024-01-02 09:00:00'),
    (3, 'Garage C', '2024-05-01 10:00:00'),
    (4, 'Garage A', '2024-12-09 08:00:00'); -- Bicycle 1 also stands at Central Parking at a later time



INSERT INTO bms.Rent (User_ID, Bicycle_ID, Parking_Place, Deposit, Issue_Time)
VALUES
    (1, 1, 'Central Parking', 150.00, '2024-12-09 10:00:00'),
    (2, 2, 'Garage B', 200.00, '2024-12-10 08:30:00');


INSERT INTO bms.Defects (Bicycle_ID, Defect, Date, Zone)
VALUES
    (3, 'Flat Tire', '2024-12-09', 'Front Wheel');

