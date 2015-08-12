package com.buckriderstudio.numbergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Menyo on 1/8/2015.
 */
public class MenuScreen implements Screen {
    SpriteBatch batch;
    Stage stage;
    Skin skin;
    TextureAtlas atlas;

    ActionResolver resolver;

    boolean hardMode;

    public MenuScreen(final ActionResolver resolver)
    {
        this.resolver = resolver;
        Gdx.input.setCatchBackKey(true);

        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
        atlas = new TextureAtlas("skin.pack");
        skin = new Skin(Gdx.files.internal("skin.json"), atlas);

        Table table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        table.center().top();

        Label title = new Label("NumberLympics", skin, "large");
        table.add(title).pad(10);
        table.row();
        Label label = new Label("You should get what you think you deserve!", skin);
        label.setWrap(true);
        label.setAlignment(Align.center);
        table.add(label).pad(20).width(stage.getWidth() - stage.getWidth() / 4);
        table.row();

        final TextButton mode = new TextButton("Easy", skin, "small");
        table.add(mode).width(stage.getWidth() / 2).pad(20);
        table.row();

        TextButton play = new TextButton("Play", skin, "small");
        table.add(play).width(stage.getWidth() / 2).pad(20);
        table.row();

        TextButton achievements = new TextButton("Achievements", skin, "small");
        table.add(achievements).width(stage.getWidth() / 2).pad(20);
        table.row();

        TextButton scoreboard = new TextButton("Score Board", skin, "small");
        table.add(scoreboard).width(stage.getWidth() / 2).pad(20);
        table.row();

        TextButton options = new TextButton("Options", skin, "small");
        table.add(options).width(stage.getWidth() / 2).pad(20);
        table.row();

        TextButton exit = new TextButton("Exit", skin, "small");
        table.add(exit).expand().width(stage.getWidth() / 2);

        mode.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (hardMode)
                {
                    hardMode = false;
                    mode.setText("Easy");
                }
                else
                {
                    hardMode = true;
                    mode.setText("Hard");
                }
            }
        });

        play.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                ((Game)(Gdx.app.getApplicationListener())).setScreen(new GameScreen(hardMode, resolver));
            }
        });

        achievements.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resolver.getAchievements();
            }
        });

        scoreboard.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resolver.getLeaderboard();
            }
        });

        exit.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });


        table.row();
        TextButton showAdd = new TextButton("Show Add", skin, "small");
        showAdd.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resolver.showInterstitialAd();
            }
        });

        table.add(showAdd).width(stage.getWidth() / 2).pad(20);



        Gdx.input.setInputProcessor(stage);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( .06f, .07f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
        {
            Gdx.app.log("GameScreen", "Back Key Pressed!");
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
