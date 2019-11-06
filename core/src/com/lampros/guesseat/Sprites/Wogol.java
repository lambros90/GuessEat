package com.lampros.guesseat.Sprites;

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
import com.lampros.guesseat.Screens.PlayScreen;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class Wogol extends Enemy1stStage {


    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Integer lives;
    private boolean runningRight;

    public Wogol(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        /*for (int i = 4; i < 8; i++){
            frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("wogol_idle_anim_f0"), i * 18, 1, 16, 20));
        }*/
        frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("output-onlinepngtools (2)"), 0, 448, 680, 448));
        frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("output-onlinepngtools (2)"), 0, 0, 680, 448));
        frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("output-onlinepngtools (2)"), 680, 448, 680, 448));
        frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("output-onlinepngtools (2)"), 680, 0, 680, 448));
        frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("output-onlinepngtools (2)"), 1360, 448, 680, 448));
        frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("output-onlinepngtools (2)"), 1360, 0, 680, 448));
        frames.add(new TextureRegion(screen.getAtlasOrc().findRegion("output-onlinepngtools (2)"), 2040, 448, 680, 448));

        walkAnimation = new Animation<TextureRegion>(0.1f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 30 / GuessEat.PPM, 22 / GuessEat.PPM); //16 sets the size of the character
        setToDestroy = false;
        destroyed = false;
        lives = 3;

        runningRight = true;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
            //setRegion(new TextureRegion(screen.getAtlasDead().findRegion("skull"), 1, 1,16,16));
            setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
            //setRegion();
        }
        else if(!destroyed) {
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

      /*  FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM);*/
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(5.5f, 10.5f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(5.5f, -7.5f).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-10.5f, -7.5f).scl(1 / GuessEat.PPM);
        shape.set(vertice);

        fdef.filter.categoryBits = GuessEat.WOGOL_BIT;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.MY_CHARACTER_BIT |
                GuessEat.WALLS_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void hitOnTouch() {
        if (lives < 1) {
            setToDestroy = true;
        }
        else
            lives --;
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }
}
