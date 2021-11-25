Feature: Hunt the Wumpus

  Scenario: Player moves to nearby cave
    Given player is in cave 0
    When player moves to cave 7
    Then player will be at cave 7


