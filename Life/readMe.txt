GameBoardSequential is the sequential version of the game of life, which uses CellGridSequential for its board
GameBoardParallel is the parallel version, which uses CellGridThread for its board, which takes in the size of the dimensions of the board( only handles square boards), and the number of threads to use, which is restricted to 4 or 16

commented out in main is the code for use of the graphical representation of the game board. remove the while(true) loop and use the commented code as the whole of main

Once my board size exceeded 1024x1024, the ColoredGrid becomes too large to use as given.
Sequential code generation aging takes from 6-12 seconds at grid size 8192x8192.
Parallel code takes around 6-3 seconds at the same size.