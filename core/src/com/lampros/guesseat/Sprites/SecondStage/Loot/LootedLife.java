package com.lampros.guesseat.Sprites.SecondStage.Loot;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.HudStage2;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.MyCharacterStage2;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class LootedLife extends Loot {
    private Sound soundLife;

    public LootedLife(SecondStage screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlasLife().findRegion("life"), 1, 1, 16, 16);
        //velocity = new Vector2(0, 0.1f);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //body.setLinearVelocity(velocity);

    }

    @Override
    public void defineLoot() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.gravityScale=1;
        //bdef.angularVelocity =0.5f;
        //bdef.gravityScale=0.0f; //gravity effect it works here
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM);
        /*fdef.restitution=0.6f;
        fdef.density = 0.5f;
        fdef.friction=0.4f;*/
        bdef.gravityScale=1;
        fdef.filter.categoryBits = GuessEat.LOOT_BIT; //maybe change it to apple_bit
        fdef.filter.maskBits = GuessEat.MY_CHARACTER_BIT | GuessEat.GROUND_BIT;

        fdef.shape = shape;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void use(MyCharacterStage2 player) {
        soundLife = GuessEat.manager.get("audio/sounds/pick_up_health.mp3", Sound.class);
        soundLife.play();
        destroy();
        //Gdx.app.log("test","destroy");
        HudStage2.addLives();

    }
}
