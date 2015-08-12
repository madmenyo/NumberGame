package com.buckriderstudio.numbergame;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Menyo on 3/8/2015.
 */
public class DialogCreator {

    public static void AchievementDialog(GameScreen screen, String text)
    {
        Dialog dialog = new Dialog("", screen.skin, "dialog");
        Label label = new Label(text, screen.skin);
        label.setAlignment(Align.center);
        label.setWrap(true);

        dialog.getContentTable().add(label).width(screen.stage.getWidth() - screen.stage.getWidth() / 4).pad(10);
        //dialog.text(label);

        //dialog.setHeight(screen.stage.getHeight() / 2);
        //dialog.setWidth(screen.stage.getWidth() - screen.stage.getWidth() / 4);
        dialog.setPosition(screen.stage.getWidth() / 2 - dialog.getWidth() / 2, screen.stage.getHeight() / 2 - dialog.getHeight() / 2);

        TextButton button = new TextButton("Ok", screen.skin, "small");
        button.padLeft(button.getWidth() / 2).padRight(button.getWidth() / 2);

        dialog.getButtonTable().pad(10);
        dialog.button(button).setWidth(500);

        dialog.show(screen.stage);
    }

}
