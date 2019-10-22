package Life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;

import Life.GameBoardSequential.BHandler;

public class GameBoardParallel {

	private static final int DIM = 8192;
	private static final String REPLAY = "Replay";
	private static JFrame application;
	private static JButton button;
	private static Color[][] grid;
	private static CellGridThread board;
	// final String FILENAME = "observation_test.dat";

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		while (true) {
			board = new CellGridThread(DIM, 16);
			long startms = System.currentTimeMillis();
			System.out.println("start time: " + startms + "milliseconds ");
			board.game();
			long finishms = System.currentTimeMillis();
			System.out.println("finish time: " + finishms + "milliseconds ");
			System.out.println("Game board generation evolved in " + (finishms - startms)  + " milliseconds");
		}
		/*
		 * grid = new Color[DIM][DIM]; board = new CellGridThread(DIM, 16); application
		 * = new JFrame(); application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * //fillGrid(grid);
		 * 
		 * ColoredGrid gridPanel = new ColoredGrid(grid); application.add(gridPanel,
		 * BorderLayout.CENTER);
		 * 
		 * button = new JButton(REPLAY); button.addActionListener(new BHandler());
		 * application.add(button, BorderLayout.PAGE_END);
		 * 
		 * application.setSize(DIM * 4, (int) (DIM * 4.4));
		 * application.setVisible(true); application.repaint(); animate();
		 */
	}

	private static void animate() throws InterruptedException {
		button.setEnabled(false);

		for (int i = 0; i < DIM; i++) {
			fillGrid(grid);
			application.repaint();
			long startms = System.currentTimeMillis();
			board.game();
			long finishms = System.currentTimeMillis();
			System.out.println("Game board generation evolved in " + (finishms - startms) / 1000 + " seconds");
			Thread.sleep(1000);
		}
		button.setEnabled(true);
		application.repaint();
	}

	static class BHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (REPLAY.equals(e.getActionCommand())) {
				new Thread() {
					public void run() {
						try {
							animate();
						} catch (InterruptedException e) {
							System.exit(0);
						}
					}
				}.start();
			}
		}
	};

	static private final Color COLD = new Color(0x0a, 0x37, 0x66), HOT = Color.RED;
	static private int offset = 0;

	private static void fillGrid(Color[][] grid) {
		int pixels = grid.length * grid[0].length;
		for (int r = 0; r < grid.length; r++)
			for (int c = 0; c < grid[r].length; c++)
				grid[r][c] = interpolateColor((double) (board.getCell(r, c).isAlive() ? 1.0 : 0.0), COLD, HOT);
		offset += DIM;
	}

	private static Color interpolateColor(double ratio, Color a, Color b) {
		int ax = a.getRed();
		int ay = a.getGreen();
		int az = a.getBlue();
		int cx = ax + (int) ((b.getRed() - ax) * ratio);
		int cy = ay + (int) ((b.getGreen() - ay) * ratio);
		int cz = az + (int) ((b.getBlue() - az) * ratio);
		return new Color(cx, cy, cz);
	}
}
