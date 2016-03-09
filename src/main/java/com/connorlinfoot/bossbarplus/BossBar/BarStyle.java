package com.connorlinfoot.bossbarplus.BossBar;

public enum BarStyle {
	SOLID(0),
	SEGMENTED_6(1),
	SEGMENTED_10(2),
	SEGMENTED_12(3),
	SEGMENTED_20(4);
	private int id;

	BarStyle(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
