package com.madgeargames.copista.notes;

import com.badlogic.gdx.graphics.Color;

public class Note implements Comparable<Note> {
	private final String name;
	private final Color color;
	public final int id;

	public Note(int id, String name, int r, int g, int b) {
		this.id = id;
		this.name = name;
		this.color = new Color(((float) r) / 255, ((float) g) / 255,
				((float) b) / 255, 1);
	}

	public String name() {
		return this.name;
	}

	public Color getColor() {
		return this.color;
	}

	@Override
	public int compareTo(Note o) {
		if (this.id == o.id) {
			return 0;
		} else if (this.id > o.id) {
			return 1;
		} else {
			return -1;
		}
	}
}
