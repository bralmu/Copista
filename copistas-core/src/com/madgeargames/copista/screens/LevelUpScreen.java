package com.madgeargames.copista.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madgeargames.copista.Copista;

public class LevelUpScreen extends BaseScreen {

	static int playerId = -1;
	float metrobarRotation = 45;
	float metrobarRotationPeriod = .667f;
	boolean wait = false;
	float waitCountDown;
	final float waitTime = 1.5f;

	public LevelUpScreen() {
		Image bg = new Image(new Texture("levelup.png"));
		bg.setCenterPosition(640 / 2, 360 / 2);
		stage.addActor(bg);

		// metrobar
		Image metrobar = new Image(new Texture("metrobar.png"));
		metrobar.setCenterPosition(640 / 3 * 2 + 640 / 6, 360 / 2);
		metrobar.setOrigin(8, 0);
		metrobar.rotateBy(metrobarRotation / 2);
		metrobar.addAction(Actions.forever(Actions.sequence(
				Actions.rotateBy(-metrobarRotation, metrobarRotationPeriod),
				Actions.rotateBy(metrobarRotation, metrobarRotationPeriod))));
		stage.addActor(metrobar);
		// colors
		stage.addActor(new Colors());
		// sequences
		stage.addActor(new Sequences());
		// texts
		stage.addActor(new Texts());
		// waiter
		stage.addActor(new Waiter());

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
		Image[] colors = new Image[4];
		float time = 0;

		public Colors() {
			colors[0] = new Image(new Texture(Gdx.files.internal("levelupcolors3.png")));
			colors[1] = new Image(new Texture(Gdx.files.internal("levelupcolors2.png")));
			colors[2] = new Image(new Texture(Gdx.files.internal("levelupcolors1.png")));
			colors[3] = new Image(new Texture(Gdx.files.internal("levelupcolors0.png")));
		}

		@Override
		public void act(float delta) {
			time += delta;
			super.act(delta);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			int i = (int) (time / 2) % 4;
			colors[i].draw(batch, parentAlpha);
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
			font.drawMultiLine(batch, texts[0], -3, 355, 150, HAlignment.RIGHT);
			font.setColor(new Color(.9f, .9f, .9f, 1f));
			font.drawMultiLine(batch, texts[1], 217, 355, 150, HAlignment.RIGHT);
			font.setColor(Color.WHITE);
			font.drawMultiLine(batch, texts[2], 425, 355, 150, HAlignment.RIGHT);
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
