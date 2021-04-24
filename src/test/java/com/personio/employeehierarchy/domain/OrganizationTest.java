package com.personio.employeehierarchy.domain;

import com.personio.employeehierarchy.persistence.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationTest {

    private static final String EMPLOYEE_NAME = "Solid Snake";
    private static final String MANAGER_NAME = "Big Boss";
    private static final String UNKNOWN_EMPLOYEE_NAME = "Revolver Ocelot";

    @InjectMocks
    Organization organization;

    @Mock
    EmployeeRepository employeeRepositoryMock;

    private ArgumentCaptor<Employee> employeeArgumentCaptor;
    private Employee employee;
    private Employee manager;

    @BeforeEach
    public void setUp() {
        this.employee = new Employee(EMPLOYEE_NAME);
        this.manager = new Employee(MANAGER_NAME);
        this.employee.setManager(manager);
        this.employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
    }

    @Test
    public void itShouldForwardCallToRepositoryWhenAddingEmployees() {
        final var anotherManager = new Employee(UNKNOWN_EMPLOYEE_NAME);
        when(employeeRepositoryMock.findByNameOrCreate(EMPLOYEE_NAME)).thenReturn(employee);
        when(employeeRepositoryMock.findByNameOrCreate(MANAGER_NAME))
                .thenReturn(anotherManager);
        organization.addEmployee(EMPLOYEE_NAME, MANAGER_NAME);

        verify(employeeRepositoryMock).save(employeeArgumentCaptor.capture());
        assertThat(employeeArgumentCaptor.getValue().getManager().get(), is(anotherManager));
    }

    @Test
    public void itShouldReturnManagedEmployeeFindByName() {
        when(employeeRepositoryMock.findByName(EMPLOYEE_NAME))
                .thenReturn(Optional.of(employee));

        final var foundEmployee =
                organization.getEmployee(EMPLOYEE_NAME).get().getManager();

        assertThat(foundEmployee.get(), is(manager));
    }

    @Test
    public void itShouldFailIfNoSuchEmployeePresent() {
        when(employeeRepositoryMock.findByName(UNKNOWN_EMPLOYEE_NAME))
                .thenReturn(Optional.empty());
        assertThat(organization.getEmployee(UNKNOWN_EMPLOYEE_NAME).isEmpty(), is(true));
    }

    @Test
    public void itShouldFailIfAddingEmployeesWithMoreThanOneRoot() {
        when(employeeRepositoryMock.countRoots()).thenReturn(2L);
        final Exception exception = assertThrows(InvalidOrganizationException.class,
                () -> organization.verifySingleRoot());
        assertThat(exception.getMessage(), is("ERROR: Multiple roots detected: []"));
    }

    @Test
    public void itShouldReturnRootEmployee() {
        when(employeeRepositoryMock.findRoots())
                .thenReturn(Collections.unmodifiableList(List.of(manager)));
        assertThat(organization.getRoot().get(), is(manager));
    }

    @Test
    public void itShouldFailIfNoRootPresent() {
        when(employeeRepositoryMock.findRoots()).thenReturn(Collections.emptyList());
        assertThat(organization.getRoot().isEmpty(), is(true));
    }
}
