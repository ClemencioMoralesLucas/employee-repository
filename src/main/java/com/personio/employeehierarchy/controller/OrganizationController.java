package com.personio.employeehierarchy.controller;

import com.personio.employeehierarchy.domain.HierarchicalOrganization;
import com.personio.employeehierarchy.domain.exceptions.InvalidOrganizationException;
import com.personio.employeehierarchy.domain.exceptions.NotFoundEmployeeException;
import com.personio.employeehierarchy.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@AllArgsConstructor
public class OrganizationController {

    public static final String INVALID_OR_EMPTY_JSON_MESSAGE = "Invalid or empty JSON";
    public static final String EMPLOYEE_NOT_FOUND_MESSAGE = "Employee not found";

    private final OrganizationService organizationService;

    @PostMapping(value = "/api/v1/organization", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map> setEmployees(@RequestBody final Map<String, String> employeesMap) {
        validateInputMap(employeesMap);
        log.info(this.getClass().getSimpleName() + "-> setOrganization: " + employeesMap);
        organizationService.addEmployees(employeesMap);
        return HierarchicalOrganization.assembleTopDownHierarchy(organizationService
                .getRoot().map(List::of).orElse(List.of()));
    }

    @GetMapping(value = "/api/v1/organization/employee/{employeeName}/management",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map> getEmployee(@PathVariable(value = "employeeName") final String employeeName) {
        log.info(this.getClass().getSimpleName() + "-> getEmployee: " + employeeName);
        try {
            return HierarchicalOrganization.assembleBottomUpHierarchy(organizationService.
                    getEmployee(employeeName).get());
        } catch (final NoSuchElementException e) {
            throw new NotFoundEmployeeException(EMPLOYEE_NOT_FOUND_MESSAGE);
        }
    }

    private void validateInputMap(final Map<String, String> employeesMap) {
        if(CollectionUtils.isEmpty(employeesMap)) {
            throw new InvalidOrganizationException(INVALID_OR_EMPTY_JSON_MESSAGE);
        }
    }
}
