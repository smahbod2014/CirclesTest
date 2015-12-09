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
        kryo.register(SampleMessage.class);
    }
    
    public static class SampleMessage {
    	String text;
    	public SampleMessage(String text) {
    		this.text = text;
    	}
    	public SampleMessage() {}
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
}
