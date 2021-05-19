package com.company.employeehierarchy.domain;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class HierarchicalOrganization {

    public static Map<String, Map> assembleBottomUpHierarchy(final Employee employee) {
        final var optionalManager = employee.getManager();
        return optionalManager.<Map<String, Map>>map(value ->
                Map.of(employee.getName(), assembleBottomUpHierarchy(value)))
                .orElseGet(() -> Map.of(employee.getName(), Map.of()));
    }

    public static Map<String, Map> assembleTopDownHierarchy(final List<Employee> employeeList) {
        return employeeList.isEmpty() ? Map.of() : employeeList.stream().collect(
                toMap(Employee::getName, employee -> assembleTopDownHierarchy(employee.getManagedEmployees())));
    }
}
