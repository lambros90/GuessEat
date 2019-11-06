package com.lampros.guesseat.Sprites.BossStage.BossStageElements;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.BossStage;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class BossFireBall extends Sprite {

    private BossStage screen;
    private World world;
    private Array<TextureRegion> frames;
    private Animation <TextureRegion> fireAnimation;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private boolean fireRight;
    private Body b2body;

    public BossFireBall(BossStage screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();
        frames = new Array<TextureRegion>();
        /*for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlasFireball().findRegion("fireball"), i * 8, 1, 8, 8));
        }*/
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlasWhiteFireball().findRegion("fireball"), i * 256, 0, 256, 256));
        }
        fireAnimation = new Animation<TextureRegion>(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 15 / GuessEat.PPM, 15 / GuessEat.PPM);
        defineBossFireBall();
    }

    private void defineBossFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 /GuessEat.PPM : getX() - 12 /GuessEat.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM);
        fdef.filter.categoryBits = GuessEat.OBSTACLE_BOSS_BIT;
        fdef.filter.maskBits = GuessEat.GROUND_BIT | GuessEat.MY_CHARACTER_BOSS_STAGE_BIT;

        fdef.shape = shape;
        //fdef.restitution = 1;
        //fdef.friction = 0;
        b2body.setGravityScale(0);
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 2.5f : -2.5f, 0));//2.5f));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            //return;
        }
        /*stateTime += dt;
        setRegion(fireAnimation  .getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);*/
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }


}
