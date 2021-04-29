Feature: Infrastructure-related scenarios

  Scenario: Users without credentials can not access the system
    When we call the application with wrong credentials
    Then we receive an unauthenticated error code