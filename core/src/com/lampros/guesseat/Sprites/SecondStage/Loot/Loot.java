package com.lampros.guesseat.Sprites.SecondStage.Loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.MyCharacterStage2;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public abstract class Loot extends Sprite {
    protected SecondStage screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Loot(SecondStage screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
        defineLoot();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineLoot();
    public abstract void use(MyCharacterStage2 player);

    public void update(float dt){
        if(toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;

        }
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){ //can only destroy objects inside update method
        toDestroy = true;
    }
}
