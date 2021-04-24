package com.personio.employeehierarchy.domain;

import com.personio.employeehierarchy.persistence.EmployeeRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Component
public class Organization {

    private static final int MAXIMUM_ROOTS_ALLOWED = 1;
    private final EmployeeRepository employeeRepository;

    public Organization(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Employee> getRoot() {
        return employeeRepository.findRoots().stream().findAny();
    }

    public Optional<Employee> getEmployee(final String employeeName) {
        return employeeRepository.findByName(employeeName);
    }

    public void addEmployee(final String employeeName, final String managerName) {
        final var employee = employeeRepository.findByNameOrCreate(employeeName);
        final var manager = employeeRepository.findByNameOrCreate(managerName);
        employee.setManager(manager);
        manager.addManagedEmployee(employee);
        checkCyclicDep(employee, employee);
        employeeRepository.save(employee);
    }

    public void verifySingleRoot() {
        if (MAXIMUM_ROOTS_ALLOWED < employeeRepository.countRoots()) {
            final var roots = employeeRepository.findRoots().stream().collect(toList()).stream()
                    .map(Employee::getName).collect(toList());
            throw new IllegalArgumentException("ERROR: Multiple roots detected: " + roots);
        }
    }

    private void checkCyclicDep(final Employee employee, final Employee other) {
        if (!employee.isRoot()) {
            employee.getManager().ifPresent(manager -> {
                if (manager.equals(other)) {
                    throw new IllegalArgumentException(format("ERROR: Cyclic dependency detected" +
                            "for employee [%s]", employee.getName()));
                }
                checkCyclicDep(manager, employee);
            });
        }
    }
}

