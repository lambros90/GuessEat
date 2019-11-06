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

/**
 * Based on Brent Aureli https://github.com/BrentAureli/SuperMario
 */


public class HudStage3 implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private  static Integer lives;
    private float timeCount;
    private static Integer power;
    private static Integer worldTimer;
    private static Integer appleBalls;
    private static Label livesRemaining;
    private static Label powerLabel;
    private Label countdownLabel;
    private Label livesLabel;
    private Label pointsLabel;
    private Label timeLabel;
    private Label appleBallsLabel;
    private BitmapFont font;
    private static Label appleBallsCounter;
    private static boolean timeUp;

    public HudStage3(SpriteBatch sb) {
        setWorldTimer(120);
        lives = HudStage2.getLives();
        appleBalls = HudStage2.getAppleBalls();
        power = HudStage2.setPowerForBoss();
        viewport = new FitViewport(GuessEat.V_WIDTH, GuessEat.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
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
        if(HudStage2.getScore() == 2800){
            powerLabel = new Label(String.format("X3", power), new Label.LabelStyle(font, Color.YELLOW));
        }
        else if (HudStage2.getScore() < 2800 && HudStage2.getScore() > 1800){
            powerLabel = new Label(String.format("X2", power), new Label.LabelStyle(font, Color.YELLOW));
        }
        else {
            powerLabel = new Label(String.format("X1", power), new Label.LabelStyle(font, Color.YELLOW));
        }
        livesLabel = new Label("LIVES", new Label.LabelStyle(font, Color.RED));
        pointsLabel = new Label("POWER", new Label.LabelStyle(font, Color.YELLOW));
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(font, Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        appleBallsLabel = new Label("APPLEBALLS", new Label.LabelStyle(font, Color.GREEN));
        appleBallsCounter = new Label(String.format("%03d", appleBalls), new Label.LabelStyle(font, Color.GREEN));
        table.add(pointsLabel).expandX().padTop(10);
        table.add(appleBallsLabel).expandX().padTop(10);
        table.add(livesLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(powerLabel).expandX();
        table.add(appleBallsCounter).expandX();
        table.add(livesRemaining).expandX();
        table.add(countdownLabel).expandX();
        stage.addActor(table);
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

    public static void addScore(){
        appleBalls = appleBalls -1;
        appleBallsCounter.setText(String.format("%03d", appleBalls));
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
        HudStage3.lives = lives;
    }

    public static boolean isTimeUp() {
        return timeUp;
    }

    public static void setWorldTimer(Integer worldTimer) {
        HudStage3.worldTimer = worldTimer;
    }

    public static void setTimeUp(boolean timeUp) {
        HudStage3.timeUp = timeUp;
    }

    public static Integer getAppleBalls() {
        return appleBalls;
    }

    @Override
    public void dispose() {
        stage.dispose();
        Gdx.app.log("HudStage3","Got disposed");
    }
}

