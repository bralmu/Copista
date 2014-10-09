package com.madgeargames.copista.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MarcadorPuntuacion extends Actor {
	float value;
	float x;
	Sprite bar;

	public MarcadorPuntuacion() {
		setValue(100);
	}

	@Override
	public void act(float delta) {

		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		bar.draw(batch, parentAlpha);
	}

	public void setValue(float value) {
		int height = (int) (value * 3.6f);
		int r, g;
		if (value > 50) {
			g = 255;
			r = 255 - ((int) value - 50) * (255 / 50);
		} else {
			r = 255;
			g = 255 - (50 - (int) value) * (255 / 50);
		}
		Pixmap pixmap = new Pixmap(20, 360, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fillRectangle(0, 0, 20, 360);
		pixmap.setColor(new Color((float) r / 255, (float) g / 255, 0f, 1f));
		pixmap.fillRectangle(0, 360 - height, 20, height);
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		bar = new Sprite(texture);
		bar.setX(x);
		this.value = value;
	}

	@Override
	public void setX(float x) {
		this.x = x;
		bar.setX(x);
	}

	@Override
	public float getWidth() {
		return 20;
	}
}