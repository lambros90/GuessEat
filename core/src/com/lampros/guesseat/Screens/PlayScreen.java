package com.lampros.guesseat.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lampros.guesseat.Controllers.Controller;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.Hud;
import com.lampros.guesseat.Sprites.Enemy1stStage;
import com.lampros.guesseat.Sprites.Fruit;
import com.lampros.guesseat.Sprites.MyCharacter;
import com.lampros.guesseat.Tools.B2WorldCreator;
import com.lampros.guesseat.Tools.CollisionListener;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class PlayScreen implements Screen, ApplicationListener {
    private GuessEat game;
    private TextureAtlas atlasApple;
    private TextureAtlas atlasPoints;
    private TextureAtlas atlasOrc;
    private TextureAtlas atlasIdle;
    private TextureAtlas atlasRun;
    private TextureAtlas atlasJump;
    private TextureAtlas atlasDeadDino;
    private OrthographicCamera camera;
    private Viewport gamePort;
    private Hud hud;
    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private Controller controller;
    private MyCharacter player;
    private Integer levelHeight;
    private Music musicMainMenu;
    private Music musicPlayscreen;

    public PlayScreen(GuessEat game) {
        atlasIdle = new TextureAtlas("characters/LittleDino/LittleDinoIdle/LittleDinoIdle.pack");
        atlasRun = new TextureAtlas("characters/LittleDino/LittleDinoRun/LittleDinoRun.pack");
        atlasJump = new TextureAtlas("characters/LittleDino/LittleDinoJump/LittleDinoJump.pack");
        atlasDeadDino = new TextureAtlas("characters/LittleDino/LittleDinoDead/LittleDinoDead.pack");
        atlasApple = new TextureAtlas("fruits/apple/apple_animated.pack");
        atlasPoints = new TextureAtlas("fruits/points/Points.pack");
        atlasOrc = new TextureAtlas("characters/Orc/Orc.pack");
        this.game = game;
        //create cam used to follow dino through cam world
        camera = new OrthographicCamera();
        musicMainMenu = GuessEat.manager.get("audio/music/main_menu.mp3", Music.class);
        musicMainMenu.stop();
        musicPlayscreen = GuessEat.manager.get("audio/music/playscreen.mp3", Music.class);
        musicPlayscreen.setLooping(true);
        musicPlayscreen.play();
        //create a FitViewport to maintain virtual aspect ratio despite changes
        gamePort = new FitViewport(GuessEat.V_WIDTH / GuessEat.PPM, GuessEat.V_HEIGHT / GuessEat.PPM, camera);
        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);
        //Load the map and setup the map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("stage_1/stage_1.tmx");
        MapProperties props = map.getProperties();
        levelHeight = props.get("height", Integer.class);
        renderer = new OrthogonalTiledMapRenderer(map,1 / GuessEat.PPM);
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        player = new MyCharacter(this);
        world.setContactListener(new CollisionListener(game));
        controller = new Controller();
    }

    @Override
    public void show() {

    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
    }

    public void update(float dt) {
        //Handle user input first
        handleInput(dt);
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        for (Fruit fruit : creator.getFruits()) {
            fruit.update(dt);
        }
        for (Enemy1stStage enemy1stStage : creator.getWogols()) {
            enemy1stStage.update(dt);
        }
        hud.update(dt);
        if(Hud.isTimeUp()) {
            game.setScreen(new GameOverScreen(game));
            Hud.setTimeUp(false);
        }
        if(player.currentState != MyCharacter.State.DEAD && player.b2body.getPosition().y > 1 /GuessEat.PPM){
            camera.position.x = (float)Math.round(player.b2body.getPosition().x * 100f)/100f ;
            camera.update();

        }
        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;
        boundary(camera, startX, startY, 3640 / GuessEat.PPM,levelHeight  - startY * 2);
        //Tell the renderer to draw only what the camera can see in the game world
        renderer.setView(camera);
    }

    private void handleInput(float dt){
        if(!player.myCharacterIsDead()) {
            if ((controller.isUpPressed()) && (player.b2body.getLinearVelocity().y == 0)  && Hud.getWorldTimer() < 59) {
                player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
                controller.setUpPressed(false);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) && player.b2body.getLinearVelocity().x <= 2 && Hud.getWorldTimer() < 59)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) && player.b2body.getLinearVelocity().x >= -2 && Hud.getWorldTimer() < 59)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || controller.isHomePressed()){// && !controller.isPause()) {
            Gdx.app.log("playscreen handleinput", "home button was clicked");
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

   /* public void handleInput(float dt){
        if(!player.myCharacterIsDead()) {
            if ((controller.isUpPressed()) && (player.b2body.getLinearVelocity().y == 0)  && Hud.getWorldTimer() < 59) {

                player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
                controller.setUpPressed(false);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) && Hud.getWorldTimer() < 59)
                player.b2body.setLinearVelocity(new Vector2(1, player.b2body.getLinearVelocity().y));
            else if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) && Hud.getWorldTimer() < 59)
                player.b2body.setLinearVelocity(new Vector2(-1, player.b2body.getLinearVelocity().y));
            else
                player.b2body.setLinearVelocity(new Vector2(0, player.b2body.getLinearVelocity().y));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || controller.isHomePressed()){// && !controller.isPause()) {
            Gdx.app.log("playscreen handleinput", "home button was clicked");
            game.setScreen(new MainMenuScreen(game));
        }
       *//* if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || controller.isHomePressed()) {
            game.setScreen(new MainMenuScreen(game));
        }*//*
    }*/


    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle(GuessEat.TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
        if(!controller.isPause()) {
            update(delta);
        }
            //Clear the game screen with Black
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //render the game map
            renderer.render();
            //render the Box2DDebugLines
            b2dr.render(world, camera.combined);
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            player.draw(game.batch);
            for (Fruit fruit : creator.getFruits())
                fruit.draw(game.batch);
            for (Enemy1stStage enemy1stStage : creator.getWogols())
                enemy1stStage.draw(game.batch);
            game.batch.end();
            //set the batch to now draw what the hud camera sees
            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();
            if(Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
            if (gameOver()) {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
            if (gameOver2()) {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
    }

    private boolean gameOver(){
        if(player.currentState == MyCharacter.State.DEAD && player.getStateTimer() > 3) {
            Gdx.app.log("PlayScreen gameOver", "player dies by enemy");
            return true;
        }
        return false;
    }

    private boolean gameOver2(){
        if ((player.b2body.getPosition().y <-1 /GuessEat.PPM && player.getStateTimer() > 2)){
            Hud.removeLives();
            Gdx.app.log("PlayScreen gameOver2", "life has been removed");
        }
        if ((player.b2body.getPosition().y <-1 /GuessEat.PPM && player.getStateTimer() > 2) && !MyCharacter.deathReason  && Hud.getLives() > 0) {
            Gdx.app.log("PlayScreen gameOver2", "player is redifined because of falling");
            Hud.setWorldTimer(60);
            player.redefineCharacter();
            return false;
        }
        else if ( !MyCharacter.deathReason && Hud.getLives() < 1){
            Gdx.app.log("PlayScreen gameOver2", "player dies because of falling");
            return true;
        }
            return false;
    }

    private void boundary (Camera camera, float startX, float startY, float width, float height){
        Vector3 position = camera.position;
        if(position.x < startX){
            position.x = startX;
        }
        if(position.y < startY){
            position.y = startY;
        }
        if(position.x >  width){
            position.x =  width;
        }
        if(position.y > startY + height){
            position.y = startY + height;
        }
        camera.position.set(position);
        camera.update();
    }

    public TextureAtlas getAtlasApple() {
        return atlasApple;
    }

    public TextureAtlas getAtlasIdle() {
        return atlasIdle;
    }

    public TextureAtlas getAtlasDeadDino() {
        return atlasDeadDino;
    }

    public TextureAtlas getAtlasPoints() {
        return atlasPoints;
    }

    public TextureAtlas getAtlasRun() {
        return atlasRun;
    }

    public TextureAtlas getAtlasJump() {
        return atlasJump;
    }

    public TextureAtlas getAtlasOrc() {
        return atlasOrc;
    }

    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void render() {

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
        map.dispose();
        b2dr.dispose();
        hud.dispose();
        atlasApple.dispose();
        atlasPoints.dispose();
        creator.dispose();
        atlasOrc.dispose();
        atlasJump.dispose();
        atlasRun.dispose();
        atlasDeadDino.dispose();
        atlasIdle.dispose();
        controller.dispose();
        hud.dispose();
        //renderer.dispose();
        //world.dispose();
        Gdx.app.log("PlayScreen","Got disposed");
    }

}
