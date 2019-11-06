package com.lampros.guesseat.Sprites.BossStage.BossStageElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
import com.lampros.guesseat.Scenes.HudStage3;
import com.lampros.guesseat.Screens.BossStage;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class MyBoss extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private Animation <TextureRegion> MyCharacterStand;
    private Animation <TextureRegion> MyCharacterRun;
    private Animation <TextureRegion> MyBossDead;
    private float stateTimer;
    private boolean runningRight;
    public BossStage screen;
    private boolean setToDestroyBoss;
    private boolean destroyed;


    private Array<BossFireBall> bossFireBalls; //testing bossApples
    private static Integer livesBoss;
    private static Vector2 velocityBoss;
    private boolean reverse;
    private boolean MyBossIsDead;
    private Music musicMyBoss;
    private Sound soundGhostDying;

    public TextureRegion region;


    public MyBoss(BossStage screen){
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        setToDestroyBoss = false;
        musicMyBoss = GuessEat.manager.get("audio/music/enemy.mp3", Music.class);
        musicMyBoss.setLooping(true);
        musicMyBoss.setVolume(0.3f);
        musicMyBoss.play();

        Array <TextureRegion> frames = new Array <TextureRegion>();
        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(screen.getAtlasGhostRun().findRegion("output-onlinepngtools (18)"), i * 454, 0, 454, 540));
        }
        MyCharacterRun = new Animation<TextureRegion>(0.3f, frames);
        frames.clear();

        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(screen.getAtlasGhostRun().findRegion("output-onlinepngtools (18)"), i * 454, 0, 454, 540));
        }
        MyCharacterStand = new Animation<TextureRegion>(0.3f, frames);
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(screen.getAtlasGhostDead().findRegion("white_hands_drop_down"), i * 758, 0, 758, 901));
        }
        MyBossDead= new Animation<TextureRegion>(0.4f, frames);

        livesBoss = 25;
        reverse = false;

        setBounds(getX(), getY(), 60 / GuessEat.PPM, 71 / GuessEat.PPM); //16 sets the size of the character

        velocityBoss = new Vector2(0.6f,0);

        bossFireBalls = new Array<BossFireBall>();
        defineCharacter();
    }

    private void defineCharacter(){
        MyBossIsDead = false;
        BodyDef bdef = new BodyDef();
        bdef.position.set( 1000/ GuessEat.PPM, 150 / GuessEat.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-15, 16).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(12, 16).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(12, -15).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-15, -15).scl(1 / GuessEat.PPM);
        shape.set(vertice);

        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = GuessEat.OBSTACLE_BOSS_BIT;
        fdef.filter.maskBits = GuessEat.MOVING_PLATFORM_BIT |
                GuessEat.OBSTACLE_BIT |
                GuessEat.MY_CHARACTER_BOSS_STAGE_BIT |
                GuessEat.WALLS_BIT |
                GuessEat.GROUND_BIT |
                GuessEat.APPLEBALL_BIT;
        b2body.createFixture(fdef).setUserData(this);

        //boss' attack point
        FixtureDef fdef2 = new FixtureDef();
        EdgeShape attackPoint = new EdgeShape();
        attackPoint.set(new Vector2(0 / GuessEat.PPM, 14 / GuessEat.PPM), new Vector2(0 / GuessEat.PPM, 1   / GuessEat.PPM));
        fdef2.filter.categoryBits = GuessEat.FRUIT_BIT;
        fdef2.filter.maskBits = GuessEat.APPLEBALL_BIT;
        fdef2.shape = attackPoint;
        //fdef2.isSensor = false; //it ensures that the feet collide with the ground. If it were a sensor, it would not collide with the ground, but the whole point of adding feet is to provide a smoother surface for mario to use to slide on rough surfaces, like a sled on rough snow.
        b2body.createFixture(fdef2).setUserData(this); //testarw apo feet to kanw this
    }

    public void update(float dt){
        //update the sprite to correspond with the position of the Box2d body
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
        if((setToDestroyBoss && !destroyed)) {//|| lives < 1 && !bossDead){
            world.destroyBody(b2body);
            destroyed = true;
            stateTimer = 0;
            Gdx.app.log("boss died inside update","");
            MyBossIsDead = true;

        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocityBoss);
        }

        for(BossFireBall ball : bossFireBalls) {
            ball.update(dt);
            if(ball.isDestroyed())
                bossFireBalls.removeValue(ball, true);
        }
    }

    private TextureRegion getFrame(float dt) {
        //get MyCharacter's current state
        currentState = getState();
        //TextureRegion region;

        //get corresponding animation keyFrame, depending on the state
        switch (currentState) {
            case DEAD:
                region = MyBossDead.getKeyFrame(stateTimer, true);
                break;
            /*case JUMPING:
                region = MyCharacterJump.getKeyFrame(stateTimer);
                break;*/
            case RUNNING:
                region = MyCharacterRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = MyCharacterStand.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;


        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && !region.isFlipX()) {
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
        if(MyBossIsDead)
            return State.DEAD;
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x !=0 )
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public static void reverseVelocity(boolean x, boolean y){
        if(x)
            velocityBoss.x = -velocityBoss.x;
        if(y)
            velocityBoss.y = -velocityBoss.y;
    }

    public  void OnHit(){
        if (livesBoss < 1) {
            Gdx.app.log("MyBoss OnHit", "BossIsDead");
            soundGhostDying = GuessEat.manager.get("audio/sounds/ghost_dying.mp3", Sound.class);
            soundGhostDying.play();
            Filter filter = new Filter();
            filter.maskBits = GuessEat.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            velocityBoss.x = 0;
            velocityBoss.y = 0;
            b2body.setGravityScale(17);
            //b2body.applyLinearImpulse(new Vector2(0, -0.3F), b2body.getWorldCenter(), true);
            MyBossIsDead = true;
            musicMyBoss = GuessEat.manager.get("audio/music/enemy.mp3", Music.class);
            musicMyBoss.stop();
        }
    }


    public void fire(){
        if(!MyBossIsDead) {
            bossFireBalls.add(new BossFireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
            bossFireBalls.add(new BossFireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : true));
            bossFireBalls.add(new BossFireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? false : true));
        }
    }


    public void draw(Batch batch) {
        super.draw(batch);


        for (BossFireBall ball : bossFireBalls)
            ball.draw(batch);
    }

    public static void setLives(Integer lives) {
        livesBoss = lives;
    }

    public boolean isMyBossIsDead() {
        return MyBossIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public static Integer getLives() {
        return livesBoss;
    }
}
