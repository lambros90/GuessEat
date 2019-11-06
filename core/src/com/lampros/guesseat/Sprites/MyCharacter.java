package com.lampros.guesseat.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.Hud;
import com.lampros.guesseat.Screens.PlayScreen;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class MyCharacter extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};
    public State currentState;
    private State previousState;
    public World world;
    public static Body b2body;
    private Animation <TextureRegion> MyCharacterStand;
    private Animation <TextureRegion> MyCharacterRun;
    private Animation <TextureRegion> MyCharacterJump;
    private Animation <TextureRegion> MyCharacterDead;
    private static float stateTimer;
    private boolean runningRight;
    private Sound soundDying;
    private static boolean MyCharacterIsDead;
    private static boolean timeToRedfineMyCharacter;
    private PlayScreen screen;
    private Sound soundScream;
    public static boolean deathReason;
    public static boolean allowMovement;

    public MyCharacter(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        deathReason =false;
        allowMovement = false;
        Array <TextureRegion> frames = new Array <TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 0, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 0, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 681, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 681, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 1362, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 1362, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 2043, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasRun().findRegion("Run (2)"), 2043, 0, 600, 472));
        MyCharacterRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 0, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 0, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 681, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 681, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 1362, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 1362, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 2043, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasJump().findRegion("Jump (10)"), 2043, 0, 600, 472));
        MyCharacterJump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 0, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 0, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 681, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 681, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 1362, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 1362, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 2043, 0, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 2724, 472, 600, 472));
        frames.add(new TextureRegion(screen.getAtlasIdle().findRegion("Idle (10)"), 2724, 0, 600, 472));
        MyCharacterStand = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        for (int i = 0; i < 6; i ++) {
            frames.add(new TextureRegion(screen.getAtlasDeadDino().findRegion("Dead (1)"), i * 681, 0, 680, 472));
        }
        MyCharacterDead = new Animation<TextureRegion>(0.1f, frames);
        defineCharacter();
        //16 sets the size of the character
        setBounds(1, 1, 30 / GuessEat.PPM, 21 / GuessEat.PPM);
    }

    public void defineCharacter(){
       // HudStage2.setWorldTimer(60);
        setDeathReason(false);
        MyCharacterIsDead = false;
        soundScream = GuessEat.manager.get("audio/sounds/dinosaur_scream.mp3", Sound.class);
        soundScream.play();

        /*
         * Created by brentaureli on 25/11/15. https://github.com/BrentAureli/SuperMario
         */

        BodyDef bdef = new BodyDef();
        bdef.position.set(38 / GuessEat.PPM + getWidth()/2 / GuessEat.PPM, 100 / GuessEat.PPM + getHeight() /2 / GuessEat.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(5.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(5.5f, -7.5f).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-10.5f, -7.5f).scl(1 / GuessEat.PPM);
        shape.set(vertice);

        fdef.shape = shape;
        fdef.filter.categoryBits = GuessEat.MY_CHARACTER_BIT;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.ADVANCE_LEVEL_BIT |
                GuessEat.FRUIT_BIT |
                GuessEat.WOGOL_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //character's feet
        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / GuessEat.PPM, -9.5f / GuessEat.PPM), new Vector2(2 / GuessEat.PPM, -9.5f / GuessEat.PPM));
        fdef2.filter.categoryBits = GuessEat.MY_CHARACTER_FEET;
        fdef2.filter.maskBits = GuessEat.GROUND_BIT;
        fdef2.shape = feet;
        fdef2.isSensor = true; //it ensures that the feet collide with the ground. If it were a sensor, it would not collide with the ground, but the whole point of adding feet is to provide a smoother surface for mario to use to slide on rough surfaces, like a sled on rough snow.
        b2body.createFixture(fdef2).setUserData("feet");
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        if(timeToRedfineMyCharacter)
            redefineCharacter();
    }

    private TextureRegion getFrame(float dt) {
        //get MyCharacter's current state
        currentState = getState();
        TextureRegion region;
        //get corresponding animation keyFrame, depending on the state
        switch (currentState) {
            case DEAD:
                region = MyCharacterDead.getKeyFrame(stateTimer, false);
                break;
            case JUMPING:
                region = MyCharacterJump.getKeyFrame(stateTimer, true);
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

    /*
     * Created by brentaureli on 25/11/15. https://github.com/BrentAureli/SuperMario
     */

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

    public static float getStateTimer(){
        return stateTimer;
    }




    public void hit(){
        if (Hud.getLives() > 1 ) {
            timeToRedfineMyCharacter = true;
        }
        else{
            soundDying = GuessEat.manager.get("audio/sounds/dying.mp3", Sound.class);
            soundDying.play();
            Filter filter = new Filter();
            filter.maskBits = GuessEat.GROUND_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.setGravityScale(100);
            MyCharacterIsDead = true;
        }
    }

    public void redefineCharacter() {
        soundScream = GuessEat.manager.get("audio/sounds/dinosaur_scream.mp3", Sound.class);
        soundScream.play();
        Hud.setWorldTimer(60);
        setDeathReason(false);
        MyCharacterIsDead = false;
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(38 / GuessEat.PPM, 150 / GuessEat.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(5.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(5.5f, -7.5f).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-10.5f, -7.5f).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        fdef.shape = shape;

        fdef.filter.categoryBits = GuessEat.MY_CHARACTER_BIT;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.ADVANCE_LEVEL_BIT |
                GuessEat.WOGOL_BIT |
        GuessEat.FRUIT_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //CHANGE ALL FDEFS TO ADAPT TO CHARS BODY

        //character's feet
        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-2 / GuessEat.PPM, -9.5f / GuessEat.PPM), new Vector2(2 / GuessEat.PPM, -9.5f / GuessEat.PPM));
        fdef2.filter.categoryBits = GuessEat.MY_CHARACTER_FEET;
        fdef2.filter.maskBits = GuessEat.GROUND_BIT;
        fdef2.shape = feet;
        fdef2.isSensor = true; //it ensures that the feet collide with the ground. If it were a sensor, it would not collide with the ground, but the whole point of adding feet is to provide a smoother surface for mario to use to slide on rough surfaces, like a sled on rough snow.
        b2body.createFixture(fdef2).setUserData("feet");

        timeToRedfineMyCharacter = false;
    }

    public static void setDeathReason(boolean deathReason) {
        MyCharacter.deathReason = deathReason;
    }

    public void draw(Batch batch){
        super.draw(batch);
    }

}
