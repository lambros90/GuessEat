package com.lampros.guesseat.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.lampros.guesseat.Controllers.ControllerBackButton;
import com.lampros.guesseat.GuessEat;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class InstructionsScreen implements Screen {

    private final GuessEat game;
    private OrthographicCamera camera;
    private ControllerBackButton controller;
    private Texture instructionsImg;
    private Texture androidInstructions;
    private Texture desktopInstructions;

    public InstructionsScreen(GuessEat game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 208);
        controller = new ControllerBackButton();
        Pixmap pixmap200 = new Pixmap(Gdx.files.internal("mainMenuScreen/bg.jpg"));
        Pixmap pixmap100 = new Pixmap(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, pixmap200.getFormat());
        pixmap100.drawPixmap(pixmap200,
                0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
                0, 0, pixmap100.getWidth(), pixmap100.getHeight()
        );
        instructionsImg = new Texture(pixmap100);
        androidInstructions = new Texture("mainMenuScreen/androidInstructions.png");
        desktopInstructions = new Texture("mainMenuScreen/desktopInstructions.png");
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
        game.batch.draw(instructionsImg, 0, 0);
        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            game.batch.draw(androidInstructions, 0, 0, camera.viewportWidth, camera.viewportHeight);
        }
        else
            game.batch.draw(desktopInstructions, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.batch.end();
        controller.draw();
    }

    private void handleInput(float dt){
        if(controller.pressBack()){
            Gdx.app.log("", "back button was pressed");
            game.setScreen(new MainMenuScreen(game));
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
        Gdx.app.log("InstructionsScreen","Got disposed");
    }
}
