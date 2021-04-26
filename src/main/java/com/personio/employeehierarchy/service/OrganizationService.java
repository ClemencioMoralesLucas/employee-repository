package com.personio.employeehierarchy.service;

import com.personio.employeehierarchy.domain.Employee;
import com.personio.employeehierarchy.domain.Organization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class OrganizationService {

    private final Organization organization;

    public Optional<Employee> getRoot() {
        return organization.getRoot();
    }

    public Optional<Employee> getEmployee(final String employeeName) {
        return organization.getEmployee(employeeName);
    }

    @Transactional
    public void addEmployees(final Map<String, String> newEmployees) {
        newEmployees.forEach(organization::addEmployee);
        organization.verifySingleRoot();
    }
}
