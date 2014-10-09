package com.madgeargames.copista.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.madgeargames.copista.Copista;

public class GameOverScreen extends BaseScreen {

	private static int musicCopyistsPoints = 0;
	private static int sequenceMemoryPoints = 0;
	private static int pitchIdentificationPoints = 0;
	private static int speedPoints = 0;
	private String text;
	private static int winner = -1;
	private String winnerString;

	public GameOverScreen() {
		Results results = new Results();
		stage.addActor(results);
	}

	public static int getSequenceMemoryPoints() {
		return sequenceMemoryPoints;
	}

	public static void setSequenceMemoryPoints(int sequenceMemoryPts) {
		sequenceMemoryPoints = sequenceMemoryPts;
		musicCopyistsPoints = sequenceMemoryPoints + pitchIdentificationPoints + speedPoints;
	}

	public static int getPitchIdentificationPoints() {
		return pitchIdentificationPoints;
	}

	public static void setWinner(int player) {
		winner = player;
	}

	public static void setPitchIdentificationPoints(int pitchIdentificationPts) {
		pitchIdentificationPoints = pitchIdentificationPts;
		musicCopyistsPoints = sequenceMemoryPoints + pitchIdentificationPoints + speedPoints;
	}

	public static int getSpeedPoints() {
		return speedPoints;
	}

	public static void setSpeedPoints(int speedPts) {
		speedPoints = speedPts;
		musicCopyistsPoints = sequenceMemoryPoints + pitchIdentificationPoints + speedPoints;
	}

	@Override
	protected void onPressBack() {
		onPressOk();
	}

	@Override
	protected void onPressOk() {
		System.out.println("Ok pressed in gameOverScreen");
		Copista.getInstance().setScreen(Copista.getInstance().gameModeScreen);
	}

	@Override
	public void show() {
		if (winner != -1) {
			this.winnerString = "PLAYER " + winner + " WINS!\n";
		} else {
			this.winnerString = "\n";
		}
		this.text = winnerString + "Sequence memory \t" + sequenceMemoryPoints
				+ "pts.\nPitch identification \t" + pitchIdentificationPoints
				+ "pts.\nListening speed \t" + speedPoints + "pts.\n\nMusic copyist hability \t"
				+ musicCopyistsPoints + "pts.";
		Copista.charactersSet.characters[Copista.maestroIndex].playMusic();
		super.show();
	}

	@Override
	public void hide() {
		winner = -1;
		super.hide();
	}

	private class Results extends Actor {
		BitmapFont font;

		public Results() {
			font = Copista.font;
			font.setColor(Color.GRAY);
			font.setScale(0.25f);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			font.drawMultiLine(batch, text, 280, 270, 200, HAlignment.RIGHT);
		}

	}

}
