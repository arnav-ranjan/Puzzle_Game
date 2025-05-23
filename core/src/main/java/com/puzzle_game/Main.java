package com.puzzle_game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private OrthographicCamera camera;

    private Texture backgroundTexture;
    private Texture characterTexture; // TODO: fix the background of the character

    private float VIEWPORT_WIDTH = 100f;
    private float VIEWPORT_HEIGHT = 100f;

    private double millitime = 0.0;
    private int time = 0;
    private int nextTime = 0;

    Sprite playerSprite;

    FitViewport viewport;

    @Override
    public void create() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT * height/width);
        camera.position.set(0, 0, 0);
        camera.update();

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
        camera.viewportWidth = VIEWPORT_WIDTH;
        camera.viewportHeight = VIEWPORT_HEIGHT * height / width;
        camera.update();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() { // get keyboard inputs

        float speed = 3.0f;
        float delta = Gdx.graphics.getDeltaTime();
        double temptime = time;

        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            playerSprite.translateX(speed * delta);
            System.out.println(playerSprite.getX());

        } else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            playerSprite.translateX(-speed * delta);
            System.out.println(playerSprite.getX());

        } if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            playerSprite.translateY(delta);
            }

    }

    /*
     * Wraps the sprite around the screen so that it reappears on the screen no matter how far it travels
     */
    private void logic() { 
        
        float xPos = playerSprite.getX();
        float yPos = playerSprite.getY();
        millitime += 0.02;
        if((int)millitime % 2 == 0 && (int)millitime != nextTime)
        {
            nextTime = (int) millitime;
            time++;
            System.out.println(time);
        }
        
        if(xPos > 16) {
            playerSprite.setPosition(0, yPos);
            playerSprite.translateX(-1);

        } else if(xPos < -1) {
            playerSprite.setPosition(viewport.getWorldWidth(), yPos);
        }

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
