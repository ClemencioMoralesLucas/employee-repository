package com.personio.employeehierarchy.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

class EmployeeTest {

    private static final String EMPLOYEE_NAME = "Solid Snake";
    private static final String BOSS_NAME = "Big Boss";

    private static final Employee EMPLOYEE = new Employee(EMPLOYEE_NAME);
    private static final Employee BOSS = new Employee(BOSS_NAME);

    @Test
    public void itShouldBeRoot() {
        assertThat(EMPLOYEE.isRoot(), is(true));
    }

    @Test
    public void itShouldNotBeRoot() {
        final var employee = new Employee(EMPLOYEE_NAME);
        employee.setManager(BOSS);
        assertThat(employee.isRoot(), is(false));
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
}