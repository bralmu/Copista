package com.madgeargames.copista.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.madgeargames.copista.Copista;

public class BaseScreen implements Screen {

	private boolean fullscreen = false;
	protected Stage stage;
	protected Stage controllersStage;
	int[] p0keys = Copista.p0keys;
	int[] p1keys = Copista.p1keys;

	public BaseScreen() {
		this.stage = new Stage(new ScreenViewport());
		this.controllersStage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(this.controllersStage);
		this.controllersStage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
					BaseScreen.this.onPressBack();
				}
				if (keycode == Keys.ENTER) {
					BaseScreen.this.onPressOk();
				}
				if (keycode == Keys.DPAD_UP) {
					BaseScreen.this.onPressUp();
				}
				if (keycode == Keys.DPAD_DOWN) {
					BaseScreen.this.onPressDown();
				}
				if (keycode == Keys.DPAD_RIGHT) {
					BaseScreen.this.onPressRight();
				}
				for (int i = 0; i < p1keys.length; i++) {
					if (p0keys[i] == keycode) {
						onPressKey(0, i);
					} else if (p1keys[i] == keycode) {
						onPressKey(1, i);
					}
				}
				if (keycode == Keys.F) {
					setFullscreen();
				}
				return super.keyDown(event, keycode);
			}
		});
	}

	protected void onPressBack() {
	}

	protected void onPressOk() {
	}

	protected void onPressUp() {
	}

	protected void onPressDown() {
	}

	protected void onPressRight() {
	}

	protected void onPressKey(int player, int keyIndex) {

	}

	private void setFullscreen() {
		this.fullscreen = !this.fullscreen;
		if (fullscreen) {
			DisplayMode currentMode = Gdx.graphics.getDesktopDisplayMode();
			Gdx.graphics.setDisplayMode(currentMode.width, currentMode.height, true);
		} else {
			Gdx.graphics.setDisplayMode(640, 360, false);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(delta);
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// evita que se amplíe el campo de visión.
		this.stage.setViewport(new FitViewport(640, 360, this.stage.getCamera()));
		// evita que se deforme el aspect ratio.
		this.stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this.controllersStage);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		this.stage.dispose();
	}

}
