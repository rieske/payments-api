Feature: get payment resource

  Scenario: client issues a GET to /api/v1/payments/{paymentId} for non existing payment id
    Given payment with id 09a8fe0d-e239-4aff-8098-7923eadd0b98 does not exist
    When the client issues a GET /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json
    Then the client receives status code of 404
    And the response body is empty

  Scenario: client issues a GET to /api/v1/payments/{paymentId} for existing payment id
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a GET /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains resource matching payment.json
    And the interaction is documented as get-payment

  Scenario: client issues a GET to /api/v1/payments/{paymentId} with invalid payment id
    When the client issues a GET /api/v1/payments/not-a-uuid accepting application/json
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.message | Invalid UUID string: not-a-uuid |
    And the interaction is documented as get-payment-invalid-id