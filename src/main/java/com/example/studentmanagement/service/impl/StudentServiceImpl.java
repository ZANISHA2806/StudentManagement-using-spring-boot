package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Address;
import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.Department;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.repository.DepartmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
                              DepartmentRepository departmentRepository,
                              CourseRepository courseRepository) {

        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Student saveStudent(Student student) {

        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));
    }

    @Override
    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }

    @Override
    public Student updateStudent(Long id, Student student) {

        Student existing = getStudentById(id);

        existing.setName(student.getName());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());

        if (student.getAddress() != null) {

            if (student.getAddress() != null) {

                if (existing.getAddress() == null) {

                    existing.setAddress(student.getAddress());

                } else {

                    Address existingAddress = existing.getAddress();

                    existingAddress.setStreet(student.getAddress().getStreet());
                    existingAddress.setCity(student.getAddress().getCity());
                    existingAddress.setState(student.getAddress().getState());

                    // Keep both sides synchronized
                    existingAddress.setStudent(existing);
                }
            }
        }

        return studentRepository.save(existing);
    }

    @Override

    @Transactional
    public void deleteStudent(Long id) {

        Student student = getStudentById(id);

        // Break bidirectional relationship
        student.removeAddress();

        studentRepository.delete(student);
    }
    @Override
    public void updateStudentName(Long id, String name) {

        Student student = getStudentById(id);

        student.setName(name);

        // Dirty Checking
    }

    @Override
    public Student assignDepartment(Long studentId, Long departmentId) {

        Student student = getStudentById(studentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        student.setDepartment(department);

        return studentRepository.save(student);
    }

    @Override
    public Student assignCourse(Long studentId, Long courseId) {

        Student student = getStudentById(studentId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        student.getCourses().add(course);

        return studentRepository.save(student);
    }

    @Override
    public Page<Student> getStudents(int page,
                                     int size,
                                     String sortBy) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        return studentRepository.findAll(pageable);
    }

}