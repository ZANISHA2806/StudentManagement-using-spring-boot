package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Department;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.DepartmentRepository;
import com.example.studentmanagement.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

//    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
//        this.departmentRepository = departmentRepository;
//    }

    @Override
    public Department saveDepartment(Department department) {

        return departmentRepository.save(department);
    }

    @Override
    public Department getDepartmentById(Long id) {
        log.info("Fetching Department {}", id);
        return departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

    }

    @Override
    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    @Override
    public Department updateDepartment(Long id,
                                       Department department) {

        Department existing = getDepartmentById(id);

        existing.setDepartment_name(department.getDepartment_name());
        log.info("Updating Department {}", id);
        return departmentRepository.save(existing);
    }

    @Override
    public void deleteDepartment(Long id) {

        Department department = getDepartmentById(id);
        log.info("Deleting Department {}", id);
        departmentRepository.delete(department);
    }

}