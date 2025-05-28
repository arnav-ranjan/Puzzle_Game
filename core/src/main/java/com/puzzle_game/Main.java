package com.puzzle_game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private OrthographicCamera camera;

    private Texture backgroundTexture;
    private Texture characterTexture; // TODO fix background
    private Texture platformTexture;
    private ArrayList<Sprite> platforms1 = new ArrayList<>();

    private float VIEWPORT_WIDTH = 100f;
    private float VIEWPORT_HEIGHT = 100f;

    private double millitime = 0.0;
    private int time = 0;
    private int nextTime = 0;
    private double temptime = 0;
    private int up = 0;
    private int stage = 1;

    Sprite playerSprite;

    @Override
    public void create() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT * height/width);
        camera.position.set(0, 0, 0);
        camera.update();

        int[] stage1Plats = {-47, -45, 10, 3, -50, -50, 100, 1};

        batch = new SpriteBatch();
        backgroundTexture = new Texture("background.png");
        characterTexture = new Texture("Whiteboxguy.png");
        platformTexture = new Texture("testplats.png");

        for (int i = 0; i < stage1Plats.length; i += 4) {
            Sprite platSprite = new Sprite(platformTexture);
            platSprite.setBounds(stage1Plats[i], stage1Plats[i + 1], stage1Plats[i + 2], stage1Plats[i + 3]);
            platforms1.add(platSprite);
        }

        playerSprite = new Sprite(characterTexture);
        playerSprite.setSize(5, 5);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = VIEWPORT_WIDTH;
        camera.viewportHeight = VIEWPORT_HEIGHT;
        camera.update();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() { // get keyboard inputs

        float speed = 10.0f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            playerSprite.translateX(speed * delta);
            System.out.println(playerSprite.getX());

        } else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            playerSprite.translateX(-speed * delta);
            System.out.println(playerSprite.getX());

        } if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            if(up == 0 || up == 1) { // Allows for double jump
                playerSprite.translateY(0.2f);
                up++;
                temptime = millitime;
            }
        }

    }

    /*
     * Wraps the sprite around the screen so that it reappears on the screen no matter how far it travels
     */
    private void logic() { 

        float xPos = playerSprite.getX();
        float yPos = playerSprite.getY();
        millitime += 0.02; //Increment speed of game

        // Function occurs every second when millitime is 0.02
        if((int)millitime % 2 == 0 && (int)millitime != nextTime)
        {
            nextTime = (int) millitime;
            time++;
            System.out.println(time);
        }
        
        if(xPos > VIEWPORT_WIDTH/2) {
            playerSprite.setPosition(-VIEWPORT_WIDTH/2 - playerSprite.getWidth(), yPos);

        } else if(xPos < -VIEWPORT_WIDTH/2 - playerSprite.getWidth()) {
            playerSprite.setPosition(VIEWPORT_WIDTH/2, yPos);
        }
        
        boolean isinAir = true;
        for (Sprite s : platforms1) {
            if(yPos >= s.getY() + s.getHeight() && xPos > s.getX() - 5 && xPos < s.getX() + s.getWidth() && isinAir) {
                gravity(s.getY() + s.getHeight());
                isinAir = false;
            }
        }
    }

    private void gravity(float bound) {

        float gravity = 0.5f;
        float timeIncrement = (float)(millitime - temptime);

        if(up > 0) {
            playerSprite.translateY(0.3f); // upwards velocity value
        } if(playerSprite.getY() - (gravity * timeIncrement) < bound) {
            playerSprite.setY(bound);
            up = 0;
            temptime = millitime;
        } else if(playerSprite.getY() > bound) {
            playerSprite.translateY(-gravity * timeIncrement);
            System.out.println(playerSprite.getY());
        } else {
            playerSprite.setY(bound);
            up = 0;
            temptime = millitime;
        }
    }

    private void draw() {

        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        batch.draw(backgroundTexture, -VIEWPORT_WIDTH/2, -VIEWPORT_HEIGHT/2, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        for(Sprite s : platforms1) {
            s.draw(batch);
        }

        playerSprite.draw(batch);
        

        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
    }
}
