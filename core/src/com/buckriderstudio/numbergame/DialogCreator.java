package com.buckriderstudio.numbergame;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Menno Gouw on 3/8/2015.
 */
public class DialogCreator {

    /**
     * Creates a dialog screen in front of the current screen
     * @param screen current screen
     * @param text text to display
     */
    public static void AchievementDialog(GameScreen screen, String text)
    {
        Dialog dialog = new Dialog("", screen.getSkin(), "dialog");
        Label label = new Label(text, screen.getSkin());
        label.setAlignment(Align.center);
        label.setWrap(true);

        dialog.getContentTable().add(label).width(screen.getStage().getWidth() - screen.getStage().getWidth() / 4).pad(10);

        dialog.setPosition(screen.getStage().getWidth() / 2 - dialog.getWidth() / 2, screen.getStage().getHeight() / 2 - dialog.getHeight() / 2);

        TextButton button = new TextButton("Ok", screen.getSkin(), "small");
        button.padLeft(button.getWidth() / 2).padRight(button.getWidth() / 2);

        dialog.getButtonTable().pad(10);
        dialog.button(button).setWidth(500);

        dialog.show(screen.getStage());
    }

}
