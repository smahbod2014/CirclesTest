package com.koda.circlestest;

import com.badlogic.gdx.Input;

/**
 * Created by Sean on 12/7/2015.
 */
public class MyTextInputListener implements Input.TextInputListener {

    Game game;

    public MyTextInputListener(Game game) {
        this.game = game;
    }

    @Override
    public void input(String text) {
        game.nameToSend = text;
    }

    @Override
    public void canceled() {

    }
}
