package com.personio.employeehierarchy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.personio.employeehierarchy"})
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.personio.employeehierarchy"})
@EnableSwagger2
public class EmployeeHierarchyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeHierarchyApplication.class, args);
	}

}
