USE opd_care;

-- SQL to seed an initial Admin user for testing
INSERT INTO users (user_id, username, password, full_name, role) 
VALUES ('U-ADMIN-001', 'admin', 'admin123', 'System Administrator', 'ADMIN');

-- SQL to seed a Doctor for testing
INSERT INTO users (user_id, username, password, full_name, role) 
VALUES ('U-DOC-001', 'doctor', 'doc123', 'Dr. John Smith', 'DOCTOR');

-- SQL to seed a Receptionist for testing
INSERT INTO users (user_id, username, password, full_name, role) 
VALUES ('U-RECEPT-001', 'recept', 'recept123', 'Receptionist Ann', 'RECEPT');
