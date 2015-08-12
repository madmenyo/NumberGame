package com.buckriderstudio.numbergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;

public class NumberGame extends Game {
    private ActionResolver actionResolver;

    public NumberGame(ActionResolver actionResolver)
    {
        this.actionResolver = actionResolver;
    }
	
	@Override
	public void create () {

        this.setScreen(new MenuScreen(actionResolver));
	}
}
