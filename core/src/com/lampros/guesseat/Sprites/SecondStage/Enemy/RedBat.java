package com.lampros.guesseat.Sprites.SecondStage.Enemy;

import com.badlogic.gdx.Gdx;
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
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.MyCharacterStage2;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootDefinition;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootedGoldenApple;
import com.lampros.guesseat.Sprites.SecondStage.Loot.LootedLife;

import static com.lampros.guesseat.Sprites.MyCharacterStage2.randomCounter;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class  RedBat extends Enemy {

    private float stateTime;
    private Animation<TextureRegion> flyAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Integer lives;
    private boolean runningRight;
    private double random;


    public RedBat(SecondStage screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        /*for (int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlasRedBat()()().findRegion("wizzard_m_run_anim_f0"), i * 18, 1, 16, 28));
        }*/
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 0, 468, 680, 468));
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 0, 0, 680, 468));
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 680, 468, 680, 468));
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 680, 0, 680, 468));
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 1360, 468, 680, 468));
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 1360, 0, 680, 468));
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 2048, 468, 680, 468));
        frames.add(new TextureRegion(screen.getAtlasRedBat().findRegion("output-onlinepngtools (11)"), 2048, 0, 680, 468));
        flyAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM); //16 sets the size of the character
        setToDestroy = false;
        destroyed = false;
        lives = 3;
        random = 0;


        //runningRight = true;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
           // setRegion(new TextureRegion(screen.getAtlasDead().findRegion("skeletonhead"), 0, 0,256,256));
           // setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
            //setRegion();
        }
        else if(!destroyed) {
            /*if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !flyAnimation.getKeyFrame(stateTime, true).isFlipX()) {
                flyAnimation.getKeyFrame(stateTime,true).flip(true, false);
                runningRight = false;
            } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && flyAnimation.getKeyFrame(stateTime, true).isFlipX()) {
                flyAnimation.getKeyFrame(stateTime,true).flip(true, false);
                runningRight = true;
            }*/
            b2body.setLinearVelocity(velocity2);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(flyAnimation.getKeyFrame(stateTime, true));
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
/*
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GuessEat.PPM);*/
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-7f, 7.5f).scl(1 / GuessEat.PPM);
        vertice[1] = new Vector2(5.5f, 7.5f).scl(1 / GuessEat.PPM);
        vertice[2] = new Vector2(5.5f, -7.5f).scl(1 / GuessEat.PPM);
        vertice[3] = new Vector2(-7f, -7.5f).scl(1 / GuessEat.PPM);
        shape.set(vertice);
        fdef.shape = shape;

        fdef.filter.categoryBits = GuessEat.WIZARD_BIT;
        //fdef.isSensor = true;

        //fdef.density =10;
        fdef.filter.maskBits = GuessEat.GROUND_BIT |
                GuessEat.FRUIT_BIT |
                GuessEat.MY_CHARACTER_BIT |
                GuessEat.OGRE_BIT |
                GuessEat.WALLS_BIT |
                GuessEat.APPLEBALL_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void hitOnTouch() {
       // setBounds(getX(), getY(), 8 / GuessEat.PPM, 8 / GuessEat.PPM);
        if (lives < 1) {
            random = (Math.random() * ((3800 - 450) + 1)) +450;
            if(b2body.getPosition().x > random / GuessEat.PPM && MyCharacterStage2.randomCounter < 2) {
                screen.spawnLoot(new LootDefinition(new Vector2(b2body.getPosition().x, b2body.getPosition().y + 16 / GuessEat.PPM), LootedLife.class));
                setToDestroy = true;
                MyCharacterStage2.setRandomCounter(randomCounter+1);
                Gdx.app.log("heart spawn", "");
                System.out.println(MyCharacterStage2.getRandomCounter());
            }
            else
                screen.spawnLoot(new LootDefinition(new Vector2(b2body.getPosition().x, b2body.getPosition().y + 16 / GuessEat.PPM), LootedGoldenApple.class));
                setToDestroy = true;
        }
        else
            lives --;
    }

    @Override
    public void hitOnRelease() {
        setBounds(getX(), getY(), 16 / GuessEat.PPM, 16 / GuessEat.PPM);
    }

    public void draw(Batch batch){
        if(!destroyed )//|| stateTime < 1)
            super.draw(batch);
    }

}
