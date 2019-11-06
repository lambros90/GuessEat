package com.lampros.guesseat.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class ControllerBackButton implements Disposable {

    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera cam;
    private boolean backPressed;

    public ControllerBackButton(){
        cam = new OrthographicCamera();
        viewport = new FitViewport(400, 208, cam);
        stage = new Stage(viewport, GuessEat.batch);
        stage.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {

                switch(keycode){
                    case Input.Keys.ESCAPE:
                        backPressed = true;
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {

                switch(keycode){
                    case Input.Keys.ESCAPE:
                        backPressed = false;
                        break;
                }
                return true;
            }
        });
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);
        Image backButton = new Image(new Texture("ControllerArrows/home.png"));
        backButton.setSize(25, 25);
        backButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                backPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backPressed = false;
            }
        });
        table.add(backButton).size(backButton.getWidth(), backButton.getHeight()).pad(187.2f).padTop(170);
    }

    public void draw(){
        stage.draw();
    }

    public boolean pressBack() {
        return backPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        Gdx.app.log("ControllerBackButton","Got disposed");
    }
}
