package com.lampros.guesseat.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.lampros.guesseat.Controllers.ControllerStage3;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.HudStage3;
import com.lampros.guesseat.Sprites.BossStage.BossStageElements.BossApple;
import com.lampros.guesseat.Sprites.BossStage.BossStageElements.EnemyBossStage;
import com.lampros.guesseat.Sprites.BossStage.BossStageElements.MyBoss;
import com.lampros.guesseat.Sprites.BossStage.MovingObjectsBoss.MovingObjectsBoss;
import com.lampros.guesseat.Sprites.BossStage.ObjectsBoss;
import com.lampros.guesseat.Sprites.MyCharacterStage3;
import com.lampros.guesseat.Tools.B2WorldCreatorStage3;
import com.lampros.guesseat.Tools.CollisionListener;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */


public class BossStage implements Screen {

    private GuessEat game;
    private TextureAtlas atlasLava;
    private TextureAtlas atlasSpikes;
    private TextureAtlas atlasApple;
    private TextureAtlas atlasRedApple;
    private TextureAtlas atlasMovingPlatform;
    private TextureAtlas atlasGoldenApple;
    private TextureAtlas atlasBigDemon;
    private TextureAtlas atlasFireball;
    private OrthographicCamera camera;
    private Viewport gamePort;
    private HudStage3 HudStage3;
    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreatorStage3 creator;
    private ControllerStage3 controller;
    //Sprites
    private MyCharacterStage3 player;
    private MyBoss boss; //test
    private Integer levelHeight;
    private TextureAtlas atlasIdle;
    private TextureAtlas atlasRun;
    private TextureAtlas atlasJump;
    private TextureAtlas atlasDeadDino;
    private TextureAtlas atlasGhostRun;
    private TextureAtlas atlasGhostDead;
    private TextureAtlas atlasWhiteFireball;
    private boolean bossJump;

    public BossStage(GuessEat game) {
        atlasIdle = new TextureAtlas("characters/LittleDino/LittleDinoIdle/LittleDinoIdle.pack");
        atlasRun = new TextureAtlas("characters/LittleDino/LittleDinoRun/LittleDinoRun.pack");
        atlasJump = new TextureAtlas("characters/LittleDino/LittleDinoJump/LittleDinoJump.pack");
        atlasDeadDino = new TextureAtlas("characters/LittleDino/LittleDinoDead/LittleDinoDead.pack");
        atlasLava = new TextureAtlas("Objects/Lava/Lava.pack");
        atlasSpikes = new TextureAtlas("Objects/Obstacles/Spikes/Spikes.pack");
        atlasMovingPlatform = new TextureAtlas("Objects/MovingPlatform/MovingPlatform.pack");
        atlasApple = new TextureAtlas("fruits/apple/apple_animated.pack");
        atlasRedApple = new TextureAtlas("fruits/red apple/RedApple.pack");
        atlasFireball = new TextureAtlas("Objects/Fireball/Fireball.pack");
        atlasGoldenApple = new TextureAtlas("fruits/golden apple/GoldenApple.pack");
        atlasGhostRun = new TextureAtlas("characters/Ghost/Move/GhostMove.pack");
        atlasGhostDead = new TextureAtlas("characters/Ghost/Dead/GhostDead.pack");
        atlasWhiteFireball = new TextureAtlas("Objects/WhiteFireball/WhiteFireball.pack");
        this.game = game;
        //create cam used to follow mario through cam world
        camera = new OrthographicCamera();
        //create a FitViewport to maintain virtual aspect ratio despite changes
        gamePort = new FitViewport(GuessEat.V_WIDTH / GuessEat.PPM, GuessEat.V_HEIGHT / GuessEat.PPM, camera);
        //create our game HudStage2 for scores/timers/level info
        HudStage3 = new HudStage3(game.batch);
        //Load the map and setup the map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("stage_3/stage_3.tmx");
        MapProperties props = map.getProperties();
        levelHeight = props.get("height", Integer.class);
        renderer = new OrthogonalTiledMapRenderer(map,1 / GuessEat.PPM);
        //initially set the camera to be centered correctly at the start of the map
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        boss = new MyBoss(this);//test
        player = new MyCharacterStage3(this);
        creator = new B2WorldCreatorStage3(this);
        bossJump = false;
        world.setContactListener(new CollisionListener(game));
        controller = new ControllerStage3();
        BossApple.setToDestroyAppleFalse();
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
        boss.update(dt); //test
        for(ObjectsBoss object : creator.getLavaBoss()){
            object.update(dt);
        }
        for (MovingObjectsBoss object : creator.getSpikesBoss()){
            object.update(dt);
            if(object.getX() < player.getX() + 0.5)
                object.b2body.setActive(true);
        }
        for(EnemyBossStage enemy : creator.getMovingPlatforms()){
            enemy.update(dt);
        }
        HudStage3.update(dt);
        if(player.currentState != MyCharacterStage3.State.DEAD){
            camera.position.x = (float)Math.round(player.b2body.getPosition().x * 100f)/100f ;
            camera.update();
        }
        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;
        boundary(camera, startX, startY, 1720 / GuessEat.PPM,levelHeight * GuessEat.PPM - startY * 2);
        if(HudStage3.isTimeUp()) {
            //HudStage2.setWorldTimer(60);
            game.setScreen(new GameOverScreen(game));
            HudStage3.setTimeUp(false);
            dispose();
        }


        //Tell the renderer to draw only what the camera can see in the game world
        renderer.setView(camera);
    }


