package com.lampros.guesseat.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.Hud;
import com.lampros.guesseat.Screens.PlayScreen;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class Pie extends Fruit {

    private float stateTime;
    private Animation <TextureRegion> idleAnimation;
    private Array <TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Sound soundApple;

    public Pie(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array <TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlasApple().findRegion("apple"), i * 18, 1, 16, 16));
        }
        idleAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 12 / GuessEat.PPM, 12 / GuessEat.PPM);
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
            setRegion(new TextureRegion(screen.getAtlasPoints().findRegion("evolved"), 303, 605, 300, 300));
            setBounds(getX(), getY(), 32 / GuessEat.PPM, 32 / GuessEat.PPM);
        }
        else if (!destroyed) {
            //b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(idleAnimation.getKeyFrame(stateTime, true));

        }
    }

    @Override
    protected void defineFruit() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        //bdef.gravityScale=0.0f; //gravity effect it works here
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM);
        fdef.filter.categoryBits = GuessEat.FRUIT_BIT;
        fdef.filter.maskBits = GuessEat.MY_CHARACTER_BIT;

        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        /*//Create the interactive part of the fruit (bottom part)
        FixtureDef fdef2 = new FixtureDef();
        PolygonShape feet = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-3, 3).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(3, -3).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(-5, -8).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(5, -8).scl(1 / GuessEat.PPM);
        feet.set(vertice);

        fdef2.shape = feet;
        //fdef.restitution = 0.5f; //bounce effect
        //bdef.gravityScale=0.0f; //gravity effect doesnt work here
        fdef2.filter.categoryBits = GuessEat.PIE_FEET_BIT;

        //access the apple from collision handlerer
        b2body.createFixture(fdef2).setUserData(this);*/


    }
    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnTouch() {
        setToDestroy = true;
        Hud.addScore(300);
        soundApple = GuessEat.manager.get("audio/sounds/apple_bite.wav", Sound.class);
        soundApple.play();

    }
}
