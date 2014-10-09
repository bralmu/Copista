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
	private final String[] texts = { "-> Practice <-\nTwo players\nHelp",
			"Practice\n-> Two players <-\nHelp", "Practice\nTwo players\n-> Help <-" };
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
	}

	@Override
	protected void onPressBack() {
		Gdx.app.exit();
	}

	@Override
	protected void onPressOk() {
		switch (selectedOption) {
		case 0:
			BattleScreen.resetGame = true;
			BattleScreen.twoPlayers = false;
			Copista.getInstance().setScreen(Copista.getInstance().battleScreen);
			break;
		case 1:
			BattleScreen.resetGame = true;
			BattleScreen.twoPlayers = true;
			Copista.getInstance().setScreen(Copista.getInstance().battleScreen);
			break;
		default:
			Copista.getInstance().setScreen(Copista.getInstance().helpScreen);
		}
	}

	@Override
	public void show() {
		Copista.charactersSet.characters[Copista.maestroIndex].playMusic();
		super.show();
	}

	@Override
	protected void onPressUp() {
		if (selectedOption == 0) {
			selectedOption = 2;
		} else {
			selectedOption = (selectedOption - 1) % 3;
		}
	}

	@Override
	protected void onPressDown() {
		selectedOption = (selectedOption + 1) % 3;
	}

	@Override
	protected void onPressRight() {
		onPressOk();
	}

	public class Menu extends Actor {
		BitmapFont font;

		public Menu() {
			font = Copista.font;// new BitmapFont();
			font.setColor(Color.BLACK);
			font.setScale(0.25f);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
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
			font.drawMultiLine(batch, "v0.16 Mad Gear Games 2014. Created by BrunoXe. ", 230, 20,
					200, HAlignment.CENTER);
		}
	}

}
