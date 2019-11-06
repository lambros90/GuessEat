package com.lampros.guesseat.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lampros.guesseat.GuessEat;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class GameOverScreen implements Screen {

    private final GuessEat game;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private Music musicSecondStage;
    private Music musicMyBoss;
    private Music musicPlayscreen;
    private Sound soundDying;
    private Sound soundScream;
    private Sound soundGhostDying;
    private Sound soundApple;
    private Sound soundZap;
    private Sound soundEvolve;
    private Sound soundSplash;
    private Sound soundLife;
    private Sound soundGameOver;
    private Texture backgroundImg;
    private Texture gameoverImg;

    public GameOverScreen(GuessEat game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 208);
        viewport = new FillViewport(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((GuessEat) game).batch);
        musicPlayscreen = GuessEat.manager.get("audio/music/playscreen.mp3", Music.class);
        musicPlayscreen.stop();
        musicSecondStage = GuessEat.manager.get("audio/music/secondstage.mp3", Music.class);
        musicSecondStage.stop();
        musicMyBoss = GuessEat.manager.get("audio/music/enemy.mp3", Music.class);
        musicMyBoss.stop();
        soundDying = GuessEat.manager.get("audio/sounds/dying.mp3", Sound.class);
        soundDying.stop();
        soundGhostDying = GuessEat.manager.get("audio/sounds/ghost_dying.mp3", Sound.class);
        soundGhostDying.stop();
        soundScream = GuessEat.manager.get("audio/sounds/dinosaur_scream.mp3", Sound.class);
        soundScream.stop();
        soundApple = GuessEat.manager.get("audio/sounds/apple_bite.wav", Sound.class);
        soundApple.stop();
        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
        soundZap.stop();
        soundEvolve = GuessEat.manager.get("audio/sounds/evolve.mp3", Sound.class);
        soundEvolve.stop();
        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
        soundSplash.stop();
        soundLife = GuessEat.manager.get("audio/sounds/pick_up_health.mp3", Sound.class);
        soundLife.stop();
        soundGameOver = GuessEat.manager.get("audio/sounds/gameover.mp3", Sound.class);
        soundGameOver.play();

        Pixmap pixmap200 = new Pixmap(Gdx.files.internal("mainMenuScreen/bg.jpg"));
        Pixmap pixmap100 = new Pixmap(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, pixmap200.getFormat());
        pixmap100.drawPixmap(pixmap200,
                0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
                0, 0, pixmap100.getWidth(), pixmap100.getHeight()
        );
        //pixmap100.dispose();
        //pixmap200.dispose();

        backgroundImg = new Texture(pixmap100);
        gameoverImg = new Texture("mainMenuScreen/gameover.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen((GuessEat) game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(backgroundImg, 0, 0);
        game.batch.draw(gameoverImg, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        //musicPlayscreen.dispose(); if disposed android doesnt load it
        //musicMyBoss.dispose(); if disposed android doesnt load it
        //musicSecondStage.dispose(); if disposed android doesnt load it
        Gdx.app.log("GameOverScreen","Got Disposed");
    }
}
