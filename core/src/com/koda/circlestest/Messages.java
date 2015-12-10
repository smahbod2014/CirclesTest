package com.koda.circlestest;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by Sean on 12/6/2015.
 */
public class Messages {

    private Messages() {}

    public static void registerMessages(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(NewPlayerMessage.class);
        kryo.register(PositionUpdateMessage.class);
        kryo.register(DisconnectMessage.class);
        kryo.register(WelcomeMessage.class);
        kryo.register(RegistrationMessage.class);
        kryo.register(RegistrationResponse.class);
        kryo.register(LoginMessage.class);
        kryo.register(LoginResponse.class);
    }
    
    public static class WelcomeMessage {
    	String text;
    	public WelcomeMessage(String text) {
    		this.text = text;
    	}
    	public WelcomeMessage() {}
    }

    public static class NewPlayerMessage {
        public String name;
        public float x;
        public float y;
        public int id;
        
        public NewPlayerMessage() {}

        public NewPlayerMessage(String name, float x, float y) {
            this(name, x, y, -1);
        }

        public NewPlayerMessage(String name, float x, float y, int id) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.id = id;
        }
    }

    public static class DisconnectMessage {
        public int id;

        public DisconnectMessage(int id) {
            this.id = id;
        }
        
        public DisconnectMessage() {}
    }

    public static class PositionUpdateMessage {
        public int id;
        public float x;
        public float y;

        public PositionUpdateMessage(int id, float x, float y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
        
        public PositionUpdateMessage() {}
    }

    public static class RegistrationMessage {
        public static int RESULT_ACCEPTED = 1;
        public static int RESULT_USER_EXISTS = 2;
        public String username;
        public String password;
        public int resultCode;

        public RegistrationMessage(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public RegistrationMessage() {}
    }

    public static class RegistrationResponse {
        public static int RESULT_ACCEPTED = 1;
        public static int RESULT_USER_EXISTS = 2;
        public int resultCode;

        public RegistrationResponse(int resultCode) {
            this.resultCode = resultCode;
        }

        public RegistrationResponse() {}
    }

    public static class LoginMessage {
        public String username;
        public String password;

        public LoginMessage(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public LoginMessage() {}
    }

    public static class LoginResponse {
        public static int RESULT_ACCEPTED = 1;
        public static int RESULT_BAD_USERNAME = 2;
        public static int RESULT_BAD_PASSWORD = 3;
        public static int RESULT_OTHER_USER_LOGGED_IN = 4;
        public int resultCode;

        public LoginResponse(int resultCode) {
            this.resultCode = resultCode;
        }

        public LoginResponse() {}
    }
}
