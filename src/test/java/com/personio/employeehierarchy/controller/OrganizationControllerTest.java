package com.personio.employeehierarchy.controller;

import com.personio.employeehierarchy.domain.Employee;
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
    public void itShouldForwardCallToServiceWhenSettingOrganization() {
        organizationController.setOrganizationService(foxhoundEmployees);
        verify(organizationServiceMock).addEmployees(foxhoundEmployees);
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
    public void itShouldReturnHierarchyWithRootWhenCallingGetOrganization() {
        when(organizationServiceMock.getRoot())
                .thenReturn(Optional.of(new Employee(GRAY_FOX_NAME)));
        assertThat(organizationController.getOrganizationService(),
                is(Collections.unmodifiableMap(Map.of(GRAY_FOX_NAME, Map.of()))));
        verify(organizationServiceMock).getRoot();
        verifyNoMoreInteractions(organizationServiceMock);
    }

    @Test
    public void itShouldReturnEmptyOrganizationWhenCallingGetOrganization() {
        when(organizationServiceMock.getRoot()).thenReturn(Optional.empty());
        assertThat(organizationController.getOrganizationService(),
                is(Collections.unmodifiableMap(Map.of())));
    }
}
