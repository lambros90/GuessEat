package com.lampros.guesseat.Sprites.SecondStage.Enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootDefinition;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootedApple;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class Knight extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Integer lives;
    private boolean runningRight;


    public Knight(SecondStage screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        /*for (int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlasKnight().findRegion("ogre_idle_anim_f0"), i * 32, 1, 32, 32));
        }*/
        /*for (int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlasMonsterBlue().findRegion("run"), i * 243, 0, 243, 242));
        }*/
        frames.add(new TextureRegion(screen.getAtlasKnight().findRegion("1"), 0, 512, 512, 512));
        frames.add(new TextureRegion(screen.getAtlasKnight().findRegion("1"), 0, 0, 512, 512));
        frames.add(new TextureRegion(screen.getAtlasKnight().findRegion("1"), 512, 512, 512, 512));
        frames.add(new TextureRegion(screen.getAtlasKnight().findRegion("1"), 512, 0, 512, 512));
        frames.add(new TextureRegion(screen.getAtlasKnight().findRegion("1"), 1024, 512, 512, 512));
        walkAnimation = new Animation<TextureRegion>(0.1f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 32 / GuessEat.PPM, 27 / GuessEat.PPM); //16 sets the size of the character
        setToDestroy = false;
        destroyed = false;
        lives = 2;

        runningRight = true;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
            Filter filter = new Filter();
            filter.maskBits = GuessEat.NOTHING_BIT;
            for (Fixture fixture : b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.applyLinearImpulse(new Vector2(0, 1), b2body.getWorldCenter(), true);
            //setRegion(new TextureRegion(screen.getAtlasDead().findRegion("greensplash"), 0, 0,880,761));

            //setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
            //setRegion();
        }
        else if(!destroyed) {
           /* if(getX()   < 128 / GuessEat.PPM){
                reverseVelocity(true,false);}
            if (b2body.getPosition().x > 220 / GuessEat.PPM){
                reverseVelocity(true,false);
            }*/
            if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !walkAnimation.getKeyFrame(stateTime, true).isFlipX()) {
                walkAnimation.getKeyFrame(stateTime,true).flip(true, false);
                runningRight = false;
            } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && walkAnimation.getKeyFrame(stateTime, true).isFlipX()) {
                 walkAnimation.getKeyFrame(stateTime,true).flip(true, false);
                 runningRight = true;
             }
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }


    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        //bdef.position.set(45 / GuessEat.PPM, 32 / GuessEat.PPM);
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        //b2body.setAngularVelocity(-90 * DEGTORAD);

    /*    FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / GuessEat.PPM);

*/

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-7f, 8.f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(5.5f, 8.f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(5.5f, -7.5f).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-7f, -7.5f).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        fdef.shape = shape;

        fdef.filter.categoryBits = GuessEat.OGRE_BIT; //fix the moving by the fireball
        //fdef.isSensor = true;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BIT |
                GuessEat.OGRE_BIT |
                GuessEat.WALLS_BIT |
                GuessEat.APPLEBALL_BIT;

        //fdef.density =10;
       // b2body.setGravityScale(1);
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void hitOnTouch() {
        //setBounds(getX(), getY(), 8 / GuessEat.PPM, 8 / GuessEat.PPM);
        if (lives < 1) {

            screen.spawnLoot(new LootDefinition(new Vector2(b2body.getPosition().x, b2body.getPosition().y + 16 / GuessEat.PPM),
                    LootedApple.class));
            setToDestroy = true;
        }
        else
            lives --;
    }

    public void hitOnRelease(){
       // setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
    }

    public void draw(Batch batch){
        if(!destroyed )//|| stateTime < 1)
            super.draw(batch);
    }
}
