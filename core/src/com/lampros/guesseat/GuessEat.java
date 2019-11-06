package com.lampros.guesseat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.lampros.guesseat.Screens.MainMenuScreen;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class GuessEat extends Game {


	//Virtual Screen size and Box2d Scale(Pixels Per Meter)
	public static final float PPM = 100;

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;

	public static final String TITLE = "Guess Eat"; //test
	public static final int SCALE = 3; //test


	/*
	Box2D Collision Bits
	Box2D categories are bit fields (coded as shorts, thus on 16 bit)
	Possible categories are powers of 2
	 */
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short MY_CHARACTER_BIT = 2;
	public static final short FRUIT_BIT = 4;
	public static final short OBSTACLE_BIT = 8;
	public static final short OGRE_BIT = 16;
	public static final short APPLEBALL_BIT = 32;
	public static final short WALLS_BIT = 64;
	public static final short WIZARD_BIT = 128;
	public static final short WOGOL_BIT = 256;
	public static final short ADVANCE_LEVEL_BIT = 512;
	public static final short FLASK_BIT = 1024;
	public static final short MY_CHARACTER_FEET = 2048;
	public static final short ADVANCE_LEVEL2_BIT = 4096;
	public static final short OBSTACLE_BOSS_BIT = 8192;
	public static final short MY_CHARACTER_BOSS_STAGE_BIT= 16384;
	public static final short MOVING_PLATFORM_BIT= 256;
	public static final short LOOT_BIT= 16384; //32768

	//A container that holds all the images and textures and used to draw them in the screen
    //public static SpriteBatch batch;
	public static SpriteBatch batch;

	public static AssetManager manager;
	//private Controller controller;

	//test main screen
	public BitmapFont font;

	//testing pausing state

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		//manager.load("audio/music/Triton.mp3", Music.class);
		manager.load("audio/sounds/apple_bite.wav", Sound.class);
		manager.load("audio/sounds/dinosaur_scream.mp3", Sound.class);
		manager.load("audio/sounds/gameover.mp3", Sound.class);
		manager.load("audio/sounds/dying.mp3", Sound.class);
		manager.load("audio/sounds/main_menu_button_clicking.mp3", Sound.class);
		manager.load("audio/music/main_menu.mp3", Music.class);
		manager.load("audio/music/playscreen.mp3", Music.class);
		manager.load("audio/music/secondstage.mp3", Music.class);
		manager.load("audio/sounds/enemy_collision.mp3", Sound.class);
		manager.load("audio/sounds/evolve.mp3", Sound.class);
		manager.load("audio/music/squelch.mp3", Music.class);
		manager.load("audio/music/enemy.mp3", Music.class);
		manager.load("audio/sounds/splash.mp3", Sound.class);
		manager.load("audio/sounds/pick_up_health.mp3", Sound.class);
		manager.load("audio/sounds/ghost_dying.mp3", Sound.class);
		manager.load("audio/music/congratulations.mp3", Music.class);
		//manager.load("audio/sounds/Zombie In Pain-SoundBible.com-134322253.mp3", Sound.class);
		//manager.load("audio/sounds/Zombie Long Death-SoundBible.com-554299929.mp3", Sound.class);

		manager.finishLoading();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
		//setScreen(new PlayScreen(this));

	}


	@Override
	public void render () {
		//gsm.update(Gdx.graphics.getDeltaTime()); //test
		//gsm.render(batch); //test

		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/

		//Delegates the render method to whatever screen is active at the time
		super.render();

	}
	
	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
		font.dispose();
		Gdx.app.log("GuessEat","Got disposed");

	}
}
