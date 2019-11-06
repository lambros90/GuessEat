package com.lampros.guesseat.Controllers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
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

public class Controller implements Disposable {

    private Viewport viewport;
    private Stage stage;
    private boolean leftPressed, rightPressed, pausePressed, homePressed;
    private OrthographicCamera cam;
    private boolean upPressed;
    private  boolean isPause;
    private Group pauseGroup;
    private Music musicPlayscreen;
    private Sound soundApple;
    private Sound soundScream;
    private Sound soundZap;
    private Sound soundDying;

    public Controller(){

        upPressed = false;
        cam = new OrthographicCamera();
        viewport = new FitViewport(400, 208, cam);
        stage = new Stage(viewport, GuessEat.batch);
        stage.addListener(new InputListener(){

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = true;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = true;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = true;
                        break;
                    case Input.Keys.ESCAPE:
                        if(!isPause) {
                            homePressed = true;
                        }
                        break;
                    case Input.Keys.BACKSPACE:
                        if(pausePressed){
                            pausePressed = false;
                            resumeGame();
                            soundScream.resume();
                            musicPlayscreen.play();
                            soundApple.resume();
                            soundZap.resume();
                            soundDying.resume();
                        }
                        else {
                            pausePressed = true;
                            pauseGame();
                        }
                        break;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch(keycode){
                    case Input.Keys.UP:
                        upPressed = false;
                        break;
                    case Input.Keys.LEFT:
                        leftPressed = false;
                        break;
                    case Input.Keys.RIGHT:
                        rightPressed = false;
                        break;
                    case Input.Keys.ESCAPE:
                        homePressed = false;
                        break;
                    case Input.Keys.ENTER:
                        break;
                }
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.center().bottom();
        Image upImg = new Image(new Texture("ControllerArrows/arrowUp.png"));
        upImg.setSize(35, 35);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }

        });
        Image rightImg = new Image(new Texture("ControllerArrows/arrowRight.png"));
        rightImg.setSize(35, 35);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }

        });
        Image leftImg = new Image(new Texture("ControllerArrows/arrowLeft.png"));
        leftImg.setSize(35, 35);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }

        });
        Image imgHome = new Image(new Texture("ControllerArrows/home.png"));
        imgHome.setSize(25, 27);
        imgHome.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isPause)
                 homePressed= true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

        });
        Image imgPause = new Image(new Texture("ControllerArrows/pause.png"));
        imgPause.setSize(25, 25);
        imgPause.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(pausePressed){
                    Gdx.app.log("controller touchDown","pause button is clicked");
                }
                else {
                    Gdx.app.log("controller touchDown","pause button is not clicked");
                    pausePressed = true;
                    pauseGame();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

        });
        table.padLeft(400).padBottom(5);
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight()).spaceLeft(30);
        table.add(imgHome).size(imgHome.getWidth(), imgHome.getHeight()).spaceLeft(75);
        table.add(imgPause).size(imgPause.getWidth(), imgPause.getHeight()).spaceLeft(30);
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight()).spaceLeft(95);
        stage.addActor(table);
    }

    private void pauseGame(){
        isPause = true;
        if(pausePressed ) {
            pauseGroup = new Group();
            Image imgResume = new Image(new Texture("ControllerArrows/right.png"));
            imgResume.setSize(50, 50);
            musicPlayscreen = GuessEat.manager.get("audio/music/playscreen.mp3", Music.class);
            musicPlayscreen.pause();
            //System.out.println(musicPlayscreen.getPosition());
            soundScream = GuessEat.manager.get("audio/sounds/dinosaur_scream.mp3", Sound.class);
            soundScream.pause();
            soundApple = GuessEat.manager.get("audio/sounds/apple_bite.wav", Sound.class);
            soundApple.pause();
            soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
            soundZap.pause();
            soundDying = GuessEat.manager.get("audio/sounds/dying.mp3", Sound.class);
            soundDying.pause();
            Gdx.app.log("controller touchDown-pauseGroup","resume button is active");
            if(Gdx.app.getType() == Application.ApplicationType.Android){

                imgResume.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.app.log("controller touchDown-pauseGroup","resume button is clicked");
                    pausePressed = false;
                    resumeGame();
                    soundScream.resume();
                    soundApple.resume();
                    soundZap.resume();
                    soundDying.resume();
                    musicPlayscreen.play();
                    return true;
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                }
            });}
            pauseGroup.addActor(imgResume);
            pauseGroup.setPosition((stage.getWidth() / 2f) - 25, (stage.getHeight() / 2f) - 25);
            stage.addActor(pauseGroup);
        }
    }

    private void resumeGame(){
        if(isPause){
            isPause = false;
            pauseGroup.remove();
        }
    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public void setUpPressed(boolean value) {
        upPressed = value;
    }

    public boolean isHomePressed() {
        return homePressed;
    }

    @Override
    public void dispose() {
        stage.dispose();
        Gdx.app.log("Controller","Got disposed");
    }

    public  boolean isPause() {
        return isPause;
    }

}
