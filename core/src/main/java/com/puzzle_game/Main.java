package com.puzzle_game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

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

    private float velocity = 0.3f;
    private double millitime = 0.0;
    private int time = 0;
    private int nextTime = 0;
    private double temptime = 0;
    private int up = 0;
    private int stage = 1;
    private int spawnX = 0;
    private int spawnY = -49;

    Sprite playerSprite;

    @Override
    public void create() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT * height/width);
        camera.position.set(0, 0, 0);
        camera.update();

        int[] stage1Plats = {-47, -47, 10, 5, -55, -53, 110, 4, 37, -40, 10, 5, 20, -35, 10, 5};

        batch = new SpriteBatch();
        backgroundTexture = new Texture("background.png");
        characterTexture = new Texture("Whiteboxguy.png");
        platformTexture = new Texture("platformNew.png");

        for (int i = 0; i < stage1Plats.length; i += 4) {
            Sprite platSprite = new Sprite(platformTexture);
            platSprite.setBounds(stage1Plats[i], stage1Plats[i + 1], stage1Plats[i + 2], stage1Plats[i + 3]);
            platforms1.add(platSprite);
        }

        playerSprite = new Sprite(characterTexture);
        playerSprite.setSize(5, 4);
        playerSprite.setPosition(spawnX, spawnY);
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

        float speed = 10.0f; // speed of player
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            playerSprite.translateX(speed * delta);
            System.out.println(playerSprite.getX());

        } else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            playerSprite.translateX(-speed * delta);
            System.out.println(playerSprite.getX());

        } if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            if(up == 0 || up == 1) { // Allows for double jump
                playerSprite.translateY(velocity);
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
        platformcollisisons(platforms1);
    }

    private void gravity(float bound) {

        float gravity = 0.5f;
        float timeIncrement = (float)(millitime - temptime);

        if(up > 0) {
            playerSprite.translateY(velocity); // upwards velocity value
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

    private boolean undercollision(Sprite contact) {
        if (playerSprite.getY() + playerSprite.getHeight() > contact.getY() && playerSprite.getY() + playerSprite.getHeight() - 2*velocity < contact.getY() && playerSprite.getX() > contact.getX() - playerSprite.getWidth() && playerSprite.getX() < contact.getX() + contact.getWidth()) {
            return true;
        } else {
            return false;
        }
    }

    private String sidecollision(Sprite contact) {
        if(playerSprite.getY() < contact.getY() + contact.getHeight() && playerSprite.getY() + playerSprite.getHeight() > contact.getY()) {
            if (playerSprite.getX() < contact.getX() + contact.getWidth() && playerSprite.getX() > contact.getX() + contact.getWidth() - 2*velocity) {
                return "right";
            } else if (playerSprite.getX() + playerSprite.getWidth() > contact.getX() && playerSprite.getX() + playerSprite.getWidth() < contact.getX() + 2*velocity) {
                return "left";
            } else {
                return "none";
            }
        } else {
            return "none";
        }
    }

    private void platformcollisisons(ArrayList<Sprite> platformsWorld) {
        float maxHeight = -100;
        int platIndex = 0;
        int platIncrement = 0;
        for (Sprite s : platformsWorld) {
            if(playerSprite.getY() >= s.getY() + s.getHeight() && playerSprite.getX() > s.getX() - playerSprite.getWidth() && playerSprite.getX() < s.getX() + s.getWidth()) {
                if (s.getY() + s.getHeight() > maxHeight) {
                    maxHeight = s.getY() + s.getHeight();
                    platIndex = platIncrement;
                }
            } if (sidecollision(s).equals("right")) {
                playerSprite.setX(s.getX() + s.getWidth());
            } else if (sidecollision(s).equals("left")) {
                playerSprite.setX(s.getX() - playerSprite.getWidth());
            } else if (undercollision(s) && playerSprite.getX() > s.getX() - playerSprite.getWidth() && playerSprite.getX() < s.getX() + s.getWidth()) {
                playerSprite.setY(s.getY() - playerSprite.getHeight());
            }
            platIncrement++;
        }
        gravity(platformsWorld.get(platIndex).getY() + platformsWorld.get(platIndex).getHeight());
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
