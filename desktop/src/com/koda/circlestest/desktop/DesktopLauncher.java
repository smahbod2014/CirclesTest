package com.koda.circlestest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.koda.circlestest.CirclesTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CirclesTest";
		config.width = 960;
		config.height = 540;
		config.resizable = false;
		config.y = 20;
		new LwjglApplication(new CirclesTest(), config);
	}
}
