Feature: create payment resource

  Scenario Outline: client issues a POST to /api/v1/payments
    When the client issues a POST to /api/v1/payments with payload <fixture>
    Then the client receives status code of 201
    And new resource location in Location header
    And the response body is empty
    And the interaction is documented as create-payment

    Examples:
      | fixture                           |
      | payment.json                      |
      | validation/no-sender-charges.json |
      | validation/no-forex.json          |


  Scenario Outline: client issues a POST to /api/v1/payments accepting json
    When the client issues a POST to /api/v1/payments accepting application/json with payload <fixture>
    Then the client receives status code of 201
    And response contains Content-Type header with value application/json;charset=UTF-8
    And new resource location in Location header
    And the response body contains resource matching <fixture>
    And the interaction is documented as create-payment-with-response

    Examples:
      | fixture                           |
      | payment.json                      |
      | validation/no-sender-charges.json |
      | validation/no-forex.json          |


  Scenario: client issues a POST to /api/v1/payments
    Given payment payment.json exists
    When the client issues a POST to /api/v1/payments with payload payment.json
    Then the client receives status code of 409
    And the response body contains bad request description
    And the interaction is documented as create-payment-conflict


  Scenario Outline: client issues a POST to /api/v1/payments with malformed request
    When the client issues a POST to /api/v1/payments accepting application/json with payload <fixture>
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains bad request description
    And the interaction is documented as create-payment-malformed-request

    Examples:
      | fixture                   |
      | validation/malformed.json |
      | validation/empty.json     |

  Scenario Outline: client issues a POST to /api/v1/payments with malformed request
    When the client issues a POST to /api/v1/payments accepting application/json with payload payment.json with field <field> having <invalid-value>
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains bad request description
    And the interaction is documented as create-payment-bad-request

    Examples:
      | field                                                     | invalid-value |
      | attributes.amount                                         | abc           |
      | attributes.currency                                       | foobar        |
      | attributes.processing_date                                | abc           |
      | attributes.processing_date                                | 20180101      |
      | attributes.charges_information.receiver_charges_currency  | aaabbb        |
      | attributes.charges_information.receiver_charges_currency  | aaabbb        |
      | attributes.fx.exchange_rate                               | aaa           |
      | attributes.fx.exchange_rate                               | -a            |
      | attributes.fx.original_amount                             | aaa           |
      | attributes.fx.original_currency                           | bbbccc        |
      | attributes.charges_information.sender_charges[0].amount   | bbbccc        |
      | attributes.charges_information.sender_charges[0].currency | bbbccc        |

  Scenario Outline: client issues a POST to /api/v1/payments with an invalid request
    When the client issues a POST to /api/v1/payments accepting application/json with payload <fixture>
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains validation error description
    And the interaction is documented as create-payment-bad-request

    Examples:
      | fixture                      |
      | validation/empty-object.json |


  Scenario Outline: client issues a POST to /api/v1/payments with an invalid request
    When the client issues a POST to /api/v1/payments accepting application/json with payload payment.json with field <field> having <invalid-value>
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains validation error description
    And the interaction is documented as create-payment-bad-request

    Examples:
      | field                                                     | invalid-value |
      | type                                                      | null          |
      | type                                                      | foo           |
      | version                                                   | null          |
      | organisation_id                                           | null          |
      | attributes                                                | null          |
      | attributes.amount                                         | null          |
      | attributes.amount                                         | 0             |
      | attributes.amount                                         | -42           |
      | attributes.currency                                       | null          |
      | attributes.charges_information                            | null          |
      | attributes.beneficiary_party                              | null          |
      | attributes.debtor_party                                   | null          |
      | attributes.sponsor_party                                  | null          |
      | attributes.end_to_end_reference                           | null          |
      | attributes.numeric_reference                              | null          |
      | attributes.payment_id                                     | null          |
      | attributes.payment_purpose                                | null          |
      | attributes.payment_scheme                                 | null          |
      | attributes.payment_type                                   | null          |
      | attributes.processing_date                                | null          |
      | attributes.reference                                      | null          |
      | attributes.scheme_payment_type                            | null          |
      | attributes.scheme_payment_sub_type                        | null          |
      | attributes.beneficiary_party.account_name                 | null          |
      | attributes.beneficiary_party.account_number               | null          |
      | attributes.beneficiary_party.account_number_code          | null          |
      | attributes.beneficiary_party.account_type                 | null          |
      | attributes.beneficiary_party.address                      | null          |
      | attributes.beneficiary_party.bank_id                      | null          |
      | attributes.beneficiary_party.bank_id_code                 | null          |
      | attributes.beneficiary_party.name                         | null          |
      | attributes.debtor_party.account_name                      | null          |
      | attributes.debtor_party.account_number                    | null          |
      | attributes.debtor_party.account_number_code               | null          |
      | attributes.debtor_party.address                           | null          |
      | attributes.debtor_party.bank_id                           | null          |
      | attributes.debtor_party.bank_id_code                      | null          |
      | attributes.debtor_party.name                              | null          |
      | attributes.sponsor_party.account_number                   | null          |
      | attributes.sponsor_party.bank_id                          | null          |
      | attributes.sponsor_party.bank_id_code                     | null          |
      | attributes.charges_information.bearer_code                | null          |
      | attributes.charges_information.receiver_charges_amount    | null          |
      | attributes.charges_information.receiver_charges_amount    | -1            |
      | attributes.charges_information.receiver_charges_currency  | null          |
      | attributes.fx.contract_reference                          | null          |
      | attributes.fx.exchange_rate                               | null          |
      | attributes.fx.exchange_rate                               | 0             |
      | attributes.fx.exchange_rate                               | -1            |
      | attributes.fx.original_amount                             | null          |
      | attributes.fx.original_amount                             | 0             |
      | attributes.fx.original_amount                             | -1            |
      | attributes.fx.original_currency                           | null          |
      | attributes.charges_information.sender_charges[0].amount   | null          |
      | attributes.charges_information.sender_charges[0].amount   | 0             |
      | attributes.charges_information.sender_charges[0].amount   | -1            |
      | attributes.charges_information.sender_charges[0].currency | null          |
