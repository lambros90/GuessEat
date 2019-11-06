package com.lampros.guesseat.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lampros.guesseat.GuessEat;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class CongratulationsScreen implements Screen {

    private final GuessEat game;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private Music music;
    private Music musicSecondStage;
    private Music musicBoss;
    private Music musicCongratulations;
    private Texture backgroundImg;
    private Texture congratulationsImg;

    public CongratulationsScreen(GuessEat game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 208);
        viewport = new FillViewport(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((GuessEat) game).batch);
        music = GuessEat.manager.get("audio/music/playscreen.mp3", Music.class);
        music.stop();
        musicSecondStage = GuessEat.manager.get("audio/music/secondstage.mp3", Music.class);
        musicSecondStage.stop();
        musicBoss = GuessEat.manager.get("audio/music/enemy.mp3", Music.class);
        musicBoss.stop();
        musicCongratulations = GuessEat.manager.get("audio/music/congratulations.mp3", Music.class);
        musicCongratulations.setLooping(true);
        musicCongratulations.play();
        Pixmap pixmap200 = new Pixmap(Gdx.files.internal("mainMenuScreen/bg.jpg"));
        Pixmap pixmap100 = new Pixmap(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, pixmap200.getFormat());
        pixmap100.drawPixmap(pixmap200,
                0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
                0, 0, pixmap100.getWidth(), pixmap100.getHeight()
        );
        backgroundImg = new Texture(pixmap100);
        congratulationsImg = new Texture("mainMenuScreen/congratulations.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(backgroundImg, 0, 0);
        game.batch.draw(congratulationsImg, 0, 0, camera.viewportWidth, camera.viewportHeight);
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
        Gdx.app.log("CongratulationsScreen","Got disposed");
    }
}
