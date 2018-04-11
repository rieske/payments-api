Feature: create payment resource

  Scenario: client issues a POST to /api/v1/payments
    When the client issues a POST to "/api/v1/payments" with payload "payment.json"
    Then the client receives status code of 201
    And new resource location in Location header
    And the response body is empty
    And the interaction is documented as "create-payment"

  Scenario: client issues a POST to /api/v1/payments requesting json
    When the client issues a POST to "/api/v1/payments" with payload "payment.json" requesting "application/json"
    Then the client receives status code of 201
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And new resource location in Location header
    And the response body contains resource matching "payment.json"
    And the interaction is documented as "create-payment-with-response"

  Scenario Outline: client issues a POST to /api/v1/payments with malformed request
    When the client issues a POST to "/api/v1/payments" with payload <fixture> requesting "application/json"
    Then the client receives status code of 400
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And the response body contains bad request description
    And the interaction is documented as "create-payment-bad-request"

    Examples:
      | fixture                              |
      | "validation/malformed.json"          |
      | "validation/empty.json"              |
      | "validation/non-numeric-amount.json" |

  Scenario Outline: client issues a POST to /api/v1/payments with an invalid request
    When the client issues a POST to "/api/v1/payments" with payload <fixture> requesting "application/json"
    Then the client receives status code of 400
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And the response body contains validation error description
    And the interaction is documented as "create-payment-bad-request"

    Examples:
      | fixture                                   |
      | "validation/empty-object.json"            |
      | "validation/missing-amount.json"          |
      | "validation/zero-amount.json"             |
      | "validation/negative-amount.json"         |
      | "validation/missing-version.json"         |
      | "validation/missing-organisation-id.json" |
      | "validation/missing-attributes.json"      |
