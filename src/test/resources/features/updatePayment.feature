Feature: update payment resource

  Scenario: client issues a PATCH to /api/v1/payments/{paymentId} for non existing payment id
    Given payment with id "09a8fe0d-e239-4aff-8098-7923eadd0b98" does not exist
    When the client issues a PATCH "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" with body "payment.json"
    Then the client receives status code of 404
    And the response body is empty


  Scenario: client issues a PATCH to /api/v1/payments/{paymentId} for existing payment id
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a PATCH "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" with body "payment.json"
    Then the client receives status code of 204
    And the response body is empty

  Scenario: client issues a PATCH to /api/v1/payments/{paymentId} for existing payment id
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a PATCH "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" with body "payment.json" requesting "application/json"
    Then the client receives status code of 200
    And the response body contains resource matching "payment.json"