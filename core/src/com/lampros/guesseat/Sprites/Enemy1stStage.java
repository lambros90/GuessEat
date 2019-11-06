package com.lampros.guesseat.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lampros.guesseat.Screens.PlayScreen;
import com.lampros.guesseat.Screens.SecondStage;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public abstract class Enemy1stStage extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    public Vector2 velocity2;

    public Enemy1stStage(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(0.5F, 0);
        velocity2 = new Vector2(0,1);
        //b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnTouch();

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
}
