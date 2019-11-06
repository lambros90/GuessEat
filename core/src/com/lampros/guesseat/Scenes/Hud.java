package com.lampros.guesseat.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lampros.guesseat.GuessEat;
import com.lampros.guesseat.Tools.B2WorldCreator;

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */


public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private  static Integer lives;
    private float timeCount;
    private static Integer score;
    private static Integer worldTimer;
    private static Label livesRemaining;
    private static Label scoreLabel;
    private Label countdownLabel;
    private Label livesLabel;
    private Label worldLabel;
    private Label pointsLabel;
    private Label timeLabel;
    private static Label applesCounterLabel;
    private static Integer applesCounter;
    private static boolean timeUp;
    private static Integer appleBalls;
    private BitmapFont font;

    public Hud(SpriteBatch sb) {
        lives = 3;
        worldTimer = 60;
        score = 0;
        viewport = new FitViewport(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        applesCounter = 0;
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/KaushanScript-Regular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.getData().setScale(0.3f, 0.3f);
        livesRemaining = new Label(String.format("%01d", lives), new Label.LabelStyle(font, Color.RED));
        scoreLabel = new Label(String.format("%04d", score), new Label.LabelStyle(font, Color.YELLOW));
        livesLabel = new Label("LIVES", new Label.LabelStyle(font, Color.RED));
        worldLabel = new Label("APPLES", new Label.LabelStyle(font, Color.GREEN));
        pointsLabel = new Label("POINTS", new Label.LabelStyle(font, Color.YELLOW));
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(font, Color.WHITE));
        applesCounterLabel = new Label(String.format("%01d", applesCounter), new Label.LabelStyle(font, Color.GREEN));
        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        table.add(pointsLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(livesLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(applesCounterLabel).expandX();
        table.add(livesRemaining).expandX();
        table.add(countdownLabel).expandX();
        stage.addActor(table);
    }

    public static Integer getScore() {
        return score;
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static Integer getAppleBalls() {
        return appleBalls;
    }

    public static void setAppleBalls(Integer score) {
        Hud.appleBalls = score / 22 ;
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%04d", score));
    }

    public static void addApples(int value){
        applesCounter += value;
        applesCounterLabel.setText(String.format("%01d", applesCounter));
    }

    public static void removeLives(){
        lives --;
        livesRemaining.setText(String.format("%01d", lives));
    }

    public static Integer getLives() {
        return lives;
    }

    public static Integer getWorldTimer() {
        return worldTimer;
    }

    public static void setLives(Integer lives) {
        Hud.lives = lives;
    }

    public static boolean isTimeUp() {
        return timeUp;
    }

    public static void setWorldTimer(Integer worldTimer) {
        Hud.worldTimer = worldTimer;
    }

    public static void setTimeUp(boolean timeUp) {
        Hud.timeUp = timeUp;
    }

    @Override
    public void dispose() {
        stage.dispose();
        Gdx.app.log("Hud","Got disposed");
    }

}

