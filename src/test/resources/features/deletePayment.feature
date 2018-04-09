Feature: delete payment resource

  Scenario: client issues a DELETE to /api/v1/payments/{paymentId} for non existing payment id
    Given payment with id "09a8fe0d-e239-4aff-8098-7923eadd0b98" does not exist
    When the client issues a DELETE "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98"
    Then the client receives status code of 404
    And the response body is empty

  Scenario: client issues a GET to /api/v1/payments/{paymentId} for existing payment id
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a DELETE "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98"
    Then the client receives status code of 204
    And the response body is empty
    And payment with id "09a8fe0d-e239-4aff-8098-7923eadd0b98" does not exist
    And the interaction is documented as "delete-payment"
