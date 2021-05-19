package com.company.employeehierarchy.service;

import com.company.employeehierarchy.domain.Employee;
import com.company.employeehierarchy.domain.Organization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    private static final String EMPLOYEE_NAME_1 = "Sniper Wolf";
    private static final String EMPLOYEE_NAME_2 = "Vulkan Raven";
    private static final String EMPLOYEE_NAME_3 = "Psycho Mantis";
    private static final String EMPLOYEE_NAME_4 = "Revolver Ocelot";
    private static final String EMPLOYEE_NAME_5 = "Grey Fox";

    @InjectMocks
    OrganizationService organizationService;

    @Mock
    Organization organizationMock;

    @Test
    public void itShouldForwardCallToOrganizationWhenGettingEmployeesAndVerifySingleRoot() {
        organizationService.addEmployees(Collections.unmodifiableMap(Map.of(EMPLOYEE_NAME_2,
                EMPLOYEE_NAME_3, EMPLOYEE_NAME_4, EMPLOYEE_NAME_5)));
        verify(organizationMock).addEmployee(EMPLOYEE_NAME_2, EMPLOYEE_NAME_3);
        verify(organizationMock).addEmployee(EMPLOYEE_NAME_4, EMPLOYEE_NAME_5);
        verify(organizationMock).verifySingleRoot();
    }

    @Test
    public void itShouldForwardCallToOrganizationWhenGettingEmployee() {
        final var actualOptionalEmployee = Optional.of(new Employee());
        when(organizationMock.getEmployee(EMPLOYEE_NAME_1)).thenReturn(actualOptionalEmployee);
        assertThat(actualOptionalEmployee,
                is(organizationService.getEmployee(EMPLOYEE_NAME_1)));
    }

    @Test
    public void itShouldForwardCallToOrganizationWhenGettingRoot() {
        final var actualOptionalEmployee = Optional.of(new Employee());
        when(organizationMock.getRoot()).thenReturn(actualOptionalEmployee);
        assertThat(actualOptionalEmployee, is(organizationService.getRoot()));
    }
}