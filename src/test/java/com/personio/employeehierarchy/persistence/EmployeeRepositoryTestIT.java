package com.personio.employeehierarchy.persistence;

import com.personio.employeehierarchy.domain.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class EmployeeRepositoryTestIT {

    private static final String BIG_BOSS_NAME = "Big Boss";
    private static final String SOLID_SNAKE_NAME = "Solid Snake";

    private static final String DANA_WHITE_BOSS_NAME = "Dana White";
    private static final String FRANK_MIR_NAME = "Frank Mir";

    private static final String ELON_MUSK_MANAGER_NAME = "Elon Musk";
    private static final String HANK_SCORPIO_NAME = "Hank Scorpio";

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void itShouldRetrieveAllRootsWhenFindingRoots() {
        final Employee metalGearHierarchy = saveEmployeeWithBoss(BIG_BOSS_NAME, SOLID_SNAKE_NAME);
        final Employee ufcHierarchy = saveEmployeeWithBoss(DANA_WHITE_BOSS_NAME, FRANK_MIR_NAME);
        final var roots = employeeRepository.findRoots();
        assertThat(roots.size(), is(2));
        assertThat(roots, contains(metalGearHierarchy, ufcHierarchy));
    }

    @Test
    public void itShouldReturnRootsCounterWhenCountingRoots() {
        saveEmployeeWithBoss(BIG_BOSS_NAME, SOLID_SNAKE_NAME);
        saveEmployeeWithBoss(DANA_WHITE_BOSS_NAME, FRANK_MIR_NAME);
        assertThat(employeeRepository.countRoots(), is((long) 2));
    }

    @Test
    public void itShouldCreateNonExistingEmployeesWhenFindingByNameOrCreate() {
        final var newEmployeeCreated = employeeRepository.findByNameOrCreate(HANK_SCORPIO_NAME);
        assertThat(new Employee(HANK_SCORPIO_NAME), is(newEmployeeCreated));
    }

    @Test
    @Transactional
    public void itShouldStoreComplexHierarchiesOnSave() {
        final var boss = new Employee(BIG_BOSS_NAME);
        final var manager = new Employee(ELON_MUSK_MANAGER_NAME);
        final var employee = new Employee(SOLID_SNAKE_NAME);
        persistThreeStepHierarchy(boss, manager, employee);

        verifyEmployeeIsConsistent(boss, manager, employee);
        verifyBossIsConsistent(boss, manager, employee);
        assertThat(employeeRepository.findAll().size(), is(3));
    }

    private Employee saveEmployeeWithBoss(final String bossName, final String employeeName) {
        final var boss = new Employee(bossName);
        final var employee = new Employee(employeeName);
        employee.setManager(boss);
        employeeRepository.save(employee);
        return boss;
    }

    private void verifyBossIsConsistent(final Employee boss, final Employee manager, final Employee employee) {
        final var foundBoss = employeeRepository.findByName(BIG_BOSS_NAME).get();
        assertThat(foundBoss, is(boss));
        assertThat(foundBoss.getManagedEmployees().get(0), is(manager));
        assertThat(foundBoss.getManagedEmployees().get(0).getManagedEmployees().get(0), is(employee));
    }

    private void verifyEmployeeIsConsistent(final Employee boss, final Employee manager, final Employee employee) {
        final var retrievedEmployee = employeeRepository.findByName(SOLID_SNAKE_NAME).get();
        assertThat(retrievedEmployee, is(employee));
        assertThat(retrievedEmployee.getManager().orElseThrow(), is(manager));
        assertThat(retrievedEmployee.getManager().orElseThrow().getManager().orElseThrow(), is(boss));
    }

    private void persistThreeStepHierarchy(final Employee boss, final Employee manager, final Employee employee) {
        employee.setManager(manager);
        manager.addManagedEmployee(employee);
        manager.setManager(boss);
        boss.addManagedEmployee(manager);
        employeeRepository.save(employee);
    }
}
