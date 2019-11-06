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
import com.lampros.guesseat.Screens.BossStage;

import com.lampros.guesseat.Sprites.BossStage.BossStageElements.MovingPlatform;
import com.lampros.guesseat.Sprites.BossStage.LavaBoss;
import com.lampros.guesseat.Sprites.BossStage.MovingObjectsBoss.SpikesBoss;
import com.lampros.guesseat.Sprites.MyCharacterStage3;
import com.lampros.guesseat.Sprites.SecondStage.MovingObjects.Flask;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */


public class B2WorldCreatorStage3 {

    private Array <MovingPlatform> movingPlatforms;
    private Array <LavaBoss> lava;
    private Array <SpikesBoss> spikes;
    //private Array <Boss> boss;
    private Array <Flask> flask;
    private MyCharacterStage3 player;


    public B2WorldCreatorStage3(BossStage screen) {
        this.player = player;
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
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
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
        lava = new Array<LavaBoss>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            lava.add(new LavaBoss(screen, (rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM));
        }

        //spikes falling
        spikes = new Array <SpikesBoss>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            spikes.add(new SpikesBoss(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }

        //moving platforms
        movingPlatforms = new Array <MovingPlatform>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            movingPlatforms.add(new MovingPlatform(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
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

        //Boss
       /* boss = new Array <Boss>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            boss.add(new Boss(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }*/

        /*//create lava
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
        ogres = new Array <Ogre>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            ogres.add(new Ogre(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }*/

        /*//fake walls
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.WALLS_BIT;
            body.createFixture(fdef);
        }*/

       /* //wizards
        wizards = new Array <Wizard>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            wizards.add(new Wizard(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }

        //flask red
        flask = new Array <Flask>();
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            flask.add(new Flask(screen, ((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM), ((rect.getY() + rect.getHeight() / 2) / GuessEat.PPM)));
        }*/

            /*bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.GROUND_BIT;
            body.createFixture(fdef);}
*/
        /*//ground
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.gravityScale=1;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / GuessEat.PPM, (rect.getY() + rect.getHeight() / 2) / GuessEat.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / GuessEat.PPM, rect.getHeight() / 2 / GuessEat.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GuessEat.GROUND_BIT;
            body.createFixture(fdef);
        }*/


}

    public Array<LavaBoss> getLavaBoss() {
        return lava;
    }

    public Array<SpikesBoss> getSpikesBoss() {
        return spikes;
    }

    public Array<MovingPlatform> getMovingPlatforms() {
        return movingPlatforms;
    }

    public void dispose() {
    }

    //public Array<Boss> getBoss() {
     //   return boss;
    //}
}



