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
public class Game extends Listener {

    private Client client;
    private GameLogic gameLogic = new GameLogic();
    private int id;
    private Array<Color> playerColors = new Array<Color>();
    Player player;
    boolean inGame = false;
    String nameToSend;

    public Game() {
//        client = new Client();
//        client.addListener(this);
//        Messages.registerMessages(client);
//        client.start();
//        Log.set(Log.LEVEL_TRACE);
//        
//        try {
//            client.connect(5000, "52.88.99.161", 3001, 3002);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gdx.input.getTextInput(new MyTextInputListener(Game.this), "Enter your player name", "", "Jonathan Joestar");
            }
        }).start();
    	
    	client = new Client();
		Messages.registerMessages(client);
		client.addListener(this);
		client.start();
		try {
			client.connect(5000, "52.88.99.161", 3001, 3002);
		} catch (IOException e) {
			e.printStackTrace();
		}

        playerColors.add(Color.ORANGE);
        playerColors.add(Color.BLUE);
        playerColors.add(Color.YELLOW);
        playerColors.add(Color.MAGENTA);
        playerColors.add(Color.CHARTREUSE);
    }

    public void input(float dt) {
        if (client.isConnected() && inGame && player != null) {
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            if (client.isConnected()) {
                if (!inGame) {
                    inGame = true;
                    System.out.println("Entered the game. This simulates being logged in in the main menu");
                    Messages.NewPlayerMessage m = new Messages.NewPlayerMessage(nameToSend, 200, 200);
                    client.sendTCP(m);
                }
                else {
                    System.out.println("You're already connected!");
                }
            }
            else {
                System.out.println("Error: Not connected to the server");
            }
        }
    }

    public void update(float dt) {
        if (client.isConnected() && inGame && player != null) {
//        	System.out.println("Updating player position to server");
            gameLogic.update(dt);

            if (player.hasMoved()) {
            	Messages.PositionUpdateMessage m = new Messages.PositionUpdateMessage(client.getID(), player.position.x, player.position.y);
                client.sendUDP(m);
                player.updatePosition();
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
    	if (inGame) {
    		int i = 0;
//    		System.out.println("BEGIN RENDERING=====");
            for (int id : gameLogic.getIds()) {
            	Player p = gameLogic.getPlayer(id);
//            	System.out.println("Using color i = " + i + " (" + playerColors.get(i).toString() + ")");
        		Renderer.render(p, shapeRenderer, playerColors.get(i++));
            }
    	}
    }

    @Override
    public void received(Connection connection, Object object) {
//    	System.out.println("Received something from the server?");
        if (object instanceof Messages.NewPlayerMessage && inGame) {
            Messages.NewPlayerMessage m = (Messages.NewPlayerMessage) object;
            gameLogic.addPlayer(m);
            gameLogic.sortPlayers(client.getID());

            // if this is us, we need a reference to ourselves
            if (m.id == client.getID()) {
                player = gameLogic.getPlayer(m.id);
            }
        }
        else if (object instanceof Messages.DisconnectMessage && inGame) {
            Messages.DisconnectMessage m = (Messages.DisconnectMessage) object;
            gameLogic.removePlayer(m);
        }
        else if (object instanceof Messages.PositionUpdateMessage && inGame) {
            Messages.PositionUpdateMessage m = (Messages.PositionUpdateMessage) object;
            gameLogic.updatePlayerPosition(m);
        }
        else if (object instanceof Messages.SampleMessage) {
        	Messages.SampleMessage m = (Messages.SampleMessage) object;
        	System.out.println("Server says this: " + m.text);
        }
    }
}
