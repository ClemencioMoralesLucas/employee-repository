package com.personio.employeehierarchy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager")
    private Employee manager;

    @OneToMany
    private final List<Employee> managedEmployees = new ArrayList<>();

    public Employee(final String name) {
        this.name = name;
    }

    public Optional<Employee> getManager() {
        return Optional.ofNullable(manager);
    }

    public void setManager(final Employee parent) {
        this.manager = parent;
    }

    public void addManagedEmployee(final Employee managed) {
        this.managedEmployees.add(managed);
    }

    public boolean isRoot() {
        return manager == null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Employee employee = (Employee) o;
        return name.equals(employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

