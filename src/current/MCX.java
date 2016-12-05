package current;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MCX {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				init();
			}
		});
	}

	private static void init() {
		JFrame f = new JFrame("MCX-Current");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new MCXPanel());
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}

class MCXPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final short WIDTH = 640;
	private final short HEIGHT = 320;
	private final short BLOCK_PIXELS = 16;
	private final short WIDTH_BLOCKS = WIDTH / BLOCK_PIXELS;
	private final short HEIGHT_BLOCKS = HEIGHT / BLOCK_PIXELS;

	private final String IMG = "res/img/";
	private final String CMD_GENERATE = "CMD_GENERATE";
	private final String CMD_PAINT = "CMD_PAINT";

	private final String BIOME = "BIOME";
	private final int BIOME_GRASS = 0;
	private final int BIOME_DESERT = 1;

	private int blockX = 0;
	private int blockY = 0;
	private final ImageIcon[][] map = new ImageIcon[WIDTH_BLOCKS][HEIGHT_BLOCKS];
	private JTextField seedField;
	private final Timer paintTimer = new Timer(7, this);

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

	public MCXPanel() {
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

		final JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(seedPanel);
		bottom.add(buttonPanel);
		bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		add(top);
		add(bottom);

	}

	private void cmdGenerate() {

		blockX = 0;
		blockY = 0;

		writeMap(getStem(getRoot(seedField.getText())));

		paintTimer.setActionCommand(CMD_PAINT);
		paintTimer.start();

	}

	private void writeMap(Map<String, Integer> stem) {

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {

				boolean shouldDraw = false;
				if (Math.abs(y - (Math.sin(x) + x / 5)) < 1) {
					shouldDraw = true;
				}

				if (shouldDraw) {
					map[x][HEIGHT_BLOCKS - y - 1] = new ImageIcon(
							IMG + (stem.get(BIOME).equals(BIOME_GRASS) ? "grass.png" : "sand.png"));

					for (int y2 = HEIGHT_BLOCKS - y; y2 < HEIGHT_BLOCKS; y2++) {
						map[x][y2] = new ImageIcon(
								IMG + (stem.get(BIOME).equals(BIOME_GRASS) ? "dirt.png" : "sand.png"));
					}

				} else {
					map[x][HEIGHT_BLOCKS - y - 1] = new ImageIcon(IMG + "air.png");
				}

			}
		}

	}

	private void drawMap(Graphics2D g) {

		if (blockY >= HEIGHT_BLOCKS) { // If row exceeds limit, reset it and go
			// to next col
			blockY = 0;
			blockX++;
		}

		final int pos = blockX * WIDTH + blockY; // Compute the raw position

		loops: for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {

				final int currentPos = x * HEIGHT + y;
				if (currentPos > pos) { // If current raw position exceeds pos,
					// stop drawing
					break loops;
				}

				final Image img = map[x][y].getImage();
				g.drawImage(img, BLOCK_PIXELS * x, BLOCK_PIXELS * y, this);
			}
		}

		if (blockX >= WIDTH_BLOCKS) { // If column exceeds limit, stop process
			paintTimer.stop();
			return;
		}

		blockY++;

	}

	private Map<String, Integer> getStem(long root) {

		final Map<String, Integer> stem = new HashMap<String, Integer>();
		stem.put(BIOME, root > 0 ? BIOME_DESERT : BIOME_GRASS);

		// final int[] rootArray =
		// String.valueOf(Math.abs(root)).chars().map(Character::getNumericValue).toArray();

		return stem;

	}

	private long getRoot(String input) {

		long seed = input.isEmpty() ? System.currentTimeMillis() : toSeed(input);
		long root = new Random(seed).nextLong();
		System.out.println(seed + " | " + root);

		return root;

	}

	private long toSeed(String input) {

		try {
			return Long.parseLong(input);
		} catch (NumberFormatException e) {
			return input.hashCode();
		}

	}

	@Override
	public final void actionPerformed(ActionEvent e) {

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
