package com.lampros.guesseat.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.PlayScreen;
import com.lampros.guesseat.Sprites.Apple;
//import com.lampros.guesseat.Sprites.Egg;
import com.lampros.guesseat.Sprites.Fruit;
import com.lampros.guesseat.Sprites.Pie;
import com.lampros.guesseat.Sprites.Wogol;

/*import com.lampros.collision.Sprites.Enemies.FruitTest;
import com.lampros.collision.Sprites.FruitApple;
import com.lampros.collision.Sprites.FruitEgg;
import com.lampros.collision.Sprites.FruitPie;*/

//import com.lampros.collision.Sprites.advanceStage2;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */

public class B2WorldCreator implements Disposable {

    private Array <Apple> apples;
    private Array <Pie> pies;
    private Array <Wogol> wogols;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //ground
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.GROUND_BIT;
            body.createFixture(fdef);
        }


        //create apples
        apples = new Array<Apple>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            apples.add(new Apple(screen, rect.getX() / GuessEat.PPM, rect.getY() / GuessEat.PPM));
        }

        //create pies
        pies = new Array<Pie>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            pies.add(new Pie(screen, rect.getX() / GuessEat.PPM, rect.getY() / GuessEat.PPM));
        }

        //create eggs
       /* eggs = new Array<Egg>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            eggs.add(new Egg(screen, rect.getX() / GuessEat.PPM, rect.getY() / GuessEat.PPM));
        }*/

        //wogols
        wogols = new Array <Wogol>();
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            wogols.add(new Wogol(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }

        //fake walls
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.WALLS_BIT;
            body.createFixture(fdef);
        }

        //advance level
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.ADVANCE_LEVEL_BIT;
            body.createFixture(fdef);
        }

    }

    public Array<Wogol> getWogols() {
        return wogols;
    }

    public Array <Fruit> getFruits(){
        Array<Fruit> fruits = new Array<Fruit>();
        fruits.addAll(apples);
        fruits.addAll(pies);
        //fruits.addAll(eggs);
        return fruits;
    }

    @Override
    public void dispose() {

    }
       /* public Array <Apple> getApples() {
            return apples;
        }

        public Array<Pie> getPies() {
            return pies;
        }

        public Array<Egg> getEggs() {
            return eggs;
        }*/
}



