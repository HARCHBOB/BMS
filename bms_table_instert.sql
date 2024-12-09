
SELECT * FROM bms.bicycle;
-- Insert Users
INSERT INTO bms.User (Vardas_Pavarde, Gimimo_data, Paso_numeris, Gatve, Namas, Butas)
VALUES 
('Jonas Jonaitis', '1990-01-15', 'AA123456', 'Gedimino g.', '10', '15A'),
('Petras Petraitis', '1985-03-10', 'BB654321', 'Vilniaus g.', '20', NULL),
('Ona Onaitė', '1995-05-20', 'CC987654', NULL, NULL, NULL);

-- Insert Clients
INSERT INTO bms.Client (User_ID, Uzstatas)
VALUES
(1, 50.00),
(2, 100.00);

-- Insert Employees
INSERT INTO bms.Employee (User_ID, Atlyginimas)
VALUES
(3, 1200.00);

-- Insert Bicycles
INSERT INTO bms.Bicycle (Kaina, Spalva, Brendas, Leidimo_metai)
VALUES
(300.00, 'Red', 'Trek', 2020),
(400.00, 'Blue', 'Giant', 2021),
(250.00, 'Black', 'Merida', 2019);

-- Insert Defektai
INSERT INTO bms.Defektai (Bicycle_ID, Defektas, Data, Zona)
VALUES
(1, 'Flat tire', '2024-01-01', 'Rear Wheel'),
(2, 'Broken chain', '2024-02-01', 'Transmission');

-- Insert Parking
INSERT INTO bms.Parking (Parkavimo_vieta, Bicycle_ID, Parkavimo_prazia, Parkavimo_pabaiga)
VALUES
('Garage A', 1, '2024-01-01 08:00:00', '2024-01-05 18:00:00'),
('Garage B', 2, '2024-01-02 09:00:00', NULL), -- Still in storage
('Garage C', NULL, '2024-05-01 10:00:00', NULL);

-- Insert Rentals
INSERT INTO bms.Nuomavimas (User_ID, Bicycle_ID, Parkavimo_vieta, Uzstatas, Isdavimo_laikas, Grazinimo_laikas)
VALUES
(1, 3, 'Garage C', 50.00, '2024-03-01 10:00:00', '2024-03-05 15:00:00'),
(2, 1, 'Garage A', 100.00, '2024-04-01 12:00:00', NULL); -- Active rental

-- Insert Paimta
INSERT INTO bms.Paimta (Bicycle_ID, Rent_ID)
VALUES
(3, 1);

-- Insert Stovi
INSERT INTO bms.Stovi (Bicycle_ID, Parkavimo_vieta)
VALUES
(1, 'Garage A'),
(2, 'Garage B');


-- Insert Kur_Pasiimti
INSERT INTO bms.Kur_Pasiimti (Parkavimo_vieta, Rent_ID)
VALUES
('Garage A', 2),
('Garage C', 1);
