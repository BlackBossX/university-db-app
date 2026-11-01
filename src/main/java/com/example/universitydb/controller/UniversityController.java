package com.example.universitydb.controller;

import com.example.universitydb.dto.CourseCountDto;
import com.example.universitydb.dto.CourseDto;
import com.example.universitydb.dto.EnrollmentDto;
import com.example.universitydb.dto.LecturerDto;
import com.example.universitydb.dto.StudentDto;
import com.example.universitydb.model.Course;
import com.example.universitydb.repository.CourseRepository;
import com.example.universitydb.repository.DepartmentRepository;
import com.example.universitydb.repository.EnrollmentRepository;
import com.example.universitydb.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Read-only endpoints for the lab queries. DTO mapping stays inside the read transaction. */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UniversityController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    // ---------- students ----------

    @GetMapping("/students")
    public List<StudentDto> students(@RequestParam(required = false) String name) {
        var list = (name == null || name.isBlank())
            ? studentRepository.findAllByOrderByFullNameAsc()
            : studentRepository.findByFullNameContainingIgnoreCase(name.trim());
        return list.stream().map(StudentDto::from).toList();
    }

    @GetMapping("/students/{code}")
    public ResponseEntity<StudentDto> student(@PathVariable String code) {
        return studentRepository.findByStudentCode(code)
            .map(s -> ResponseEntity.ok(StudentDto.from(s)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/students/{code}/enrollments")
    public List<EnrollmentDto> studentEnrollments(@PathVariable String code) {
        return enrollmentRepository.findByStudent_StudentCode(code)
            .stream().map(EnrollmentDto::from).toList();
    }

    // ---------- courses ----------

    @GetMapping("/courses")
    public List<CourseDto> courses(@RequestParam(required = false) Integer minCredits,
        @RequestParam(required = false) String department) {
        List<Course> list;
        if (minCredits != null) {
            list = courseRepository.findByCreditsGreaterThanEqual(minCredits);
        } else if (department != null && !department.isBlank()) {
            list = courseRepository.findByLecturer_Department_Name(department.trim());
        } else {
            list = courseRepository.findAll();
        }
        return list.stream().map(CourseDto::from).toList();
    }

    @GetMapping("/courses/{code}/students")
    public List<StudentDto> courseStudents(@PathVariable String code) {
        return enrollmentRepository.findByCourse_Code(code)
            .stream().map(e -> StudentDto.from(e.getStudent())).toList();
    }

    // ---------- departments ----------

    @GetMapping("/departments/{code}/lecturers")
    public ResponseEntity<List<LecturerDto>> departmentLecturers(@PathVariable String code) {
        return departmentRepository.findWithLecturers(code)
            .map(d -> ResponseEntity.ok(d.getLecturers().stream().map(LecturerDto::from).toList()))
            .orElse(ResponseEntity.notFound().build());
    }

    // ---------- enrollments and reports ----------

    @GetMapping("/enrollments")
    public List<EnrollmentDto> enrollments(@RequestParam String grade) {
        return enrollmentRepository.findByGrade(grade)
            .stream().map(EnrollmentDto::from).toList();
    }

    @GetMapping("/reports/course-counts")
    public List<CourseCountDto> courseCounts() {
        return enrollmentRepository.countStudentsPerCourse()
            .stream().map(CourseCountDto::from).toList();
    }
}
