package com.koda.circlestest;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;
import java.util.Stack;

public class StateManager {

    private static HashMap<String, GameState> allStates = new HashMap<>();
    private static HashMap<String, Object> params = new HashMap<>();
    private static GameState gameState;

    public static GameState getGameState(String name) {
        return allStates.get(name);
    }

    public static void addState(String name, GameState state) {
        allStates.put(name, state);
    }

    public static void setState(GameState state) {
        StateManager.gameState = state;
        gameState.show();
    }

    public static void setState(String name) {
        if (allStates.get(name) == null) {
            System.out.println("State " + name + " does not exist");
            return;
        }

        setState(allStates.get(name));
    }

    public static void update(float dt) {
        gameState.input(dt);
        gameState.update(dt);
    }

    public static void render(SpriteBatch sb) {
        gameState.render(sb);
    }

    public static void render(ShapeRenderer shapeRenderer) {
        gameState.render(shapeRenderer);
    }

    public static void pause() {
        gameState.pause();
    }

    public static void resume() {
        gameState.resume();
    }

    public static void resize(int width, int height) {
        gameState.resize(width, height);
    }

    public static void dispose() {
        gameState.dispose();
    }

    public static void disposeAll() {
        for (GameState gs : allStates.values()) {
            gs.dispose();
        }
    }

    public static void putParam(String name, Object param) {
        params.put(name, param);
    }

    public static Object getParam(String name) {
        return params.get(name);
    }
}
