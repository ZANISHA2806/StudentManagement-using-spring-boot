package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Create Student
    @PostMapping
    public Student saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    // Get Student By Id
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // Get All Students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Update Student
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id,
                                 @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    // Delete Student (Soft Delete)
    @DeleteMapping("/{id}")
    //In REST APIs, a 204 status code is best practice for delete operations.
    // It tells the client that the action succeeded and
    // there is no additional data to be return in response body
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    // Dirty Checking Example
    @PatchMapping("/{id}/name")
    public String updateStudentName(@PathVariable Long id,
                                    @RequestParam String name) {

        studentService.updateStudentName(id, name);

        return "Student name updated successfully.";
    }

    // Assign Department
    @PutMapping("/{studentId}/departments/{departmentId}")
    public Student assignDepartment(@PathVariable Long studentId,
                                    @PathVariable Long departmentId) {

        return studentService.assignDepartment(studentId, departmentId);
    }

    // Assign Course
    @PutMapping("/{studentId}/courses/{courseId}")
    public Student assignCourse(@PathVariable Long studentId,
                                @PathVariable Long courseId) {

        return studentService.assignCourse(studentId, courseId);
    }

    // Pagination & Sorting
    @GetMapping("/page")
    public Page<Student> getStudents(@RequestParam int page,
                                     @RequestParam int size,
                                     @RequestParam String sortBy) {

        return studentService.getStudents(page, size, sortBy);
    }

}