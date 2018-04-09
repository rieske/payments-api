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
