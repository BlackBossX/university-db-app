package com.example.universitydb.dto;

import com.example.universitydb.model.Course;

public record CourseDto(String code, String title, Integer credits, String lecturer, String department) {
    public static CourseDto from(Course s) {
        return new CourseDto(s.getCode(), s.getTitle(), s.getCredits(),
                s.getLecturer().getName(), s.getLecturer().getDepartment().getName());
    }
}
