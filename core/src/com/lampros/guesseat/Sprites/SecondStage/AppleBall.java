package com.lampros.guesseat.Sprites.SecondStage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.SecondStage;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class AppleBall extends Sprite {

    private SecondStage screen;
    private World world;
    private Array <TextureRegion> frames;
    private Animation <TextureRegion> fireAnimation;
    private float stateTime;
    private boolean  destroyed;
    private static boolean setToDestroyApple;
    private boolean fireRight;
    private Body b2body;

    public AppleBall(SecondStage screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlasApple().findRegion("apple"), i * 18, 1, 16, 16));
        }
        fireAnimation = new Animation<TextureRegion>(  0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 10 / GuessEat.PPM, 10 / GuessEat.PPM);
        defineAppleball();
    }

    private void defineAppleball(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 /GuessEat.PPM : getX() - 12 /GuessEat.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / GuessEat.PPM);
        fdef.filter.categoryBits = GuessEat.APPLEBALL_BIT;
        fdef.filter.maskBits = GuessEat.OGRE_BIT |
        GuessEat.WIZARD_BIT |
        GuessEat.GROUND_BIT;
        //GuessEat.FLASK_BIT  ;

        fdef.shape = shape;
        fdef.density = 1;
        //fdef.restitution = 1;
        //fdef.friction = 0;
        b2body.setGravityScale(0);
        //fdef.isSensor=true;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 4 : -4, 0));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //if((stateTime > 2 || setToDestroyApple) && !destroyed) {
        if((stateTime > 2 || setToDestroyApple) && !destroyed) {

                //setRegion(new TextureRegion(screen.getAtlasPoints().findRegion("evolved"), 1, 605, 300, 300));
            //setBounds(getX(), getY(), 32 / GuessEat.PPM, 32 / GuessEat.PPM);
            world.destroyBody(b2body);
            destroyed = true;

            setToDestroyAppleFalse();
            Gdx.app.log("AppleBall updated method","setToDestroyApple == true && setToDestroyApple becomes False");
            // b2body = null;
            //return;

        }
        /*stateTime += dt;
        setRegion(fireAnimation  .getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
*/
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x,2f);
        //if((fireRight && b2body.getLinearVelocity().x < 0 ) || (!fireRight && b2body.getLinearVelocity().x > 0))
          //  setToDestroyApple();
//        if(b2body.getLinearVelocity().y > 2f)
  //            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        /*if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroyApple();*/
    }

    public static void setToDestroyApple(){
        setToDestroyApple = true;
    }

    public static void setToDestroyAppleFalse(){
        setToDestroyApple = false;
    }

    public boolean isDestroyed(){
        return destroyed;
    }


}