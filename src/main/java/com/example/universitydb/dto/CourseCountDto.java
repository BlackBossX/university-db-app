package com.example.universitydb.dto;

public record CourseCountDto(String courseCode, String title, Long studentCount) {
    public static CourseCountDto from(CourseEnrollmentCount p) {
        return new CourseCountDto(p.getCourseCode(), p.getTitle(), p.getStudentCount());
    }
}
