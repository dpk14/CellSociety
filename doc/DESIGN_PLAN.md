## Introduction

The primary design goal for this project is to create abstractions of
the subsystems that could be easily manipulated to make the entire
program as modular as possible.

We plan to have the simulation be closed, so that the algorithm for
each simulation is contained in one place. Each cell will keep its
own grid position to take the work away from the simulation.

## Overview

![alt text](https://coursework.cs.duke.edu/compsci308_2019spring/cellsociety_team14/blob/master/doc/class_layout.png "class layout text")


## User interface

![alt text](https://coursework.cs.duke.edu/compsci308_2019spring/cellsociety_team14/blob/master/doc/user_interface_plan.jpeg "Plan text")

## Design details

#### Use cases
1) Apply rules to middle cell
- in `Simulation` class, generate a list of cells and their locations based on the grid
- in `Simulation` class, find neighbours of the middle cell
- in `Simulation` class and in the list of cells, call method that swaps current cell for a empty cell
- render the list of cells on the grid

2) Apply rules to edge cell
- the only difference is in the method that finds neighbours
- bound-checks must be done

3) Move to next generation
- in `Simulation` class, iterate through the list of cells and render the grid based on these cells' locations
- pass this grid to the main class
- main class will render the visualization by adding to `root` node

4) Set simulation parameter
- user will change parameter in the UI
- this will call an update method in the `Simulation` class that will update this parameter

5) Switch simulations
- clear all cells in grid
- initialize new simulation

## Design considerations

## Team responsibilities
