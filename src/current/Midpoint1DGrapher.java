package current;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Midpoint1DGrapher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				init();
			}
		});
	}

	private static void init() {
		final JFrame f = new JFrame("Midpoint 1D Grapher");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new Midpoint1DGrapherPanel());
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}

class Midpoint1DGrapherPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final short WIDTH = 640;
	private final short HEIGHT = 480;
	private final String CMD_GENERATE = "CMD_GENERATE";
	private final String CMD_PAINT = "CMD_PAINT";
	
	private int xCoord = 0;
	private final int[] map = new int[WIDTH];
	private JTextField seedField = null;
	private JLabel seedlabel2 = null;
	private final Random random = new Random();
	private final Timer paintTimer = new Timer(2, this);

	private final JPanel main = new JPanel(true) {
		private static final long serialVersionUID = 1L;
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (paintTimer.isRunning()) {
				drawMap((Graphics2D) g);
			}
		}
	};

	public Midpoint1DGrapherPanel() {
		initUI();
	}

	private void initUI() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final Dimension size = new Dimension(WIDTH, HEIGHT);
		main.setPreferredSize(size);
		main.setMinimumSize(size);
		main.setMaximumSize(size);
		main.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		final JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(main);
		top.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		final JLabel seedlabel = new JLabel("Seed:");
		seedField = new JTextField("default", 8);
		seedField.setActionCommand(CMD_GENERATE);
		seedField.addActionListener(this);
		final JPanel seedPanel = new JPanel();
		seedPanel.add(seedlabel);
		seedPanel.add(seedField);

		final JButton genButton = new JButton("Generate");
		genButton.setPreferredSize(new Dimension(130, 30));
		genButton.setActionCommand(CMD_GENERATE);
		genButton.addActionListener(this);
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(genButton);
		
		seedlabel2 = new JLabel("0");
		final JPanel infoPanel = new JPanel();
		infoPanel.add(seedlabel2);

		final JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(seedPanel);
		bottom.add(buttonPanel);
		bottom.add(infoPanel);
		bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		add(top);
		add(bottom);
	}

	private void cmdGenerate() {
		long seed = getSeed(seedField.getText());
		random.setSeed(seed);
		seedlabel2.setText(String.valueOf(seed));
		doMidpoint();
		
		xCoord = 0;
		paintTimer.setActionCommand(CMD_PAINT);
		paintTimer.start();
		for (int val : map) {
			System.out.print(val + " ");
		}
		System.out.println();
	}

	// Here the midpoint code begins.
	public void doMidpoint() {
		// Erase the old map array..
		for (int x = 0; x < map.length; x++) {
			map[x] = 0;
		}

		// Setup points in the 2 corners of the map.
		int mid = HEIGHT / 2;
		map[0] = random.nextInt(mid) + mid / 2;
		map[map.length - 1] = random.nextInt(mid) + mid / 2;

		// Do the midpoint
		calcMidpoint(0, map.length - 1);
	}

	// This is the actual mid point displacement code.
	public boolean calcMidpoint(int x1, int x2) {
		// If this is pointing at just on pixel, exit because it doesn't need
		// doing}
		if (x2 - x1 < 2) {
			return false;
		}

		// Find distance between points and use when generating a random number.
		int dist = (x2 - x1);
		int hdist = dist / 2;
		// Find Middle Point
		int midx = (x1 + x2) / 2;
		// Get pixel colors of corners
		int c1 = map[x1];
		int c2 = map[x2];

		// If Not already defined, work out the midpoints of the corners of
		// the rectangle by means of an average plus a random number.
		if (map[midx] == 0) {
			map[midx] = ((c1 + c2 + random.nextInt(dist) - hdist) / 2);
		} 
		// Work out the middle point
		map[midx] = ((c1 + c2 + random.nextInt(dist) - hdist) / 2);

		// Divide this rectangle into 4,  call again For Each smaller rectangle
		calcMidpoint(x1, midx);
		calcMidpoint(midx, x2);
		calcMidpoint(x1, midx);
		calcMidpoint(midx, x2);

		return true;
	}

	private void drawMap(Graphics2D g) {

		for (int x = 0; x < xCoord - 1; x++) {
			g.drawLine(x, HEIGHT - map[x], x + 1, HEIGHT - map[x + 1]);
		}
		
		if (xCoord >= WIDTH) { // If column exceeds limit, stop process
			paintTimer.stop();
			return;
		}
		xCoord++;
	}

	private long getSeed(String input) {
		long seed = input.isEmpty() ? System.currentTimeMillis() : toSeed(input);
		return seed;
	}

	private long toSeed(String input) {
		try {
			return Long.parseLong(input);
		} catch (NumberFormatException e) {
			return input.hashCode();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final String cmd = e.getActionCommand();

		switch (cmd) {
		case CMD_GENERATE:
			cmdGenerate();
			break;
		case CMD_PAINT:
			main.repaint();
			break;
		default:
			throw new AssertionError(cmd);
		}

	}
}