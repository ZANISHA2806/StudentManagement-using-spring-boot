package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)

    // hibernate goes to role table search for primsry key column and connects both the table with primary key col
    // if we want to make another non primary column as referenced column to connect both the tables then
    //@JoinColumn(
    //    name = "role_name",
    //    referencedColumnName = "name"
    //)
    //private Role role;
    @JoinColumn(name = "role_id")
    private Role role;


    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(nullable = false)
    private boolean deleted = false;
}