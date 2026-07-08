package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.response.ApiResponse;
import com.example.studentmanagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

//    public StudentController(StudentService studentService) {
//
//        this.studentService = studentService;
//    }

    // Create Student
//    @PostMapping
//    public StudentResponse saveStudent(
//@Valid
//            @RequestBody StudentRequest request){
//
//        return studentService.saveStudent(request);
//
//    }


    //request json - student request -student service -student response - api response -response entity - client
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> saveStudent(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.saveStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Student created successfully",
                                response
                        )
                );
    }

    // Get Student By Id
//    @GetMapping("/{id}")
//    public StudentResponse getStudent(@PathVariable Long id) {
//        return studentService.getStudentById(id);
//    }

    @PreAuthorize(
            "@authorizationService.canAccessStudent(authentication.principal,#id)"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>>
    getStudent(@PathVariable Long id){

        StudentResponse response =
                studentService.getStudentById(id);

        ApiResponse<StudentResponse> apiResponse =
                new ApiResponse<>(
                        true,
                        "Student fetched successfully",
                        response
                );

        return ResponseEntity.ok(apiResponse);

    }

    // Get All Students

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents() {

        List<StudentResponse> students = studentService.getAllStudents();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Students fetched successfully",
                        students
                )
        );
    }

    // Update Student
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.updateStudent(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Student updated successfully",
                        response
                )
        );
    }

    // Delete Student (Soft Delete)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    //In REST APIs, a 204 status code is best practice for delete operations.
    // It tells the client that the action succeeded and
    // there is no additional data to be return in response body
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiResponse<StudentResponse>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(

                new ApiResponse<>(

                        true,

                        "Student deleted successfully",

                        null

                )

        );
    }

    // Dirty Checking Example
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @PatchMapping("/{id}/name")
    public ResponseEntity<ApiResponse<String>> updateStudentName(@PathVariable Long id,
                                    @RequestParam String name) {

        studentService.updateStudentName(id, name);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Student name updated successfully",
                        name
                )
        );
    }

    // Assign Department
    @PutMapping("/{studentId}/departments/{departmentId}")
    public StudentResponse assignDepartment(@PathVariable Long studentId,
                                    @PathVariable Long departmentId) {

        return studentService.assignDepartment(studentId, departmentId);
    }

    // Assign Course
    @PutMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<ApiResponse<StudentResponse>> assignCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        StudentResponse response =
                studentService.assignCourse(studentId, courseId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Course assigned successfully",
                        response
                )
        );
    }

    // Pagination & Sorting
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudents(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {

        Page<StudentResponse> students =
                studentService.getStudents(page, size, sortBy);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Students fetched successfully",
                        students
                )
        );

}}