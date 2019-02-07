## Checklist refactoring

After considering the bugs from design.cs.duke.edu, we did not notice
huge design flaws in our code. Much of the errors can be fixed quickly
and individually, so we decided to focus on more pressing design issues.

## General refactoring

#### XML Parser
So we realized there as many cases of repition within the createCell and createSimulation methods. I repeated the same two lines multiple times. This is because I need to pass in a created map into the constructor for the simulation subclass, but I cannot get the specific data fields until I have an instance of the subclass. In order to fix this. We can avoid the need for the data field list altogether by having a method in the super class that, when passed in an element, automatically creates the map of all fields and values in the XML file. There is no need for concern over the actual contents of the map until it is passed into the instantiated simulation. A similar strategy can be applied to the switch cases implemented in createCells.

#### Visualization - rendering grid
The current setup of our design is for the Grid class to have full control over the grid and its type. Then the Visualization class would iterate through the grid in the Grid class and render each individual cell. We discussed at length, how we can generalize this rendering process--instead of calling a different rendering method for each type of grid. We decided against this type of generalization because the similarities between different grid types are so few, and we did not want to have the Grid class handle its visualization. Also, in anticipation of the SugarScape grid mechanism, it would be in our best interest to keep our grid visualization as is.
