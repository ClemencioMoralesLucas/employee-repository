package com.personio.employeehierarchy.persistence;

import com.personio.employeehierarchy.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByName(String name);

    default Employee findByNameOrCreate(String name) {
        return findByName(name).orElseGet(() -> new Employee(name));
    }

    default List<Employee> findRoots() {
        return findDistinctByManagerIsNull();
    }

    default long countRoots() {
        return countDistinctByManagerIsNull();
    }

    List<Employee> findDistinctByManagerIsNull();

    long countDistinctByManagerIsNull();

}
