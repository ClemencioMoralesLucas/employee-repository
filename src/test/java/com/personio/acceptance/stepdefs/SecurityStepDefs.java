package com.personio.acceptance.stepdefs;

import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpRequest;

import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Testcontainers
@Slf4j
public class SecurityStepDefs implements En {

    private String applicationPort = "8080";
    private int statusCode;

    public SecurityStepDefs() {

        When("^we call the application with wrong credentials$", () -> {
            final var request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + applicationPort + "/api/v1/organization"))
                .header("Authorization", "Basic xXxXxXxXxXxXxXxXxX==")
                .GET().build();

            statusCode = newHttpClient().send(request, ofString()).statusCode();
        });

        Then("^we receive an unauthenticated error code$", () -> {
            assertThat(statusCode, is(401));
        });
    }
}
