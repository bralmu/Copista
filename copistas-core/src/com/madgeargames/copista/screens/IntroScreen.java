package com.madgeargames.copista.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madgeargames.copista.Copista;

public class IntroScreen extends BaseScreen {
	public IntroScreen() {
		Image logo = new Image(new Texture("batallamusical2.png"));
		logo.setCenterPosition(320, 180);
		logo.setColor(1, 1, 1, 0);
		logo.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.delay(2f), Actions.fadeOut(2f),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						Copista.getInstance().setScreen(Copista.getInstance().gameModeScreen);
					}
				})));
		this.stage.addActor(logo);

	}

	@Override
	public void show() {

	}
}
