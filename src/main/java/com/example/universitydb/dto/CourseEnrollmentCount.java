package com.example.universitydb.dto;

/** Interface projection populated from the aliases in the enrollment report query. */
public interface CourseEnrollmentCount {
    String getCourseCode();
    String getTitle();
    Long getStudentCount();
}
