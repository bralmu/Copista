package com.madgeargames.copista;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.madgeargames.copista.characters.CharactersSet;
import com.madgeargames.copista.screens.BattleScreen;
import com.madgeargames.copista.screens.GameModeScreen;
import com.madgeargames.copista.screens.GameOverScreen;
import com.madgeargames.copista.screens.HelpScreen;
import com.madgeargames.copista.screens.IntroScreen;
import com.madgeargames.copista.screens.LevelUpScreen;
import com.madgeargames.copista.screens.LoadingScreen;

public class Copista extends Game {

	static Copista instance;
	public static BitmapFont font;
	public static CharactersSet charactersSet;
	public static int maestroIndex;
	public Screen introScreen, gameModeScreen, battleScreen, levelUpScreen, gameOverScreen,
			helpScreen, loadingScreen;
	public static int[] p0keys = new int[] { Keys.NUM_1, Keys.NUM_2, Keys.NUM_3, Keys.NUM_4,
			Keys.NUM_5, Keys.NUM_6, Keys.NUM_7 };
	public static String[] p0keysName = new String[] { "1", "2", "3", "4", "5", "6", "7" };
	public static int[] p1keys = new int[] { Keys.Z, Keys.X, Keys.C, Keys.V, Keys.B, Keys.N, Keys.M };
	public static String[] p1keysName = new String[] { "Z", "X", "C", "V", "B", "N", "M" };

	@Override
	public void create() {
		instance = this;
		charactersSet = new CharactersSet();
		maestroIndex = (int) (Math.random() * 3);
		loadingScreen = new LoadingScreen();
		load2();
		this.setScreen(levelUpScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	public void load2() {
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("vSHandprinted.otf"));
		FreeTypeFontParameter fontparams = new FreeTypeFontParameter();
		fontparams.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
		fontparams.size = 64;
		font = gen.generateFont(fontparams);
		introScreen = new IntroScreen();
		gameModeScreen = new GameModeScreen();
		battleScreen = new BattleScreen();
		levelUpScreen = new LevelUpScreen();
		gameOverScreen = new GameOverScreen();
		helpScreen = new HelpScreen();
	}

	public static Copista getInstance() {
		return instance;
	}

	public static CharactersSet getCharacterSet() {
		return charactersSet;
	}

}