-- Schema Definition
 CREATE SCHEMA IF NOT EXISTS bms AUTHORIZATION postgres;

-- Updated User Table
CREATE TABLE bms.User (
    User_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Vardas_Pavarde VARCHAR(80) NOT NULL,
    Gimimo_data DATE NOT NULL,
    Paso_numeris VARCHAR(32) UNIQUE NOT NULL,
    Gatve VARCHAR(32) DEFAULT 'N/A',
    Namas VARCHAR(10) DEFAULT 'N/A',
    Butas VARCHAR(10) DEFAULT 'N/A',
    Sukurimo_data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    Uzstatas DECIMAL(10, 2) CONSTRAINT Uzstatato_Dydis CHECK(Uzstatas > 0 AND Uzstatas < 1000) ,
    Registracijos_data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Updated Employee Table
CREATE TABLE bms.Employee (
    Admin_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    User_ID INT REFERENCES bms.User(User_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    Atlyginimas DECIMAL(10, 2) CONSTRAINT Atlyginimo_Dydis CHECK(Atlyginimas > 0)
);

-- Updated Bicycle Table
CREATE TABLE bms.Bicycle (
    Bicycle_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Kaina DECIMAL(10, 2) NOT NULL CONSTRAINT Kainos_Dydis CHECK(Kaina > 0),
    Spalva VARCHAR(50),
    Brendas VARCHAR(100) NOT NULL,
    Leidimo_metai SMALLINT NOT NULL
);

-- Updated Defektai Table
CREATE TABLE bms.Defektai (
    Defekto_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    Defektas VARCHAR(255) NOT NULL,
    Data DATE NOT NULL,
    Zona VARCHAR(100)
);

-- Updated Parking Table
CREATE TABLE bms.Parking (
    Parkavimo_vieta VARCHAR(100) PRIMARY KEY,
    Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    Parkavimo_prazia TIMESTAMP,
    Parkavimo_pabaiga TIMESTAMP
);

-- Updated Nuomavimas Table
CREATE TABLE bms.Nuomavimas (
    Rent_ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    User_ID INT NOT NULL REFERENCES bms.User(User_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    Parkavimo_vieta VARCHAR(100) NOT NULL,
    Uzstatas DECIMAL(10, 2) CONSTRAINT Uzstatato_Dydis CHECK(Uzstatas > 0 AND Uzstatas < 1000),
    Isdavimo_laikas TIMESTAMP,
    Grazinimo_laikas TIMESTAMP
);

alter table bms.Nuomavimas
ALTER COLUMN Uzstatas DROP NOT NULL;
;

-- Updated Paimta Table
CREATE TABLE bms.Paimta (
    Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    Rent_ID INT REFERENCES bms.Nuomavimas(Rent_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    PRIMARY KEY (Bicycle_ID, Rent_ID)
);

-- Updated Stovi Table
CREATE TABLE bms.Stovi (
    Bicycle_ID INT REFERENCES bms.Bicycle(Bicycle_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    Parkavimo_vieta VARCHAR(100) REFERENCES bms.Parking(Parkavimo_vieta) ON DELETE CASCADE ON UPDATE RESTRICT,
    PRIMARY KEY (Bicycle_ID, Parkavimo_vieta)
);

-- Updated Kur_Pasiimti Table
CREATE TABLE bms.Kur_Pasiimti (
    Parkavimo_vieta VARCHAR(100) REFERENCES bms.Parking(Parkavimo_vieta) ON DELETE CASCADE ON UPDATE RESTRICT,
    Rent_ID INT REFERENCES bms.Nuomavimas(Rent_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    PRIMARY KEY (Parkavimo_vieta, Rent_ID)
);

CREATE UNIQUE INDEX idx_unique_paso_numeris ON bms.User(Paso_numeris);
CREATE UNIQUE INDEX idx_unique_parkavimo_pradzia ON bms.Parking(Parkavimo_prazia);
CREATE UNIQUE INDEX idx_unique_parkavimo_pabaiga ON bms.Parking(Parkavimo_pabaiga);
CREATE INDEX idx_non_unique_vardas ON bms.User(Vardas_Pavarde);
CREATE INDEX idx_non_unique_bicycle_kaina ON bms.Bicycle(Kaina);


CREATE VIEW bms.Active_Rentals AS
SELECT * FROM bms.Nuomavimas WHERE Grazinimo_laikas IS NULL;

CREATE VIEW bms.Employee_Clients AS
SELECT e.Admin_ID, c.Client_ID, u.Vardas_Pavarde
FROM bms.Employee e
JOIN bms.Client c ON e.User_ID = c.User_ID
JOIN bms.User u ON u.User_ID = c.User_ID;

CREATE VIEW bms.Defect_Report AS
SELECT d.Defekto_ID, d.Bicycle_ID, d.Defektas, d.Data, b.Brendas
FROM bms.Defektai d
JOIN bms.Bicycle b ON d.Bicycle_ID = b.Bicycle_ID;

CREATE OR REPLACE VIEW bms.Parking_With_Duration AS
SELECT 
    Parkavimo_vieta,
    Bicycle_ID,
    Parkavimo_prazia,
    Parkavimo_pabaiga,
    COALESCE(Parkavimo_pabaiga, CURRENT_TIMESTAMP) - Parkavimo_prazia AS Stovejimo_laikas
FROM 
    bms.Parking;

-- Materialized Views
CREATE MATERIALIZED VIEW bms.Bicycle_Availability AS
SELECT b.Bicycle_ID, COUNT(s.Parkavimo_vieta) AS Available_Count
FROM bms.Bicycle b
LEFT JOIN bms.Stovi s ON b.Bicycle_ID = s.Bicycle_ID
GROUP BY b.Bicycle_ID;

CREATE MATERIALIZED VIEW bms.Parking_Usage AS
SELECT Parkavimo_vieta, COUNT(*) AS Usage_Count
FROM bms.Stovi
GROUP BY Parkavimo_vieta;

CREATE MATERIALIZED VIEW bms.Bicycle_Status AS
SELECT b.Bicycle_ID, 
       b.Brendas, 
       b.Spalva, 
       CASE WHEN s.Parkavimo_vieta IS NULL THEN 'In Use' ELSE 'Parked' END AS Status
FROM bms.Bicycle b
LEFT JOIN bms.Stovi s ON b.Bicycle_ID = s.Bicycle_ID;

-- Triggers
CREATE OR REPLACE FUNCTION bms.update_rent_status()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Grazinimo_laikas IS NOT NULL THEN
        INSERT INTO bms.Stovi (Bicycle_ID, Parkavimo_vieta)
        VALUES (NEW.Bicycle_ID, NEW.Parkavimo_vieta);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_rent_update
AFTER UPDATE ON bms.Nuomavimas
FOR EACH ROW
EXECUTE FUNCTION bms.update_rent_status();

CREATE OR REPLACE FUNCTION bms.log_bicycle_defects()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO bms.Defektai (Bicycle_ID, Defektas, Data, Zona)
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
AFTER INSERT OR UPDATE OR DELETE ON bms.Stovi
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
AFTER INSERT OR UPDATE OR DELETE ON bms.Stovi
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

-- Trigger on Stovi table
CREATE TRIGGER trg_refresh_bicycle_status_stovi
AFTER INSERT OR UPDATE OR DELETE ON bms.Stovi
FOR EACH STATEMENT
EXECUTE FUNCTION bms.refresh_bicycle_status();
