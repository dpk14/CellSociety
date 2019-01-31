# Brian Jordan bjj17 and Michael Zhang sz114 Discussion

## Part  One

1)
  -MZ: visualization part knows about the rendering of the grid, so
  it hides the root node from the rest of the program; only visualization knows what each cell looks like
  -BJ: visualization only knows about the group node of cells and UI components
2)
  -MZ: no inheritance for visualization, but have abstract `Cell` and `Simulation` classes. Cells have to know where they are in the grid, and each simulation has to own a 2D array grid.
  -BJ: have superclasses for simulation and cell. Simulation in charge of interactions between cells - responsible for updating cells. Cell superclass owns how it should be visualized.
3)
  -MZ: no polymorphism in visualization but there is for cell and simulation because of the super classes we made
  -BJ: cell variables are closed but the ability to change them is open only to simulation class. Polymorphism in simulation and cell classes.
4)
5)

## Part Two

1)
2)
3)
4)


## Part Three
1)
2)
3)

