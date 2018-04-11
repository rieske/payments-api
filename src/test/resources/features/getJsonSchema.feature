Feature: get json schema

  Scenario: client issues a GET to /api/v1/profile/payments requesting json schema
    When the client issues a GET /api/v1/profile/payments requesting application/schema+json
    Then the client receives status code of 200
    And response contains Content-Type header with value application/schema+json;charset=UTF-8
    And the interaction is documented as payment-schema
