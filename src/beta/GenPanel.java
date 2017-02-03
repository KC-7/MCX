package beta;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GenPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	final static int PX_PER_BLOCK = 16; 	// Pixels per block

	private interface BlockType {}
	private enum BlockHolder implements BlockType {
		EMPTY, SURFACE, INTERIOR
	}
	private enum Block implements BlockType {
		AIR, GRASS, DIRT, STONE, SAND, GRASS_SNOW, SNOW;
	}
	private enum Biome {
		GRASS, DESERT, TUNDRA;
	}

	private final int cols;
	private final int rows;
	private final Timer paintTimer;
	private final String CMD_PAINT = "CMD_PAINT";
	
	private int currentCol;
	private long seed;
	private int[] map;
	private Biome biome;
	private final BlockType[][] world;

	public GenPanel(int cols, int rows) {

		this.cols = cols;
		this.rows = rows;
		world = new BlockType[cols][rows];
		paintTimer = new Timer(10, this);
		paintTimer.setActionCommand(CMD_PAINT);
		
		// Sets size to number of blocks * pixels per block
		final Dimension size = new Dimension(PX_PER_BLOCK * cols, PX_PER_BLOCK * rows);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

	}

	void generate(TerrainMapper terrainMapper) {

		seed = terrainMapper.getSeed();
		map = terrainMapper.getMap();
		biome = Biome.values()[terrainMapper.getRandom().nextInt(Biome.values().length)];
		writeWorld();
		refineWorld();
		currentCol = 0;
		paintTimer.start();

	}
	
	private void writeWorld() {
		
		for (int x = 0; x < world.length; x++) {
			
			// Write surface blocks as per the heightmap
			int y = map[x * PX_PER_BLOCK];			// Spread out height collection	
			y += (PX_PER_BLOCK / 2);				// Ensure rounding in next step's division
			y /= PX_PER_BLOCK;						// Convert y to block count
			world[x][y] = BlockHolder.SURFACE;
			
			// Write empty blocks above surface
			for (int y2 = y - 1; y2 >= 0; y2--) {
				world[x][y2] = BlockHolder.EMPTY;
			}
			
			// Write interior blocks below surface
			for (int y2 = y + 1; y2 < rows; y2++) {
				world[x][y2] = BlockHolder.INTERIOR;
			}

		}

	}

	private void refineWorld() {

		for (int x = 0; x < world.length; x++) {

			for (int y = 0; y < world[0].length; y++) {

				final BlockHolder blockHolder = (BlockHolder) world[x][y];
				switch (blockHolder) {
				case EMPTY:
					world[x][y] = Block.AIR;
					break;
				case SURFACE:
					switch(biome) {
					case DESERT:
						world[x][y] = Block.SAND;
						break;
					case GRASS:
						world[x][y] = Block.GRASS;
						break;
					case TUNDRA:
						world[x][y] = Block.GRASS_SNOW;
						break;
					default:
						throw new AssertionError(biome);
					}
					break;
				case INTERIOR:
					switch(biome) {
					case DESERT:
						world[x][y] = Block.SAND;
						break;
					case GRASS:
						world[x][y] = Block.DIRT;
						break;
					case TUNDRA:
						world[x][y] = Block.DIRT;
						break;
					default:
						throw new AssertionError(biome);
					}					break;
				default:
					throw new AssertionError(blockHolder);
				}

			}

		}

	}

	private void drawWorld(Graphics g) {
		final Graphics2D gfx = (Graphics2D)g;

		for (int x = 0; x < currentCol; x++) {
			for (int y = 0; y < world[0].length; y++) {

				final String name = (world[x][y] == null) ? "air" : world[x][y].toString().toLowerCase();
				final Image img = new ImageIcon("res/img/" + name + ".png").getImage();
				gfx.drawImage(img, PX_PER_BLOCK * x, PX_PER_BLOCK * y, this);

			}
		}
		currentCol++;
		if (currentCol > cols) { 
			paintTimer.stop();
		}
		
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (paintTimer.isRunning()) {
			drawWorld(g);
		} else {
			paintTimer.start();
		}
		g.drawString(String.valueOf(seed), PX_PER_BLOCK, PX_PER_BLOCK);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		final String cmd = ae.getActionCommand();
		switch(cmd) {
		case CMD_PAINT:
			repaint();
			break;
		default:
			throw new AssertionError(cmd);
		}
		
	}
	
	int getRows() {
		return rows;
	}
	
}
