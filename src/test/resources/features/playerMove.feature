Feature: Player movement options

  As the player
  I want to be able to move across the map
  So that I can hunt the wumpus and adventure

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
    Then player is dead
    And game is lost

  Scenario: Player moves to cave with bat
  The player should change location when he enters a cave with bat, the bat will also change location
    Given player is in cave 11
    And bat 1 is in cave 19
    When player moves to caves
      | 12 | 19 |
    Then bat will teleport player to cave 8 and itself to cave 4
    Then player will be at cave 8
    And bat 1 will be at cave 4

  Scenario: Player moves to cave with pit
  If the player enters a cave with pit, the player should die and the game should be over
    Given player is in cave 11
    And pit 1 is in cave 3
    When player moves to caves
      | 2 | 3 |
    Then player is dead
    And game is lost

  Scenario: Player moves to cave with pit and bat
  If the player enters a cave with pit and bat, the player should die and the game should be over
    Given player is in cave 11
    And bat 1 is in cave 19
    And pit 1 is in cave 19
    When player moves to caves
      | 12 | 19 |
    Then player is dead
    And game is lost