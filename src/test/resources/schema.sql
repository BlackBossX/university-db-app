CREATE TABLE department ( 
  id    BIGINT AUTO_INCREMENT PRIMARY KEY, 
  code  VARCHAR(10)  NOT NULL UNIQUE, 
  name  VARCHAR(100) NOT NULL 
); 
  
CREATE TABLE lecturer ( 
  id            BIGINT AUTO_INCREMENT PRIMARY KEY, 
  name          VARCHAR(100) NOT NULL, 
  email         VARCHAR(100) NOT NULL UNIQUE, 
  department_id BIGINT NOT NULL, 
  CONSTRAINT fk_lecturer_department 
    FOREIGN KEY (department_id) REFERENCES department (id) 
); 
  
CREATE TABLE course ( 
  id          BIGINT AUTO_INCREMENT PRIMARY KEY, 
  code        VARCHAR(10)  NOT NULL UNIQUE, 
  title       VARCHAR(100) NOT NULL, 
  credits     INT NOT NULL CHECK (credits BETWEEN 1 AND 6), 
  lecturer_id BIGINT NOT NULL, 
  CONSTRAINT fk_course_lecturer 
    FOREIGN KEY (lecturer_id) REFERENCES lecturer (id) 
); 
  
CREATE TABLE student ( 
  id           BIGINT AUTO_INCREMENT PRIMARY KEY, 
  student_code VARCHAR(10)  NOT NULL UNIQUE, 
  full_name    VARCHAR(100) NOT NULL, 
  email        VARCHAR(100) NOT NULL UNIQUE 
); 
  
CREATE TABLE enrollment ( 
  id         BIGINT AUTO_INCREMENT PRIMARY KEY, 
  student_id BIGINT NOT NULL, 
  course_id  BIGINT NOT NULL, 
  grade      VARCHAR(2), 
  CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES student (id), 
  CONSTRAINT fk_enrollment_course  FOREIGN KEY (course_id)  REFERENCES course (id), 
  CONSTRAINT uq_enrollment UNIQUE (student_id, course_id) 
);


