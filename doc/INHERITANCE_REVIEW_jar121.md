# Configuration Review
### Worked on by: jar121 & ih52
## Part 1
### What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?
* Ian: My program is creating a SimulationInfo class that contains all of the XML parsed data, and makes it available to the classes that need the data to initialize the starting configuration. All of the XML info is read in and passed to a new SimulationInfo object.

* Jorge: There is an XMLParser Class that reads in any XML file and returns a Simulation instance. The rest of the program has no need to know how it was created. Within the parser class, cells are also created and initialized in the grid that will be put into the simulation that is retur



### What inheritance hierarchies are you intending to build within your area and what behavior are they based around?
* Ian: I am using inheritance hierarchies to define what type of simulation is being created. There will be an abstract RuleKeeper class that has subclasses defining the rules and allowed states of each game. 

* Jorge: Within configuration, I am taking advantage of inheritance when creating the Simulation object and the Cell objects that populate it. Since I want my getSimulation method to be able to return any kind of Simulation subclass, I use abstract methods within enumerations that represent these two kinds of objects.

### What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?
* Ian: I am trying to make the RuleKeeper class closed so that none of the cells' states will be altered in the future by anything other than what the rules state they should be affected by.
* Jorge: I am trying to make the entire setup of the grid as closed as possible, making the grid and its cells all at once and then giving it up. There are dependencies however on the Simulation subclasses' Data fields in order to make the methods more abstract within parser class.

### What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?
### Why do you think your design is good (also define what your measure of good is)?
* Ian: I throw an exception if the file name or type is not valid, or if the media type of the xml file is not listed as "Simulation". If any of the above occurs, an exception is thrown and the program cannot run, as the correct information for the simulation is not taken in. 

* Jorge: throwing exceptions when wrong file name, simulation type does not match any of the listed ones. Something I should take into account is if one of the data labels don't match the expected ones which could could problems with the grid layout


## Part 2
### How is your area linked to/dependent on other areas of the project?
* Ian: I view the main part of configuration as setting up the other areas, visualization and simulation, correctly. I will pass in the initial state of the cells, make sure everything is correct, and establish the correct set of rules so that the simulation can run correctly and the visualization can show what is happening in the simulation.

* Jorge: The other areas depend on the configuration area mroe than the other way around. My area is in charge of creating the simulation itself which will later update the grid. The configuration and simulation is then completely indpendent of visualization, which requires the grid be given from the Simulation instance.

### Are these dependencies based on the other class's behavior or implementation?
* Ian: No these are not dependent on other class's implementation. The only thing that affects the beginning information is the information in the data file. Rather, most other classes depend on this class' information to obtain the initial format of both the simulation and the visualization.

* Jorge: This is completely independent of the other areas. Configuration is respoinsible for creating the Simulation instance completely dependent on the XMl 

### How can you minimize these dependencies?
### Go over one pair of super/sub classes in detail to see if there is room for improvement. 
### Focus on what things they have in common (these go in the superclass) and what about them varies (these go in the subclass).


## Part 3
### Come up with at least five use cases for your part (most likely these will be useful for both teams).
### What feature/design problem are you most excited to work on?
### What feature/design problem are you most worried about working on?
## Jorge Raad