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


  Scenario: Player moves to a cave with wumpus and gets eaten
  If the player enters a cave with wumpus inside it, the player should die and the game should be over
    Given player is in cave 9
    And wumpus is in cave 18
    When player moves to cave 10
    When player moves to cave 18
    Then game is over
