package com.personio.employeehierarchy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.personio.employeehierarchy"})
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.personio.employeehierarchy"})
public class EmployeeHierarchyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeHierarchyApplication.class, args);
	}

}
