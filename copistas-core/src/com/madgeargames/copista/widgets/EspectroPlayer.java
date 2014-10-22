package com.madgeargames.copista.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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
	Music[] sounds = new Music[8];

	public EspectroPlayer(Size size, Position position) {
		this.size = size;
		this.position = position;
		generarZonas(new int[] {}, null);
		for (int i = 0; i < sounds.length; i++) {
			sounds[i] = Gdx.audio.newMusic(Gdx.files.internal(i + ".ogg"));
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		zonas.draw(batch, parentAlpha);
	}

	@Override
	public void clear() {
		this.clearActions();
		generarZonas(new int[] {}, new NoteSet(new int[] {}));
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
			// color fill
			for (int i = 0; i < notas.length; i++) {
				int noteId = notas[i];
				int noteIndex = noteSet.getNoteIndex(noteId);
				pixmap.setColor(noteSet.getNoteById(noteId).getColor());
				pixmap.fillRectangle(width * noteIndex, 0, width, height);
			}
			// grid
			System.out.println("Drawing " + (noteSet.size() - 1) + " lines.");
			pixmap.setColor(Color.BLACK);
			pixmap.drawLine(0, 0, 640, 0);
			for (int i = 0; i < noteSet.size() - 1; i++) {
				pixmap.drawLine(width * (i + 1), 0, width * (i + 1), 360);
			}
		}
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		zonas = new Sprite(texture);
		if (size == Size.half && position == Position.lower) {
			zonas.setY(-height);
		}
	}

	public void showNotes(Sequence sequence, float sustainTime, float silenceTime, NoteSet noteSet,
			boolean soundEnabled) {
		final int[] notas = sequence.toArray();
		final NoteSet finalNoteSet = noteSet;
		final boolean sound = soundEnabled;
		for (int i = 0; i < notas.length; i++) {
			final int j = i;
			this.addAction(Actions.sequence(Actions.delay((sustainTime + silenceTime) * i),
					Actions.run(new Runnable() {

						@Override
						public void run() {
							// Show and play note
							generarZonas(new int[] { notas[j] }, finalNoteSet);
							if (sound) {
								playNote(notas[j]);
							}
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
		if (sounds[noteId].isPlaying()) {
			sounds[noteId].stop();
		}
		sounds[noteId].play();
	}

	public enum Size {
		half, full;
	}

	public enum Position {
		upper, lower;
	}
}
