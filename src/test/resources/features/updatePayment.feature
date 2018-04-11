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
    And the interaction is documented as "update-payment"

  Scenario: client issues a PATCH to /api/v1/payments/{paymentId} for existing payment id
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a PATCH "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" with body "payment.json" requesting "application/json"
    Then the client receives status code of 200
    And the response body contains resource matching "payment.json"
    And the interaction is documented as "update-payment-with-response"

  Scenario Outline: client issues a PATCH to /api/v1/payments/{paymentId} with partial update request
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a PATCH "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" with body <fixture> requesting "application/json"
    Then the client receives status code of 200
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And the response body contains resource matching "payment.json"

    Examples:
      | fixture                                       |
      | "validation/empty-object.json"                |
      | "validation/missing-amount.json"              |
      | "validation/missing-version.json"             |
      | "validation/missing-organisation-id.json"     |
      | "validation/missing-attributes.json"          |
      | "validation/missing-currency.json"            |
      | "validation/missing-beneficiary.json"         |
      | "validation/missing-charges-information.json" |
      | "validation/missing-fx.json"                  |
      | "validation/missing-debtor.json"              |
      | "validation/missing-sponsor.json"             |

  Scenario Outline: client issues a PATCH to /api/v1/payments/{paymentId} with malformed request
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a PATCH "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" with body <fixture> requesting "application/json"
    Then the client receives status code of 400
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And the response body contains bad request description
    And the interaction is documented as "update-payment-bad-request"

    Examples:
      | fixture                              |
      | "validation/malformed.json"          |
      | "validation/empty.json"              |
      | "validation/non-numeric-amount.json" |
      | "validation/invalid-currency.json"   |


  Scenario Outline: client issues a PATCH to /api/v1/payments/{paymentId} with an invalid request
    Given payment "payment.json" exists with id "09a8fe0d-e239-4aff-8098-7923eadd0b98"
    When the client issues a PATCH "/api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98" with body <fixture> requesting "application/json"
    Then the client receives status code of 400
    And response contains "Content-Type" header with value "application/json;charset=UTF-8"
    And the response body contains validation error description
    And the interaction is documented as "update-payment-bad-request"

    Examples:
      | fixture                           |
      | "validation/zero-amount.json"     |
      | "validation/negative-amount.json" |


