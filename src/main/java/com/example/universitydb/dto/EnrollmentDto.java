package com.example.universitydb.dto;

import com.example.universitydb.model.Enrollment;

public record EnrollmentDto(String studentCode, String studentName, String courseCode, String courseTitle, String grade) {
    public static EnrollmentDto from(Enrollment s) {
        return new EnrollmentDto(s.getStudent().getStudentCode(), s.getStudent().getFullName(),
                s.getCourse().getCode(), s.getCourse().getTitle(), s.getGrade());
    }
}
