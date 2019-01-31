Part 1
* What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program? 

We're hiding the methods that deal with the unique relationships and interactions between cells for each simulation type.
The GUI doesn't know the specific rules of the simulation or how it is implemented, it only has access to initialization methods
and methods that update the grid in the simulation class. 
 
* What inheritance hierarchies are you intending to build within your area and what behavior are they based around?

There is a general simulation superclass, with subclasses that extend these for specific cell interaction instructions.
We also have a cell superclass with general methods handling location and relocation, with subclass extentions that contain
methods specific to the simulation, which contain variables and methods used to track the behavior and characteristics of 
the same object as it is translated through the grid.

 
* What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?

The simulation class has two public methods accessible by the visualization, one of which updates the grid, calling multiple 
private methods containing simulation-specific rules for cell relations, and the other which handles initializing the grid. 

* What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?

My section is extremely error prone, because it contains every instruction for how the simulation should actually change, and why. 
I am handling them by having the front end create a temporary basic simulation interface that allows me to click through and follow 
the changes of each cell on each frame.

* Why do you think your design is good (also define what your measure of good is)?

My design is good because it contains loose general frameworks which can be efficiently extended to accomodate a variety of 
specific simulation types. 

##Part 2
* How is your area linked to/dependent on other areas of the project?

The simulation is linked to the the visualization only through methods that update and initialize the grid. It also calls 
methods contained internally in the cell subclasses.

* Are these dependencies based on the other class's behavior or implementation?

The dependencies between the visualization and simulation are very minimal. The visualization calls the same methods within the 
simulation class regardless of the simulation type. The simulation calls different cell methods specific to the type of the 
cell in the simulation, but that is inevitable, as some variables necessary for the simulation 
are tracked by specific cells. 

* How can you minimize these dependencies?

I believe we have done our best already to minimize dependencies, and I think the dependencies existing are necessary.


Go over one pair of super/sub classes in detail to see if there is room for improvement 
Focus on what things they have in common (these go in the superclass) and what about them varies (these go in the subclass).


###Part 3
Come up with at least five use cases for your part (most likely these will be useful for both teams).
What feature/design problem are you most excited to work on?
What feature/design problem are you most worried about working on?