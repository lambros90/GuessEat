package com.lampros.guesseat.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.HudStage3;
import com.lampros.guesseat.Screens.BossStage;
import com.lampros.guesseat.Sprites.BossStage.BossStageElements.BossApple;

import static com.lampros.guesseat.Sprites.MyCharacterStage2.jump;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class MyCharacterStage3 extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private Animation <TextureRegion> MyCharacterStand;
    private Animation <TextureRegion> MyCharacterRun;
    private Animation <TextureRegion> MyCharacterJump;
    private Animation <TextureRegion> MyCharacterDead;
    private float stateTimer;
    private boolean runningRight;
    private Sound sound;
    private GuessEat game;
    private boolean MyCharacterIsDead;
    private boolean timeToRedfineMyCharacter;
    public BossStage screen;

    private Integer ballsCounter;
    private Array<BossApple> bossApples; //testing bossApples
    //public static boolean deathReasonStage2;
    //public static boolean jump;
    public static boolean lavaDamageStage3;


    public MyCharacterStage3(BossStage screen){
        //super(screen.getAtlas().findRegion("big_zombie_idle_anim_f0"));
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        lavaDamageStage3 = true;
        //deathReasonStage2 =false;
        //jump = false;
        //ballsCounter = HudStage2.getScore();

        Array <TextureRegion> frames = new Array <TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 0, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 0, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 681, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 681, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 1362, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 1362, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 2043, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 2043, 0, 680, 472));
        MyCharacterRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 0, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 0, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 681, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 681, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 1362, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 1362, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 2043, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 2043, 0, 680, 472));
        MyCharacterJump = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 0, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 0, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 681, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 681, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 1362, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 1362, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 2043, 0, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 2724, 472, 680, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 2724, 0, 680, 472));
        MyCharacterStand = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        for (int i = 0; i < 6; i ++) {
            frames.add(new TextureRegion(screen.getAtlasDeadDino().findRegion("Dead (1)"), i * 681, 0, 680, 472));
        }
        MyCharacterDead = new Animation<TextureRegion>(0.1f, frames);

        defineCharacter();
        //MyCharacterStand = new TextureRegion(getTexture(), 1, 1, 34, 34);
        setBounds(1, 1, 30 / GuessEat.PPM, 21 / GuessEat.PPM); //16 sets the size of the character
        //setRegion(MyCharacterStand);

        bossApples = new Array<BossApple>();

    }

    public void defineCharacter(){
        sound = GuessEat.manager.get("audio/sounds/dinosaur_scream.mp3", Sound.class);
        sound.play();
        //setDeathReasonStage2(false);

        MyCharacterIsDead = false;
        BodyDef bdef = new BodyDef();
        bdef.position.set(36 / GuessEat.PPM, 200 / GuessEat.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-7.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(3.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(3.5f, -7).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-7.5f, -7).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        //shape.set(vertice);

        fdef.shape = shape;
        fdef.filter.categoryBits = GuessEat.MY_CHARACTER_BOSS_STAGE_BIT;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.OBSTACLE_BOSS_BIT |
                GuessEat.MOVING_PLATFORM_BIT |
                GuessEat.OBSTACLE_BIT;

        b2body.createFixture(fdef).setUserData(this);

        //character's feet
        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-3 / GuessEat.PPM, -9.5f  / GuessEat.PPM), new Vector2(3 / GuessEat.PPM, -9.5f  / GuessEat.PPM));
        fdef2.filter.categoryBits = GuessEat.MY_CHARACTER_FEET;
        fdef2.filter.maskBits = GuessEat.GROUND_BIT | GuessEat.MOVING_PLATFORM_BIT;
        fdef2.shape = feet;
        fdef2.isSensor = true; //it ensures that the feet collide with the ground. If it were a sensor, it would not collide with the ground, but the whole point of adding feet is to provide a smoother surface for mario to use to slide on rough surfaces, like a sled on rough snow.
        b2body.createFixture(fdef2).setUserData(this); //testarw apo feet to kanw this

    }

    public void update(float dt){
        //update the sprite to correspond with the position of the Box2d body
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        if(b2body.getLinearVelocity().x ==0){

        }
        if(timeToRedfineMyCharacter)
            redefineCharacter();

        for(BossApple ball : bossApples) {
            ball.update(dt);
            if(ball.isDestroyed())
                bossApples.removeValue(ball, true);
        }
    }

    public TextureRegion getFrame(float dt) {
        //get MyCharacter's current state
        currentState = getState();
        TextureRegion region;

        //get corresponding animation keyFrame, depending on the state
        switch (currentState) {
            case DEAD:
                region = MyCharacterDead.getKeyFrame(stateTimer, false);
                break;
            case JUMPING:
                region = MyCharacterJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = MyCharacterRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = MyCharacterStand.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        if (currentState.equals(previousState))
            stateTimer += dt;
        else
            stateTimer = 0;

        previousState = currentState;
        return region;
    }

    public State getState(){
        if(MyCharacterIsDead)
            return State.DEAD;
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x !=0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public boolean myCharacterIsDead(){
        return MyCharacterIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void dispose(){
        sound.dispose();

    }

    public void hit(){
        if (HudStage3.getLives() > 1) {
            //MyCharacterIsDead = false;
            timeToRedfineMyCharacter = true;
            //setBounds(getX(), getY(), getWidth(), getHeight() / 2);
        }
        else{

            GuessEat.manager.get("audio/sounds/dying.mp3", Sound.class).play();
            Filter filter = new Filter();
            filter.maskBits = GuessEat.GROUND_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.setGravityScale(100);
            MyCharacterIsDead = true;

            //game.setScreen(new GameOverScreen(game));
            //dispose();
           /* Filter filter = new Filter();
            filter.maskBits = GuessEat.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }*/

            //b2body.setGravityScale(0f);
            //setBounds(1,1,32,32);
            //b2body.applyLinearImpulse(new Vector2(0, 1f), b2body.getWorldCenter(), true);
        }
    }

    /*public void evolve(){
        setBounds(1, 1, 24 / GuessEat.PPM, 24 / GuessEat.PPM);
        jump = true;
    }*/

    public void redefineCharacter() {
        sound = GuessEat.manager.get("audio/sounds/dinosaur_scream.mp3", Sound.class);
        sound.play();
        lavaDamageStage3 = true;
        HudStage3.setWorldTimer(120);
       // setDeathReasonStage2(false);
        MyCharacterIsDead = false;
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(36 / GuessEat.PPM, 200 / GuessEat.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-7.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(3.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(3.5f, -7).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-7.5f, -7).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        fdef.shape = shape;
        //fdef.friction=100;

        fdef.filter.categoryBits = GuessEat.MY_CHARACTER_BOSS_STAGE_BIT;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.OBSTACLE_BOSS_BIT |
        GuessEat.MOVING_PLATFORM_BIT |
        GuessEat.OBSTACLE_BIT;

        b2body.createFixture(fdef).setUserData(this);

        //CHANGE ALL FDEFS TO ADAPT TO CHARS BODY

        //character's feet
        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4 / GuessEat.PPM, -7.5f  / GuessEat.PPM), new Vector2(4 / GuessEat.PPM, -7.5f  / GuessEat.PPM));
        fdef2.filter.categoryBits = GuessEat.MY_CHARACTER_FEET;
        fdef2.filter.maskBits = GuessEat.GROUND_BIT | GuessEat.MOVING_PLATFORM_BIT;
        fdef2.shape = feet;
        fdef2.isSensor = true; //it ensures that the feet collide with the ground. If it were a sensor, it would not collide with the ground, but the whole point of adding feet is to provide a smoother surface for mario to use to slide on rough surfaces, like a sled on rough snow.
        b2body.createFixture(fdef2).setUserData("feet");

        timeToRedfineMyCharacter = false;
    }

   /* public void fire(){
        if (HudStage2.getBossApples() > 0 && jump == true ){
            Gdx.app.log("asd","asda");
            ballsCounter = HudStage2.getBossApples();
            ballsCounter--;
            bossApples.add(new BossApple(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
            HudStage2.addScore(ballsCounter);

        }
    }*/

    public void fire(){
        if (HudStage3.getAppleBalls() > 0 && jump == true ){
            ballsCounter = HudStage3.getAppleBalls();
            ballsCounter--;
            bossApples.add(new BossApple(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
            HudStage3.addScore();

        }
    }


    public void draw(Batch batch) {
        super.draw(batch);


        for (BossApple ball : bossApples)
            ball.draw(batch);
    }

    public static void setLavaDamageStage3(boolean lavaDamageStage3) {
        MyCharacterStage3.lavaDamageStage3 = lavaDamageStage3;
    }
}
