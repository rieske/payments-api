Feature: get all payment resources

  Scenario: client issues a GET to /api/v1/payments given no payments exist
    Given no payments exist
    When the client issues a GET /api/v1/payments accepting application/json
    Then the client receives status code of 200
    And the response body contains an empty list of payments
    And the interaction is documented as get-all-payments-empty

  Scenario: client issues a GET to /api/v1/payments a single payment list exists
    Given payment payment.json exists
    When the client issues a GET /api/v1/payments accepting application/json
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json
    And the interaction is documented as get-all-payments

  Scenario: client issues a GET to /api/v1/payments given payments list exists
    Given payments payments-list.json exist
    When the client issues a GET /api/v1/payments accepting application/json
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json
    And the response body contains a list of payments matching payments-list.json