package com.koda.circlestest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class CirclesTest extends ApplicationAdapter {
	static Array<TextPlaceholder> textsToRender = new Array<TextPlaceholder>();
	static GlyphLayout glyphLayout = new GlyphLayout();
	static BitmapFont font;

	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	OrthographicCamera viewCamera;
	Game game;

	@Override
	public void create () {
		viewCamera = new OrthographicCamera();
		viewCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		spriteBatch = new SpriteBatch();
		shapeRenderer.setProjectionMatrix(viewCamera.combined);
		spriteBatch.setProjectionMatrix(viewCamera.combined);
		game = new Game();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ninja-naruto.regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 32;
		params.genMipMaps = true;
		params.magFilter = Texture.TextureFilter.Linear;
		params.minFilter = Texture.TextureFilter.Linear;
		font = generator.generateFont(params);
		generator.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float dt = Gdx.graphics.getDeltaTime();
		game.input(dt);
		game.update(dt);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		game.render(shapeRenderer);
		shapeRenderer.end();

		if (textsToRender.size > 0) {
			spriteBatch.begin();
			for (int i = 0; i < textsToRender.size; i++) {
				TextPlaceholder tp = textsToRender.get(i);
				tp.font.draw(spriteBatch, tp.text, tp.position.x, tp.position.y);
			}
			spriteBatch.end();
			textsToRender.clear();
		}
	}

	public static void postTextToRender(String text, float x, float y, float scale, Color color, BitmapFont font) {
		if (font == null) {
			System.out.println("FONT NULL");
			return;
		}
		
		if (text == null) {
			System.out.println("TEXT NULL");
			return;
		}
		glyphLayout.setText(font, text);
		x -= glyphLayout.width / 2;
		y += glyphLayout.height / 2;
		textsToRender.add(new TextPlaceholder(font, text, x, y, scale, color));
	}
}
