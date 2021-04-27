package com.personio.employeehierarchy.controller;

import com.personio.employeehierarchy.domain.HierarchicalOrganization;
import com.personio.employeehierarchy.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map> getOrganizationService() {
        log.info(this.getClass().getSimpleName() + "-> getOrganization");
        return HierarchicalOrganization.assembleTopDownHierarchy(organizationService
                .getRoot().map(List::of).orElse(List.of()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setOrganizationService(@RequestBody final Map<String, String> employeesMap) {
        log.info(this.getClass().getSimpleName() + "-> setOrganization");
        log.info("Controller calling service with " + employeesMap);
        organizationService.addEmployees(employeesMap);
    }

    @GetMapping(value = "employee/{employeeName}/management",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map> getEmployee(@PathVariable(value = "employeeName") final String employeeName) {
        log.info(this.getClass().getSimpleName() + "-> getEmployee");
        return HierarchicalOrganization.assembleBottomUpHierarchy(organizationService.
                getEmployee(employeeName).get());
    }
}
