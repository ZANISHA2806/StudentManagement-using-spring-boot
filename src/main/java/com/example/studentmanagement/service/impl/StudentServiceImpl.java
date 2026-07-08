package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.Department;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.mapper.StudentMapper;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.repository.DepartmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor // this is provided by lombok it generates constructore by default we dont need to write constructor on our own
public class StudentServiceImpl implements StudentService {
// why static - only one logger per class
    // the below line creates logger factory for this cls
       private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;

//    public StudentServiceImpl(StudentRepository studentRepository,
//                              DepartmentRepository departmentRepository,
//                              CourseRepository courseRepository,
//                              StudentMapper studentMapper) {
//
//        this.studentRepository = studentRepository;
//        this.departmentRepository = departmentRepository;
//        this.courseRepository = courseRepository;
//        this.studentMapper = studentMapper;
//    }

    // ---------------------------
    // Private Helper
    // ---------------------------

    private Student findStudentEntity(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id " + id));
    }

    // ---------------------------
    // CRUD
    // ---------------------------

    @Override
    public StudentResponse saveStudent(StudentRequest request) {

        log.info("Creating student with email: {}", request.getEmail());
        Student student = studentMapper.toEntity(request);

        Student savedStudent = studentRepository.save(student);

        log.info("Student created successfully with ID: {}", savedStudent.getId());

        return studentMapper.toResponse(savedStudent);
    }

    @Override
    public StudentResponse getStudentById(Long id) {

        log.info("Fetching student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Student not found with ID: {}", id);

                    return new ResourceNotFoundException(
                            "Student not found with id " + id
                    );
                });

        log.info("Student fetched successfully");

        return studentMapper.toResponse(student);
    }

    @Override
    public List<StudentResponse> getAllStudents() {

        log.info("Fetching all students");

        List<StudentResponse> students = studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponse)
                .toList();

        log.info("Total students found: {}", students.size());

        return students;
    }

    @Override
    public StudentResponse updateStudent(Long id,
                                         StudentRequest request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found"));

        student.setName(request.getName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());

        Student updatedStudent = studentRepository.save(student);

        return studentMapper.toResponse(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {

        log.info("Deleting student {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Student {} not found", id);

                    return new ResourceNotFoundException(
                            "Student not found with id " + id);
                });

        studentRepository.delete(student);

        log.info("Student {} deleted successfully", id);
    }

    @Override
    @Transactional
    public void updateStudentName(Long id, String name) {

        log.info("Updating name of student {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {

                    log.error("Student {} not found", id);

                    return new ResourceNotFoundException(
                            "Student not found with id " + id);
                });

        student.setName(name);

        log.info("Student name changed successfully");
    }

    @Override
    public StudentResponse assignDepartment(Long studentId, Long departmentId) {

        log.info("Assigning Department {} to Student {}",
                departmentId,
                studentId);

        Student student = findStudentEntity(studentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        student.setDepartment(department);

        Student updatedStudent = studentRepository.save(student);

        log.info("Department assigned successfully");

        return studentMapper.toResponse(updatedStudent);
    }
    @Override
    public StudentResponse assignCourse(Long studentId, Long courseId) {

        log.info("Assigning Course {} to Student {}",
                courseId,
                studentId);

        Student student = findStudentEntity(studentId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        student.getCourses().add(course);

        Student updatedStudent = studentRepository.save(student);

        log.info("Course assigned successfully");

        return studentMapper.toResponse(updatedStudent);
    }
    @Override
    public Page<StudentResponse> getStudents(int page,
                                             int size,
                                             String sortBy) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        Page<Student> studentPage = studentRepository.findAll(pageable);

        //why map() - a page student returns student while we need studentresponse
        // so map converts each element while preserving all the meta data like total page,size , element etc...
        return studentPage.map(studentMapper::toResponse);
    }
}