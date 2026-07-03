package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    Student saveStudent(Student student);

    Student getStudentById(Long id);

    List<Student> getAllStudents();

    Student updateStudent(Long id, Student student);

    void deleteStudent(Long id);

    void updateStudentName(Long id, String name);

    Student assignDepartment(Long studentId, Long departmentId);

    Student assignCourse(Long studentId, Long courseId);

    Page<Student> getStudents(int page, int size, String sortBy);

}