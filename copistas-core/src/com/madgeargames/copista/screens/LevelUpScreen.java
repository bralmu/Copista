package com.madgeargames.copista.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madgeargames.copista.Copista;
import com.madgeargames.copista.notes.NoteSet;
import com.madgeargames.copista.notes.NoteSetGenerator;

public class LevelUpScreen extends BaseScreen {

	static int playerId = -1;
	boolean wait = false;
	float waitCountDown;
	final float waitTime = 1.5f;
	Metronome metronome;

	public LevelUpScreen() {
		// colors
		stage.addActor(new Colors());
		// sequences
		stage.addActor(new Sequences());
		// texts
		stage.addActor(new Texts());
		// waiter
		stage.addActor(new Waiter());
		// metronome
		metronome = new Metronome();
		metronome.setPosition(427 + (213 - 160) / 2, (360 - 300) / 2);
		stage.addActor(metronome);

	}

	public static void setPlayerId(int id) {
		playerId = id;
	}

	@Override
	protected void onPressKey(int player, int keyIndex) {
		if (player == playerId && !wait) {
			if (keyIndex == 0) {
				if (BattleScreen.currentNoteSet.size() < 8) {
					BattleScreen.increaseNoteSet();
					Copista.getInstance().setScreen(Copista.getInstance().battleScreen);
				}
			} else if (keyIndex == 1) {
				BattleScreen.increaseSequenceLenght();
				Copista.getInstance().setScreen(Copista.getInstance().battleScreen);
			} else if (keyIndex == 2) {
				BattleScreen.increaseSpeed();
				metronome.increaseSpeed();
				Copista.getInstance().setScreen(Copista.getInstance().battleScreen);
			}
		}
	}

	@Override
	protected void onPressBack() {
		Copista.getInstance().setScreen(Copista.getInstance().gameModeScreen);
	}

	@Override
	public void show() {
		wait = true;
		waitCountDown = waitTime;
		super.show();
	}

	private class Colors extends Actor {
		Sprite[] sprites = new Sprite[6];
		int height = 360;
		int width = 215;
		float time = 0;

		public Colors() {
			generateSprites();
		}

		@Override
		public void act(float delta) {
			time += delta;
			super.act(delta);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			int i = (int) (time) % 6;
			sprites[i].draw(batch, parentAlpha);
		}

		private void generateSprites() {
			NoteSet noteset = NoteSetGenerator.generateNoteSet(2);
			for (int i = 2; i < 8; i++) {
				sprites[i - 2] = generateSprite(noteset);
				noteset = NoteSetGenerator.increaseNoteSet(noteset);
			}
		}

		private Sprite generateSprite(NoteSet noteset) {
			Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
			for (int i = 0; i < noteset.size(); i++) {
				pixmap.setColor(noteset.getNote(i).getColor());
				pixmap.fillRectangle(((width / noteset.size()) + 1) * i, 0, width / noteset.size(),
						height);
			}
			Texture texture = new Texture(pixmap);
			pixmap.dispose();
			return new Sprite(texture);
		}

	}

	private class Sequences extends Actor {
		Image[] sequences = new Image[2];

		public Sequences() {
			sequences[0] = new Image(new Texture("levelupsequence.png"));
			sequences[1] = new Image(new Texture("levelupsequence.png"));
			sequences[0].addAction(Actions.forever(Actions.sequence(Actions.moveTo(0, 360, 2),
					Actions.alpha(0f), Actions.moveTo(0, -360, 0.1f), Actions.alpha(1f),
					Actions.delay(.33f), Actions.delay(2f))));
			sequences[1].addAction(Actions.forever(Actions.sequence(Actions.alpha(0f),
					Actions.moveTo(0, -360, 0.1f), Actions.alpha(1f), Actions.delay(.33f),
					Actions.moveTo(0, 360, 2), Actions.delay(2f))));
		}

		@Override
		public void act(float delta) {
			for (Image i : sequences) {
				i.act(delta);
			}
			super.act(delta);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			for (Image i : sequences) {
				i.draw(batch, parentAlpha);
			}
		}

	}

	private class Metronome extends Actor {
		Image bar, body;
		Group group = new Group();
		float metrobarRotation = 60;
		float metrobarRotationPeriod = 1f;

