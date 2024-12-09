
-- 1. Constraints
--Test Duplicate paso_numeris
INSERT INTO bms.User (Name_Surname, Date_of_birth, Passport_Number)
VALUES ('Duplicate User', '1995-01-01', 'TEST12345');

-- Test uzstatas < 0
INSERT INTO bms.Client (User_ID, Deposit)
VALUES (1, -50.00); -- Negative deposit

-- Test atlyginimas < 0
INSERT INTO bms.Employee (User_ID, Salary)
VALUES (1, 0.00); -- Zero salary

-- Test Client with non existant user id
INSERT INTO bms.Client (User_ID, Deposit)
VALUES (6666, 100.00);


SELECT * FROM bms.User;
SELECT * FROM bms.Bicycle;


-- 2. Test Trigger: Logging User Creation
INSERT INTO bms.User (Name_Surname, Date_of_birth, Passport_Number)
VALUES ('Trigger Test', '1990-01-01', 'TRIG12345');
SELECT * FROM bms.Logs WHERE Event = 'New User Created';

-- 3 Test Trigger: Update Rent Status. 
UPDATE bms.Rent
SET Return_Time = CURRENT_TIMESTAMP
WHERE Rent_ID = 2;

-- Check if bycle is in storage after update
SELECT * FROM bms.Standing WHERE Bicycle_ID = 2;


-- 4. Test Trigger: Refresh Materialized Views
INSERT INTO bms.Parking (Parking_Place, Bicycle_ID, Parking_Beginning, Parking_Ending)
VALUES ('Garage D', NULL, NULL, NULL);

INSERT INTO bms.Standing (Bicycle_ID, Parking_Place)
VALUES (3, 'Garage D');

-- Refresh view and query materialized view:
REFRESH MATERIALIZED VIEW bms.Bicycle_Availability;
SELECT * FROM bms.Bicycle_Availability;



-- 5. Test Calculated Stovejimo_laikas 
SELECT p.Parking_Place, p.Bicycle_ID, d.Parking_Time
FROM bms.Parking p, bms.Parking_With_Duration d
WHERE p.Bicycle_ID = d.Bicycle_ID;


-- 6. Test: VIEW Tables
-- Only rentals with that are active (grazinimo_laikas = Null)
SELECT * FROM bms.Active_Rentals;
-- Bicycle status should be "Parked"/"In Use"
SELECT * FROM bms.Bicycle_Status;


-- 7. Test Cascading Deletes
DELETE FROM bms.User WHERE User_ID = 1;
-- Check dependent tables:
SELECT * FROM bms.Client WHERE User_ID = 1;
SELECT * FROM bms.Employee WHERE User_ID = 1;


-- 8. Test Index Performance
EXPLAIN ANALYZE SELECT * FROM bms.User WHERE Passport_Number = 'TEST12345';
	

-- 9. Delete data
TRUNCATE TABLE bms.Where_To_Take CASCADE;
TRUNCATE TABLE bms.Standing CASCADE;
TRUNCATE TABLE bms.Taken CASCADE;
TRUNCATE TABLE bms.Rent CASCADE;
TRUNCATE TABLE bms.Parking CASCADE;
TRUNCATE TABLE bms.Defects CASCADE;
TRUNCATE TABLE bms.Bicycle CASCADE;
TRUNCATE TABLE bms.Employee CASCADE;
TRUNCATE TABLE bms.Client CASCADE;
TRUNCATE TABLE bms.User CASCADE;

ALTER SEQUENCE bms.user_user_id_seq RESTART WITH 1;
ALTER SEQUENCE bms.client_client_id_seq RESTART WITH 1;
ALTER SEQUENCE bms.employee_admin_id_seq RESTART WITH 1;
ALTER SEQUENCE bms.bicycle_bicycle_id_seq RESTART WITH 1;
ALTER SEQUENCE bms.defects_defect_id_seq RESTART WITH 1;
ALTER SEQUENCE bms.rent_rent_id_seq RESTART WITH 1;



