package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.CourseRequest;
import com.example.studentmanagement.dto.CourseResponse;
import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.mapper.CourseMapper;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse createCourse(CourseRequest request) {

        log.info("Creating course {}", request.getCourseName());

        Course course = courseMapper.toEntity(request);

        Course savedCourse = courseRepository.save(course);

        log.info("Course created successfully");

        return courseMapper.toResponse(savedCourse);
    }

    @Override
    public CourseResponse getCourseById(Long id) {

        log.info("Fetching course {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id " + id));

        return courseMapper.toResponse(course);
    }

    @Override
    public List<CourseResponse> getAllCourses() {

        log.info("Fetching all courses");

        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    public CourseResponse updateCourse(Long id,
                                       CourseRequest request) {

        log.info("Updating course {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id " + id));

        course.setCourseName(request.getCourseName());

        Course updatedCourse = courseRepository.save(course);

        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {

        log.info("Deleting course {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found with id " + id));

        course.setDeleted(true);

        log.info("Course soft deleted successfully");
    }
}