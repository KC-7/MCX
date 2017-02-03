package beta;

import java.awt.Color;
import java.awt.Dimension;
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

public class TerrainGen {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame("TerrainGen v1");
				frame.add(new TerrainGenPanel());
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

class TerrainGenPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final String CMD_GENERATE = "CMD_GENERATE";
	private final String CMD_LEFT = "CMD_LEFT"; 
	private final String CMD_RIGHT = "CMD_RIGHT";
	private final GenPanel genPanel = new GenPanel(80, 35);
	private JTextField roughness_input_field;
	private JTextField seed_input_field;

	public TerrainGenPanel() {
		initUI();
	}
	
	private void initUI() {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(initTop());
		add(initBottom());
		
	}
	
	// No layout. Contains GenPanel.
	private JPanel initTop() {
		
		final JPanel top = new JPanel();
		top.add(genPanel);
		
		return top;
		
	}
	
	// Vertical layout. Contains settings, buttons, and generator.
	private JPanel initBottom() {
		
		final JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(initBottom_Settings());
		bottom.add(initBottom_Buttons());
		bottom.add(initBottom_Generator());
		bottom.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		return bottom;
		
	}
	
	// Horizontal layout. Contains seed and roughness.
	private JPanel initBottom_Settings() {
		
		final JPanel settings = new JPanel();
		settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));
		settings.add(initBottom_Settings_Seed());
		settings.add(initBottom_Settings_Roughness());
		
		return settings;
		
	}
	
	// No layout. Contains seed label and field.
	private JPanel initBottom_Settings_Seed() {
		
		final JPanel seed = new JPanel();
		final JLabel seed_input_label = new JLabel("Seed:");
		seed_input_field = new JTextField("default", 10);
		seed_input_field.setActionCommand(CMD_GENERATE);
		seed_input_field.addActionListener(this);
		seed.add(seed_input_label);
		seed.add(seed_input_field);
		
		return seed;
		
	}
	
	// No layout. Contains roughness label and field.
	private JPanel initBottom_Settings_Roughness() {
		
		final JPanel roughness = new JPanel();
		final JLabel roughness_input_label = new JLabel("Roughness:");
		roughness_input_field = new JTextField("1.0", 2);
		roughness_input_field.setActionCommand(CMD_GENERATE);
		roughness_input_field.addActionListener(this);
		roughness.add(roughness_input_label);
		roughness.add(roughness_input_field);
		
		return roughness;
		
	}
	
	// No layout. Contains left and right buttons.
	private JPanel initBottom_Buttons() {
		
		final JPanel buttons = new JPanel();
		buttons.add(initBottom_Buttons_Left());
		buttons.add(initBottom_Buttons_Right());
		
		return buttons;
		
	}
	
	// No layout. Contains left button.
	private JPanel initBottom_Buttons_Left() {
		
		final JPanel left = new JPanel();
		final JButton button = new JButton("<--");
		button.setActionCommand(CMD_LEFT);
		button.addActionListener(this);
		left.add(button);
		
		return left;
	}
	
	// No layout. Contains right button.
	private JPanel initBottom_Buttons_Right() {
		
		final JPanel right = new JPanel();
		final JButton button = new JButton("-->");
		button.setActionCommand(CMD_RIGHT);
		button.addActionListener(this);
		right.add(button);
		
		return right;
	}
	
	// No layout. Contains generate button.
	private JPanel initBottom_Generator() {
		
		final JPanel generator = new JPanel();
		final JButton generateButton = new JButton("Generate Terrain");
		generateButton.setPreferredSize(new Dimension(130, 30));
		generateButton.setActionCommand(CMD_GENERATE);
		generateButton.addActionListener(this);
		generator.add(generateButton);
		
		return generator;
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
		final String cmd = ae.getActionCommand();
		switch (cmd) {
		case CMD_LEFT:
			break;
		case CMD_RIGHT:
			break;
		case CMD_GENERATE:
			final int minFromTop = 3 * GenPanel.PX_PER_BLOCK;
			final int maxFromTop = (genPanel.getRows() - 3) * GenPanel.PX_PER_BLOCK;
			final double roughness = Double.parseDouble(roughness_input_field.getText());
			final String seed = seed_input_field.getText();
			final int startVal = (genPanel.getRows() * GenPanel.PX_PER_BLOCK) / 2;
			final int endVal = startVal;
			genPanel.generate(new TerrainMapper(minFromTop, maxFromTop, roughness, seed, startVal, endVal, 2000));
			break;
		default:
			throw new AssertionError(cmd);
		}
		
	}
	
}
