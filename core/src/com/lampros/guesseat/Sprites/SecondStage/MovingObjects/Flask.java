package com.lampros.guesseat.Sprites.SecondStage.MovingObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.MyCharacterStage2;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class Flask extends MovingObjects {

    private float stateTime;
    private Animation <TextureRegion> idleAnimation;
    private Array <TextureRegion> frames;
    private boolean setToDestroyFlask;
    private boolean destroyed;
    private Sound sound;
    private Texture spikes;
    private Vector2 velocity;
    //private static Integer lives;
    //private MyCharacterStage2 player;

    public Flask(SecondStage screen, float x, float y) {
        super(screen, x, y);
        frames = new Array <TextureRegion>();
        for(int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(screen.getAtlasFlask().findRegion("flask_big_red"), i * 18, 1, 16, 16));
        }
        idleAnimation = new Animation<TextureRegion>(0.4f, frames);
        //spikes = new Texture("Objects/Obstacles/spikes.png");
        stateTime = 0;
        setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
        setToDestroyFlask = false;
        destroyed = false;
        b2body.setActive(true);
        //lives=1;
        //velocity = new Vector2(0,-1);
    }

    public void update(float dt){
        stateTime += dt;
        /*if(lives ==0){
            setToDestroyFlask = true;
        }*/
        if(setToDestroyFlask && !destroyed){
            world.destroyBody(b2body); //auto bgazei bug ton hrwa
            //b2body = null;
            destroyed = true;
            stateTime = 0;
            setRegion(new TextureRegion(screen.getAtlasPoints().findRegion("evolved"), 1, 1, 300, 300));
            setBounds(getX(), getY(), 40 / GuessEat.PPM, 40 / GuessEat.PPM);
        }
        else if (!destroyed) {
            //b2body.setLinearVelocity(0,-1);
            setPosition((b2body.getPosition().x - getWidth() / 2 ), (b2body.getPosition().y - getHeight() / 2));
            setRegion(idleAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineObjects() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        //bdef.position.set((getX() + getWidth() / 2) / GuessEat.PPM, (getY() + getHeight() / 2) / GuessEat.PPM));
        bdef.type = BodyDef.BodyType.StaticBody;
        //bdef.gravityScale=0.0f; //gravity effect it works here
        b2body = world.createBody(bdef);
        //b2body.setGravityScale(1);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM);
        /*vertice[0] = new Vector2(-8, 8).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(8, 8).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(8, -8).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-8, -8).scl(1 / GuessEat.PPM);
        shape.set(vertice);*/
        fdef.shape = shape;
        fdef.isSensor= true;
        fdef.filter.categoryBits = GuessEat.FLASK_BIT;
        fdef.filter.maskBits = //GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BIT|
        GuessEat.GROUND_BIT ;
        //GuessEat.APPLEBALL_BIT;
                //GuessEat.OBJECT_BIT |

        //bdef.gravityScale=0.1f;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void hitOnTouch() {
            setToDestroyFlask = true;
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

}
