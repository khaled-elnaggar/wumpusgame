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

# TODO: Enemy player shoot test list
#  DONE - Enemy kills player so player loses
#  Enemy kills the wumpus and player loses
#  Enemy kills itself
#  Player kills enemy player
#  Enemy runs out of arrows and can not move or shoot
#  Dead enemy can not move or shoot
#
#  Player moves -> enemy shoots -> wumpus wakes up and moves
#  Player shoots and wumpus moves to eat enemy player
#
