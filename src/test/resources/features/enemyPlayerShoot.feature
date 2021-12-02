Feature: Enemy player shoot feature

  As the player
  I want the enemy player to shoot similar to me
  So that the game gets even more competitive


  Scenario: Enemy player shoots random linked caves with player
  Enemy player randomly shoots from 1 to 5 caves and an arrow kills the player
    Given player is in cave 9
    And enemy player is in cave 11
    When player moves to cave 1
    And enemy player shoots caves
      | 10 | 9 | 1 |
    Then player is dead
    And game is lost

  Scenario: Enemy player shoots wumpus
  Enemy player randomly shoots from 1 to 5 caves and an arrow kills the wumpus
  Player loses game
    Given player is in cave 9
    And enemy player is in cave 11
    And wumpus is in cave 18
    When player moves to cave 1
    And enemy player shoots caves
      | 10 | 18 |
    Then wumpus is dead
    And game is lost

  Scenario: Enemy player shoots itself
  Enemy player randomly shoots from 1 to 5 caves and the arrow kills it
    Given player is in cave 9
    And enemy player is in cave 11
    When player moves to cave 1
    And enemy player shoots caves
      | 10 | 11 |
    Then enemy player is dead

  Scenario: Enemy player is shot by player
  The player shoots the enemy player
    Given player is in cave 9
    And enemy player is in cave 11
    When player shoots an arrow at caves
      | 10 | 11 |
    Then enemy player is dead

# TODO: Enemy player shoot test list
#  DONE - Enemy kills player so player loses
#  DONE - Enemy kills the wumpus and player loses
#  DONE - Enemy kills itself
#  DONE - Player kills enemy player
#  Enemy runs out of arrows and can not move or shoot
#  Dead enemy can not move or shoot
#
#  Player moves -> enemy shoots -> wumpus wakes up and moves
#  Player shoots and wumpus moves to eat enemy player
#
