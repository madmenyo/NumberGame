package com.buckriderstudio.numbergame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.buckriderstudio.numbergame.NumberGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int)(720 / 1.3f);
        config.height = (int)(1280 / 1.3f);
		new LwjglApplication(new NumberGame(new ActionResolverDesktop()), config);
	}
}
