package com.lampros.guesseat.Screens;

import com.badlogic.gdx.Application;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lampros.guesseat.Controllers.ControllerStage2;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.HudStage2;
import com.lampros.guesseat.Sprites.MyCharacterStage2;
import com.lampros.guesseat.Sprites.SecondStage.Enemy.Enemy;
import com.lampros.guesseat.Sprites.SecondStage.Loot.Loot;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootDefinition;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootedApple;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootedGoldenApple;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootedLife;
import com.lampros.guesseat.Sprites.SecondStage.MovingObjects.MovingObjects;
import com.lampros.guesseat.Sprites.SecondStage.Objects;
import com.lampros.guesseat.Tools.B2WorldCreatorStage2;
import com.lampros.guesseat.Tools.CollisionListener;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class SecondStage implements Screen {
    private GuessEat game;
    private TextureAtlas atlasLava;
    private TextureAtlas atlasSpikes;
    private TextureAtlas atlasFlask;
    private TextureAtlas atlasApple;
    private TextureAtlas atlasRedApple;
    private TextureAtlas atlasGoldenApple;
    private TextureAtlas atlasLife;
    private TextureAtlas atlasRedBat;
    private TextureAtlas atlasKnight;
    private TextureAtlas atlasIdle;
    private TextureAtlas atlasRun;
    private TextureAtlas atlasJump;
    private TextureAtlas atlasDeadDino;
    private TextureAtlas atlasPoints;
    private OrthographicCamera camera;
    private Viewport gamePort;
    private HudStage2 HudStage2;
    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreatorStage2 creator;
    private ControllerStage2 controller;
    //Sprites
    private MyCharacterStage2 player;
    private Array<Loot> loots;
    private LinkedBlockingQueue<LootDefinition> lootsToSpawn;
    private Music musicPlayscreen;
    private Music musicSecondStage;
    private Integer levelHeight;

    public SecondStage(GuessEat game) {
        atlasIdle = new TextureAtlas("characters/LittleDino/LittleDinoIdle/LittleDinoIdle.pack");
        atlasRun = new TextureAtlas("characters/LittleDino/LittleDinoRun/LittleDinoRun.pack");
        atlasJump = new TextureAtlas("characters/LittleDino/LittleDinoJump/LittleDinoJump.pack");
        atlasDeadDino = new TextureAtlas("characters/LittleDino/LittleDinoDead/LittleDinoDead.pack");
        atlasApple = new TextureAtlas("fruits/apple/apple_animated.pack");
        atlasPoints = new TextureAtlas("fruits/points/Points.pack");
        atlasLava = new TextureAtlas("Objects/Lava/Lava32/Lava.pack");
        atlasSpikes = new TextureAtlas("Objects/Obstacles/Spikes/Spikes.pack");
        atlasFlask = new TextureAtlas("Objects/Flask/Flask.pack");
        atlasRedBat = new TextureAtlas("characters/MonsterFly/MonsterFly.pack");
        atlasKnight = new TextureAtlas("characters/Knight/Knight.pack");
        atlasRedApple = new TextureAtlas("fruits/red apple/RedApple.pack");
        atlasGoldenApple = new TextureAtlas("fruits/golden apple/GoldenApple.pack");
        atlasLife = new TextureAtlas("Objects/Life/Life.pack");
        musicPlayscreen = GuessEat.manager.get("audio/music/playscreen.mp3", Music.class);
        musicPlayscreen.stop();
        musicSecondStage = GuessEat.manager.get("audio/music/secondstage.mp3", Music.class);
        musicSecondStage.setLooping(true);
        musicSecondStage.play();
        this.game = game;
        //create cam used to follow mario through cam world
        camera = new OrthographicCamera();
        //create a FitViewport to maintain virtual aspect ratio despite changes
        gamePort = new FitViewport(GuessEat.V_WIDTH / GuessEat.PPM, GuessEat.V_HEIGHT / GuessEat.PPM, camera);
        //create our game HudStage2 for scores/timers/level info
        HudStage2 = new HudStage2(game.batch);
        //Load the map and setup the map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("stage_2/stage_2_updated.tmx");
        MapProperties props = map.getProperties();
        levelHeight = props.get("height", Integer.class);
        renderer = new OrthogonalTiledMapRenderer(map,1 / GuessEat.PPM);
        //initially set the camera to be centered correctly at the start of the map
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        player = new MyCharacterStage2(this);
        creator = new B2WorldCreatorStage2(this);
        world.setContactListener(new CollisionListener(game));
        controller = new ControllerStage2();
        loots = new Array<Loot>();
        lootsToSpawn = new LinkedBlockingQueue<LootDefinition>();
    }

    public void spawnLoot(LootDefinition ldef) {
        lootsToSpawn.add(ldef);
    }

    public void handleSpawningLoots(){
        if(!lootsToSpawn.isEmpty()){
            LootDefinition ldef = lootsToSpawn.poll(); //pop for a queue
            if(ldef.type == LootedApple.class) {
                loots.add(new LootedApple(this, ldef.position.x, ldef.position.y));
            }
            if(ldef.type == LootedGoldenApple.class) {
                loots.add(new LootedGoldenApple(this, ldef.position.x, ldef.position.y));
            }
            if(ldef.type == LootedLife.class) {
                loots.add(new LootedLife(this, ldef.position.x, ldef.position.y));
            }
        }
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
        handleSpawningLoots();
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        for(Objects object : creator.getLava()){
            object.update(dt);
        }
        for (MovingObjects object : creator.getSpikes()){
            object.update(dt);
            if(object.getX() < player.getX() + 1)
                object.b2body.setActive(true);
        }
        for (MovingObjects object : creator.getFlask()){
            object.update(dt);
        }
        for(Enemy enemy : creator.getKnights()){
            enemy.update(dt);
        }
        for(Enemy enemy : creator.getRedBats()){
            enemy.update(dt);
        }
        for(Loot loot : loots) {
            loot.update(dt);
        }
        HudStage2.update(dt);
        if(player.currentState != MyCharacterStage2.State.DEAD){
            camera.position.x = (float)Math.round(player.b2body.getPosition().x * 100f)/100f ;
            camera.update();
        }
        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;
        boundary(camera, startX, startY, 3640 / GuessEat.PPM,levelHeight * GuessEat.PPM - startY * 2);
        if(HudStage2.isTimeUp()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
            HudStage2.setTimeUp(false);
        }
        //Tell the renderer to draw only what the camera can see in the game world
        renderer.setView(camera);
    }

    public void handleInput(float dt){ //PROPER ONE
        //control the player using immediate impulses
        if(!player.isMyCharacterIsDead()) {
            if (( controller.isUpPressed()) && (player.b2body.getLinearVelocity().y == 0) && player.currentState!= MyCharacterStage2.State.DEAD && HudStage2.getWorldTimer()< 118){
                if(MyCharacterStage2.jump ==true){
                    player.b2body.applyLinearImpulse(new Vector2(0, 3.5f), player.b2body.getWorldCenter(), true);
                    controller.setUpPressed(false);//change jump
                }
                else {
                    player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
                    controller.setUpPressed(false); }//change jump
             }
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) && player.b2body.getLinearVelocity().x <= 2 && HudStage2.getWorldTimer()< 118)
                player.b2body.applyLinearImpulse(new Vector2(0.1f , 0), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) && player.b2body.getLinearVelocity().x >= -2 && HudStage2.getWorldTimer()< 118)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || controller.isFireballPressed()) && HudStage2.getWorldTimer()< 118){
                controller.setFireballPressed(false);
                player.fire();}}
            if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || controller.isHomePressed()){
                game.setScreen(new MainMenuScreen(game));
                dispose();
        }
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
        for(Enemy enemy : creator.getKnights()){
            enemy.draw(game.batch);
        }
        for(Enemy enemy : creator.getRedBats()){
            enemy.draw(game.batch);
        }
        for(Objects object : creator.getLava())
            object.draw(game.batch);
        for(MovingObjects object : creator.getSpikes())
            object.draw(game.batch);
        for(MovingObjects object : creator.getFlask())
            object.draw(game.batch);
        for(Loot loot: loots){
            loot.draw(game.batch);
        }
        game.batch.end();
        HudStage2.stage.draw();
        if(Gdx.app.getType() == Application.ApplicationType.Android)
        controller.draw();
        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver(){
        if(player.currentState == MyCharacterStage2.State.DEAD && player.getStateTimer() > 2) {
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

    private void boundary (Camera camera, float startX, float startY, float width, float height){
        Vector3 position = camera.position;
        if(position.x < startX){
            position.x = startX;
        }
        if(position.y < startY){
            position.y = startY;
        }
        if(position.x > startX + width){
            position.x = startX + width;
        }
        if(position.y > startY + height){
            position.y = startY + height;
        }
        camera.position.set(position);
        camera.update();
    }

    public TextureAtlas getAtlasLava() {
        return atlasLava;
    }

    public TextureAtlas getAtlasSpikes() {
        return atlasSpikes;
    }

    public TextureAtlas getAtlasFlask() {
        return atlasFlask;
    }

    public TextureAtlas getAtlasApple() {
        return atlasApple;
    }

    public TextureAtlas getAtlasRedApple() {
        return atlasRedApple;
    }

    public TextureAtlas getAtlasGoldenApple() {
        return atlasGoldenApple;
    }

    public TextureAtlas getAtlasLife() {
        return atlasLife;
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

    public TextureAtlas getAtlasRedBat() {
        return atlasRedBat;
    }

    public TextureAtlas getAtlasKnight() {
        return atlasKnight;
    }

    public TextureAtlas getAtlasPoints() {
        return atlasPoints;
    }

    @Override
    public void dispose() {
        map.dispose();
        b2dr.dispose();
        HudStage2.dispose();
        atlasSpikes.dispose();
        atlasLava.dispose();
        atlasApple.dispose();
        atlasDeadDino.dispose();
        atlasFlask.dispose();
        atlasGoldenApple.dispose();
        atlasIdle.dispose();
        atlasJump.dispose();
        atlasKnight.dispose();
        atlasLife.dispose();
        atlasPoints.dispose();
        atlasRedApple.dispose();
        atlasRedBat.dispose();
        atlasRun.dispose();
        controller.dispose();
        creator.dispose();
        Gdx.app.log("SecondStage","Got disposed");
    }
}
