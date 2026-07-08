package com.example.studentmanagement.dto;

import jakarta.validation.constraints.*;

public class StudentRequest {


    //Not blank checks the entered is value or not it restricts even blank space and null
    @NotBlank(message = "Student name is required")
    @Size(min = 3, max = 100,
            message = "Name must contain 3 to 100 characters")
    private String name;


//not null checks only whether the entered value is null or not it access white space
    @NotNull(message = "Age is required")
    @Min(value = 18,
            message = "Age must be at least 18")
    @Max(value = 60,
            message = "Age cannot exceed 60")
    private Integer age;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private Long departmentId;

    public StudentRequest() {
    }

    public StudentRequest(String name,
                          Integer age,
                          String email,
                          Long departmentId) {

        this.name = name;
        this.age = age;
        this.email = email;
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}