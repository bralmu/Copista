package com.madgeargames.copista.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madgeargames.copista.Copista;

public class HelpScreen extends BaseScreen {
	public HelpScreen() {
		Image bg = new Image(new Texture("help.png"));
		bg.setPosition(32, 50);
		stage.addActor(bg);
		touchablePoints.add(new TouchablePoint(0, 0, 1000, "anywhere"));
	}

	@Override
	protected void onTouchDown(String touchablePointId) {
		onPressOk();
	}

	@Override
	protected void onPressBack() {
		onPressOk();
	}

	@Override
	protected void onPressOk() {
		Copista.getInstance().setScreen(Copista.getInstance().gameModeScreen);
	}
}
