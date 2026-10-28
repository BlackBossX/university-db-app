-- ============================================================ -- Anomaly demo: ONE flat table (1NF only). Used in Task 3. -- ============================================================ 
CREATE DATABASE IF NOT EXISTS university_db 
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 
USE university_db; 
  
CREATE TABLE registration_flat ( 
  student_id       VARCHAR(10), 
  student_name     VARCHAR(100), 
  student_email    VARCHAR(100), 
  course_code      VARCHAR(10), 
  course_title     VARCHAR(100), 
  credits          INT, 
  lecturer_name    VARCHAR(100), 
  lecturer_email   VARCHAR(100), 
  department_code  VARCHAR(10), 
  department_name  VARCHAR(100), 
  grade            VARCHAR(2), 
  PRIMARY KEY (student_id, course_code) 
) ENGINE=InnoDB; 
  
INSERT INTO registration_flat VALUES 
 ('S001','Kamal Silva','kamal@example.com','CS101','Intro to Programming',3,'Dr. 
Perera','perera@uni.lk','D01','Computer Science','A'), 
 ('S001','Kamal Silva','kamal@example.com','CS102','Database Systems',3,'Dr. 
Fernando','fernando@uni.lk','D01','Computer Science','B+'), 
 ('S002','Nimali Perera','nimali@example.com','CS101','Intro to Programming',3,'Dr. 
Perera','perera@uni.lk','D01','Computer Science','B'), 
 ('S002','Nimali Perera','nimali@example.com','ST201','Probability Theory',3,'Dr. 
Jayasinghe','jayasinghe@uni.lk','D02','Statistics','A-'), 
 ('S003','Saman Fernando','saman@example.com','CS102','Database Systems',3,'Dr. 
Fernando','fernando@uni.lk','D01','Computer Science','C+'), 
 ('S003','Saman Fernando','saman@example.com','ST201','Probability Theory',3,'Dr. 
Jayasinghe','jayasinghe@uni.lk','D02','Statistics','B'), 
 ('S004','Dilini Jayawardena','dilini@example.com','CS201','Web Development',4,'Dr. 
Perera','perera@uni.lk','D01','Computer Science','A'); 





-- ============================================================ -- Run AFTER anomaly-demo. SQL. One statement at a time (Task 2). -- ============================================================ 
 
USE university_db; 
  -- A. REDUNDANCY + UPDATE anomaly -------------------------------- -- How many times is Dr. Perera's e-mail stored? 
 
SELECT COUNT(*) AS copies
FROM registration_flat
WHERE lecturer_email = 'perera@uni.lk';
  -- Change the e-mail in ONE row only (a careless UPDATE) 
 
UPDATE registration_flat 
SET lecturer_email = 'perera.new@uni.lk'
WHERE student_id = 'S001' AND course_code = 'CS101'; 
  
 -- Dr. Perera now has two different e-mails! 
 
SELECT DISTINCT lecturer_name, lecturer_email 
FROM registration_flat WHERE lecturer_name = 'Dr. Perera'; 





-- B. INSERT anomaly ------------------------------------------ -- A new course that has no students yet cannot be stored 
 
INSERT INTO registration_flat 
  (course_code, course_title, credits, lecturer_name, lecturer_email, department_code, department_name) 
VALUES 
  ('CS301', 'Software Engineering', 3, 'Dr. Fernando', 'fernando@uni.lk', 'D01', 'Computer Science'); 




-- C. DELETE anomaly --------------------------------------------------- -- Dilini (S004) is the only student of CS201 and leaves the university 
DELETE FROM registration_flat WHERE student_id = 'S004'; 
  -- The course CS201 (title, credits, lecturer) has vanished too 
SELECT * FROM registration_flat WHERE course_code = 'CS201'; 