		public Metronome() {
			Pixmap pixmap = new Pixmap(24, 200, Format.RGBA8888);
			// bar
			pixmap.setColor(Color.BLACK);
			pixmap.fillRectangle(6, 0, 11, 200);
			pixmap.setColor(Color.LIGHT_GRAY);
			pixmap.fillRectangle(0, 0, 24, 30);
			Texture texture = new Texture(pixmap);
			pixmap.dispose();
			bar = new Image(texture);
			bar.setPosition(80 - 12, 90);
			bar.setOrigin(12, 0);
			bar.rotateBy(metrobarRotation / 2);
			bar.addAction(Actions.forever(Actions.sequence(
					Actions.rotateBy(-metrobarRotation, metrobarRotationPeriod),
					Actions.rotateBy(metrobarRotation, metrobarRotationPeriod))));
			// body
			pixmap = new Pixmap(160, 300, Format.RGBA8888);
			pixmap.setColor(Color.MAROON);
			pixmap.fillRectangle(0, 200, 160, 300);
			pixmap.setColor(Color.DARK_GRAY);
			pixmap.fillTriangle(0, 200, 80, 0, 160, 200);
			pixmap.setColor(Color.BLACK);
			pixmap.fillCircle(80, 210, 12);
			pixmap.setColor(Color.YELLOW);
			for (int i = 0; i < 13; i++) {
				int y = (int) (50 + i * (5 + i / 2f));
				pixmap.drawLine(70 - i * 2, y, 90 + i * 2, y);
			}
			texture = new Texture(pixmap);
			pixmap.dispose();
			body = new Image(texture);
			// group
			group.addActor(body);
			group.addActor(bar);
		}

		private void increaseSpeed() {
			if (metrobarRotationPeriod > .2f) {
				metrobarRotationPeriod -= .1f;
			} else {
				metrobarRotationPeriod *= .67f;
			}
			System.out.println("Increasing speed to " + metrobarRotationPeriod);
			Pixmap pixmap = new Pixmap(24, 200, Format.RGBA8888);
			pixmap.setColor(Color.BLACK);
			pixmap.fillRectangle(6, 0, 11, 200);
			pixmap.setColor(Color.LIGHT_GRAY);
			pixmap.fillRectangle(0, (int) ((1 - metrobarRotationPeriod) * 180), 24, 30);
			Texture texture = new Texture(pixmap);
			pixmap.dispose();
			bar = new Image(texture);
			bar.setPosition(80 - 12, 90);
			bar.setOrigin(12, 0);
			bar.rotateBy(metrobarRotation / 2);
			bar.addAction(Actions.forever(Actions.sequence(
					Actions.rotateBy(-metrobarRotation, metrobarRotationPeriod),
					Actions.rotateBy(metrobarRotation, metrobarRotationPeriod))));
			group.clear();
			group.addActor(body);
			group.addActor(bar);
		}

		@Override
		public void setPosition(float x, float y) {
			group.setPosition(x, y);
		}

		@Override
		public void act(float delta) {
			group.act(delta);
			super.act(delta);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			group.draw(batch, parentAlpha);
		}
	}

	private class Texts extends Actor {
		BitmapFont font;
		String[] texts = new String[] { "Richer", "Longer", "Faster" };
		String playerkeys;

		public Texts() {
			font = Copista.font;
			font.setColor(Color.BLACK);
			font.setScale(1);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			font.setColor(Color.WHITE);
			font.drawMultiLine(batch, texts[0], -10, 355, 150, HAlignment.RIGHT);
			font.setColor(new Color(.5f, .5f, .5f, 1f));
			font.drawMultiLine(batch, texts[1], 205, 355, 150, HAlignment.RIGHT);
			font.setColor(Color.BLACK);
			font.drawMultiLine(batch, texts[2], 420, 355, 150, HAlignment.RIGHT);
			if (!wait) {
				font.setColor(Color.BLACK);
				font.drawMultiLine(batch, "PLAYER " + (playerId + 1) + "\nCHOOSES\n" + playerkeys,
						245, 100, 150, HAlignment.CENTER);
			}
		}

		@Override
		public void act(float delta) {
			playerkeys = "( ";
			for (int i = 0; i < 3; i++) {
				if (playerId == 0)
					playerkeys += Copista.p0keysName[i] + " ";
				else
					playerkeys += Copista.p1keysName[i] + " ";
			}
			playerkeys += ")";
		}

	}

	private class Waiter extends Actor {
		@Override
		public void act(float delta) {
			if (wait) {
				waitCountDown -= delta;
				if (waitCountDown < 0) {
					wait = false;
				}
			}
			super.act(delta);
		}
	}
}
