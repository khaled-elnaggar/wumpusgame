Feature: Enemy player move feature

  As the player
  I want the enemy player to move similar to me
  So that the game gets more competitive

  Background:
    Given player is in cave 0
    And enemy player is in cave 16
    And wumpus is in cave 18
    And bat 1 is in cave 19
    And bat 2 is in cave 13
    And pit 1 is in cave 3
    And pit 2 is in cave 13

  Scenario: Enemy player moves to a linked cave
  Every time the player takes an action either move or shoot, the enemy player has 50% to move
    Given player is in cave 9
    And enemy player is in cave 16
    When player moves to cave 1
    Then enemy player will wake up and move to cave 6
    And enemy player will be at cave 6

  Scenario: Enemy player moves to a linked cave
  Every time the player takes an action either move or shoot, the enemy player has 50% to move
    Given player is in cave 9
    And enemy player is in cave 16
    When player shoots at cave 1
    Then enemy player will wake up and move to cave 6
    And enemy player will be at cave 6

  Scenario: Enemy player moves to a cave with wumpus
  Every time the player takes an action either move or shoot, the enemy player has 50% to move
  Enemy player enters a cave with wumpus and dies
    Given player is in cave 9
    And enemy player is in cave 16
    And wumpus is in cave 18
    When player moves to caves
      | 1 | 2 |
    Then enemy player will wake up and move to caves
      | 17 | 18 |
    And enemy player is dead

  Scenario: Enemy player moves to a cave with bat
  Every time the player takes an action either move or shoot, the enemy player has 50% to move
  Enemy player enters a cave with bat and teleports to another cave
    Given player is in cave 9
    And enemy player is in cave 11
    And bat 1 is in cave 19
    When player moves to caves
      | 1 | 2 |
    Then enemy player will wake up and move to caves
      | 12 | 19 |
    Then bat will teleport player to cave 8 and itself to cave 4
    And enemy player will be at cave 8
    And bat 1 will be at cave 4

  Scenario: Enemy player moves to a cave with pit
  Every time the player takes an action either move or shoot, the enemy player has 50% to move
  Enemy player enters a cave with pit and dies
    Given player is in cave 9
    And enemy player is in cave 11
    And pit 1 is in cave 3
    When player moves to caves
      | 1 | 2 |
    Then enemy player will wake up and move to caves
      | 2 | 3 |
    Then enemy player is dead


# TODO: Enemy player move test list
#  DONE - Player shoots and enemy moves
#  DONE - Enemy moves to a random linked cave
#  DONE - Enemy moves to a random cave and gets eaten by the wumpus
#  DONE - Enemy moves to a random cave and dies inside a pit
#  DONE - Enemy moves to a random cave and bat teleports it


