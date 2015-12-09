package com.koda.circlestest;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 12/6/2015.
 */
public class Player {
    public Vector2 position;
    public Vector2 oldPosition;
    public float speed;
    public float radius;
    public String name;

    public Player(float x, float y, float speed, float radius, String name) {
        position = new Vector2(x, y);
        this.speed = speed;
        this.radius = radius;
        this.name = name;
        oldPosition = new Vector2(position);
    }

    public void update(float dt) {

    }
    
    public boolean hasMoved() {
		return !position.equals(oldPosition);
    }
    
    public void updatePosition() {
    	oldPosition.set(position);
    }
}
