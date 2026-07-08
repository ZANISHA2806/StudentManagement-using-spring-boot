package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.UserRequest;
import com.example.studentmanagement.dto.UserResponse;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.mapper.UserMapper;
import com.example.studentmanagement.repository.RoleRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest request) {

        log.info("Creating user: {}", request.getUsername());

        User user = userMapper.toEntity(request);

        // Encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign role
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + request.getRole()));

        user.setRole(role);

        // Link student if role is ROLE_STUDENT
        if ("ROLE_STUDENT".equals(request.getRole())) {

            if (request.getStudentId() == null) {
                throw new IllegalArgumentException(
                        "Student ID is required for ROLE_STUDENT");
            }

            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Student not found with id " + request.getStudentId()));

            user.setStudent(student);
        }

        User savedUser = userRepository.save(user);

        log.info("User created successfully with ID {}", savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        log.info("Fetching all active users");

        return userRepository.findByDeletedFalse()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id));

        user.setUsername(request.getUsername());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + request.getRole()));

        user.setRole(role);

        // Update student link if role is ROLE_STUDENT
        if ("ROLE_STUDENT".equals(request.getRole())) {

            if (request.getStudentId() == null) {
                throw new IllegalArgumentException(
                        "Student ID is required for ROLE_STUDENT");
            }

            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Student not found with id " + request.getStudentId()));

            user.setStudent(student);

        } else {

            // Remove student association for ADMIN/TEACHER
            user.setStudent(null);
        }

        log.info("User updated successfully");

        // Dirty checking will update automatically
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id));

        user.setDeleted(true);

        log.info("User soft deleted successfully");
    }
}