package com.example.studentmanagement.mapper;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    // why private constructor - it contains only utility methods
// so we prevent java creation it is the common java pattern for DTO
    private StudentMapper(){}
    public Student toEntity(StudentRequest request) {

        Student student = new Student();

        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());

        return student;
    }

    public StudentResponse toResponse(Student student) {

        StudentResponse response = new StudentResponse();

        response.setId(student.getId());
        response.setName(student.getName());
        response.setAge(student.getAge());
        response.setEmail(student.getEmail());

        if (student.getDepartment() != null) {
            response.setDepartmentName(student.getDepartment().getDepartment_name());
        }

        return response;
    }
}