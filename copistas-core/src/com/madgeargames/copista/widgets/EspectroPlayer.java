package com.madgeargames.copista.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.madgeargames.copista.notes.NoteSet;
import com.madgeargames.copista.sequences.Sequence;

public class EspectroPlayer extends Actor {

	Sprite zonas;
	Size size;
	Position position;

	public EspectroPlayer(Size size, Position position) {
		this.size = size;
		this.position = position;
		generarZonas(new int[] {}, null);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		zonas.draw(batch, parentAlpha);
	}

	private void generarZonas(int[] notas, NoteSet noteSet) {
		int height;
		int width;
		if (size == Size.full) {
			height = 360;
		} else {
			height = 180;
		}

		Pixmap pixmap = new Pixmap(640, 360, Pixmap.Format.RGBA8888);
		if (noteSet != null && noteSet.size() > 0) {
			width = 640 / noteSet.size();
			for (int i = 0; i < notas.length; i++) {
				int noteId = notas[i];
				int noteIndex = noteSet.getNoteIndex(noteId);
				pixmap.setColor(noteSet.getNoteById(noteId).getColor());
				pixmap.fillRectangle(width * noteIndex, 0, width, height);
			}
		}
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		zonas = new Sprite(texture);
		if (size == Size.half && position == Position.lower) {
			zonas.setY(-height);
		}
	}

	public void showNotes(Sequence sequence, float sustainTime, float silenceTime, NoteSet noteSet) {
		final int[] notas = sequence.toArray();
		final NoteSet finalNoteSet = noteSet;
		for (int i = 0; i < notas.length; i++) {
			final int j = i;
			this.addAction(Actions.sequence(Actions.delay((sustainTime + silenceTime) * i),
					Actions.run(new Runnable() {

						@Override
						public void run() {
							// Show and play note
							generarZonas(new int[] { notas[j] }, finalNoteSet);
							playNote(notas[j]);
						}
					})));
			this.addAction(Actions.sequence(
					Actions.delay((sustainTime + silenceTime) * i + sustainTime),
					Actions.run(new Runnable() {

						@Override
						public void run() {
							// Hide note
							generarZonas(new int[] {}, finalNoteSet);
						}
					})));
		}
	}

	private void playNote(int noteId) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(noteId + ".ogg"));
		float randomVolume = (float) (0.1 + Math.random() / 10);
		float randomPan = (float) (-.5 + Math.random());
		sound.play(randomVolume);
	}

	public enum Size {
		half, full;
	}

	public enum Position {
		upper, lower;
	}
}
