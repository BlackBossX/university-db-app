package com.example.universitydb.dto;

import com.example.universitydb.model.Student;

public record StudentDto(String studentCode, String fullName, String email) {
    public static StudentDto from(Student s) {
        return new StudentDto(s.getStudentCode(), s.getFullName(), s.getEmail());
    }
}
