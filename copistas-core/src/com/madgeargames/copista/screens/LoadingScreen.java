package com.madgeargames.copista.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madgeargames.copista.Copista;

public class LoadingScreen extends BaseScreen {
	public LoadingScreen() {
		Image logo = new Image(new Texture("madgear.png"));
		logo.setCenterPosition(320, 180);
		SequenceAction shared = Actions.sequence(Actions.delay(2f), Actions.run(new Runnable() {

			@Override
			public void run() {
				Copista.getInstance().load2();
				Copista.charactersSet.characters[Copista.maestroIndex].playMusic();
				Copista.getInstance().setScreen(Copista.getInstance().introScreen);
			}
		}));
		logo.addAction(shared);
		this.stage.addActor(logo);
		Image red = new Image(new Texture("madgearR.png"));
		red.setCenterPosition(187, 243);
		red.setOriginX(red.getWidth() / 2);
		red.setOriginY(red.getHeight() / 2);
		red.addAction(Actions.forever(Actions.sequence(Actions.rotateBy(-10), Actions.delay(.04f))));
		stage.addActor(red);
		Image black = new Image(new Texture("madgearB.png"));
		black.setCenterPosition(103, 158);
		black.setOriginX(black.getWidth() / 2);
		black.setOriginY(black.getHeight() / 2);
		black.addAction(Actions.forever(Actions.sequence(Actions.rotateBy(5), Actions.delay(.04f))));
		stage.addActor(black);
	}
}
