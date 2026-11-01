package com.example.universitydb.dto;

import com.example.universitydb.model.Lecturer;

public record LecturerDto(String name, String email, String department) {
    public static LecturerDto from(Lecturer s) {
        return new LecturerDto(s.getName(), s.getEmail(), s.getDepartment().getName());
    }
}
