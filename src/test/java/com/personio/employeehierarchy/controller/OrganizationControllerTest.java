package com.personio.employeehierarchy.controller;

import com.personio.employeehierarchy.domain.Employee;
import com.personio.employeehierarchy.domain.exceptions.InvalidOrganizationException;
import com.personio.employeehierarchy.domain.exceptions.NotFoundEmployeeException;
import com.personio.employeehierarchy.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

    private static final String LIQUID_SNAKE_NAME = "Liquid Snake";
    private static final String VULKAN_RAVEN_NAME = "Vulkan Raven";
    private static final String REVOLVER_OCELOT_NAME = "Revolver Ocelot";
    private static final String PSYCHO_MANTIS_NAME = "Psycho Mantis";
    private static final String SNIPER_WOLF_NAME = "Sniper Wolf";
    private static final String BIG_BOSS_NAME = "Big Boss";
    private static final String SOLID_SNAKE_NAME = "Solid Snake";
    private static final String GRAY_FOX_NAME = "Gray Fox";
    private static final String NON_EXISTENT_EMPLOYEE = "Decoy Octopus";

    @InjectMocks
    OrganizationController organizationController;

    @Mock
    OrganizationService organizationServiceMock;

    private Map<String, String> foxhoundEmployees;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        foxhoundEmployees = Collections.unmodifiableMap(Map.of(
                VULKAN_RAVEN_NAME, LIQUID_SNAKE_NAME,
                REVOLVER_OCELOT_NAME, LIQUID_SNAKE_NAME,
                PSYCHO_MANTIS_NAME, LIQUID_SNAKE_NAME,
                SNIPER_WOLF_NAME, LIQUID_SNAKE_NAME,
                LIQUID_SNAKE_NAME, BIG_BOSS_NAME
        ));
        employee = new Employee(SOLID_SNAKE_NAME);
    }

    @Test
    public void itShouldForwardCallToServiceWhenSettingEmployeesAndReturnFormattedOrganization() {
        when(organizationServiceMock.getRoot())
                .thenReturn(Optional.of(new Employee(GRAY_FOX_NAME)));
        final var expectedResultMap =
                organizationController.setEmployees(foxhoundEmployees);
        verify(organizationServiceMock).addEmployees(foxhoundEmployees);
        verify(organizationServiceMock).getRoot();
        assertThat(expectedResultMap,
                is(Collections.unmodifiableMap(Map.of(GRAY_FOX_NAME, Map.of()))));
        verifyNoMoreInteractions(organizationServiceMock);
    }

    @Test
    public void itShouldThrowInvalidOrganizationExceptionIfInvalidJsonGiven() {
        final Exception exception = assertThrows(InvalidOrganizationException.class, () -> {
            organizationController.setEmployees(null);
        });
        assertThat(exception.getMessage(), is(OrganizationController.INVALID_OR_EMPTY_JSON_MESSAGE));
        verifyNoMoreInteractions(organizationServiceMock);
    }

    @Test
    public void itShouldThrowInvalidOrganizationExceptionIfEmptyJsonGiven() {
        final Map<String, String> emptyMap = Collections.unmodifiableMap(Map.of());
        final Exception exception = assertThrows(InvalidOrganizationException.class, () -> {
            organizationController.setEmployees(emptyMap);
        });
        assertThat(exception.getMessage(), is(OrganizationController.INVALID_OR_EMPTY_JSON_MESSAGE));
        verifyNoMoreInteractions(organizationServiceMock);
    }

    @Test
    public void itShouldForwardCallToServiceWhenGettingEmployee() {
        when(organizationServiceMock.getEmployee(SOLID_SNAKE_NAME))
                .thenReturn(Optional.of(employee));
        assertThat(organizationController.getEmployee(SOLID_SNAKE_NAME),
                is(Collections.unmodifiableMap(Map.of(SOLID_SNAKE_NAME, Map.of()))));
        verify(organizationServiceMock).getEmployee(SOLID_SNAKE_NAME);
        verifyNoMoreInteractions(organizationServiceMock);
    }

    @Test
    public void itShouldForwardCallToServiceWhenGettingOrganization() {
        when(organizationServiceMock.getRoot())
                .thenReturn(Optional.of(new Employee(GRAY_FOX_NAME)));
        assertThat(organizationController.getOrganization(),
                is(Collections.unmodifiableMap(Map.of(GRAY_FOX_NAME, Map.of()))));
        verify(organizationServiceMock).getRoot();
        verifyNoMoreInteractions(organizationServiceMock);
    }

    @Test
    public void itShouldReturnInvalidOrganizationExceptionWhenGettingNonExistentEmployee() {
        final Exception exception = assertThrows(NotFoundEmployeeException.class, () -> {
            organizationController.getEmployee(NON_EXISTENT_EMPLOYEE);
        });
        assertThat(exception.getMessage(), is(OrganizationController.EMPLOYEE_NOT_FOUND_MESSAGE));
        verify(organizationServiceMock).getEmployee(NON_EXISTENT_EMPLOYEE);
        verifyNoMoreInteractions(organizationServiceMock);
    }
}
