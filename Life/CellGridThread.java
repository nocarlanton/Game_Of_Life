package Life;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CellGridThread {
	private Cell[][] oldGrid;// need 2 grids, one for current iteration, one for prior
	private Cell[][] newGrid;
	int P;
	CyclicBarrier barr;

	CellGridThread(int dim, int p) {
		makeBoard(dim);
		while(p % 4 != 0) //number of threads always rounds up to power of 4
			p++;
		if(P > 16)
			P = 16;
		barr = new CyclicBarrier(p);
		this.P = p;
		newGrid = new Cell[dim][dim];
	}

	class LifeThread implements Runnable {
		private int myRow, myCol, threadP, length, width, tid;
		private Cell[][] board;
		// borderLists indexed from 0-depth; north and south give back rows and index by
		// column, east and west give back columns and index by rows

		LifeThread(Cell[][] board, int p, int startRow, int startCol, int length, int width, int id) {
			this.board = board;
			this.myRow = startRow;
			this.myCol = startCol;
			this.length = length;// number of rows to solve
			this.width = width;// number of columns to solve
			this.threadP = p;
			this.tid = id;
			barr = new CyclicBarrier(p);
			
		}

		LifeThread(Cell[][] board, int p, int startRow, int startCol, int depth, int id) {
			this.board = board;
			this.myRow = startRow;
			this.myCol = startCol;
			this.length = depth;// number of rows to solve
			this.width = depth;// number of columns to solve
			this.threadP = p;
			this.tid = id;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (int row = myRow; row < myRow + width; row++)
				for (int col = myCol; col < myCol + length; col++) {
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
					// System.out.print(newGrid[row][col].isAlive() ? "*" : "U");
				}
			try {
				//System.out.println("waiting on barrier in thread " + tid);
				barr.await();
			} catch (InterruptedException ex) {
				System.out.println("CellGrid thread " + tid + " InterruptedException");
				return;
			} catch (BrokenBarrierException ex) {
				System.out.println("CellGrid" + tid + " BrokenBarrierException");
				return;
			}
			//System.out.println("cleared barrier");
			// wait until all threads are done, then copy your portion into oldGrid
			for (int row = myRow; row < myRow + width; row++)
				for (int col = myCol; col < myCol + length; col++) {
					oldGrid[row][col].setAlive(newGrid[row][col].isAlive());
				}
		}
		
		private int getNeighbors(int row, int col) {
			int count = 0;
			count += addNeighbor(row - 1, col - 1);
			count += addNeighbor(row, col - 1);
			count += addNeighbor(row + 1, col - 1);
			count += addNeighbor(row - 1, col);
			count += addNeighbor(row + 1, col);
			count += addNeighbor(row - 1, col + 1);
			count += addNeighbor(row, col + 1);
			count += addNeighbor(row + 1, col + 1);
			return count;
		}

		// addNeighbor must determine if the row and column is valid, and 
		public int addNeighbor(int row, int col) {
			// Cell[] temp;
			if (row < 0 || row > board.length || col < 0 || col > board[0].length) // if outside of boards of board
				return 0; // return 0, neighbor is dead
			if (row >= 0 && row < oldGrid.length) // if valid row [0, size)
				if (col >= 0 && col < oldGrid[row].length) {
					return board[row][col].isAlive() ? 1 : 0;		
				}
			return 0;
		}

	}

	public Cell getCell(int row, int col) {
		return oldGrid[row][col];
	}

	public void makeBoard(int dim) {
		int start = dim / 2; // initial board set to 'exploder' shape in the center of the grid
		oldGrid = new Cell[dim][dim];
		for (int row = 0; row < dim; row++)
			for (int col = 0; col < dim; col++) {
				oldGrid[row][col] = new Cell();
				if(Math.random() > .5)
					oldGrid[row][col].setAlive(true);
				/*oldGrid[row][col].setAlive(false);
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
				}*/
			}
	}

	// threads split into quadrants until each thread holds a manageable size
	public void game() {
		// for (int i = 0; i < P; i++) {
		int dim = oldGrid.length;
		int tids = 0;
		if (P > 4) { 
			int splitSize = dim/4;//quarter of array
		new Thread(new LifeThread(oldGrid, P, 0, 0, splitSize, splitSize, tids++)).start();//upper left quadrant
		new Thread(new LifeThread(oldGrid, P, splitSize, 0, splitSize, splitSize, tids++)).start(); //
		new Thread(new LifeThread(oldGrid, P, splitSize, splitSize, splitSize, splitSize, tids++)).start();
		new Thread(new LifeThread(oldGrid, P, 0, splitSize, splitSize, splitSize, tids++)).start();
		
		new Thread(new LifeThread(oldGrid, P, dim / 2, 0, splitSize, splitSize, tids++)).start();//lower left quadrant
		new Thread(new LifeThread(oldGrid, P, 3*splitSize, 0, splitSize, splitSize, tids++)).start();
		new Thread(new LifeThread(oldGrid, P, dim / 2, splitSize, splitSize, splitSize, tids++)).start();
		new Thread(new LifeThread(oldGrid, P, 3*splitSize, splitSize, splitSize, splitSize, tids++)).start();
		
		new Thread(new LifeThread(oldGrid, P, 0, dim/2, splitSize, splitSize, tids++)).start();//upper right
		new Thread(new LifeThread(oldGrid, P, splitSize, dim/2, splitSize, splitSize, tids++)).start();
		new Thread(new LifeThread(oldGrid, P, 0, 3*splitSize, splitSize, splitSize, tids++)).start();
		new Thread(new LifeThread(oldGrid, P, splitSize, 3*splitSize, splitSize, splitSize, tids++)).start();
		
		new Thread(new LifeThread(oldGrid, P, dim/2, dim/2, splitSize, splitSize, tids++)).start();//lower right
		new Thread(new LifeThread(oldGrid, P, dim/2, 3*splitSize, splitSize, splitSize, tids++)).start();
		new Thread(new LifeThread(oldGrid, P, 3*splitSize, dim/2, splitSize, splitSize, tids++)).start();
		new Thread(new LifeThread(oldGrid, P, 3*splitSize, 3*splitSize, splitSize, splitSize, tids++)).start();
		}else {
			new Thread(new LifeThread(oldGrid, P, 0, 0, dim / 2, dim / 2, tids++)).start();//upper left quadrant
			new Thread(new LifeThread(oldGrid, P, dim / 2, 0, dim / 2, dim / 2, tids++)).start();//lower left quadrant
			new Thread(new LifeThread(oldGrid, P, 0, dim/2, dim/2, dim/2, tids++)).start();//upper right
			new Thread(new LifeThread(oldGrid, P, dim/2, dim/2, dim/2, dim/2, tids++)).start();//lower right
		}
		/*
		 * int splitSize = dim/4; int divs = 0; while(splitSize > 10000) { splitSize =
		 * splitSize/4; divs++; } for(int i = 0; i < divs; i++) { new Thread(new
		 * LifeThread(oldGrid, P, i*splitSize, i *splitSize, splitSize, splitSize,
		 * i)).start(); }
		 */

	}
}
