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

The main class contains all code regarding scene setup, with event methods that take user inputs (like dials, etc) and convert them to constructor parameters for a new simulation. Contains a method that initializes the instance variable myGrid. Contains a step function, which will call a method in the simulation class that updates the Grid according to the rules of the simulation type, and returns the grid as myGrid. Lastly, has a method that iterates through myGrid and adds the new objects in the grid to the scene root.

The simulation class is a superclass with a subclass for each simulation type. Has a main public method, accessed by the main class, with a grid parameter. This method iterates through the grid, asking for the cell type at each index, then performing necessary methods specific to the function of that cell.  Then this cell’s variables are updated and added to an ArrayList “changes.” Once one iteration has been made, a new grid is initialized and built from the changes ArrayList. This grid is returned, and the main class reads it and converts each cell object inside to an imageview with a position corresponding to a location on the grid, and adds this child to the root to be displayed. The simulation class contains all methods that will be used while iterating through the grid to handle cell interactions and additions on the grid.  

There will be a mainpackage.Cell abstract super-class as well as several sub-classes (such as fire, tree, empty cells for Spreading Fire, an agent cell for Segregation, and fish and shark cells for Wa-Tor World) corresponding to the different simulations to be run. The mainpackage.Cell super-class may contain several instance variables (such as x and y coordinates within the grid) and methods that will be applicable to every cell type, but the sub-classes will likely need to have additional instance variables and several overridden methods specific to the simulations they are a part of. We also never have to worry about a method being called on a cell from a different sub-class because each simulation works with only cells specific to it.

See the User Cases section below for more specific insight.


#### Use cases
1) Apply rules to middle cell
- in `mainpackage.Simulation` class, generate a list of cells and their locations based on the grid
- in `mainpackage.Simulation` class, find neighbours of the middle cell
- in `mainpackage.Simulation` class and in the list of cells, call method that swaps current cell for a empty cell
- render the list of cells on the grid

2) Apply rules to edge cell
- the only difference is in the method that finds neighbours
- bound-checks must be done

3) Move to next generation
- in `mainpackage.Simulation` class, iterate through the list of cells and render the grid based on these cells' locations
- pass this grid to the main class
- main class will render the visualization by adding to `root` node

4) Set simulation parameter
- user will change parameter in the UI
- this will call an update method in the `mainpackage.Simulation` class that will update this parameter

5) Switch simulations
- clear all cells in grid
- initialize new simulation

## Design considerations

One issue critical to correctly updating the grid of cells is having a way of making sure updates to cells are based entirely on the positions in the previous generation and that an update to a cell does not affect another cell within the same generation. We considered different things such as within each cell, defining instance variables such as next state. While a nextState variable mike sense for a simulation such as Spreading Fire, it does not make sense for simulations like WaTor world where each cell has differing behavior and it would make more sense to actually relocate a cell. We then considered having a current x and y and a next x and y but decided against the idea because of the inefficient overuse of instance variables. We then discussed using the grid of cells and then having an arraylist of cells to change, but we ran into problems with knowing whether cells have been switched already or not. Finally we decided on transferring the cells to a list while updating the instance variables that contain their indices. Once we have iterated through the grid, we should have a list of cells that we can place in a new grid. Placing the updated cells on a new grid reduces the possibility of hidden bugs.


One major issue we will need to address is the delegation of tasks between the Class that contains the simulation and the cells themselves. We believe it is important that we take advantage of OO programming and make our cell objects very dynamic and useful. However, we need to discuss just how much the cell should control. While we originally believed it would make sense for the cell to control and update its position, states, and neighbors, we soon realized that it would be very inefficient to have the cells control their neighbors, especially because the simulation is what contains the grid. We discussed ways we can pass in neighbors into the cell when necessary but because many times we will need to create entirely new objects and place them on the grid, we are currently thinking that it is not wrong for the simulation to control these problems. While it makes sense the individual cells control their own behavior, we don’t think that is is wrong that the simulation controls the inter-cell relationships and their positions on the grid. That would save the unnecessary trouble of having to constantly having to update the neighbors within the cell. Also, our current way of updating the grid so that changes made while updating it don’t affect each other is by changing the cells’ row and column indices within the cell and then later updating the grid. Having operations be done depending on the the neighbors as defined within the cell would get messy since we cannot update the cells’ neighbors until we have iterated through the whole grid. It seems having the neighbors defined within the cell simply does not make sense.


While we now have a general idea of how classes should be divided, the question of which class should be in charge of what is one that will continue to be addressed. Decisions need to be made early in order to avoid inefficient code later.


## Team responsibilities

The `mainpackage.Simulation` class will take the most work, so we will each be responsible for some methods in the class. Daniel will take ownership of the class and make sure we are on the right track.

Michael will deal with merge conflicts and the coordination between our code.

Jorge and Michael take take ownership of the front-end.
