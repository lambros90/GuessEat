package com.lampros.guesseat.Sprites.SecondStage.MovingObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.MyCharacterStage2;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public abstract class MovingObjects extends Sprite {

    protected World world;
    protected SecondStage screen;
    public Body b2body;
    public Vector2 velocity;

    public MovingObjects(SecondStage screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineObjects();
        velocity = new Vector2(0, -0.5f);
        b2body.setActive(false);
    }

    protected abstract void defineObjects();
    public abstract void hitOnTouch();
    public abstract void update(float dt);

   /* public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = - velocity.x;
        if(y)
            velocity.y = - velocity.y;
    }*/

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
