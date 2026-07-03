package com.example.studentmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;


@Entity
@NamedQueries({@NamedQuery(
        name = "Student.findAllStudents",
        query = "SELECT s FROM Student s"
),@NamedQuery(
        name = "Student.findByEmail",
        query = "SELECT s FROM Student s WHERE s.email = :email"
)})
@Table(name = "students")


@SQLDelete(sql = "UPDATE students SET deleted = true WHERE id=?")

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_name", nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    //here we have given the foreign key of the address table to the students
    // but we have not created the address table so when i run the application
    // it will throw error saying foreign table not found so we give cascade here
    //when we have multiple foreign tables then saving everytable manually would be
    // a tedious process so we use cascade

    @OneToOne(cascade ={ CascadeType.PERSIST,
            CascadeType.MERGE}
    )//saves child cls frst before parent cls is saved
    @JoinColumn(name = "address_id")
    @JsonManagedReference("student-address")
    private Address address;

    //MANY STUDENT BELONGS TO ONE DEPARTMENT AND FOREIGN KEY WILL ALWAYS BE IN A TABLE MANY TABLE
    @JsonBackReference("department-students")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;


    //many to many
@ManyToMany

@JoinTable(name="student_courses",
joinColumns = @JoinColumn(name="student_id"),
inverseJoinColumns = @JoinColumn(name="course_id")) // it creates a new table by joining col from both tables
    private List<Course> courses = new ArrayList<>();

//to do soft delete

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted = false;


    public Student() {
    }


    public Student(Address address, Integer age, List<Course> courses, boolean deleted, Department department, String email, String name) {
        this.address = address;
        this.age = age;
        this.courses = courses;
        this.deleted = deleted;
        this.department = department;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {

        // Break old relationship
        if (this.address != null) {
            this.address.setStudent(null);
        }

        this.address = address;

        // Create new relationship
        if (address != null) {
            address.setStudent(this);
        }
    }
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }





    //The two Java collections are separate objects in memory.
    //To keep them synchronized, add helper methods.
    public void addCourse(Course course) {

        courses.add(course);

        course.getStudents().add(this);
    }
    public void removeCourse(Course course) {

        courses.remove(course);

        course.getStudents().remove(this);
    }
    public void removeAddress() {

        if (this.address != null) {
            this.address.setStudent(null);
            this.address = null;
        }
    }
}