package com.lampros.guesseat.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Controllers.MainMenuController;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class MainMenuScreen implements Screen {

    private final GuessEat game;
    private OrthographicCamera camera;
    private MainMenuController controller;

    private Music musicMainMenu;
    private Music musicPlayscreen;
    private Music musicSecondStage;
    private Music musicMyBoss;
    private Music musicCongratulations;
    private Sound soundGameOver;
    private Sound soundDying;
    private Sound soundGhostDying;
    private Sound soundButton;
    private Sound soundScream;
    private Sound soundApple;
    private Sound soundZap;
    private Sound soundEvolve;
    private Sound soundSplash;
    private Sound soundLife;

    private Texture bgImg;

    public MainMenuScreen(GuessEat game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 208);
        controller = new MainMenuController();
        soundGameOver = GuessEat.manager.get("audio/sounds/gameover.mp3", Sound.class);
        soundGameOver.stop();
        musicMainMenu = GuessEat.manager.get("audio/music/main_menu.mp3", Music.class);
        musicMainMenu.setLooping(true);
        musicMainMenu.play();
        musicPlayscreen = GuessEat.manager.get("audio/music/playscreen.mp3", Music.class);
        musicPlayscreen.stop();
        musicSecondStage = GuessEat.manager.get("audio/music/secondstage.mp3", Music.class);
        musicSecondStage.stop();
        musicMyBoss = GuessEat.manager.get("audio/music/enemy.mp3", Music.class);
        musicMyBoss.stop();
        musicCongratulations = GuessEat.manager.get("audio/music/congratulations.mp3", Music.class);
        musicCongratulations.stop();
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

        Pixmap pixmap200 = new Pixmap(Gdx.files.internal("mainMenuScreen/bg.jpg"));
        Pixmap pixmap100 = new Pixmap(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, pixmap200.getFormat());
        pixmap100.drawPixmap(pixmap200,
                0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
                0, 0, pixmap100.getWidth(), pixmap100.getHeight()
        );
        bgImg = new Texture(pixmap100);
        pixmap100.dispose();
        pixmap200.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        camera.update();
        game.batch.begin();
        game.batch.draw(bgImg, 0, 0);
        game.batch.end();
        controller.draw();
    }

    public void handleInput(float dt){
        if(controller.pressPlay()){
            Gdx.app.log("", "we are in play game");
            soundButton = GuessEat.manager.get("audio/sounds/main_menu_button_clicking.mp3", Sound.class);
            soundButton.play();
            game.setScreen(new PlayScreen(game));
            dispose();
        }
        if(controller.pressInstructions()){
            Gdx.app.log("", "we are in instuction game");
            soundButton = GuessEat.manager.get("audio/sounds/main_menu_button_clicking.mp3", Sound.class);
            soundButton.play();
            game.setScreen(new InstructionsScreen(game));
            dispose();
        }
        if(controller.pressExit()){
            Gdx.app.log("", "we are in exit game");
            soundButton = GuessEat.manager.get("audio/sounds/main_menu_button_clicking.mp3", Sound.class);
            soundButton.play();
            Gdx.app.exit();
            dispose();
        }
    }
    public void update(float dt) {
        handleInput(dt);
    }
    @Override
    public void resize(int width, int height) {
        controller.resize(width,height);

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
        controller.dispose();
        bgImg.dispose();
        Gdx.app.log("MainMenuScreen","Got disposed");
    }
}
