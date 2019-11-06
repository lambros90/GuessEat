package com.lampros.guesseat.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Disposable;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Scenes.Hud;
import com.lampros.guesseat.Scenes.HudStage2;
import com.lampros.guesseat.Scenes.HudStage3;
import com.lampros.guesseat.Screens.BossStage;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.Apple;
import com.lampros.guesseat.Sprites.BossStage.BossStageElements.BossApple;
import com.lampros.guesseat.Sprites.BossStage.BossStageElements.EnemyBossStage;
import com.lampros.guesseat.Sprites.BossStage.BossStageElements.MyBoss;
import com.lampros.guesseat.Sprites.Enemy1stStage;
import com.lampros.guesseat.Sprites.Fruit;
import com.lampros.guesseat.Sprites.MyCharacter;
import com.lampros.guesseat.Sprites.MyCharacterStage2;
import com.lampros.guesseat.Sprites.MyCharacterStage3;
import com.lampros.guesseat.Sprites.SecondStage.AppleBall;
import com.lampros.guesseat.Sprites.SecondStage.Enemy.Enemy;
import com.lampros.guesseat.Sprites.SecondStage.Loot.Loot;
import com.lampros.guesseat.Sprites.SecondStage.MovingObjects.MovingObjects;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class CollisionListener implements ContactListener, Disposable {

    private GuessEat game;
    private Sound soundZap;
    private Sound soundSplash;
    private Sound soundEvolve;
    private Sound soundApple;

    public CollisionListener(GuessEat game) {
        this.game = game;
    }

    /**
     * Created by brentaureli on 25/11/15. https://github.com/BrentAureli/SuperMario
     */

    @Override
    public void beginContact(Contact contact) {

        //get the fixtures of the collisions
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //use OR on category bits
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

            switch (cDef) {
                case GuessEat.MY_CHARACTER_BOSS_STAGE_BIT| GuessEat.OBSTACLE_BOSS_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BOSS_STAGE_BIT) {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage3) fixA.getUserData()).hit();
                        HudStage3.removeLives();
                       // MyCharacterStage2.setDeathReasonStage2(true);
                        Gdx.app.log("character stage 3 died once by BOSS", "");
                    } else {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage3) fixB.getUserData()).hit();
                        HudStage3.removeLives();
                        //MyCharacterStage2.setDeathReasonStage2(true);
                        Gdx.app.log("character stage 3 died once by BOSS", "");
                    }
                    break;
                case GuessEat.APPLEBALL_BIT | GuessEat.GROUND_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.APPLEBALL_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        AppleBall.setToDestroyApple();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("appleANYstage destroyed by ground", "");
                    } else {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        AppleBall.setToDestroyApple();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("appleANYstage destroyed by ground", "");
                    }
                    break;
                case GuessEat.APPLEBALL_BIT | GuessEat.MOVING_PLATFORM_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.APPLEBALL_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        AppleBall.setToDestroyApple();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("apple destroyed by platform", "");
                    } else {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        AppleBall.setToDestroyApple();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("apple destroyed by platform", "");
                    }
                    break;
            /*    case GuessEat.MY_CHARACTER_BOSS_STAGE_BIT | GuessEat.MOVING_PLATFORM_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BOSS_STAGE_BIT) {
                        Gdx.app.log("World Contact Listener", "Character is on the platform");
                        //contact.setFriction(100);
                        //contact.setFriction(100);
                        fixA.setFriction(100);
                        fixB.setFriction(100);
                    } else {
                        Gdx.app.log("World Contact Listener", "Character is on the platform");
                        //contact.setFriction(100);
                        fixB.setFriction(100);
                        fixA.setFriction(100);
                        //contact.setFriction(100);
                    }
                    break;*/
                case GuessEat.APPLEBALL_BIT | GuessEat.OBSTACLE_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.APPLEBALL_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        AppleBall.setToDestroyApple();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("apple destroyed by obstacle", "");
                    } else {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        AppleBall.setToDestroyApple();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("apple destroyed by obstacle", "");
                    }
                    break;
                case GuessEat.OBSTACLE_BOSS_BIT | GuessEat.WALLS_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.OBSTACLE_BOSS_BIT) {
                        MyBoss.reverseVelocity(true, false);
                       // Gdx.app.log("boss changed direction because of walls", "");
                    } else {
                        MyBoss.reverseVelocity(true, false);
                       // Gdx.app.log("boss changed direction because of walls", "");
                    }
                    break;
                case GuessEat.FRUIT_BIT | GuessEat.MY_CHARACTER_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.FRUIT_BIT) {
                        ((Fruit) fixA.getUserData()).hitOnTouch();
                        Hud.addApples(1);
                        Gdx.app.log("character stage 1 grabbed an apple", "");
                    } else
                    {
                        ((Fruit) fixB.getUserData()).hitOnTouch();
                        Hud.addApples(1);
                        Gdx.app.log("character stage 1 grabbed an apple", "");
                    }
                    break;
                    //EDW BGAZEI BUG O HERO EMFANIZETAI RANDOM
                case GuessEat.FLASK_BIT | GuessEat.MY_CHARACTER_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.FLASK_BIT) {
                        ((MovingObjects) fixA.getUserData()).hitOnTouch();
                        Gdx.app.log("FLASK WAS GRABBED DISAPPEAR NOW", "");
                    } else {
                        ((MovingObjects) fixB.getUserData()).hitOnTouch();
                        Gdx.app.log("FLASK WAS GRABBED DISAPPEAR NOW", "");
                    }
                    break;
                case GuessEat.MY_CHARACTER_BIT | GuessEat.OBSTACLE_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage2) fixA.getUserData()).hit();
                        if (MyCharacterStage2.lavaDamage == true) {
                            Gdx.app.log("character stage 2 died once by obstacle", "");
                            HudStage2.removeLives();
                            MyCharacterStage2.setLavaDamage(false);
                        }
                    } else {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage2) fixB.getUserData()).hit();
                        if (MyCharacterStage2.lavaDamage == true) {
                            Gdx.app.log("character stage 2 died once by obstacle", "");
                            HudStage2.removeLives();
                            MyCharacterStage2.setLavaDamage(false);
                        }
                    }
                    break;
                case GuessEat.MY_CHARACTER_BOSS_STAGE_BIT | GuessEat.OBSTACLE_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BOSS_STAGE_BIT) {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage3) fixA.getUserData()).hit();
                        if (MyCharacterStage3.lavaDamageStage3 == true) {
                            Gdx.app.log("character stage 3 died once by obstacle", "");
                            HudStage3.removeLives();
                            MyCharacterStage3.setLavaDamageStage3(false);
                        }
                    } else {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage3) fixB.getUserData()).hit();
                        if (MyCharacterStage3.lavaDamageStage3 == true) {
                            Gdx.app.log("character stage 3 died once by obstacle", "");
                            HudStage3.removeLives();
                            MyCharacterStage3.setLavaDamageStage3(false);
                        }
                    }
                    break;
                case GuessEat.OGRE_BIT | GuessEat.APPLEBALL_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.OGRE_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        ((Enemy) fixA.getUserData()).hitOnTouch();
                        AppleBall.setToDestroyApple();
                        Gdx.app.log("apple destroyed by ogre", "");
                        /*AppleBall.setToDestroyApple(true);
                        AppleBall.setToDestroyApple(false);*/
                    }
                    else {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        ((Enemy) fixB.getUserData()).hitOnTouch();
                        AppleBall.setToDestroyApple();
                        Gdx.app.log("apple destroyed by ogre", "");
                        /*AppleBall.setToDestroyApple(true);
                        AppleBall.setToDestroyApple(false);*/
                    }
                    //Gdx.app.log("sdad", "sdad");}
                    break;
                case GuessEat.OGRE_BIT | GuessEat.GROUND_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.OGRE_BIT) {
                        ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                       // Gdx.app.log("ogre changed direction because of ground", "");
                    } else {
                        ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                       // Gdx.app.log("ogre changed direction because of ground", "");
                    }
                    break;
                case GuessEat.OGRE_BIT | GuessEat.WALLS_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.OGRE_BIT) {
                        ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                       // Gdx.app.log("ogre changed direction because of walls", "");
                    } else {
                        ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                       // Gdx.app.log("ogre changed direction because of walls", "");
                    }
                    break;
                case GuessEat.WIZARD_BIT | GuessEat.WALLS_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.WIZARD_BIT) {
                        ((Enemy) fixA.getUserData()).reverseVelocity2(false, true);
                       // Gdx.app.log("wizard changed direction because of walls", "");
                    } else {
                        ((Enemy) fixB.getUserData()).reverseVelocity2(false, true);
                      //  Gdx.app.log("wizard changed direction because of walls", "");
                    }
                    break;
                case GuessEat.MY_CHARACTER_BIT | GuessEat.OGRE_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage2) fixA.getUserData()).hit();
                        HudStage2.removeLives();
                        //MyCharacterStage2.setDeathReasonStage2(true);
                        Gdx.app.log("character stage 2 died once by ogre", "");
                    } else {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage2) fixB.getUserData()).hit();
                        HudStage2.removeLives();
                       // MyCharacterStage2.setDeathReasonStage2(true);
                        Gdx.app.log("character stage 2 died once by ogre", "");
                    }
                    break;
                case GuessEat.MY_CHARACTER_BIT | GuessEat.WIZARD_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage2) fixA.getUserData()).hit();
                        HudStage2.removeLives();
                        //MyCharacterStage2.setDeathReasonStage2(true);
                        Gdx.app.log("character stage 2 died once by wizard", "");
                    } else {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacterStage2) fixB.getUserData()).hit();
                        HudStage2.removeLives();
                        //MyCharacterStage2.setDeathReasonStage2(true);
                        Gdx.app.log("character stage 2 died once by wizard", "");
                    }
                    break;
                case GuessEat.WIZARD_BIT | GuessEat.GROUND_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.WIZARD_BIT) {
                        ((Enemy) fixA.getUserData()).reverseVelocity2(false, true);
                      //  Gdx.app.log("wizard changed direction because of ground", "");
                    } else {
                        ((Enemy) fixB.getUserData()).reverseVelocity2(false, true);
                        //Gdx.app.log("wizard changed direction because of ground", "");
                    }
                    break;
                case GuessEat.WIZARD_BIT | GuessEat.APPLEBALL_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.WIZARD_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        ((Enemy) fixA.getUserData()).hitOnTouch();
                        AppleBall.setToDestroyApple();
                        Gdx.app.log("apple destroyed by", "wizard");
                    }
                    else {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        ((Enemy) fixB.getUserData()).hitOnTouch();
                        AppleBall.setToDestroyApple();
                        Gdx.app.log("apple destroyed by", "wizard");
                    }
                    break;
                case GuessEat.WOGOL_BIT | GuessEat.WALLS_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.WOGOL_BIT) {
                        ((Enemy1stStage) fixA.getUserData()).reverseVelocity(true, false);
                    }
                    else{//(fixB.getFilterData().categoryBits == GuessEat.FRUIT_HEAD_BIT){
                        ((Enemy1stStage) fixB.getUserData()).reverseVelocity(true, false);
                    }
                    break;
                case GuessEat.MY_CHARACTER_BIT | GuessEat.WOGOL_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacter) fixA.getUserData()).hit();
                        Hud.removeLives();
                        MyCharacter.setDeathReason(true);
                        Gdx.app.log("CollisionListener", "Orc hit Character");
                    }
                    else {
                        soundZap = GuessEat.manager.get("audio/sounds/enemy_collision.mp3", Sound.class);
                        soundZap.play();
                        ((MyCharacter) fixB.getUserData()).hit();
                        Hud.removeLives();
                        MyCharacter.setDeathReason(true);
                        Gdx.app.log("CollisionListener", "Orc hit Character");
                    }
                    break;

                case GuessEat.MY_CHARACTER_BIT | GuessEat.ADVANCE_LEVEL_BIT:
                    if(fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        Hud.setAppleBalls(Hud.getScore());
                        //HudStage2.setWorldTimer(60);
                        game.setScreen(new SecondStage(game));
                        dispose();
                        //Gdx.app.log("advance", "stage");
                    }
                    else {
                        Hud.setAppleBalls(Hud.getScore());
                        //HudStage2.setWorldTimer(60);
                        game.setScreen(new SecondStage(game));
                        dispose();
                        //Gdx.app.log("advance", "stage");
                    }
                    break;
                case GuessEat.LOOT_BIT | GuessEat.MY_CHARACTER_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.LOOT_BIT) {
                        soundApple = GuessEat.manager.get("audio/sounds/apple_bite.wav", Sound.class);
                        soundApple.play();
                        ((Loot) fixA.getUserData()).use((MyCharacterStage2) fixB.getUserData());
                        Gdx.app.log("character grabbed a loot", "");
                    }
                    else {
                        soundApple = GuessEat.manager.get("audio/sounds/apple_bite.wav", Sound.class);
                        soundApple.play();
                        ((Loot) fixB.getUserData()).use((MyCharacterStage2) fixA.getUserData());
                        Gdx.app.log("character grabbed a loot", "");
                    }
                        break;
                case GuessEat.MY_CHARACTER_BIT | GuessEat.ADVANCE_LEVEL2_BIT:
                    if(fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        //HudStage3.setAppleBalls(Hud.getScore());
                        //HudStage2.setWorldTimer(60);
                        Gdx.app.log("character advanced to level 3", "");
                        game.setScreen(new BossStage(game));
                        dispose();
                        //Gdx.app.log("advance", "stage3");
                    }
                    else {
                        //Hud.setAppleBalls(Hud.getScore());
                        //HudStage2.setWorldTimer(60);
                        Gdx.app.log("character advanced to level 3", "");
                        game.setScreen(new BossStage(game));
                        dispose();
                        //Gdx.app.log("advance", "stage3");
                    }
                    break;
                case GuessEat.APPLEBALL_BIT | GuessEat.FRUIT_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.APPLEBALL_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        if(HudStage2.getScore() == 2800){
                            MyBoss.setLives(MyBoss.getLives()-3);
                            Gdx.app.log("BOSS LOST 3 LIVES", "");
                            System.out.println(MyBoss.getLives());
                        }
                        else if (HudStage2.getScore() < 2800 && HudStage2.getScore() > 1800){
                            MyBoss.setLives(MyBoss.getLives()-2);
                            Gdx.app.log("BOSS LOST 2 LIVES", "");
                            System.out.println(MyBoss.getLives());
                        }
                        else {
                            //System.out.println(MyBoss.getLives());
                            //MyBoss.setLives(MyBoss.getLives()-1);
                            MyBoss.setLives(MyBoss.getLives()-1);
                            Gdx.app.log("BOSS LOST 1 LIFE", "1st if statement");
                            System.out.println(MyBoss.getLives());
                        }
                        //MyBoss.setLives(MyBoss.getLives()-1);

                        ((MyBoss) fixA.getUserData()).OnHit();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("apple destroyed by", "BOSS");
                    }
                    else {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        if(HudStage2.getScore() == 2800){
                            MyBoss.setLives(MyBoss.getLives()-3);
                            Gdx.app.log("BOSS LOST 3 LIVES", "");
                            System.out.println(MyBoss.getLives());
                        }
                        else if (HudStage2.getScore() < 2800 && HudStage2.getScore() > 1800){
                            MyBoss.setLives(MyBoss.getLives()-2);
                            Gdx.app.log("BOSS LOST 2 LIVES", "");
                            System.out.println(MyBoss.getLives());
                        }
                        else {
                            // System.out.println(MyBoss.getLives());
                            MyBoss.setLives(MyBoss.getLives()-1);
                            ((MyBoss) fixA.getUserData()).OnHit();
                            Gdx.app.log("BOSS LOST 1 LIFE", "else statement");
                            System.out.println(MyBoss.getLives());

                        }
                        //MyBoss.setLives(MyBoss.getLives()-1);
                        ((MyBoss) fixA.getUserData()).OnHit();
                        BossApple.setToDestroyApple();
                        Gdx.app.log("apple destroyed by", "BOSS");
                    }
                    break;
            }

        }

        @Override
        public void endContact (Contact contact) {
            //get the fixtures of the collisions
            Fixture fixA = contact.getFixtureA();
            Fixture fixB = contact.getFixtureB();

            //use OR on category bits
            int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

            switch (cDef) {
                case GuessEat.MY_CHARACTER_BIT | GuessEat.FLASK_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        soundEvolve = GuessEat.manager.get("audio/sounds/evolve.mp3", Sound.class);
                        soundEvolve.play();
                        ((MyCharacterStage2) fixA.getUserData()).evolve();
                        Gdx.app.log("character grabbed a flash", "");
                        //((MyCharacterStage2) fixA.getUserData()).evolve();
                        //Flask.setLives(0);
                        // Flask.setSetToDestroy(true);
                    } else {
                        soundEvolve = GuessEat.manager.get("audio/sounds/evolve.mp3", Sound.class);
                        soundEvolve.play();
                        ((MyCharacterStage2) fixA.getUserData()).evolve();
                        Gdx.app.log("character grabbed a flash", "");
                        // ((MyCharacterStage2) fixB.getUserData()).evolve();
                        //Flask.setLives(0);
                        // Flask.setSetToDestroy(true);
                    }
                    break;
               /* case GuessEat.OGRE_BIT | GuessEat.APPLEBALL_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.OGRE_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        ((Enemy) fixA.getUserData()).hitOnRelease();
                    }
                    else {
                        ((Enemy) fixB.getUserData()).hitOnRelease();}
                    break;
                case GuessEat.WIZARD_BIT| GuessEat.APPLEBALL_BIT:
                    if (fixA.getFilterData().categoryBits == GuessEat.WIZARD_BIT) {
                        soundSplash = GuessEat.manager.get("audio/sounds/splash.mp3", Sound.class);
                        soundSplash.play();
                        ((Enemy) fixA.getUserData()).hitOnRelease();
                    }
                    else {
                        ((Enemy) fixB.getUserData()).hitOnRelease();}
                    break;*/

              /*  case GuessEat.MY_CHARACTER_BIT | GuessEat.ADVANCE_LEVEL2_BIT:
                    if(fixA.getFilterData().categoryBits == GuessEat.MY_CHARACTER_BIT) {
                        //HudStage3.setAppleBalls(Hud.getScore());
                        //HudStage2.setWorldTimer(60);
                        game.setScreen(new BossStage(game));
                        dispose();
                        //Gdx.app.log("advance", "stage3");
                    }
                    else {
                        //Hud.setAppleBalls(Hud.getScore());
                        //HudStage2.setWorldTimer(60);
                        game.setScreen(new BossStage(game));
                        dispose();
                        //Gdx.app.log("advance", "stage3");
                    }
                    break;*/





            }
        }
        @Override
        public void preSolve (Contact contact, Manifold oldManifold){

        }

        @Override
        public void postSolve (Contact contact, ContactImpulse impulse){



        }

    @Override
    public void dispose() {

    }
}