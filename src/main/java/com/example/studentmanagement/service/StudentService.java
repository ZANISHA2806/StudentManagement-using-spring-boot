package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    StudentResponse saveStudent(StudentRequest request);

    StudentResponse getStudentById(Long id);

    List<StudentResponse> getAllStudents();


    StudentResponse updateStudent(Long id,StudentRequest request);

    void deleteStudent(Long id);

    void updateStudentName(Long id, String name);

    StudentResponse assignDepartment(Long studentId, Long departmentId);

    StudentResponse assignCourse(Long studentId, Long courseId);

    Page<StudentResponse> getStudents(int page, int size, String sortBy);

}