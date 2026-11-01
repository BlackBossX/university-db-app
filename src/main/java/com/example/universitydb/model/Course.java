package com.example.universitydb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 10, unique = true)
    private String code;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;
}
