Feature: get payment resource

  Scenario: client issues a GET to /api/v1/payments/{paymentId} for non existing payment id
    Given payment with id "09a8fe0d-e239-4aff-8098-7923eadd0b98" does not exist
    When the client issues a GET "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" requesting "application/json"
    Then the client receives status code of 404
    And the response body is empty

  Scenario: client issues a GET to /api/v1/payments/{paymentId} for existing payment id
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a GET "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" requesting "application/json"
    Then the client receives status code of 200
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And the response body contains resource matching "payment.json"