package beta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
				final JFrame frame = new JFrame("Midpoint 1D Grapher");
				frame.add(new Midpoint1DGrapherPanel());
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

}

class Midpoint1DGrapherPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final String CMD_GENERATE = "CMD_GENERATE";
	private final String CMD_PAINT = "CMD_PAINT";
	private final int WIDTH = 1300;	// 1300
	private final int HEIGHT = 625;	// 625

	private int[] map;
	private long seed;
	private int currentXPosition;

	private final Timer drawTimer = new Timer(0, this);
	private final JPanel main = new JPanel() {

		private static final long serialVersionUID = 1L;
		
		@Override 
		public void paintComponent(Graphics g) { 

			super.paintComponent(g);
			
			if (drawTimer.isRunning()) {
				
				draw((Graphics2D) g);
				
			}
			
		}
		
	};

	private JTextField seed_input_field;
	private JTextField roughness_input_field;	

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

		final JLabel seed_input_label = new JLabel("Seed:");
		seed_input_field = new JTextField("default", 8);
		seed_input_field.setActionCommand(CMD_GENERATE);
		seed_input_field.addActionListener(this);
		final JPanel seedPanel = new JPanel();
		seedPanel.add(seed_input_label);
		seedPanel.add(seed_input_field);

		final JLabel roughness_input_label = new JLabel("Roughness:");
		roughness_input_field = new JTextField("1.0", 2);
		roughness_input_field.setActionCommand(CMD_GENERATE);
		roughness_input_field.addActionListener(this);
		final JPanel roughnessPanel = new JPanel();
		roughnessPanel.add(roughness_input_label);
		roughnessPanel.add(roughness_input_field);

		final JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.X_AXIS));
		settingsPanel.add(seedPanel);
		settingsPanel.add(roughnessPanel);

		final JButton genButton = new JButton("Generate");
		genButton.setPreferredSize(new Dimension(130, 30));
		genButton.setActionCommand(CMD_GENERATE);
		genButton.addActionListener(this);
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(genButton);

		final JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(settingsPanel);
		bottom.add(buttonPanel);
		bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		add(top);
		add(bottom);
	}

	private void generate() {

		double roughness = Double.parseDouble(roughness_input_field.getText());
		seed = TerrainMapper.getSeed(seed_input_field.getText());
		final TerrainMapper tm = new TerrainMapper(10, HEIGHT - 10, roughness, seed, HEIGHT / 2, HEIGHT / 2, 2000);
		map = tm.getMap();

		drawMap();
		tm.printMap();
	}

	private void drawMap() {
		currentXPosition = 0;
		drawTimer.setActionCommand(CMD_PAINT);
		drawTimer.start();
	}
	
	private void draw(Graphics2D g) {

		// Draw a line between each point until the current max x coordinate
		for (int x = 0; x < currentXPosition - 1; x++) {
			g.drawLine(x, map[x], x + 1, map[x + 1]);
		}
		currentXPosition++;

		// If column exceeds limit, stop process
		if (currentXPosition >= map.length || currentXPosition >= WIDTH) { 
			drawTimer.stop();
			g.drawString("Seed: " + seed, 10, 20);
		}

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final String cmd = e.getActionCommand();

		switch (cmd) {
		case CMD_GENERATE:
			generate();
			break;
		case CMD_PAINT:
			main.repaint();
			break;
		default:
			throw new AssertionError(cmd);
		}

	}
	
}
