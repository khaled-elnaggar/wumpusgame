Feature: Enemy player features

  Scenario: Enemy player moves to a linked cave
  Everytime when the player takes an action either move or shoot, the enemy player has 50% to move
    Given player is in cave 9
    And enemy player is in cave 16
    But enemy player will wake up and move to cave 6
    When player moves to cave 1
    Then enemy player will be at cave 6
