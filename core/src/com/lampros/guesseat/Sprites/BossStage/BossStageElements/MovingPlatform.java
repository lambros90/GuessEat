package com.lampros.guesseat.Sprites.BossStage.BossStageElements;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.BossStage;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class MovingPlatform extends EnemyBossStage {

    private float stateTime;
    private Animation<TextureRegion> idleAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Integer lives;
    private boolean runningRight;
    private float distance;
    private boolean reverse;


    public MovingPlatform(BossStage screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlasMovingPlatform().findRegion("movingPlatform"), 1, 1, 77, 29));
        idleAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 48 / GuessEat.PPM, 16 / GuessEat.PPM); //16 sets the size of the character
        distance = b2body.getPosition().x;
        reverse = false;
        //setToDestroy = false;
        //destroyed = false;
        //lives = 2;

        //runningRight = true;
    }

    public void moveforward(float dt){
        if (reverse==false) {
            b2body.setLinearVelocity(new Vector2(.5f,0));
        }
    }

    public void moveback(float dt){
        if (reverse==true) {
            b2body.setLinearVelocity(new Vector2(-.5f,0));
        }
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setBounds(getX(), getY(), 16 / GuessEat.PPM, 8 / GuessEat.PPM);
            stateTime = 0;
            //setRegion(new TextureRegion(screen.getAtlasApple().findRegion("apple"), 1, 1, 16, 16));
        }
        else if (!destroyed) {
            float d = b2body.getPosition().x;
            moveforward(dt);
            if (d - distance >= 0.5) {
                reverse = true;
            }
            moveback(dt);
            if (d - distance <= -0.5) {
                reverse = false;
            }
            //b2body.setLinearVelocity(velocity);
            setPosition((b2body.getPosition().x - getWidth() / 2 ), (b2body.getPosition().y - getHeight() / 2));
            setRegion(idleAnimation.getKeyFrame(stateTime, true));
        }
    }


    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        //bdef.position.set(45 / GuessEat.PPM, 32 / GuessEat.PPM);
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        //b2body.setAngularVelocity(-90 * DEGTORAD);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-24, 8.5f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(24, 8.5f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(24, -8.5f).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-24, -8.5f).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        fdef.shape = shape;
        //fdef.friction=100;
        //fdef.restitution=0;

        fdef.filter.categoryBits = GuessEat.MOVING_PLATFORM_BIT; //fix the moving by the fireball
        //fdef.isSensor = true;
        fdef.filter.maskBits = GuessEat.MY_CHARACTER_BOSS_STAGE_BIT | GuessEat.MY_CHARACTER_FEET | GuessEat.APPLEBALL_BIT;
                //GuessEat.APPLEBALL_BIT;

       // b2body.setGravityScale(1);
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void hitOnTouch() {
        /*setBounds(getX(), getY(), 8 / GuessEat.PPM, 8 / GuessEat.PPM);
        if (lives < 1) {
            setToDestroy = true;
        }
        else
            lives --;*/
    }

    public void hitOnRelease(){
       // setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
    }

    @Override
    public void fire() {

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }
}
