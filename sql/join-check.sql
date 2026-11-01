-- Rebuilds the original flat table from the five normalized tables
USE university_db;
SELECT s.student_code, s.full_name, s.email AS student_email,
 c.code AS course_code, c.title, c.credits,
 l.name AS lecturer, l.email AS lecturer_email,
 d.code AS dept_code, d.name AS dept_name,
 e.grade
FROM enrollment e
JOIN student s ON s.id = e.student_id
JOIN course c ON c.id = e.course_id
JOIN lecturer l ON l.id = c.lecturer_id
JOIN department d ON d.id = l.department_id
ORDER BY s.student_code, c.code;