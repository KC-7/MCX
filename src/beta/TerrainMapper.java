package beta;

import java.util.Random;

public class TerrainMapper {

	private final int MIN;
	private final int MAX;
	private final double ROUGHNESS;
	private final Random RANDOM;
	private final int[] map;
	private long seed;
	
	public TerrainMapper(int min, int max, double roughness, String seed, int start, int end, int size) {
		this(min, max, roughness, new Random(getSeed(seed)), start, end, size);
		this.seed = getSeed(seed);
	}

	public TerrainMapper(int min, int max, double roughness, long seed, int start, int end, int size) {
		this(min, max, roughness, new Random(seed), start, end, size);
		this.seed = seed;
	}
	
	private TerrainMapper(int min, int max, double roughness, Random random, int start, int end, int size) {

		this.MIN = min;
		this.MAX = max;
		this.ROUGHNESS = roughness;
		this.RANDOM = random;
		map = new int[size];
		
		map[0] = clamp(start);
		map[map.length - 1] = clamp(end);
		displaceMidpoint(0, map.length - 1);

	}
	
	// Prints the map
	public void printMap() {
		for (int x = 0; x < map.length; x++) {
			System.out.printf("%03d", x);
			System.out.println(" | " + map[x]);
		}
	}
	
	// Returns the map
	public int[] getMap() {
		return map;
	}
	
	// Returns the seed
	public long getSeed() {
		return seed;
	}
	
	// Returns the random
	public Random getRandom() {
		return RANDOM;
	}
	
	// Empty = current time, String = hash code, number = as entered
	public static long getSeed(String input) {
		return input.isEmpty() ? System.currentTimeMillis() : toSeed(input);
	}
	
	// Converts String to hash code, number stays as entered
	private static long toSeed(String input) {
		try {
			return Long.parseLong(input);
		} catch (NumberFormatException e) {
			return input.hashCode();
		}
	}
	
	// Performs midpoint displacement and call itself on its segment's midpoints
	private void displaceMidpoint(int a, int b) {

		final int dist = b - a;
		if (dist < 2) {
			return;
		}

		final int change = RANDOM.nextInt(dist) - dist / 2;		// Plus/minus half the distance
		final int delta = (int) (change * ROUGHNESS);			// NOTE: This implements the roughness AFTER the change is calculated
		final int sum = map[a] + map[b];
		final int mid = (a + b) / 2;

		map[mid] = (sum + delta) / 2;
		map[mid] = clamp(map[mid]);

		displaceMidpoint(a, mid);
		displaceMidpoint(mid, b);

	}
	
	// Clamps a value between the fields min and max
	private int clamp(int val) {
		return Math.max(MIN, Math.min(MAX, val));
	}

}
