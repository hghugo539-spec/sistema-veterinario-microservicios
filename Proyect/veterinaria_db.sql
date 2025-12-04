
DROP TABLE IF EXISTS Invoice, Vaccine, MedicalHistory, Product, Shift, Pet, Client, Veterinarian, Address, Species, Supplier, Service, User_Role, Role, AppUser CASCADE;

-
CREATE TABLE AppUser (
    id_user SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE Role (
    id_role SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE User_Role (
    id_user INTEGER NOT NULL REFERENCES AppUser(id_user) ON DELETE CASCADE,
    id_role INTEGER NOT NULL REFERENCES Role(id_role) ON DELETE CASCADE,
    PRIMARY KEY (id_user, id_role)
);


CREATE TABLE Address (
    id_address SERIAL PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    external_number VARCHAR(20),
    neighborhood VARCHAR(100),
    city VARCHAR(100) NOT NULL,
    zip_code VARCHAR(10)
);


CREATE TABLE Client (
    id_client SERIAL PRIMARY KEY,
    id_address INTEGER REFERENCES Address(id_address),
    id_user INTEGER NOT NULL UNIQUE REFERENCES AppUser(id_user) ON DELETE CASCADE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    phone VARCHAR(20)
);
CREATE TABLE Veterinarian (
    id_veterinarian SERIAL PRIMARY KEY,
    id_user INTEGER NOT NULL UNIQUE REFERENCES AppUser(id_user) ON DELETE CASCADE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL
);


CREATE TABLE Species (
    id_species SERIAL PRIMARY KEY,
    species_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Pet (
    id_pet SERIAL PRIMARY KEY,
    id_client INTEGER NOT NULL REFERENCES Client(id_client),
    name VARCHAR(100) NOT NULL,
    birth_date DATE,
    id_species INTEGER NOT NULL REFERENCES Species(id_species),
    breed VARCHAR(100)
);

CREATE TABLE Shift (
    id_shift SERIAL PRIMARY KEY,
    id_veterinarian INTEGER NOT NULL REFERENCES Veterinarian(id_veterinarian),
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE Supplier (
    id_supplier SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20)
);

-- 11. Product
CREATE TABLE Product (
    id_product SERIAL PRIMARY KEY,
    id_supplier INTEGER REFERENCES Supplier(id_supplier),
    product_name VARCHAR(150) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    stock INTEGER NOT NULL CHECK (stock >= 0)
);

-- 12. Service
CREATE TABLE Service (
    id_service SERIAL PRIMARY KEY,
    service_name VARCHAR(100) NOT NULL UNIQUE,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0)
);

-- 13. MedicalHistory
CREATE TABLE MedicalHistory (
    id_history SERIAL PRIMARY KEY,
    id_pet INTEGER NOT NULL REFERENCES Pet(id_pet),
    id_veterinarian INTEGER NOT NULL REFERENCES Veterinarian(id_veterinarian),
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    diagnosis TEXT NOT NULL,
    treatment TEXT
);

-- 14. Vaccine
CREATE TABLE Vaccine (
    id_vaccine SERIAL PRIMARY KEY,
    id_pet INTEGER NOT NULL REFERENCES Pet(id_pet),
    vaccine_name VARCHAR(100) NOT NULL,
    application_date DATE NOT NULL,
    next_vaccine_date DATE,
    batch_number VARCHAR(50)
);

-- 15. Invoice
CREATE TABLE Invoice (
    id_invoice SERIAL PRIMARY KEY,
    id_client INTEGER NOT NULL REFERENCES Client(id_client),
    id_history INTEGER REFERENCES MedicalHistory(id_history),
    invoice_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    service_amount NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    product_amount NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    payment_status VARCHAR(50) NOT NULL
);


INSERT INTO Role (name) VALUES ('ROLE_ADMIN'), ('ROLE_CLIENT');