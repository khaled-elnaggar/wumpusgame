Feature: Player movement options

  Scenario Outline: Player moves to a linked cave
  The player should be allowed to move successfully to a cave that is connected to it
    Given player is in cave <PlayerStartingCave>
    When player moves to cave <PlayerNextCave>
    Then player will be at cave <PlayerCurrentCave>
    Examples:
      | PlayerStartingCave | PlayerNextCave | PlayerCurrentCave |
      | 0                  | 7              | 7                 |
      | 0                  | 12             | 0                 |


  Scenario: Player moves to a cave with wumpus and gets eaten
  If the player enters a cave with wumpus inside it, the player should die and the game should be over
    Given player is in cave 9
    And wumpus is in cave 18
    When player moves to caves
      | 10 | 18 |
    Then game is over

  Scenario: Player senses the wumpus in a linked cave
  The player should sense the wumpus when in a linked cave to his current
    Given player is in cave 9
    And wumpus is in cave 18
    When player moves to cave 10
    Then player senses that "there's an awful smell"

  Scenario: Player moves to cave with bat
  The player should change location when he enters a cave with bat, the bat will also change location
    Given player is in cave 11
    And a bat is in cave 19
    And bat will teleport player to cave 8 and itself to cave 4
    When player moves to caves
      | 12 | 19 |
    Then player will be at cave 8
    And a bat will be at cave 4

  Scenario: Player moves to cave with pit
  If the player enters a cave with pit, the player should die and the game should be over
    Given player is in cave 11
    And pit is in cave 3
    When player moves to caves
      | 2 | 3 |
    Then game is over

  Scenario: Player moves to cave with pit and bat
  If the player enters a cave with pit and bat, the player should die and the game should be over
    Given player is in cave 11
    And a bat is in cave 19
    And pit is in cave 19
    When player moves to caves
      | 12 | 19 |
    Then game is over