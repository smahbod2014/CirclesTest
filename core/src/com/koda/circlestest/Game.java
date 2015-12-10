package com.koda.circlestest;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Created by Sean on 12/6/2015.
 */
public class Game extends GameState {

    private Client client;
    private GameListener myListener = new GameListener();
    private GameLogic gameLogic = new GameLogic();
    private Array<Color> playerColors = new Array<Color>();
    Player player;
    boolean inGame = false;

    public Game() {
        client = CirclesTest.client;
        playerColors.add(Color.ORANGE);
        playerColors.add(Color.BLUE);
        playerColors.add(Color.YELLOW);
        playerColors.add(Color.MAGENTA);
        playerColors.add(Color.CHARTREUSE);
    }

    @Override
    public void show() {
        System.out.println("Entered the game successfully");
        client.addListener(myListener);
        String username = (String) StateManager.getParam("username");
        Messages.NewPlayerMessage m = new Messages.NewPlayerMessage(username, 200, 200);
        client.sendTCP(m);
    }

    @Override
    public void input(float dt) {
        if (player != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.position.x -= player.speed * dt;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.position.x += player.speed * dt;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.position.y -= player.speed * dt;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player.position.y += player.speed * dt;
            }
        }
    }

    public void update(float dt) {
        gameLogic.update(dt);

        if (player != null && player.hasMoved()) {
            Messages.PositionUpdateMessage m = new Messages.PositionUpdateMessage(
                    client.getID(), player.position.x, player.position.y);
            client.sendUDP(m);
            player.updatePosition();
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        int i = 0;
        for (int id : gameLogic.getIds()) {
            Player p = gameLogic.getPlayer(id);
            Renderer.render(p, shapeRenderer, playerColors.get(i++));
        }
    }

    private class GameListener extends Listener {
        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof Messages.NewPlayerMessage) {
                Messages.NewPlayerMessage m = (Messages.NewPlayerMessage) object;
                gameLogic.addPlayer(m);
                gameLogic.sortPlayers(client.getID());

                // if this is us, we need a reference to ourselves
                if (m.id == client.getID()) {
                    player = gameLogic.getPlayer(m.id);
                }
            }
            else if (object instanceof Messages.DisconnectMessage) {
                Messages.DisconnectMessage m = (Messages.DisconnectMessage) object;
                gameLogic.removePlayer(m);
            }
            else if (object instanceof Messages.PositionUpdateMessage) {
                Messages.PositionUpdateMessage m = (Messages.PositionUpdateMessage) object;
                gameLogic.updatePlayerPosition(m);
            }
        }
    }
}
