Feature: Enemy player shoot feature

  As the player
  I want the enemy player to shoot similar to me
  So that the game gets even more competitive

  Background:
    Given player is in cave 0
    And enemy player is in cave 16
    And wumpus is in cave 18
    And bat 1 is in cave 19
    And bat 2 is in cave 13
    And pit 1 is in cave 3
    And pit 2 is in cave 13

  Scenario: Enemy player shoots random linked caves with player
  Enemy player randomly shoots from 1 to 5 caves and an arrow kills the player
    Given player is in cave 9
    And enemy player is in cave 11
    When player moves to cave 1
    And from cave 11, enemy player shoots caves
      | 10 | 9 | 1 |
    Then player is dead
    And game is lost

  Scenario: Enemy player shoots wumpus
  Enemy player randomly shoots from 1 up to 5 caves and an arrow kills the wumpus
  Player loses game
    Given player is in cave 9
    And enemy player is in cave 11
    And wumpus is in cave 18
    When player moves to cave 1
    And from cave 11, enemy player shoots caves
      | 10 | 18 |
    Then wumpus is dead
    And game is lost

  Scenario: Enemy player shoots itself
  Enemy player randomly shoots from 1 to 5 caves and the arrow kills it
    Given player is in cave 9
    And enemy player is in cave 11
    When player moves to cave 1
    And from cave 11, enemy player shoots caves
      | 10 | 11 |
    Then enemy player will be dead

  Scenario: Enemy player is shot by player
  The player shoots the enemy player
  Enemy player can not shoot or move when dead
    Given player is in cave 9
    And enemy player is in cave 11
    When player shoots an arrow at caves
      | 10 | 11 |
    Then enemy player will be dead
    When player moves to cave 1
    Then enemy player will be at cave 11
    And enemy player will have 5 arrows

  Scenario: Enemy player runs out of arrows
  Enemy player can not shoot or move when out of arrows
    Given player is in cave 9
    And enemy player is in cave 11
    And enemy player uses all arrows
    When player moves to cave 1
    Then enemy player will be at cave 11
    And enemy player will have 0 arrows

  Scenario: Enemy player eaten by wumpus
  Wumpus moves and eats the enemy player
    Given player is in cave 9
    And enemy player is in cave 11
    And wumpus is in cave 18
    When player moves to cave 1
    And enemy player moves from cave 11 to cave 10
    Then player moves to cave 9
    And from cave 10, enemy player shoots caves
      | 11 | 12 |
    Then wumpus will wake up and move from cave 18 to cave 10
    And enemy player will be dead

  Scenario: Random gameplay scenario
  To test the coupling of my steps I write an arbitrary number of steps and check outcomes at the end
    Given player is in cave 11
    And enemy player is in cave 11
    And wumpus is in cave 18

    When player moves to cave 12
    And enemy player moves from cave 11 to cave 10

    Then player moves to cave 19
    And bat will teleport player to cave 15 and itself to cave 8
    And from cave 10, enemy player shoots caves
      | 9 | 8 | 17 |
    And wumpus will wake up and move from cave 18 to cave 19

    When player shoots an arrow at caves
      | 14 | 13 |
    And wumpus will wake up and move from cave 19 to cave 18
    And from cave 10, enemy player shoots caves
      | 11 | 12 | 13 | 14 |

# TODO: Enemy player shoot test list
#  DONE - Enemy kills player so player loses
#  DONE - Enemy kills the wumpus and player loses
#  DONE - Enemy kills itself
#  DONE - Player kills enemy player
#  DONE - Enemy runs out of arrows and can not move or shoot
#  DONE - Dead enemy can not move or shoot
#
#  DONE - Player moves -> enemy shoots -> wumpus wakes up and moves
#
