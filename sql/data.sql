-- ============================================================ --  Sample data (run on freshly created, EMPTY tables) -- ============================================================ 
USE university_db; 
  
INSERT INTO department (code, name) VALUES 
  ('D01', 'Computer Science'), 
  ('D02', 'Statistics'); 
  
INSERT INTO lecturer (name, email, department_id) VALUES 
  ('Dr. Perera',     'perera@uni.lk',     1), 
  ('Dr. Fernando',   'fernando@uni.lk',   1), 
  ('Dr. Jayasinghe', 'jayasinghe@uni.lk', 2); 
  
INSERT INTO course (code, title, credits, lecturer_id) VALUES 
  ('CS101', 'Intro to Programming', 3, 1), 
  ('CS102', 'Database Systems',     3, 2), 
  ('ST201', 'Probability Theory',   3, 3), 
  ('CS201', 'Web Development',      4, 1); 
  
INSERT INTO student (student_code, full_name, email) VALUES 
  ('S001', 'Kamal Silva',        'kamal@example.com'), 
  ('S002', 'Nimali Perera',      'nimali@example.com'), 
  ('S003', 'Saman Fernando',     'saman@example.com'), 
  ('S004', 'Dilini Jayawardena', 'dilini@example.com'); 
  
INSERT INTO enrollment (student_id, course_id, grade) VALUES 
  (1, 1, 'A'), 
  (1, 2, 'B+'), 
  (2, 1, 'B'), 
  (2, 3, 'A-'), 
  (3, 2, 'C+'), 
  (3, 3, 'B'), 
  (4, 4, 'A');