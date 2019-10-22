package Life;

public class CellGridSequential {
	private Cell[][] oldGrid;// need 2 grids, one for current iteration, one for prior
	private Cell[][] newGrid;
	
	CellGridSequential(int dim) {
		makeBoard(dim);
	}

	/*
	 * gameStep advances the game board by one generation initializes new game
	 * board, populate from old board, copy new game board into old one, return the
	 * new 'old' game board. this grid will never grow outside its initial size.
	 */
	public void gameStep() {
		newGrid = new Cell[oldGrid.length][oldGrid[0].length];
		for (int row = 0; row < oldGrid.length; row++) {
			for (int col = 0; col < oldGrid[row].length; col++) {// for each cell, check the rules of life
				int nebs = getNeighbors(row, col);
				newGrid[row][col] = new Cell();
				if (oldGrid[row][col].isAlive()) {
					newGrid[row][col].setAlive(true);
					if (nebs <= 1) // death by isolation
						newGrid[row][col].setAlive(false);
					if (nebs >= 4) // death by overpopulation
						newGrid[row][col].setAlive(false);		
				} else if (nebs == 3) // birth from healthy environment
					newGrid[row][col].setAlive(true);
					//System.out.print(newGrid[row][col].isAlive() ? "*" : "U");
			}
		}
		for (int row = 0; row < oldGrid.length; row++)
			for (int col = 0; col < oldGrid[row].length; col++)
				oldGrid[row][col].setAlive(newGrid[row][col].isAlive());
	}

	// takes in row & column of current cell, calls addNeighbor for each of the 8
	// neighbors, returns number of valid neighbors
	public int getNeighbors(int row, int col) {
		int count = 0;
		count += addNeighbor(row - 1, col - 1);
		count += addNeighbor(row, col - 1);
		count += addNeighbor(row + 1, col - 1);
		count += addNeighbor(row - 1, col);
		count += addNeighbor(row + 1, col);
		count += addNeighbor(row - 1, col + 1);
		count += addNeighbor(row, col +1);
		count += addNeighbor(row + 1, col + 1);
		return count;
	}

	// takes in the row and column of neighbor cell, returns 1 if living neighbor, 0 if dead or invalid neighbor
	public int addNeighbor(int row, int col) {
		if (row >= 0 && row < oldGrid.length) // if valid row [0, size)
			if (col >= 0 && col < oldGrid[row].length) {

				return oldGrid[row][col].isAlive() ? 1 : 0;
			}return 0; // invalid neighbor, is considered dead
	}

	public Cell getCell(int row, int col) {
		return oldGrid[row][col];
	}

	// makeboard takes in the dimensions of the board (only makes square boards, for
	// the sake of simplicity)
	public void makeBoard(int dim) {
		int start = dim / 2; // initial board set to 'exploder' shape in the center of the grid
		oldGrid = new Cell[dim][dim];
		for (int row = 0; row < dim; row++)
			for (int col = 0; col < dim; col++) {
				oldGrid[row][col] = new Cell();
				oldGrid[row][col].setAlive(false);
				if (col == start - 2) {
					if (row == start - 2)
						oldGrid[row][col].setAlive(true);
					if (row == start - 1)
						oldGrid[row][col].setAlive(true);
					if (row == start)
						oldGrid[row][col].setAlive(true);
					if (row == start + 1)
						oldGrid[row][col].setAlive(true);
					if (row == start + 2)
						oldGrid[row][col].setAlive(true);
				} else if (col == start) {
					if (row == start - 2)
						oldGrid[row][col].setAlive(true);
					if (row == start + 2)
						oldGrid[row][col].setAlive(true);
				} else if (col == start + 2) {
					if (row == start - 2)
						oldGrid[row][col].setAlive(true);
					if (row == start - 1)	
						oldGrid[row][col].setAlive(true);
					if (row == start)
						oldGrid[row][col].setAlive(true);
					if (row == start + 1)
						oldGrid[row][col].setAlive(true);
					if (row == start + 2)
						oldGrid[row][col].setAlive(true);
				}
			}
		// {0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {2,0}, {2,4}, {4,0}, {4,1}, {4,2}, {4,3},
		// {4,4}}); exploder shape
	}

}
