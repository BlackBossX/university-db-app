package com.example.universitydb;

import com.example.universitydb.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UniversityDbAppApplicationTests {
    @Autowired StudentRepository students;
    @Autowired CourseRepository courses;
    @Autowired LecturerRepository lecturers;
    @Autowired DepartmentRepository departments;
    @Autowired EnrollmentRepository enrollments;
    @Autowired Environment environment;

    @Test
    void customQueriesMatchTheLabDataset() {
        assertThat(students.findAllByOrderByFullNameAsc()).extracting("studentCode")
                .containsExactly("S004", "S001", "S002", "S003");
        assertThat(students.findByFullNameContainingIgnoreCase("SIL")).hasSize(1);
        assertThat(students.findByStudentCode("S001")).isPresent();
        assertThat(students.existsByEmail("kamal@example.com")).isTrue();
        assertThat(students.countByEmailEndingWith("@example.com")).isEqualTo(4);
        assertThat(courses.findByCode("CS101")).isPresent();
        assertThat(courses.findByCreditsGreaterThanEqual(4)).extracting("code").containsExactly("CS201");
        assertThat(courses.findByTitleContainingIgnoreCase("DATABASE")).hasSize(1);
        assertThat(courses.findByLecturer_Name("Dr. Perera")).hasSize(2);
        assertThat(courses.findByLecturer_Department_Name("Computer Science")).hasSize(3);
        assertThat(lecturers.findByEmail("perera@uni.lk")).isPresent();
        assertThat(lecturers.findByDepartment_Code("D01")).hasSize(2);
        assertThat(departments.findByCode("D01")).isPresent();
        assertThat(departments.findWithLecturers("D01").orElseThrow().getLecturers()).hasSize(2);
        assertThat(enrollments.findByStudent_StudentCode("S001")).hasSize(2);
        assertThat(enrollments.findByCourse_Code("CS101")).hasSize(2);
        assertThat(enrollments.findByGrade("A")).hasSize(2);
        assertThat(enrollments.findByGradeIn(List.of("A", "A-"))).hasSize(3);
        assertThat(enrollments.findByStudent_StudentCodeAndGrade("S001", "B+")).hasSize(1);
        assertThat(enrollments.countByCourse_Code("CS101")).isEqualTo(2);
        assertThat(enrollments.countStudentsPerCourse()).extracting(p -> p.getCourseCode() + ":" + p.getStudentCount())
                .containsExactly("CS101:2", "CS102:2", "ST201:2", "CS201:1");
    }

    @Test
    void endpointsSerializeRelationshipsAndHandleInvalidRequests() throws Exception {
        for (String path : List.of("/students", "/students?name=sil", "/students/S001",
                "/students/S001/enrollments", "/courses", "/courses?minCredits=4",
                "/courses?department=Computer%20Science", "/courses/CS101/students",
                "/departments/D01/lecturers", "/enrollments?grade=A", "/reports/course-counts")) {
            var response = get(path);
            assertThat(response.statusCode()).as(path).isEqualTo(200);
            assertThat(response.headers().firstValue("content-type").orElse("")).contains("application/json");
        }
        assertThat(get("/courses?minCredits=4").body()).contains("CS201", "Dr. Perera", "Computer Science");
        assertThat(get("/students/S001").body()).contains("Kamal Silva").doesNotContain("enrollments");
        assertThat(get("/students/UNKNOWN").statusCode()).isEqualTo(404);
        assertThat(get("/departments/UNKNOWN/lecturers").statusCode()).isEqualTo(404);
        assertThat(get("/enrollments").statusCode()).isEqualTo(400);
        assertThat(get("/courses?minCredits=abc").statusCode()).isEqualTo(400);
        assertThat(get("/enrollments?grade=B%2B").body()).contains("B+");
    }

    private HttpResponse<String> get(String path) throws Exception {
        String url = "http://localhost:" + environment.getProperty("local.server.port") + "/api" + path;
        return HttpClient.newHttpClient().send(HttpRequest.newBuilder(URI.create(url)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }
}
