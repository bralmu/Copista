package com.madgeargames.copista.screens;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
	protected Set<TouchablePoint> touchablePoints;
	protected TouchVisualizer touchVisualizer;
	int[] p0keys = Copista.p0keys;
	int[] p1keys = Copista.p1keys;

	public BaseScreen() {
		this.stage = new Stage(new ScreenViewport());
		this.controllersStage = new Stage(new ScreenViewport());
		touchVisualizer = new TouchVisualizer();
		touchablePoints = new HashSet<TouchablePoint>();
		// this.controllersStage.addActor(touchVisualizer);
		Gdx.input.setInputProcessor(this.controllersStage);

		// override android back key behavior
		Gdx.input.setCatchBackKey(true);

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

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				float fixedY = y;
				float fixedX = x;
				touchVisualizer.addPoint(fixedX, fixedY);
				String touchablePointId = findTouchablePoint((int) x, (int) y);
				if (touchablePointId != null) {
					onTouchDown(touchablePointId);
				}
				return false;
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

	protected void onTouchDown(String touchablePointId) {

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
		this.controllersStage.act(delta);
		this.stage.draw();
		this.controllersStage.draw();

	}

	@Override
	public void resize(int width, int height) {
		// evita que se amplíe el campo de visión.
		this.stage.setViewport(new FitViewport(640, 360, this.stage.getCamera()));
		this.controllersStage.setViewport(new FitViewport(640, 360, this.controllersStage
				.getCamera()));
		// evita que se deforme el aspect ratio.
		this.stage.getViewport().update(width, height, true);
		this.controllersStage.getViewport().update(width, height, true);
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

	private String findTouchablePoint(int x, int y) {
		String id = null;
		float distance = Float.MAX_VALUE;
		for (TouchablePoint tp : touchablePoints) {
			Vector2 touch = new Vector2(x, y);
			Vector2 point = new Vector2(tp.x, tp.y);
			float dst = touch.dst(point);
			if (dst < distance && dst < tp.maxDistance) {
				id = tp.id;
				distance = dst;
			}
		}
		return id;
	}

	class TouchablePoint {
		public int x, y, maxDistance;
		public String id;

		public TouchablePoint(int x, int y, int maxDistance, String id) {
			this.x = x;
			this.y = y;
			this.maxDistance = maxDistance;
			this.id = id;
		}
	}

	private class TouchVisualizer extends Actor {
		Pixmap pixmap = new Pixmap(640, 360, Pixmap.Format.RGBA8888);
		Sprite sprite;
		int lastTouchablePointsSize = 0;

		public void addPoint(float x, float y) {
			System.out.println("drawing at " + (int) x + " " + (int) y);
			pixmap.setColor(Color.MAGENTA);
			pixmap.fillCircle((int) x, 360 - (int) y, 10);
			sprite = new Sprite(new Texture(pixmap));
		}

		private void drawTouchablePoints() {
			pixmap.dispose();
			pixmap = new Pixmap(640, 360, Pixmap.Format.RGBA8888);
			pixmap.setColor(new Color(.3f, .8f, .3f, .5f));
			for (TouchablePoint tp : touchablePoints) {
				pixmap.fillCircle(tp.x, 360 - tp.y, tp.maxDistance);
			}
			sprite = new Sprite(new Texture(pixmap));
		}

		@Override
		public void act(float delta) {
			if (touchablePoints.size() != lastTouchablePointsSize) {
				this.lastTouchablePointsSize = touchablePoints.size();
				drawTouchablePoints();
			}
			super.act(delta);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			if (sprite != null) {
				sprite.draw(batch, parentAlpha);
			}
		}
	}

}
