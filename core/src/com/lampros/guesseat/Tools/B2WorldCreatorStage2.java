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
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Screens.SecondStage;
import com.lampros.guesseat.Sprites.SecondStage.Enemy.Knight;
import com.lampros.guesseat.Sprites.SecondStage.Enemy.RedBat;
import com.lampros.guesseat.Sprites.SecondStage.Lava;
import com.lampros.guesseat.Sprites.SecondStage.MovingObjects.Flask;
import com.lampros.guesseat.Sprites.SecondStage.MovingObjects.Spikes;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */


public class B2WorldCreatorStage2 {

    private Array <Knight> knights;
    private Array <Lava> lava;
    private Array <Spikes> spikes;
    private Array <RedBat> redBats;
    private Array <Flask> flask;

    public B2WorldCreatorStage2(SecondStage screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //ground
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.GROUND_BIT;
            body.createFixture(fdef);
        }

        //spikes not falling
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.OBSTACLE_BIT;
            body.createFixture(fdef);
        }

        //create lava
        lava = new Array<Lava>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            lava.add(new Lava(screen, (rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM));
        }

        //spikes falling
        spikes = new Array <Spikes>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spikes.add(new Spikes(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }

        //ogres
        knights = new Array <Knight>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            knights.add(new Knight(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }

        //fake walls
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.WALLS_BIT;
            body.createFixture(fdef);
        }

        //wizards
        redBats = new Array <RedBat>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            redBats.add(new RedBat(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }

        //flask red
        flask = new Array <Flask>();
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            flask.add(new Flask(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }

        //advance level
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.ADVANCE_LEVEL2_BIT;
            fdef.isSensor = true;
            body.createFixture(fdef);
        }
}

    public Array<Lava> getLava() {
        return lava;
    }

    public Array<Spikes> getSpikes() {
        return spikes;
    }

    public Array<Knight> getKnights() {
        return knights;
    }

    public Array<RedBat> getRedBats() {
        return redBats;
    }

    public Array<Flask> getFlask() {
        return flask;
    }

    public void dispose() {
    }
}



