package com.koda.circlestest;

import java.io.IOException;
import java.util.Date;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * Created by Sean on 12/6/2015.
 */
public class ServerRunner extends Listener {

    private static final int FPS = 60;

    private Server server;
    private GameLogic gameLogic;

    public static void main(String[] args) {
        new ServerRunner();
    }

    public ServerRunner() {
        UserDatabase.readFromFile();
        initializeServer();
        start();
    }

    private void initializeServer() {
        server = new Server();
        Messages.registerMessages(server);
        server.addListener(this);

        try {
            server.bind(3001, 3002);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        server.start();

        System.out.println("Server is starting...");
    }

    private void start() {
        final long sleepTime = (long) (1000.0 / FPS);
        final float frameTime = 1.0f / FPS;

        int numFramesElapsed = 0;
        gameLogic = new GameLogic();

        while (true) {
            numFramesElapsed++;
            gameLogic.update(frameTime);

            // if (numFramesElapsed % FPS == 0) {
            //     System.out.println(numFramesElapsed + " frames have passed");
            // }

            try {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connected(Connection connection) {
        // console logging
        System.out.println("Received a new connection from " + 
            connection.getRemoteAddressTCP().getHostString() +
                " at " + new Date().toString() +
                ". There are now " + server.getConnections().length + " player(s).");
        // System.out.println("Received a new connection");
        
//        System.out.println("Sending welcome message");
        connection.sendTCP(new Messages.WelcomeMessage("It's currently " + new Date().toString()));
    }

    @Override
    public void disconnected(Connection connection) {
        Messages.DisconnectMessage m = new Messages.DisconnectMessage(connection.getID());

        // get name for logging
        Player p = gameLogic.getPlayer(m.id);
        if (p != null) {
            gameLogic.removePlayer(m);
            server.sendToAllExceptTCP(m.id, m);
        }

        // console logging
        // System.out.println(connection.getRemoteAddressTCP().getAddress().getHostAddress() +
        //         " has disconnected at " + new Date().toString());

        String ip = connection.getRemoteAddressTCP() == null ? 
            "Someone" : connection.getRemoteAddressTCP().getHostString();
        System.out.println(ip + " disconnected at " + new Date().toString() +
        		". There are now " + server.getConnections().length + " player(s).");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof Messages.NewPlayerMessage) {
            Messages.NewPlayerMessage m = (Messages.NewPlayerMessage) object;
            m.id = connection.getID();
            gameLogic.addPlayer(m);

            // tell everyone else
            server.sendToAllExceptTCP(connection.getID(), m);
            
            // now tell our new player about everyone who is already here, including himself
            for (int id : gameLogic.getIds()) {
            	Player p = gameLogic.getPlayer(id);
            	Messages.NewPlayerMessage n = new Messages.NewPlayerMessage(p.name, p.position.x, p.position.y, id);
            	connection.sendTCP(n);
            }

            // console logging
            System.out.println("Received login from " + connection.getRemoteAddressTCP().getAddress().getHostAddress() +
                    " (" + m.name + ") at " + new Date().toString());
        }
        else if (object instanceof Messages.PositionUpdateMessage) {
            Messages.PositionUpdateMessage m = (Messages.PositionUpdateMessage) object;
            gameLogic.updatePlayerPosition(m);

            // tell everyone else
            server.sendToAllExceptTCP(m.id, m);
        }
        else if (object instanceof Messages.RegistrationMessage) {
            Messages.RegistrationMessage m = (Messages.RegistrationMessage) object;
            Messages.RegistrationResponse r = new Messages.RegistrationResponse();
            if (UserDatabase.userExists(m.username)) {
                r.resultCode = Messages.RegistrationResponse.RESULT_USER_EXISTS;
            }
            else {
                r.resultCode = Messages.RegistrationResponse.RESULT_ACCEPTED;
                UserDatabase.addUser(m.username, m.password);
                UserDatabase.writeToFile();
            }
            connection.sendTCP(r);
        }
        else if (object instanceof Messages.LoginMessage) {
            Messages.LoginMessage m = (Messages.LoginMessage) object;
            Messages.LoginResponse r = new Messages.LoginResponse();
            r.resultCode = UserDatabase.authenticate(m.username, m.password);
            connection.sendTCP(r);
        }
    }
}
