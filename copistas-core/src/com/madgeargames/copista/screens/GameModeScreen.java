package com.madgeargames.copista.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madgeargames.copista.Copista;

public class GameModeScreen extends BaseScreen {
	/**
	 * TODO embellecer con music score vertical en derecha e izquierda
	 * desplazándose y notas flotando semialeatoriamente.
	 */
	private final String[] texts = { "-> Practice <-\nVersus\nHelp\nExit",
			"Practice\n-> Versus <-\nHelp\nExit", "Practice\nVersus\n-> Help <-\nExit",
			"Practice\nVersus\nHelp\n-> Exit <-" };
	private int selectedOption = 0;

	public GameModeScreen() {
		Image logo = new Image(new Texture("batallamusical2.png"));
		logo.setCenterPosition(320, 260);
		logo.setScale(1f);
		stage.addActor(logo);
		Menu actor = new Menu();
		stage.addActor(actor);
		Foot foot = new Foot();
		stage.addActor(foot);
		touchablePoints.add(new TouchablePoint(332, 181, 40, "practice"));
		touchablePoints.add(new TouchablePoint(331, 145, 40, "versus"));
		touchablePoints.add(new TouchablePoint(330, 110, 40, "help"));
		touchablePoints.add(new TouchablePoint(330, 68, 40, "exit"));
	}

	@Override
	protected void onPressBack() {
		Gdx.app.exit();
	}

	@Override
	protected void onPressOk() {
		switch (selectedOption) {
		case 0:
			practice();
			break;
		case 1:
			versus();
			break;
		case 2:
			help();
			break;
		default:
			exit();
		}
	}

	private void practice() {
		BattleScreen.resetGame = true;
		BattleScreen.twoPlayers = false;
		Copista.getInstance().setScreen(Copista.getInstance().battleScreen);
	}

	private void versus() {
		BattleScreen.resetGame = true;
		BattleScreen.twoPlayers = true;
		Copista.getInstance().setScreen(Copista.getInstance().battleScreen);
	}

	private void help() {
		Copista.getInstance().setScreen(Copista.getInstance().helpScreen);
	}

	private void exit() {
		Gdx.app.exit();
	}

	@Override
	public void show() {
		// TODO: play music here
		super.show();
	}

	@Override
	protected void onPressUp() {
		if (selectedOption == 0) {
			selectedOption = 3;
		} else {
			selectedOption = (selectedOption - 1) % 4;
		}
	}

	@Override
	protected void onPressDown() {
		selectedOption = (selectedOption + 1) % 4;
	}

	@Override
	protected void onPressRight() {
		onPressOk();
	}

	@Override
	protected void onTouchDown(String touchablePointId) {
		// System.out.println("Point " + touchablePointId + " touched");
		if (touchablePointId == "practice") {
			practice();
		} else if (touchablePointId == "versus") {
			versus();
		} else if (touchablePointId == "help") {
			help();
		} else if (touchablePointId == "exit") {
			exit();
		}
	}

	public class Menu extends Actor {
		BitmapFont font;

		public Menu() {
			font = Copista.font;// new BitmapFont();
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			font.setColor(Color.BLACK);
			font.setScale(.5f);
			font.drawMultiLine(batch, texts[selectedOption], 230, 200, 200, HAlignment.CENTER);
		}
	}

	public class Foot extends Actor {
		BitmapFont font;

		public Foot() {
			font = new BitmapFont();
			font.setColor(Color.GRAY);
			font.setScale(.67f);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			font.drawMultiLine(
					batch,
					"v0.17 Mad Gear Games 2014. Created by BrunoXe. Grundschrift font by Christian Urff.",
					230, 20, 200, HAlignment.CENTER);
		}
	}

}
