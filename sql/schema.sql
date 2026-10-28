-- ============================================================ --  university_db : 3NF schema (Lab Sheet 07) -- ============================================================ 
CREATE DATABASE IF NOT EXISTS university_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 
USE university_db; 
  
CREATE TABLE department ( 
  id    BIGINT AUTO_INCREMENT PRIMARY KEY, 
  code  VARCHAR(10)  NOT NULL UNIQUE, 
  name  VARCHAR(100) NOT NULL 
) ENGINE=InnoDB; 
  
CREATE TABLE lecturer ( 
  id            BIGINT AUTO_INCREMENT PRIMARY KEY, 
  name          VARCHAR(100) NOT NULL, 
  email         VARCHAR(100) NOT NULL UNIQUE, 
  department_id BIGINT NOT NULL, 
  CONSTRAINT fk_lecturer_department 
    FOREIGN KEY (department_id) REFERENCES department (id) 
) ENGINE=InnoDB; 
  
CREATE TABLE course ( 
  id          BIGINT AUTO_INCREMENT PRIMARY KEY, 
  code        VARCHAR(10)  NOT NULL UNIQUE, 
  title       VARCHAR(100) NOT NULL, 
  credits     INT NOT NULL CHECK (credits BETWEEN 1 AND 6), 
  lecturer_id BIGINT NOT NULL, 
  CONSTRAINT fk_course_lecturer 
    FOREIGN KEY (lecturer_id) REFERENCES lecturer (id) 
) ENGINE=InnoDB; 
  
CREATE TABLE student ( 
  id           BIGINT AUTO_INCREMENT PRIMARY KEY, 
  student_code VARCHAR(10)  NOT NULL UNIQUE, 
  full_name    VARCHAR(100) NOT NULL, 
  email        VARCHAR(100) NOT NULL UNIQUE 
) ENGINE=InnoDB; 
  
CREATE TABLE enrollment ( 
  id         BIGINT AUTO_INCREMENT PRIMARY KEY, 
  student_id BIGINT NOT NULL, 
  course_id  BIGINT NOT NULL, 
  grade      VARCHAR(2), 
  CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES student (id), 
  CONSTRAINT fk_enrollment_course  FOREIGN KEY (course_id)  REFERENCES course (id), 
  CONSTRAINT uq_enrollment UNIQUE (student_id, course_id) 
) ENGINE=InnoDB;