    public void handleInput(float dt){ //PROPER ONE
        //control the player using immediate impulses
        if(!player.myCharacterIsDead()) {
            if (( controller.isUpPressed()) && (player.b2body.getLinearVelocity().y == 0) && HudStage3.getWorldTimer()< 118){
                    player.b2body.applyLinearImpulse(new Vector2(0, 3.50f), player.b2body.getWorldCenter(), true);
                    controller.setUpPressed(false);//change jump
            }
            //player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) && player.b2body.getLinearVelocity().x <= 2 && HudStage3.getWorldTimer()< 118)
                player.b2body.applyLinearImpulse(new Vector2(0.1f , 0), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) && player.b2body.getLinearVelocity().x >= -2 && HudStage3.getWorldTimer()< 118)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || controller.isFireballPressed()) {
                controller.setFireballPressed(false);
                boss.fire();
                player.fire();
            }}
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || controller.isHomePressed()){
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
        //}
        if ((boss.b2body.getPosition().y < 1) && (boss.b2body.getLinearVelocity().y == 0)){// && (bossJump == false)) {
            boss.b2body.applyLinearImpulse(new Vector2(0, 2f), boss.b2body.getWorldCenter(), true);
           // Gdx.app.log("BossStage handleinput", "enemy jumps");
            //bossJump = true;
        }
        //if((boss.b2body.getPosition().y ))
        bossJump = false;
        //else
          //  boss.b2body.applyLinearImpulse(new Vector2(0, 0));
    }


    @Override
    public void render(float delta) {
        Gdx.graphics.setTitle(GuessEat.TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
        //Separate the update logic from render
        if(!controller.isPause()) {
            update(delta);
        }

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render the game map
        renderer.render();

        //render the Box2DDebugLines
        //b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        boss.draw(game.batch); //test

        for(ObjectsBoss object : creator.getLavaBoss())
            object.draw(game.batch);

        for(MovingObjectsBoss object : creator.getSpikesBoss())
            object.draw(game.batch);

        for(EnemyBossStage enemy : creator.getMovingPlatforms()){
            enemy.draw(game.batch);
        }



        game.batch.end();

        //set the batch to now draw what the HudStage2 camera sees
//        game.batch.setProjectionMatrix(HudStage2.stage.getCamera().combined);
        HudStage3.stage.draw();

        //controller = new ControllerStage3();
        if(Gdx.app.getType() == Application.ApplicationType.Android)
        controller.draw();

        if (boss.isMyBossIsDead() && boss.getStateTimer() > 3) {
            game.setScreen(new CongratulationsScreen(game));
            dispose();
        }
        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }

    public boolean gameOver(){
        if(player.currentState == MyCharacterStage3.State.DEAD && player.getStateTimer() > 2) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        //game.dispose();
        map.dispose();
        b2dr.dispose();
        HudStage3.dispose();
        atlasLava.dispose();
        atlasSpikes.dispose();
        atlasApple.dispose();
        atlasRedApple.dispose();
        atlasMovingPlatform.dispose();
        atlasGoldenApple.dispose();
        atlasFireball.dispose();
        creator.dispose();
        controller.dispose();
        atlasIdle.dispose();
        atlasRun.dispose();
        atlasJump.dispose();
        atlasDeadDino.dispose();
        atlasGhostRun.dispose();
        atlasGhostDead.dispose();
        atlasWhiteFireball.dispose();
        Gdx.app.log("BossStage","Got disposed");
    }

    public void boundary (Camera camera, float startX, float startY, float width, float height){
        Vector3 position = camera.position;
        if(position.x < startX){
            position.x = startX;
        }
        if(position.y < startY){
            position.y = startY;
        }
        if(position.x > width){
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

    public TextureAtlas getAtlasLava() {
        return atlasLava;
    }

    public TextureAtlas getAtlasSpikes() {
        return atlasSpikes;
    }

    public TextureAtlas getAtlasMovingPlatform() {
        return atlasMovingPlatform;
    }

    public TextureAtlas getAtlasRedApple() {
        return atlasRedApple;
    }

    public TextureAtlas getAtlasGoldenApple() {
        return atlasGoldenApple;
    }

    public TextureAtlas getAtlasIdle() {
        return atlasIdle;
    }

    public TextureAtlas getAtlasRun() {
        return atlasRun;
    }

    public TextureAtlas getAtlasJump() {
        return atlasJump;
    }

    public TextureAtlas getAtlasDeadDino() {
        return atlasDeadDino;
    }

    public TextureAtlas getAtlasGhostRun() {
        return atlasGhostRun;
    }

    public TextureAtlas getAtlasGhostDead() {
        return atlasGhostDead;
    }

    public TextureAtlas getAtlasWhiteFireball() {
        return atlasWhiteFireball;
    }
}
