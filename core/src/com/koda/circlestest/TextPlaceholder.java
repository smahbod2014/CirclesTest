package com.koda.circlestest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sean on 12/6/2015.
 */
public class TextPlaceholder {
    public BitmapFont font;
    public String text;
    public Vector2 position;
    public float scale;
    public Color color;

    public TextPlaceholder(BitmapFont font, String text, float x, float y, float scale, Color color) {
        this.font = font;
        this.text = text;
        this.scale = scale;
        this.color = color;
        this.position = new Vector2(x, y);
    }
}
