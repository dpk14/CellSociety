# cellsociety

Put any written documents related to your project here, including lab discussions.

# RPS Exercise

Completed by Michael Zhang, Jorge Radd, Daniel Kingsbury

## Design Description

#### Game
- Sets up the weapons and game mechanics
- Makes the weapons known to the Main class
- Weapons are compared head-to-head
- Decides upon the winning weapon
- Awards the winning player with points

#### General
-behaves as a backbone that initiates other classes and calls methods within them
- Creates instance of Game class, gets from it all the weapons
- Creates instances of players from Player class
- Gets guesses from each player
- Calls method within Game to determine which guess wins
- Tells player which gets a score increase

#### Weapon
- Each weapon keeps track of its strengths and its name
- An instance of each weapon in which players can choose from

#### Player (abstract)
- Can have different types of players who play differently
- Abstract class so that other player subclasses could extend from it


# Planning session Saturday Jan 26

## Classes

1) Simulation (abstract class)
  - SpreadingFireSimulation subclass
  - SegregationSimulation subclass
  - WaTorSimulation subclass

2) Cell (abstract class)
  - Empty
  - Burning
  - Tree
  - Agent
  - Fish
  - Shark

## Class characteristics

- Cell
  - row index, column index
  - number of steps
  - static probability

- Simulation
  - swap two objects in the grid
  - replace one object with another
  - get neighbours
  - dealing with movement (overridden)
  -





