package com.company.acceptance.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpRequest;
import java.util.Map;

import static java.net.URI.create;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Testcontainers
@Slf4j
public class OrganizationHierarchyStepDefs implements En {

    private static final String ORGANIZATION_ENDPOINT = "/api/v1/organization";
    private static final String BASIC_AUTHORIZATION_USER_PASSWORD_BASE64 = "Basic dXNlcjpwYXNzd29yZA==";
    private String applicationPort = "8080";

    private String employeesBody;
    private String employeeBody;
    private Map<Object, Object> organizationMap;

    public OrganizationHierarchyStepDefs() {

        Given("we have set the following organization hierarchy:", (final DataTable dataTable) -> {
            final var organizationMap = dataTable.asLists(String.class).stream().skip(1)
                    .collect(toMap(row -> row.get(0), row -> row.get(1)));

            final var organizationBodyPublisher = HttpRequest.BodyPublishers.ofString(
                    new ObjectMapper().writeValueAsString(organizationMap));

            final var request = newBuilder(
                    create("http://localhost:" + applicationPort + ORGANIZATION_ENDPOINT))
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", BASIC_AUTHORIZATION_USER_PASSWORD_BASE64)
                    .POST(organizationBodyPublisher).build();

            assertThat(newHttpClient().send(request, ofString()).statusCode(), is(200));
        });

        When("^we check the organization hierarchy$", () -> {
            final var request = newBuilder(
                    create("http://localhost:" + applicationPort + ORGANIZATION_ENDPOINT))
                    .header("Authorization", BASIC_AUTHORIZATION_USER_PASSWORD_BASE64)
                    .GET().build();

            final var response = newHttpClient().send(request, ofString());
            assertThat(response.statusCode(), is(200));
            employeesBody = response.body();
        });

        Then("^organization hierarchy is:$", (String expected) -> {
            assertThat(employeesBody, is(expected.trim()));
        });

        When("^we try to add the following organization hierarchy:", (final DataTable dataTable) -> {
            organizationMap = dataTable.asLists(String.class).stream().skip(1)
                .collect(toMap(row -> row.get(0), row -> row.get(1)));
        });

        Then("^application rejects it with the following error message \"([^\"]*)\"$", (String errorMessage) -> {
            final var organizationBodyPublisher = HttpRequest.BodyPublishers.ofString(
                new ObjectMapper().writeValueAsString(organizationMap));

            final var request = newBuilder(
                create("http://localhost:" + applicationPort + ORGANIZATION_ENDPOINT))
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", BASIC_AUTHORIZATION_USER_PASSWORD_BASE64)
                .POST(organizationBodyPublisher).build();

            final var response = newHttpClient().send(request, ofString());
            assertThat(response.statusCode(), is(400));

            final var responseMap = new ObjectMapper().readValue(response.body(), Map.class);
            assertThat(responseMap.get("message"), is(errorMessage));
        });

        When("^we check the management chain for \"([^\"]*)\"$", (String employeeName) -> {
            final var request = newBuilder(
                create("http://localhost:" + applicationPort + ORGANIZATION_ENDPOINT + "/employee/" + employeeName + "/management"))
                .header("Authorization", BASIC_AUTHORIZATION_USER_PASSWORD_BASE64)
                .GET().build();

            final var response = newHttpClient().send(request, ofString());
            assertThat(response.statusCode(), is(200));
            employeeBody = response.body();
        });

        Then("^management chain is:$", (String expected) -> {
            assertThat(employeeBody, is(expected.trim()));
        });
    }
}
