package com.company.employeehierarchy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class HierarchicalOrganizationTest {

    private static final Map<Object, Object> EMPTY_MAP = Collections.unmodifiableMap(Map.of());
    private static final String BIG_BOSS_NAME = "Big Boss";
    private static final String LIQUID_SNAKE_NAME = "Liquid Snake";
    private static final String SOLID_SNAKE_NAME = "Solid Snake";
    private static final String REVOLVER_OCELOT_NAME = "Revolver Ocelot";
    private static final String SNIPER_WOLF_NAME = "Sniper Wolf";
    private static final String PSYCHO_MANTIS_NAME = "Psycho Mantis";

    private Employee bigBoss;
    private Employee liquidSnake;
    private Employee solidSnake;
    private Employee revolverOcelot;
    private Employee sniperWolf;

    @BeforeEach
    public void setUp() {
        this.bigBoss = new Employee(BIG_BOSS_NAME);
        this.liquidSnake = new Employee(LIQUID_SNAKE_NAME);
        this.solidSnake = new Employee(SOLID_SNAKE_NAME);
        this.revolverOcelot = new Employee(REVOLVER_OCELOT_NAME);
        this.sniperWolf = new Employee(SNIPER_WOLF_NAME);

        bigBoss.addManagedEmployee(liquidSnake);
        liquidSnake.addManagedEmployee(solidSnake);
        solidSnake.addManagedEmployee(revolverOcelot);
        solidSnake.addManagedEmployee(sniperWolf);
    }

    @Test
    public void itShouldAssembleBottomUpHierarchy() {
        final var bigBoss = new Employee(BIG_BOSS_NAME);
        final var liquidSnake = new Employee(LIQUID_SNAKE_NAME);
        bigBoss.addManagedEmployee(liquidSnake);
        liquidSnake.setManager(bigBoss);
        final var solidSnake = new Employee(SOLID_SNAKE_NAME);
        liquidSnake.addManagedEmployee(solidSnake);
        solidSnake.setManager(liquidSnake);

        final var expectedMap =
                Map.of(SOLID_SNAKE_NAME,
                        Map.of(LIQUID_SNAKE_NAME,
                                Map.of(BIG_BOSS_NAME, EMPTY_MAP)));
        assertThat(HierarchicalOrganization.assembleBottomUpHierarchy(solidSnake),
                is(expectedMap));
    }

    @Test
    public void itShouldAssembleBottomUpHierarchyWithoutRoots() {
        assertThat(HierarchicalOrganization.assembleBottomUpHierarchy(new Employee(PSYCHO_MANTIS_NAME)),
                is(Map.of(new Employee(PSYCHO_MANTIS_NAME).getName(), EMPTY_MAP)));
    }

    @Test
    public void itShouldAssembleTopDownHierarchy() {
        final var expectedMap =
                Map.of(BIG_BOSS_NAME, Map.of(LIQUID_SNAKE_NAME,
                        Map.of(SOLID_SNAKE_NAME,
                                Map.of(SNIPER_WOLF_NAME, EMPTY_MAP,
                                        REVOLVER_OCELOT_NAME, EMPTY_MAP))));
        assertThat(HierarchicalOrganization.assembleTopDownHierarchy(List.of(bigBoss)),
                is(expectedMap));
    }

    @Test
    public void itShouldAssembleTopDownHierarchyWithoutRoots() {
        assertThat(HierarchicalOrganization.assembleTopDownHierarchy(Collections.EMPTY_LIST),
                is(EMPTY_MAP));
    }
}
