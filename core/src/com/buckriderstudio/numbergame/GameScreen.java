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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.security.Key;

/**
 * Created by Menyo on 1/8/2015.
 */
public class GameScreen implements Screen {
    Stage stage;
    Skin skin;
    TextureAtlas atlas;
    SpriteBatch batch;

    final GameScreen gameScreen;

    int buttonSize;

    Table keyPad;
    private Label display;
    public Label getDisplay() {
        return display;
    }

    Dialog dialog;

    ActionResolver resolver;

    boolean hardMode;


    public GameScreen(final boolean hardMode, final ActionResolver resolver)
    {
        this.hardMode = hardMode;
        this.resolver = resolver;
        gameScreen = this;

        Gdx.input.setCatchBackKey(true);


        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), batch);
        atlas = new TextureAtlas("skin.pack");
        skin = new Skin(Gdx.files.internal("skin.json"), atlas);

        buttonSize = stage.getViewport().getScreenWidth() / 4;

        keyPad = new Table();
        //keyPad.setDebug(true);
        keyPad.setFillParent(true);
        keyPad.center().top();

        Label title = new Label("How much points do you deserve?", skin);
        title.setAlignment(Align.center);
        title.setWrap(true);
        keyPad.row();
        keyPad.add(title).colspan(3).width(stage.getWidth() - stage.getWidth() / 4);


        display = new Label("", skin, "display");
        display.setAlignment(Align.center);

        keyPad.row().padBottom(buttonSize / 4).padTop(buttonSize / 4);
        keyPad.add(display).width(stage.getViewport().getScreenWidth() - buttonSize).height(buttonSize / 1.5f).colspan(3).expandX();


        keyPad.row().padBottom(buttonSize / 4);
        addDigit("1");
        addDigit("2");
        addDigit("3");
        keyPad.row().padBottom(buttonSize / 4);
        addDigit("4");
        addDigit("5");
        addDigit("6");
        keyPad.row().padBottom(buttonSize / 4);
        addDigit("7");
        addDigit("8");
        addDigit("9");
        keyPad.row().padBottom(buttonSize / 4);
        addDigit("0");
        TextButton back = new TextButton("del", skin);
        TextButton ok = new TextButton("ok", skin);
        keyPad.add(back).width(buttonSize). height(buttonSize).expandX();
        keyPad.add(ok).width(buttonSize). height(buttonSize).expandX();

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (display.getText().length > 0) {
                    String n = display.getText().substring(0, display.getText().length - 1);
                    display.setText(n);
                }
            }
        });

        ok.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (display.getText().toString().length() > 0) {
                    resolver.enteredScore(Integer.parseInt(display.getText().toString()), hardMode, gameScreen);
                }
                else
                {
                    DialogCreator.AchievementDialog(gameScreen, "You need to enter a value.");
                }
            }
        });


        stage.addActor(keyPad);
        //stage.addActor(dialog);
        Gdx.input.setInputProcessor(stage);
    }

    private void showDialog()
    {

    }


    private void addDigit(final String number)
    {
        TextButton button = new TextButton(number, skin);
        keyPad.add(button).width(buttonSize). height(buttonSize).expandX();

        button.addListener(new ClickListener()
        {
           @Override
           public void clicked(InputEvent event, float x, float y)
           {
               if (display.getText().length < 4) {
                   String n = display.getText() + "" + number;
                   display.setText(n);
               }
           }
        });
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
            ((Game)(Gdx.app.getApplicationListener())).setScreen(new MenuScreen(resolver));
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

    }
}
