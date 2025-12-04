SELECT * FROM clientes;
-- 1. Detén el Clinic Service (8082) si aún no lo hiciste.
-- 2. Conéctate a la BD que recibirá los datos: vet_clinic_db

-- Inserta los datos del cliente, mapeando las columnas
INSERT INTO client (id_client, id_address, id_user, first_name, last_name, phone)
SELECT id_client, id_address, id_user, first_name, last_name, phone 
FROM Veterinaria.public.client;

TRUNCATE TABLE clientes RESTART IDENTITY;
TRUNCATE TABLE clientes RESTART IDENTITY;
TRUNCATE TABLE clientes RESTART IDENTITY CASCADE;
-- Ejecuta este comando en vet_clinic_db (ajusta el nombre de la secuencia si es necesario)
SELECT setval(pg_get_serial_sequence('clientes', 'id'), (SELECT MAX(id) FROM clientes));
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@vet.com';

UPDATE "users" SET role = 'ADMIN' WHERE email = 'admin@vet.com';