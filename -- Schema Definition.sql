
CREATE SCHEMA IF NOT EXISTS bms
    AUTHORIZATION postgres;

-- Schema Definition
CREATE SCHEMA IF NOT EXISTS bms AUTHORIZATION postgres;

-- Updated User Table
CREATE TABLE bms.User (
                          User_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          Name_Surname VARCHAR(80) NOT NULL,
                          Date_of_birth DATE NOT NULL,
                          Passport_Number VARCHAR(32) UNIQUE NOT NULL,
                          Street VARCHAR(32) DEFAULT 'N/A',
                          House VARCHAR(10) DEFAULT 'N/A',
                          Apartment VARCHAR(10) DEFAULT 'N/A',
                          Creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE bms.Logs (
                          Log_ID SERIAL PRIMARY KEY,
                          Event VARCHAR(255) NOT NULL,
                          Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Updated Client Table
CREATE TABLE bms.Client (
                            Client_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            User_ID INT REFERENCES bms.User(User_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                            Deposit DECIMAL(10, 2) CONSTRAINT Deposit_Amount CHECK(Deposit > 0 AND Deposit < 1000) ,
                            Registration_Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Updated Employee Table
CREATE TABLE bms.Employee (
                              Admin_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              User_ID INT REFERENCES bms.User(User_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                              Salary DECIMAL(10, 2) CONSTRAINT Salary_Amount CHECK(Salary > 0)
);

-- Updated Bicycle Table
CREATE TABLE bms.Bicycle (
                             Bicycle_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             Price DECIMAL(10, 2) NOT NULL CONSTRAINT Price_Amount CHECK(Price > 0),
                             Color VARCHAR(50),
                             Brand VARCHAR(100) NOT NULL,
                             Release_Date SMALLINT NOT NULL
);

-- Updated Defects Table
CREATE TABLE bms.Defects (
                             Defect_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                             Defect VARCHAR(255) NOT NULL,
                             Date DATE NOT NULL,
                             Zone VARCHAR(100)
);

-- Updated Parking Table
CREATE TABLE bms.Parking (
                             Parking_Place VARCHAR(100) PRIMARY KEY,
                             Capacity INT,
                             LocationDetails VARCHAR(255)
);


-- Updated Rent Table
CREATE TABLE bms.Rent (
                          Rent_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          User_ID INT NOT NULL REFERENCES bms.User(User_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                          Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                          Parking_Place VARCHAR(100) NOT NULL REFERENCES bms.Parking(Parking_Place) ON DELETE CASCADE ON UPDATE RESTRICT,
                          Deposit DECIMAL(10, 2) CONSTRAINT Deposit_Amount CHECK(Deposit > 0 AND Deposit < 1000),
                          Issue_Time TIMESTAMP,
                          Return_Time TIMESTAMP
);

alter table bms.Rent
    ALTER COLUMN Deposit DROP NOT NULL;
;

-- Updated Taken Table
CREATE TABLE bms.Taken (
                           Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                           Rent_ID INT REFERENCES bms.Rent(Rent_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                           PRIMARY KEY (Bicycle_ID, Rent_ID)
);

-- Updated Standing Table
CREATE TABLE bms.Standing (
                              Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                              Parking_Place VARCHAR(100) REFERENCES bms.Parking(Parking_Place) ON DELETE CASCADE ON UPDATE RESTRICT,
                              Parking_Beginning TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              Parking_Ending TIMESTAMP,
                              PRIMARY KEY (Bicycle_ID, Parking_Place)
);
-- Updated Where_To_Take Table
CREATE TABLE bms.Where_To_Take (
                                   Parking_Place VARCHAR(100) REFERENCES bms.Parking(Parking_Place) ON DELETE CASCADE ON UPDATE RESTRICT,
                                   Rent_ID INT REFERENCES bms.Rent(Rent_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
                                   PRIMARY KEY (Parking_Place, Rent_ID)
);

CREATE UNIQUE INDEX idx_unique_passport_number ON bms.User(Passport_Number);
CREATE UNIQUE INDEX idx_unique_parking_beginning ON bms.Standing(Parking_Beginning);
CREATE UNIQUE INDEX idx_unique_parking_ending ON bms.Standing(Parking_Ending);
CREATE INDEX idx_non_unique_name_surname ON bms.User(Name_Surname);
CREATE INDEX idx_non_unique_bicycle_price ON bms.Bicycle(Price);


CREATE VIEW bms.Active_Rentals AS
SELECT * FROM bms.Rent WHERE Return_Time IS NULL;

CREATE VIEW bms.Employee_Clients AS
SELECT e.Admin_ID, c.Client_ID, u.Name_Surname
FROM bms.Employee e
         JOIN bms.Client c ON e.User_ID = c.User_ID
         JOIN bms.User u ON u.User_ID = c.User_ID;

CREATE VIEW bms.Defect_Report AS
SELECT d.Defect_ID, d.Bicycle_ID, d.Defect, d.Date, b.Brand
FROM bms.Defects d
         JOIN bms.Bicycle b ON d.Bicycle_ID = b.Bicycle_ID;

CREATE OR REPLACE VIEW bms.Standing_With_Duration AS
SELECT
    Parking_Place,
    Bicycle_ID,
    Parking_Beginning,
    Parking_Ending,
    COALESCE(Parking_Ending, CURRENT_TIMESTAMP) - Parking_Beginning AS Parking_Time
FROM
    bms.Standing;

-- Materialized Views
CREATE MATERIALIZED VIEW bms.Bicycle_Availability AS
SELECT b.Bicycle_ID, b.Price, b.Color, b.Brand, b.Release_Date
FROM bms.Bicycle b
         LEFT JOIN bms.Standing s ON b.Bicycle_ID = s.Bicycle_ID
WHERE NOT EXISTS (
    SELECT 1
    FROM bms.Active_Rentals ar
    WHERE ar.Bicycle_ID = b.Bicycle_ID
)
GROUP BY b.Bicycle_ID;

CREATE MATERIALIZED VIEW bms.Parking_Usage AS
SELECT Parking_Place, COUNT(*) AS Usage_Count
FROM bms.Standing
GROUP BY Parking_Place;

CREATE MATERIALIZED VIEW bms.Bicycle_Status AS
SELECT b.Bicycle_ID,
       b.Brand,
       b.Color,
       CASE WHEN s.Parking_Place IS NULL THEN 'In Use' ELSE 'Parked' END AS Status
FROM bms.Bicycle b
         LEFT JOIN bms.Standing s ON b.Bicycle_ID = s.Bicycle_ID;

-- Triggers
CREATE OR REPLACE FUNCTION bms.update_rent_status()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Return_Time IS NOT NULL THEN
        INSERT INTO bms.Standing (Bicycle_ID, Parking_Place)
        VALUES (NEW.Bicycle_ID, NEW.Parking_Place);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_rent_update
    AFTER UPDATE ON bms.Rent
    FOR EACH ROW
EXECUTE FUNCTION bms.update_rent_status();

CREATE OR REPLACE FUNCTION bms.log_bicycle_defects()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO bms.Defects (Bicycle_ID, Defect, Date, Zone)
    VALUES (NEW.Bicycle_ID, 'Logged in Trigger', CURRENT_DATE, 'Unknown');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_defects
    AFTER INSERT ON bms.Bicycle
    FOR EACH ROW
EXECUTE FUNCTION bms.log_bicycle_defects();


CREATE OR REPLACE FUNCTION bms.log_user_creation()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO bms.Logs (Event, Timestamp)
    VALUES ('New User Created', CURRENT_TIMESTAMP);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_user_creation
    AFTER INSERT ON bms.User
    FOR EACH ROW
EXECUTE FUNCTION bms.log_user_creation();

-- Trigger for automatic refresh of Bicycle_Availability, Parking_Usage, Bicycle_Status
CREATE OR REPLACE FUNCTION bms.refresh_bicycle_availability()
    RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW bms.Bicycle_Availability;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_refresh_bicycle_availability
    AFTER INSERT OR UPDATE OR DELETE ON bms.Standing
    FOR EACH STATEMENT
EXECUTE FUNCTION bms.refresh_bicycle_availability();

CREATE OR REPLACE FUNCTION bms.refresh_parking_usage()
    RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW bms.Parking_Usage;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_refresh_parking_usage
    AFTER INSERT OR UPDATE OR DELETE ON bms.Standing
    FOR EACH STATEMENT
EXECUTE FUNCTION bms.refresh_parking_usage();

CREATE OR REPLACE FUNCTION bms.refresh_bicycle_status()
    RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW bms.Bicycle_Status;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger on Bicycle table
CREATE TRIGGER trg_refresh_bicycle_status_bicycle
    AFTER INSERT OR UPDATE OR DELETE ON bms.Bicycle
    FOR EACH STATEMENT
EXECUTE FUNCTION bms.refresh_bicycle_status();

-- Trigger on Standing table
CREATE TRIGGER trg_refresh_bicycle_status_standing
    AFTER INSERT OR UPDATE OR DELETE ON bms.Standing
    FOR EACH STATEMENT
EXECUTE FUNCTION bms.refresh_bicycle_status();
