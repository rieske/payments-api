Feature: update payment resource

  Scenario: client issues a PATCH to /api/v1/payments/{paymentId} for non existing payment id
    Given payment with id 09a8fe0d-e239-4aff-8098-7923eadd0b98 does not exist
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 with body payment.json
    Then the client receives status code of 404
    And the response body is empty

  Scenario: client issues a PATCH to /api/v1/payments/{paymentId} for existing payment id
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 with body payment.json
    Then the client receives status code of 204
    And the response body is empty
    And the interaction is documented as update-payment

  Scenario: client issues a PATCH to /api/v1/payments/{paymentId} for existing payment id
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body payment.json
    Then the client receives status code of 200
    And the response body contains resource matching payment.json
    And the interaction is documented as update-payment-with-response

  Scenario Outline: client issues a PATCH to /api/v1/payments/{paymentId} with partial update request
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body <fixture>
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains resource matching payment.json

    Examples:
      | fixture                        |
      | validation/empty-object.json   |
      | validation/sender-charges.json |
      | validation/forex.json          |

  Scenario Outline: client issues a PUT to /api/v1/payments/{paymentId} with partial update request
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH to /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with payload payment.json with field <field> having <value>
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains resource matching payment.json

    Examples:
      | field                                                     | value |
      | type                                                      | null  |
      | version                                                   | null  |
      | organisation_id                                           | null  |
      | attributes                                                | null  |
      | attributes.amount                                         | null  |
      | attributes.currency                                       | null  |
      | attributes.charges_information                            | null  |
      | attributes.beneficiary_party                              | null  |
      | attributes.debtor_party                                   | null  |
      | attributes.sponsor_party                                  | null  |
      | attributes.end_to_end_reference                           | null  |
      | attributes.numeric_reference                              | null  |
      | attributes.payment_id                                     | null  |
      | attributes.payment_purpose                                | null  |
      | attributes.payment_scheme                                 | null  |
      | attributes.payment_type                                   | null  |
      | attributes.processing_date                                | null  |
      | attributes.reference                                      | null  |
      | attributes.scheme_payment_type                            | null  |
      | attributes.scheme_payment_sub_type                        | null  |
      | attributes.beneficiary_party.account_name                 | null  |
      | attributes.beneficiary_party.account_number               | null  |
      | attributes.beneficiary_party.account_number_code          | null  |
      | attributes.beneficiary_party.account_type                 | null  |
      | attributes.beneficiary_party.address                      | null  |
      | attributes.beneficiary_party.bank_id                      | null  |
      | attributes.beneficiary_party.bank_id_code                 | null  |
      | attributes.beneficiary_party.name                         | null  |
      | attributes.debtor_party.account_name                      | null  |
      | attributes.debtor_party.account_number                    | null  |
      | attributes.debtor_party.account_number_code               | null  |
      | attributes.debtor_party.address                           | null  |
      | attributes.debtor_party.bank_id                           | null  |
      | attributes.debtor_party.bank_id_code                      | null  |
      | attributes.debtor_party.name                              | null  |
      | attributes.sponsor_party.account_number                   | null  |
      | attributes.sponsor_party.bank_id                          | null  |
      | attributes.sponsor_party.bank_id_code                     | null  |
      | attributes.charges_information.bearer_code                | null  |
      | attributes.charges_information.receiver_charges_amount    | null  |
      | attributes.charges_information.receiver_charges_currency  | null  |
      | attributes.fx.contract_reference                          | null  |
      | attributes.fx.exchange_rate                               | null  |
      | attributes.fx.original_amount                             | null  |
      | attributes.fx.original_currency                           | null  |
      | attributes.charges_information.sender_charges[0].amount   | null  |
      | attributes.charges_information.sender_charges[0].currency | null  |


  Scenario Outline: client issues a PATCH to /api/v1/payments/{paymentId} with an invalid request
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH to /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with payload payment.json with field <field> having <invalid-value>
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains validation error description
    And the interaction is documented as update-payment-bad-request

    Examples:
      | field                                                     | invalid-value |
      | type                                                      | foo           |
      | attributes.amount                                         | 0             |
      | attributes.amount                                         | -1            |
      | attributes.amount                                         | 0             |
      | attributes.amount                                         | -42           |
      | attributes.charges_information.receiver_charges_amount    | -1            |
      | attributes.fx.exchange_rate                               | 0             |
      | attributes.fx.exchange_rate                               | -1            |
      | attributes.fx.original_amount                             | 0             |
      | attributes.fx.original_amount                             | -1            |
      | attributes.charges_information.sender_charges[0].amount   | 0             |
      | attributes.charges_information.sender_charges[0].amount   | -1            |


  Scenario Outline: client issues a PATCH to /api/v1/payments/{paymentId} with malformed request
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH to /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with payload payment.json with field <field> having <invalid-value>
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains bad request description
    And the interaction is documented as update-payment-malformed-request

    Examples:
      | field               | invalid-value |
      | attributes.amount   | abc           |
      | attributes.currency | foobar        |


  Scenario Outline: client issues a PATCH to /api/v1/payments/{paymentId} with malformed request
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body <fixture>
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains bad request description

    Examples:
      | fixture                   |
      | validation/empty.json     |
      | validation/malformed.json |

  Scenario: client adds payment charges to a payment
    Given payment validation/no-sender-charges.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body:
    """
      {
        "attributes": {
          "charges_information": {
            "sender_charges": [
              {
                "amount": "5.00",
                "currency": "GBP"
              },
              {
                "amount": "10.00",
                "currency": "USD"
              }
            ]
          }
        }
      }
    """
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.attributes.charges_information.sender_charges.length()    | 2     |
      | $.attributes.charges_information.sender_charges[0].amount   | 5.00  |
      | $.attributes.charges_information.sender_charges[0].currency | GBP   |
      | $.attributes.charges_information.sender_charges[1].amount   | 10.00 |
      | $.attributes.charges_information.sender_charges[1].currency | USD   |


  Scenario: client removes payment charges from a payment
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body:
    """
      {
        "attributes": {
          "charges_information": {
            "sender_charges": []
          }
        }
      }
    """
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.attributes.charges_information.sender_charges.length() | 0 |


  Scenario: client adds forex to a payment
    Given payment validation/no-forex.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body:
    """
      {
        "attributes": {
          "fx": {
            "contract_reference": "FX12345",
            "exchange_rate": "2.00000",
            "original_amount": "200.42",
            "original_currency": "EUR"
          }
        }
      }
    """
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.attributes.fx.contract_reference | FX12345 |
      | $.attributes.fx.exchange_rate      | 2.00000 |
      | $.attributes.fx.original_amount    | 200.42  |
      | $.attributes.fx.original_currency  | EUR     |


  Scenario: client deletes forex from a payment
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body:
    """
      {
        "attributes": {
          "fx": null
        }
      }
    """
    Then the client receives status code of 200
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.attributes.fx | null |

