package com.personio.employeehierarchy.controller;

import com.personio.employeehierarchy.domain.exceptions.InvalidOrganizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class OrganizationControllerTestIT {

    private static final String VULKAN_RAVEN_NAME = "Vulkan Raven";
    private static final String SNIPER_WOLF_NAME = "Sniper Wolf";
    private static final String LIQUID_SNAKE_NAME = "Liquid Snake";
    private static final String SOLIDUS_SNAKE_NAME = "Solidus Snake";
    private static final String BIG_BOSS_NAME = "Big Boss";
    private static final String DECOY_OCTOPUS_NAME = "Decoy Octopus";

    @Autowired
    OrganizationController organizationController;

    private Map employeesBeforeUpdate;
    private Map hierarchyBeforeUpdate;

    private Map employeesAfterUpdate;
    private Map hierarchyAfterUpdate;

    private Map invalidEmployeesWithCycles;

    @BeforeEach
    public void setUp() {
        this.employeesBeforeUpdate = Collections.unmodifiableMap(Map.of(VULKAN_RAVEN_NAME, SNIPER_WOLF_NAME,
                LIQUID_SNAKE_NAME, SNIPER_WOLF_NAME,
                SNIPER_WOLF_NAME, SOLIDUS_SNAKE_NAME,
                SOLIDUS_SNAKE_NAME, BIG_BOSS_NAME));
        this.hierarchyBeforeUpdate = Collections.unmodifiableMap(Map.of(BIG_BOSS_NAME,
                Map.of(SOLIDUS_SNAKE_NAME, Map.of(SNIPER_WOLF_NAME, Map.of(LIQUID_SNAKE_NAME,
                        Map.of(), VULKAN_RAVEN_NAME, Map.of())))));

        this.employeesAfterUpdate = Collections.unmodifiableMap(Map.of(DECOY_OCTOPUS_NAME, BIG_BOSS_NAME));
        this.hierarchyAfterUpdate = Collections.unmodifiableMap(Map.of(BIG_BOSS_NAME,
                Map.of(DECOY_OCTOPUS_NAME, Map.of(), SOLIDUS_SNAKE_NAME, Map.of(SNIPER_WOLF_NAME,
                        Map.of(LIQUID_SNAKE_NAME, Map.of(), VULKAN_RAVEN_NAME, Map.of())))));

        this.invalidEmployeesWithCycles = Collections.unmodifiableMap(Map.of(SOLIDUS_SNAKE_NAME, SNIPER_WOLF_NAME));
    }

    @Test
    @Transactional
    public void controllerShouldPreventCyclicDependencies() {
        organizationController.setEmployees(employeesBeforeUpdate);
        final var exception = assertThrows(InvalidOrganizationException.class, ()
                -> organizationController.setEmployees(invalidEmployeesWithCycles));
        assertThat(exception.getMessage(),
                is("ERROR: Cyclic dependency detected for employee [" + SNIPER_WOLF_NAME + "]"));
    }

    @Test
    @Transactional
    public void itShouldSetAndGetOrganizationConsistently() {
        assertThat(organizationController.setEmployees(employeesBeforeUpdate), is(hierarchyBeforeUpdate));
        assertThat(organizationController.getOrganization(), is(hierarchyBeforeUpdate));
    }

    @Test
    @Transactional
    public void itShouldUpdateEmployees() {
        final var resultBeforeUpdate = organizationController.setEmployees(employeesBeforeUpdate);
        assertThat(resultBeforeUpdate, is(hierarchyBeforeUpdate));

        final var resultAfterUpdate = organizationController.setEmployees(employeesAfterUpdate);
        assertThat(resultAfterUpdate, is(hierarchyAfterUpdate));
    }

    @Test
    @Transactional
    public void itShouldGetExistingEmployee() {
        organizationController.setEmployees(employeesBeforeUpdate);
        final var expected =
                Collections.unmodifiableMap(Map.of(SOLIDUS_SNAKE_NAME,
                        Map.of(BIG_BOSS_NAME, Map.of())));
        assertThat(expected, is(organizationController.getEmployee(SOLIDUS_SNAKE_NAME)));
    }
}
