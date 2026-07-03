package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
//student - which entity this repo should manage
// long- what is the data type of primary key
    // -----------------------------
    // Named Queries
    // -----------------------------

    @Query(name = "Student.findAllStudents")
    List<Student> getAllStudentsUsingNamedQuery();

    @Query(name = "Student.findByEmail")
    Student getStudentByEmail(@Param("email") String email);

    // -----------------------------
    // JPQL Queries
    // -----------------------------

    @Query("""
            SELECT s
            FROM Student s
            """)
    List<Student> findAllStudents();

    @Query("""
            SELECT s
            FROM Student s
            WHERE s.department.id = :departmentId
            """)
    List<Student> findStudentsByDepartment(
            @Param("departmentId") Long departmentId);

    @Query("""
            SELECT s
            FROM Student s
            WHERE s.name LIKE :prefix
            """)
    List<Student> findStudentsByNameStartingWith(
            @Param("prefix") String prefix);

    @Query("""
            SELECT s
            FROM Student s
            WHERE s.age BETWEEN :minAge AND :maxAge
            """)
    List<Student> findStudentsBetweenAge(
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge);

    @Query("""
            SELECT s
            FROM Student s
            WHERE s.id IN :ids
            """)
    List<Student> findStudentsByIds(
            @Param("ids") List<Long> ids);

    @Query("""
            SELECT s
            FROM Student s
            ORDER BY s.name ASC
            """)
    List<Student> getStudentsSortedByName();

    @Query("""
            SELECT s
            FROM Student s
            WHERE s.age > :age
            AND s.name LIKE :prefix
            """)
    List<Student> searchStudents(
            @Param("age") Integer age,
            @Param("prefix") String prefix);

    // -----------------------------
    // Aggregate Functions
    // -----------------------------

    @Query("""
            SELECT COUNT(s)
            FROM Student s
            """)
    Long countStudents();

    @Query("""
            SELECT AVG(s.age)
            FROM Student s
            """)
    Double getAverageAge();

    @Query("""
            SELECT MAX(s.age)
            FROM Student s
            """)
    Integer getMaximumAge();

    @Query("""
            SELECT MIN(s.age)
            FROM Student s
            """)
    Integer getMinimumAge();

    // -----------------------------
    // Distinct
    // -----------------------------

    @Query("""
            SELECT DISTINCT s.department
            FROM Student s
            """)
    List<com.example.studentmanagement.entity.Department> getDepartments();

    // -----------------------------
    // Pagination
    // -----------------------------

    Page<Student> findAll(Pageable pageable);

}