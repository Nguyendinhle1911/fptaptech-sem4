package org.example.salarymanagement.repository;

import org.example.salarymanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByName(String name);
}