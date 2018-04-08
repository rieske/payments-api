Feature: get payment resource

  Scenario: client issues a GET to /api/v1/payments/{payment-id} for non existing payment id
    Given payment with id "paymentId" does not exist
    When the client issues a GET "/api/v1/payments/paymentId" requesting "application/json"
    Then the client receives status code of 404

  Scenario: client issues a GET to /api/v1/payments/{payment-id} for existing payment id
    Given payment "/fixtures/transaction.json" exists with id "paymentId"
    When the client issues a GET "/api/v1/payments/paymentId" requesting "application/json"
    Then the client receives status code of 200
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And the response body contains resource matching "/fixtures/transaction-without-id.json"