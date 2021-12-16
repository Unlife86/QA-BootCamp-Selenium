Feature: Cart operation

  Scenario: Add product
    Given Open index page
    When Open random product page
    And Add product to the cart
    Then The number of products in the basket is '1'
    When Open index page
    And Open random product page
    And Add product to the cart
    Then The number of products in the basket is '2'
    When Open index page
    And Open random product page
    And Add product to the cart
    Then The number of products in the basket is '3'

  Scenario: Delete product form cart
    Given Open index page
    When Open cart page
    Then Product count is '3'
    When Delete one the product from the cart
    Then Table Located And Visible
    When Delete one the product from the cart
    Then Table Located And Visible
    When Delete one the product from the cart
    Then Table Is Staleness Of

