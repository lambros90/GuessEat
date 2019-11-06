package com.lampros.guesseat.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lampros.guesseat.GuessEat;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GuessEat(), config);

		//config.title = GuessEat.TITLE;
		config.width = GuessEat.V_WIDTH * GuessEat.SCALE; //test
		config.height = GuessEat.V_HEIGHT * GuessEat.SCALE; //test
	}
}
