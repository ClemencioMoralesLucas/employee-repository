package com.company.employeehierarchy.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

class EmployeeTest {

    private static final String EMPLOYEE_NAME = "Solid Snake";
    private static final String BOSS_NAME = "Big Boss";

    private static final Employee EMPLOYEE = new Employee(EMPLOYEE_NAME);
    private static final Employee BOSS = new Employee(BOSS_NAME);

    @Test
    public void itShouldAllowManagedEmployees() {
        assertThat(EMPLOYEE.getManager(), is(Optional.empty()));
        EMPLOYEE.setManager(BOSS);
        BOSS.addManagedEmployee(EMPLOYEE);
        assertThat(BOSS.getManagedEmployees(), is(List.of(EMPLOYEE)));
        assertThat(EMPLOYEE.getManager(), is(Optional.of(BOSS)));
    }

    @Test
    public void itShouldBeReflexive() {
        final var employee = new Employee(EMPLOYEE_NAME);
        assertThat(EMPLOYEE, is(equalTo(employee)));
        employee.setManager(BOSS);
        assertThat(EMPLOYEE, is(equalTo(employee)));
    }

    @Test
    public void itShouldBeSymmetric() {
        final var employeeSame = new Employee(EMPLOYEE_NAME);
        assertThat(EMPLOYEE, is(equalTo(employeeSame)));
        assertThat(employeeSame, is(equalTo(EMPLOYEE)));
    }

    @Test
    public void itShouldBeTransitive() {
        final var employeeY = new Employee(EMPLOYEE_NAME);
        final var employeeZ = new Employee(EMPLOYEE_NAME);
        assertThat(EMPLOYEE, is(equalTo(employeeY)));
        assertThat(employeeY, is(equalTo(employeeZ)));
        assertThat(EMPLOYEE, is(equalTo(employeeZ)));
    }

    @Test
    public void itShouldBeEqualEmployeeHashCodeIfSameName() {
        final var employee = new Employee(EMPLOYEE_NAME);
        assertThat(EMPLOYEE.hashCode(), is(equalTo(employee.hashCode())));
        employee.setManager(BOSS);
        assertThat(EMPLOYEE.hashCode(), is(equalTo(employee.hashCode())));
    }

    @Test
    public void itShouldBeRoot() {
        final Employee employee = new Employee(EMPLOYEE_NAME);
        assertThat(employee.isRoot(), is(true));    }

    @Test
    public void itShouldNotBeRoot() {
        final var employee = new Employee(EMPLOYEE_NAME);
        employee.setManager(BOSS);
        assertThat(employee.isRoot(), is(false));
    }
}