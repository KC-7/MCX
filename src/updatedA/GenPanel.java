package updatedA;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GenPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final int BLOCK_PX = 16; // Pixels per block
	private final int WIDTH_PX; // Numbers of pixels
	private final int HEIGHT_PX; // Numbers of pixels	
	
	private enum Block {
		SURFACE, INTERIOR
	}
	
	private enum Biome {
		GRASS, DESERT
	}

	private long seed;
	private int[] map;
	private Block[][] world;

	public GenPanel(int width, int height) {

		WIDTH_PX = BLOCK_PX * width;
		HEIGHT_PX = BLOCK_PX * height;

		final Dimension size = new Dimension(WIDTH_PX, HEIGHT_PX);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

	}

	void generate(TerrainMapper terrainMapper) {

		seed = terrainMapper.getSeed();
		map = terrainMapper.getMap();
		writeWorld();
		drawWorld();

	}

	private void writeWorld() {

		for (int x = 0; x < map.length; x += BLOCK_PX) {

			int y = map[x];
			y += BLOCK_PX / 2;
			y /= BLOCK_PX;
			y *= BLOCK_PX;

			world[x][y] = Block.SURFACE;
			
			for (int y2 = y; y2 >= 0; y2--) {
				world[x][y2] = Block.INTERIOR;
			}
			
		}

	}
	
	private void refineWorld() {
		
		
		
	}

	private void drawWorld() {
		
		

	}

}
