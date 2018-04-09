Feature: delete payment resource

  Scenario: client issues a DELETE to /api/v1/payments/{payment-id} for non existing payment id
    Given payment with id "paymentId" does not exist
    When the client issues a DELETE "/api/v1/payments/paymentId"
    Then the client receives status code of 404
    And the response body is empty

  Scenario: client issues a GET to /api/v1/payments/{payment-id} for existing payment id
    Given payment "payment.json" exists with id "paymentId"
    When the client issues a DELETE "/api/v1/payments/paymentId"
    Then the client receives status code of 204
    And the response body is empty
    And payment with id "paymentId" does not exist