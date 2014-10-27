package com.madgeargames.copista.widgets;

import java.util.ArrayList;
import java.util.List;

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
	PreRender preRender;
	List<Texture> textureList = new ArrayList<Texture>();

	public EspectroPlayer(Size size, Position position) {
		this.size = size;
		this.position = position;
		// generarZonas(new int[] {}, null);
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
		if (zonas != null) {
			zonas.draw(batch, parentAlpha);
		}
	}

	@Override
	public void clear() {
		this.clearActions();
		generarZonas(new int[] {}, new NoteSet(new int[] {}));
	}

	private void generarZonas(int[] notas, NoteSet noteSet) {
		if (noteSet == null) {
			System.out.println("ASDF Oh My GodDASDASD");
		}
		// if unique note or empty grid -> we can use preRender
		if (notas.length == 1 || notas.length == 0) {
			if (preRender != null) {
				if (noteSet.equals(preRender.noteSet)) {
					if (notas.length == 0) {
						zonas = preRender.getSprite(-1);
					} else {
						zonas = preRender.getSprite(notas[0]);
					}
				} else {
					preRender.render(noteSet);
					if (notas.length == 0) {
						zonas = preRender.getSprite(-1);
					} else {
						zonas = preRender.getSprite(notas[0]);
					}
				}
			} else {
				preRender = new PreRender(noteSet);
				if (notas.length == 0) {
					zonas = preRender.getSprite(-1);
				} else {
					zonas = preRender.getSprite(notas[0]);
				}
			}
		} else { // multinote -> we won't use preRender (possible memory leak)
			zonas = render(notas, noteSet);
		}
	}

	/**
	 * Keeps a maximum of 10 textures on memory
	 */
	private void textureGarbageRecolect() {
		while (true) {
			System.out.println("Textures size is " + textureList.size());
			if (textureList.size() > 10) {
				textureList.get(0).dispose();
				textureList.remove(0);
			} else {
				break;
			}
		}
	}

	private Sprite render(int[] notas, NoteSet noteSet) {
		Sprite sprite;
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
		textureList.add(texture);
		textureGarbageRecolect();
		pixmap.dispose();
		sprite = new Sprite(texture);
		if (size == Size.half && position == Position.lower) {
			sprite.setY(-height);
		}
		return sprite;
	}

	public void showNotes(Sequence sequence, float sustainTime, float silenceTime, NoteSet noteSet,
			boolean soundEnabled) {
		final int[] notas = sequence.toArray();
		final NoteSet finalNoteSet = noteSet;
		final boolean sound = soundEnabled;
		if (notas.length == 0) {
			generarZonas(new int[] {}, noteSet);
		} else {
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

	protected class PreRender {
		NoteSet noteSet;
		Sprite[] noteSprites;
		Sprite gridSprite;

		public PreRender(NoteSet noteSet) {
			noteSprites = new Sprite[8];
			render(noteSet);
		}

		public void render(NoteSet noteSet) {
			this.noteSet = noteSet;
			for (int noteId : noteSet.getIds()) {
				noteSprites[noteId] = EspectroPlayer.this.render(new int[] { noteId }, noteSet);
			}
			gridSprite = EspectroPlayer.this.render(new int[] {}, noteSet);
		}

		public Sprite getSprite(int noteId) {
			if (noteId == -1) {
				return gridSprite;
			}
			return noteSprites[noteId];
		}
	}
}
