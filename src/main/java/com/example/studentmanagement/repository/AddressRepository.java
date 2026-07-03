package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
//    By extending JpaRepository, we automatically get:
//
//    save()
//    findById()
//    findAll()
//    delete()
//    existsById()
//
//    No SQL is required.
//
//    Hibernate generates everything.

}