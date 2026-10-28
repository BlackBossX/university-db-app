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