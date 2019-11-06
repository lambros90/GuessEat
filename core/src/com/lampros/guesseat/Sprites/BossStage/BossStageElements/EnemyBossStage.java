package com.lampros.guesseat.Sprites.BossStage.BossStageElements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lampros.guesseat.Screens.BossStage;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public abstract class EnemyBossStage extends Sprite {

    protected World world;
    protected BossStage screen;
    public Body b2body;
    public Vector2 velocity;
    public Vector2 velocity2;

    public EnemyBossStage(BossStage screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        //velocity = new Vector2(0.1f, b2body.getLinearVelocity().y);
        velocity2 = new Vector2(0.8f,0f);
        //b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnTouch();
    public abstract void hitOnRelease();
    public abstract void fire();

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

    public void reverseVelocity2(boolean x, boolean y){
        if(x)
            velocity2.x = -velocity2.x;
        if(y)
            velocity2.y = -velocity2.y;
    }

    //private float distance = b2body.getPosition().x;
    //boolean bl = false;
    //.......
    //public void moveforward(float dt){
    //        if (bl==false) {
    //           b2body.setLinearVelocity(new Vector2(.5f,0));
    //       }
    //    }
    //
    //    public void moveback(float dt){
    //        if (bl==true) {
    //            b2body.setLinearVelocity(new Vector2(-.5f,0));
    //        }
    //    }
    // public void update(float dt){
    //     ......
    //    else if (!destoryed) {
    //            float d = b2body.getPosition().x;
    //            moveforward(dt);
    //            if (d - distance >= 0.5) {
    //                bl = true;
    //            }
    //
    //            moveback(dt);
    //            if (d - distance <= -0.5) {
    //                bl = false;
    //            }
    //            ..........
    //}

}
