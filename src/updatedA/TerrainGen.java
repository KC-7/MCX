package updatedA;

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
	private final GenPanel genPanel = new GenPanel(80,35);
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
	
	// Vertical layout. Contains settings and generate button.
	private JPanel initBottom() {
		
		final JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
		bottom.add(initBottom_Settings());
		bottom.add(initBottom_Generate());
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
	
	// No layout. Contains generate button.
	private JPanel initBottom_Generate() {
		
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
		case CMD_GENERATE:
			final double roughness = Double.parseDouble(roughness_input_field.getText());
			genPanel.generate(new TerrainMapper(0, 1280, roughness, seed_input_field.getText(), 640, 640, 2000));
		}
		
	}
	
}