package com.lampros.guesseat.Sprites.BossStage;

import com.badlogic.gdx.audio.Sound;
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
import com.lampros.guesseat.Screens.SecondStage;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class LavaBoss extends ObjectsBoss {

    private float stateTime;
    private Animation <TextureRegion> idleAnimation;
    private Array <TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Sound sound;

    public LavaBoss(BossStage screen, float x, float y) {
        super(screen, x, y);
        frames = new Array <TextureRegion>();
        for(int i = 0; i < 5; i++){
            frames.add(new TextureRegion(screen.getAtlasLava().findRegion("l7"), (i * 18) + 2, 2, 16, 16));
        }
        idleAnimation = new Animation<TextureRegion>(0.1f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / GuessEat.PPM, 19 / GuessEat.PPM);
        setToDestroy = false;
        destroyed = false;
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
            //b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(idleAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineObjects() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        //bdef.gravityScale=0.0f; //gravity effect it works here
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-2, 6).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(2, 6).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(2, -6).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-2, -6).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        fdef.shape = shape;
        fdef.filter.categoryBits = GuessEat.OBSTACLE_BIT;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BOSS_STAGE_BIT |
        GuessEat.MY_CHARACTER_BIT | GuessEat.APPLEBALL_BIT;
                //GuessEat.OBJECT_BIT |

        b2body.createFixture(fdef).setUserData(this);
        /*CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM); //convert it to polygon
        fdef.filter.categoryBits = GuessEat.OBSTACLE_BIT; //maybe change it to apple_bit
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BIT |
                //GuessEat.OBJECT_BIT |
                GuessEat.APPLE_BIT |
                GuessEat.MY_CHARACTER_RIGHT_BIT |
                GuessEat.MY_CHARACTER_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);*/

       /* //Create the interactive part of the fruit (bottom part)
        PolygonShape right = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-6, 6).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(-3, 3).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(-3, -3).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-6, -6).scl(1 / GuessEat.PPM);
        right.set(vertice);

        fdef.shape = right;
        //fdef.restitution = 0.5f; //bounce effect
        //bdef.gravityScale=0.0f; //gravity effect doesnt work here
        fdef.filter.categoryBits = GuessEat.EGG_LEFT_BIT;

        //access the apple from collision handlerer
        b2body.createFixture(fdef).setUserData(this);*/


    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnTouch() {
        //sound = GuessEat.manager.get("audio/sounds/Apple_Bite-Simon_Craggs-1683647397.wav", Sound.class);
        //sound.play(0.1f);

    }
}
