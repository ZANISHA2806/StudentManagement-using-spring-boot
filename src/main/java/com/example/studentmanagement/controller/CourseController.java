package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.CourseRequest;
import com.example.studentmanagement.dto.CourseResponse;
import com.example.studentmanagement.response.ApiResponse;
import com.example.studentmanagement.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CourseController {

    private final CourseService courseService;

    // Create Course
    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = courseService.createCourse(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Course created successfully",
                        response
                ));
    }

    // Get All Courses
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {

        List<CourseResponse> response = courseService.getAllCourses();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Courses fetched successfully",
                        response
                )
        );
    }

    // Get Course By Id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable Long id) {

        CourseResponse response = courseService.getCourseById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Course fetched successfully",
                        response
                )
        );
    }

    // Update Course
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = courseService.updateCourse(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Course updated successfully",
                        response
                )
        );
    }

    // Delete Course
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Course deleted successfully",
                        null
                )
        );
    }
}