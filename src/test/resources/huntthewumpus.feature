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

  Scenario: Player shoots the wumpus
  If the player shoots a cave that contains the wumpus, the wumpus should die and game should be over
    Given player is in cave 9
    And wumpus is in cave 18
    When player moves to cave 10
    And player shoots at cave 18
    Then game is over

  Scenario: Player misses the wumpus and it remains asleep
  If the player shoots misses the wumpus cave, the wumpus may remain a sleep and not move
    Given player is in cave 0
    And wumpus is in cave 18
    And wumpus will remain asleep
    When player shoots at cave 1
    Then wumpus will be at cave 18
    And game is still on

  Scenario Outline: Player misses the wumpus and it wakes up and moves
  If the player shoots but misses the wumpus cave, the wumpus may wake up and move to a linked cave
  If player is in that cave, the wumpus will eat him
    Given player is in cave <PlayerStartingCave>
    And wumpus is in cave <WumpusStartingCave>
    But wumpus will wake up and move to cave <WumpusNextCave>
    When player moves to cave <PlayerNextCave>
    And player shoots at cave <CaveToShootAt>
    Then wumpus will be at cave <WumpusNextCave>
    And game is <GameStatus>

    Examples:
      | PlayerStartingCave | WumpusStartingCave | WumpusNextCave | PlayerNextCave | CaveToShootAt | GameStatus |
      | 9                  | 18                 | 19             | 10             | 11            | still on   |
      | 9                  | 18                 | 10             | 10             | 11            | over       |

  Scenario: Player runs out of arrows
  If the player runs out of arrows before killing the wumpus, the game is over
    Given player is in cave 0
    And wumpus is in cave 18
    And wumpus will remain asleep
    And player used all arrows but 1
    When player shoots at cave 4
    Then game is over

  Scenario: Player shoots an arrow through multiple caves
  The player can choose up to 5 caves to shoot at
    Given player is in cave 0
    And wumpus is in cave 18
    When player shoots an arrow at caves
      | 7 | 8 | 9 | 10 | 18 |
    Then game is over

  Scenario: Player shoots an arrow through multiple caves but some are not linked
  The player can choose up to 5 caves to shoot at
  If any of them is not linked, the arrow goes to a random linked cave
    Given player is in cave 0
    And wumpus is in cave 18
    But cave 15 is not linked to 0, so arrow will go to 7 instead
    And cave 3 is not linked to 10, so arrow will go to 18 instead
    When player shoots an arrow at caves
      | 15 | 8 | 9 | 10 | 3 |
    Then game is over
