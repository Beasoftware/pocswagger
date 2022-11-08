package com.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.model.Employee;

@Repository

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
