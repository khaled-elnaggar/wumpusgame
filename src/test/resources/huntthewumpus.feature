Feature: Hunt the Wumpus

  Scenario: Player moves to a linked cave
  The player should be allowed to move successfully to a cave that is connected to it
    Given player is in cave 0
    When player moves to cave 7
    Then player will be at cave 7

  Scenario: Player moves to a non-linked cave
  The player should not be able to move to cave that is not linked to his current cave
    Given player is in cave 0
    When player moves to cave 12
    Then player will be at cave 0


