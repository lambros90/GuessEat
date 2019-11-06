package com.lampros.guesseat.Sprites.BossStage.MovingObjectsBoss;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.BossStage;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.SecondStage.MovingObjects.MovingObjects;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class SpikesBoss extends MovingObjectsBoss {

    private float stateTime;
    private Animation <TextureRegion> idleAnimation;
    private Array <TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Sound sound;
    private Texture spikes;
    private Vector2 velocity;
    //private MyCharacterStage2 player;

    public SpikesBoss(BossStage screen, float x, float y) {
        super(screen, x, y);
        frames = new Array <TextureRegion>();
        //for(int i = 1; i < 7; i++){
        frames.add(new TextureRegion(screen.getAtlasSpikes().findRegion("spikesv2"), 1, 1, 29, 16));
        idleAnimation = new Animation<TextureRegion>(0.4f, frames);
        //spikes = new Texture("Objects/Obstacles/spikes.png");
        stateTime = 0;
        setBounds(0, 0, 16 / GuessEat.PPM, 16 / GuessEat.PPM);
        setToDestroy = false;
        destroyed = false;
        //velocity = new Vector2(0,-1);
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
            b2body.setLinearVelocity(0,-1.5f);
            setPosition((b2body.getPosition().x - getWidth() / 2 ), (b2body.getPosition().y - getHeight() / 2));
            setRegion(idleAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineObjects() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        //bdef.position.set((getX() + getWidth() / 2) / GuessEat.PPM, (getY() + getHeight() / 2) / GuessEat.PPM));
        bdef.type = BodyDef.BodyType.KinematicBody;
        //bdef.gravityScale=0.0f; //gravity effect it works here
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-8, 8).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(8, 8).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(8, -8).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-8, -8).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        fdef.shape = shape;
        fdef.filter.categoryBits = GuessEat.GROUND_BIT;
        fdef.filter.maskBits = //GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BOSS_STAGE_BIT;
                //GuessEat.OBJECT_BIT |

        //bdef.gravityScale=0.1f;
        b2body.createFixture(fdef).setUserData(this);

        /*FixtureDef fdef2 = new FixtureDef();
        PolygonShape shape2 = new PolygonShape();
        Vector2[] vertice2 = new Vector2[4];
        vertice[0] = new Vector2(-8, 8).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(8, 8).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(8, -8).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-8, -8).scl(1 / GuessEat.PPM);
        shape2.set(vertice2);
        fdef2.shape = shape2;
        fdef2.filter.categoryBits = GuessEat.OBSTACLE_BIT;
        fdef2.filter.maskBits = //GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BIT;
        //GuessEat.OBJECT_BIT |

        //bdef.gravityScale=0.1f;
        b2body.createFixture(fdef2).setUserData(this);
*/
        //character's feet
        FixtureDef fdef2 = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-8 / GuessEat.PPM, -8 / GuessEat.PPM), new Vector2(8 / GuessEat.PPM, -8 / GuessEat.PPM));
        //fdef2.filter.categoryBits = GuessEat.MY_CHARACTER_FEET_BIT;
        fdef2.shape = feet;
        fdef2.filter.categoryBits = GuessEat.OBSTACLE_BIT;
        fdef2.filter.maskBits = //GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BOSS_STAGE_BIT | GuessEat.APPLEBALL_BIT;
        //GuessEat.OBJECT_BIT |
        fdef2.isSensor = false; //it ensures that the feet collide with the ground. If it were a sensor, it would not collide with the ground, but the whole point of adding feet is to provide a smoother surface for mario to use to slide on rough surfaces, like a sled on rough snow.
        b2body.createFixture(fdef2).setUserData(this);

       /* CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM); //convert it to polygon
        fdef.filter.categoryBits = GuessEat.OBSTACLE_BIT; //maybe change it to apple_bit
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BIT |
                //GuessEat.OBJECT_BIT |
                GuessEat.APPLE_BIT |
                GuessEat.MY_CHARACTER_RIGHT_BIT |
                GuessEat.MY_CHARACTER_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


 //Create the interactive part of the fruit (bottom part)
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
        b2body.createFixture(fdef).setUserData(this);
*/


    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnTouch() {
        //player.defineCharacter();
        //bdef.position.set(32 / GuessEat.PPM, 100 / GuessEat.PPM);
        //MyCharacterStage2.defineCharacter();
        //sound = GuessEat.manager.get("audio/sounds/Apple_Bite-Simon_Craggs-1683647397.wav", Sound.class);
        //sound.play(0.1f);

    }
}
