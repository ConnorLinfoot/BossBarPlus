package com.connorlinfoot.bossbarplus.BossBar;

public enum BarAction {
	ADD(0),
	REMOVE(1),
	UPDATE_HEALTH(2),
	UPDATE_TITLE(3),
	UPDATE_STYLE(4),
	UPDATE_FLAGS(5);

	private int id;

	BarAction(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}
