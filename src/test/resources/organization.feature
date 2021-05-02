Feature: Business-related scenarios covering the must-have elements of the system.

  Scenario: Organization hierarchy should be correctly configured
    Given we have set the following organization hierarchy:
      | employee        | supervisor       |
      | Sniper          | Liquid Snake     |
      | Liquid Snake    | Big Boss         |
      | Big Boss        | Coronel          |
    When we check the organization hierarchy
    Then organization hierarchy is:
    """
      {"Coronel":{"Big Boss":{"Liquid Snake":{"Sniper":{}}}}}
    """

  Scenario: Application should return the management chain for an employee
    When we check the management chain for "Sniper"
    Then management chain is:
    """
      {"Sniper":{"Liquid Snake":{"Big Boss":{"Coronel":{}}}}}
    """

  Scenario: Organization hierarchy should prevent multiple roots from being added
    When we try to add the following organization hierarchy:
      | employee        | supervisor   |
      | Solid Snake     | Otacon       |
    Then application rejects it with the following error message "ERROR: Multiple roots detected: [Coronel, Otacon]"

  Scenario: Organization hierarchy should prevent cyclic dependencies
    When we try to add the following organization hierarchy:
      | employee     | supervisor           |
      | Big Boss     | Liquid Snake     |
    Then application rejects it with the following error message "ERROR: Cyclic dependency detected for employee [Liquid Snake]"
