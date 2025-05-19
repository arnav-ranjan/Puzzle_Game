package com.puzzle_game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    //GYattt
    private SpriteBatch batch;

    private Texture backgroundTexture;
    Texture characterTexture; // TODO: fix the background of the character

    Sprite playerSprite;

    FitViewport viewport;

    @Override
    public void create() {

        batch = new SpriteBatch();
        backgroundTexture = new Texture("background.png");
        characterTexture = new Texture("Whiteboxguy.png");

        playerSprite = new Sprite(characterTexture);
        playerSprite.setSize(1, 1);

        viewport = new FitViewport(16, 10);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() { // get keyboard inputs

        float speed = 2.0f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            playerSprite.translateX(speed * delta);
        } else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            playerSprite.translateX(-speed * delta);
        }
    }

    private void logic() {

    }

    private void draw() {

        ScreenUtils.clear(Color.BLACK);
        
        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        batch.begin();
        
        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        playerSprite.draw(batch);

        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
    }
}
