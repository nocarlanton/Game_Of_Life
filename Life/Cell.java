package Life;

public class Cell {
	private boolean isAlive;
	private int row, col;
	
	Cell(){
		this.isAlive = false;
	}
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
}
