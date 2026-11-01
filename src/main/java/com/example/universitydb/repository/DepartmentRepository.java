package com.example.universitydb.repository;

import com.example.universitydb.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByCode(String code);

    // Fetch the department and its lecturers in one SQL query.
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.lecturers WHERE d.code = :code")
    Optional<Department> findWithLecturers(@Param("code") String code);
}
