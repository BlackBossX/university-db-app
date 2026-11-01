package com.example.universitydb.repository;

import com.example.universitydb.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentCode(String studentCode);
    List<Student> findByFullNameContainingIgnoreCase(String text);
    List<Student> findAllByOrderByFullNameAsc();
    boolean existsByEmail(String email);
    long countByEmailEndingWith(String domain);
}
