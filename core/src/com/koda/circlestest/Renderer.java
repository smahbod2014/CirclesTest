package com.koda.circlestest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Sean on 12/6/2015.
 */
public class Renderer {

    public static void render(Player player, ShapeRenderer shapeRenderer, Color color) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(player.position.x, player.position.y, player.radius);
        CirclesTest.postTextToRender(player.name,
                player.position.x,
                player.position.y + player.radius * 1.5f, 1, Color.WHITE, CirclesTest.font);
    }
}
