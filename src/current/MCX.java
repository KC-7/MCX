package current;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
				final JFrame frame = new JFrame("MCX-Current");
				frame.getContentPane().add(new MCXPanel());
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

}

class MCXPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final int WIDTH = 1280;
	private final int HEIGHT = 560;
	private final int BLOCK_PIXELS = 16;
	private final int WIDTH_BLOCKS = WIDTH / BLOCK_PIXELS;
	private final int HEIGHT_BLOCKS = HEIGHT / BLOCK_PIXELS;

	private final String IMG = "res/img/";
	private final String CMD_GENERATE = "CMD_GENERATE";
	private final String CMD_PAINT = "CMD_PAINT";

	private final String BIOME = "BIOME";
	private final int BIOME_GRASS = 0;
	private final int BIOME_DESERT = 1;

	private int column = 0;
	private final ImageIcon[][] map = new ImageIcon[WIDTH_BLOCKS][HEIGHT_BLOCKS];
	private JTextField seed_input_field;
	private final Timer paintTimer = new Timer(40, this);

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
		top.add(main);
		top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		final JLabel seed_input_label = new JLabel("Seed:");
		seed_input_field = new JTextField("default", 8);
		seed_input_field.setActionCommand(CMD_GENERATE);
		seed_input_field.addActionListener(this);
		final JPanel seedPanel = new JPanel();
		seedPanel.add(seed_input_label);
		seedPanel.add(seed_input_field);

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

		column = 0;
		writeMap(getStem(getRoot(seed_input_field.getText())));
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
		
		// Reiterates through all previous columns (repaint() clears previous)
		for (int x = 0; x < column; x++) {
			for (int y = 0; y < map[0].length; y++) {
				g.drawImage(map[x][y].getImage(), BLOCK_PIXELS * x, BLOCK_PIXELS * y, this);
			}
		}
		column++;
		
		if (column > WIDTH_BLOCKS) { 
			paintTimer.stop();
		}

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
		
		// TODO remove when no longer needed
		System.out.printf("%19d", seed);
		System.out.print(" | ");
		System.out.printf("%20d", root);
		System.out.println();

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
