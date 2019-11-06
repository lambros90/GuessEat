package com.lampros.guesseat.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lampros.guesseat.GuessEat;

/**
 * Based on Brent Aureli on 10/23/15. https://github.com/BrentAureli/ControllerDemo/blob/master/core/src/com/brentaureli/overlaydemo/Controller.java
 */

public class MainMenuController implements Disposable {

    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera cam;
    private boolean instructionsPressed;
    private boolean playPressed;
    private boolean exitPressed;

    public MainMenuController(){
        cam = new OrthographicCamera();
        viewport = new FitViewport(400, 208, cam);
        stage = new Stage(viewport, GuessEat.batch);

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);

        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);

        Image startGameButton = new Image(new Texture("mainMenuScreen/startGame.png"));
        Image instructionsButton = new Image(new Texture("mainMenuScreen/instructions.png"));
        Image exitGameButton = new Image(new Texture("mainMenuScreen/exitGame.png"));
        Image title = new Image(new Texture("mainMenuScreen/title.png"));

        int img_width = 130;
        int img_height = 40;

        startGameButton.setSize(img_width, img_height);
        instructionsButton.setSize(img_width, img_height);
        exitGameButton.setSize(img_width, img_height);

        title.setSize(GuessEat.V_WIDTH-20, 35);


        startGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                playPressed = false;
            }
        });

        instructionsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                instructionsPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                instructionsPressed = false;
            }
        });

        exitGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                exitPressed = false;
            }
        });

        table.add(title).size(title.getWidth(), title.getHeight()).pad(5).padTop(10);
        table.row();
        table.add(startGameButton).size(startGameButton.getWidth(), startGameButton.getHeight()).pad(5);
        table.row();
        table.add(instructionsButton).size(instructionsButton.getWidth(), instructionsButton.getHeight()).pad(5);
        table.row();
        table.add(exitGameButton).size(exitGameButton.getWidth(), exitGameButton.getHeight()).pad(5);
    }

    public void draw(){
        stage.draw();
    }

    public boolean pressInstructions() {
        return instructionsPressed;
    }

    public boolean pressPlay() {
        return playPressed;
    }

    public boolean pressExit() {
        return exitPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        Gdx.app.log("MainMenuController","Got disposed");
    }
}
