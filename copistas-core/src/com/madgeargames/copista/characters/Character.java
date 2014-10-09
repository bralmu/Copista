package com.madgeargames.copista.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madgeargames.copista.sequences.Sequence;

/**
 * Future improvements: images for states (normal, pass, fail, playing,
 * listening)
 * 
 * @author MadGearGames
 * 
 */
public class Character extends Actor {
	private final String name;
	private final Sequence tune;
	private final String fileBaseName;
	private final String description;
	private Image image;
	private final float widthSmall = 130;
	private final float heightSmall = 150;
	private final float widthBig = 640;
	private final float heightBig = 150;
	private boolean isHighlighted = false;
	Sound music;
	boolean musicPlaying = false;

	public Character(String name, Sequence tune, String fileBaseName, String description) {
		super();
		this.name = name;
		this.tune = tune;
		this.fileBaseName = fileBaseName;
		this.description = description;
		image = new Image(new Texture(Gdx.files.internal(fileBaseName + ".png")));
		small();
	}

	@Override
	public String getName() {
		return name;
	}

	public Sequence getTune() {
		return tune;
	}

	public String getfileBaseName() {
		return fileBaseName;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public void act(float delta) {
		image.act(delta);
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		image.draw(batch, parentAlpha);
	}

	@Override
	public void setCenterPosition(float x, float y) {
		image.setCenterPosition(x, y);
	}

	public void highlight() {
		image.setColor(1, 1, 1, 1f);
		this.isHighlighted = true;
	}

	public boolean isHighlighted() {
		return this.isHighlighted;
	}

	public void conceal() {
		image.setColor(1f, 1f, 1f, .05f);
	}

	public void big() {
		image.setWidth(widthBig);
		image.setHeight(heightBig);
		this.setWidth(image.getWidth());
		this.setHeight(image.getHeight());
	}

	public void small() {
		image.setWidth(widthSmall);
		image.setHeight(heightSmall);
		this.setWidth(image.getWidth());
		this.setHeight(image.getHeight());
	}

	public void moodBase() {
		float xCenter = image.getCenterX();
		float yCenter = image.getCenterY();
		image = new Image(new Texture(fileBaseName + ".png"));
		image.setCenterPosition(xCenter, yCenter);
	}

	public void moodHappy() {
		float xCenter = image.getCenterX();
		float yCenter = image.getCenterY();
		image = new Image(new Texture(fileBaseName + "Happy.png"));
		image.setCenterPosition(xCenter, yCenter);
	}

	public void moodSad() {
		float xCenter = image.getCenterX();
		float yCenter = image.getCenterY();
		image = new Image(new Texture(fileBaseName + "Sad.png"));
		image.setCenterPosition(xCenter, yCenter);
	}

	public void playMusic() {
		if (music == null) {
			music = Gdx.audio.newSound(Gdx.files.internal(fileBaseName + "1.ogg"));
		}
		if (!musicPlaying) {
			musicPlaying = true;
			music.loop(.2f);
		}
	}

	public void stopMusic() {
		if (musicPlaying) {
			music.stop();
			musicPlaying = false;
		}
	}
}
