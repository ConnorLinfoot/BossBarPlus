package com.connorlinfoot.bossbarplus.BossBar;

public enum BarColor {
	PINK(0),
	BLUE(1),
	RED(2),
	GREEN(3),
	YELLOW(4),
	PURPLE(5),
	WHITE(6);
	private int id;

	BarColor(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
