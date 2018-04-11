Feature: payment business validation rules

  Scenario: can not create a payment with forex currency the same as payment currency
    When the client issues a POST to /api/v1/payments accepting application/json with payload validation/payment-with-forex-same-as-currency.json
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.message | Forex currency can not be the same as payment currency |



  Scenario: can not amend forex section to have same currency as payment currency
    Given payment payment.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body:
    """
      {
        "attributes": {
          "fx": {
            "contract_reference": "FX12345",
            "exchange_rate": "2.00000",
            "original_amount": "200.42",
            "original_currency": "GBP"
          }
        }
      }
    """
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.message | Forex currency can not be the same as payment currency |


  Scenario: can not add forex section with same currency as payment's currency
    Given payment validation/no-forex.json exists with id 09a8fe0d-e239-4aff-8098-7923eadd0b98
    When the client issues a PATCH /api/v1/payments/09a8fe0d-e239-4aff-8098-7923eadd0b98 accepting application/json with body:
    """
      {
        "attributes": {
          "fx": {
            "contract_reference": "FX12345",
            "exchange_rate": "2.00000",
            "original_amount": "200.42",
            "original_currency": "GBP"
          }
        }
      }
    """
    Then the client receives status code of 400
    And response contains Content-Type header with value application/json;charset=UTF-8
    And the response body contains jsonpaths matching
      | $.message | Forex currency can not be the same as payment currency |
