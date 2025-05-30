package com.puzzle_game;

import java.util.ArrayList;

import org.w3c.dom.Text;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.bullet.collision.integer_comparator;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private OrthographicCamera camera;

    private Texture backgroundTexture1;
    private Texture characterTexture;
    private Texture platformTexture;
    private Texture spikeTexture;
    private Texture flagTexture;
    private Texture flagActivatedTexture;
    private Texture portalTexture;
    private Texture coinTexture;

    private ArrayList<Sprite> platforms1 = new ArrayList<>();
    private ArrayList<Sprite> spikes1 = new ArrayList<>();
    private ArrayList<Sprite> coins1 = new ArrayList<>();
    private ArrayList<Sprite> flags1 = new ArrayList<>();
    private ArrayList<Sprite> flagsActivated1 = new ArrayList<>();

    private float velocity = 0.3f;
    private double millitime = 0.0;
    private int time = 0;
    private int nextTime = 0;
    private double temptime = 0;
    private int up = 0;
    private int stage = 1;
    private float spawnX = 0;
    private float spawnY = -48;
    private int coins = 0;

    private Sprite playerSprite;
    private Sprite portalSprite;

    @Override
    public void create() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT * height/width);
        camera.position.set(0, 0, 0);
        camera.update();

        batch = new SpriteBatch();
        backgroundTexture1 = new Texture("background.png");
        characterTexture = new Texture("Whiteboxguy.png");
        platformTexture = new Texture("platformNew.png");
        spikeTexture = new Texture("spikes.png");
        flagTexture = new Texture("flag.png");
        flagActivatedTexture = new Texture("flagActivated.png");
        portalTexture = new Texture("portal.png");
        coinTexture = new Texture("coin.png");

        listRender(Constants.stage1Plats, platformTexture, 10, 5, platforms1);
        listRender(Constants.stage1Spikes, spikeTexture, 4, 4, spikes1);
        listRender(Constants.stage1Flags, flagTexture, 3, 7, flags1);
        listRender(Constants.stage1Flags, flagActivatedTexture, 3, 7, flagsActivated1);
        listRender(Constants.stage1coins, coinTexture, 2, 2, coins1);

        portalSprite = new Sprite(portalTexture);
        portalSprite.setSize(10, 10);
        portalSprite.setPosition(-2, 2);

        playerSprite = new Sprite(characterTexture);
        playerSprite.setSize(5, 4);
        playerSprite.setPosition(spawnX, spawnY);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println(width);
        camera.viewportWidth = Constants.VIEWPORT_WIDTH;
        camera.viewportHeight = Constants.VIEWPORT_HEIGHT / height * width;
        camera.update();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() { // get keyboard inputs

        float speed = 15.0f; // speed of player
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
        
        if(xPos > Constants.VIEWPORT_WIDTH/2) {
            playerSprite.setPosition(-Constants.VIEWPORT_WIDTH/2 - playerSprite.getWidth(), yPos);

        } else if(xPos < -Constants.VIEWPORT_WIDTH/2 - playerSprite.getWidth()) {
            playerSprite.setPosition(Constants.VIEWPORT_WIDTH/2, yPos);
        } if (stage == 1) {
            stageObjects(platforms1, spikes1, flags1, coins1);
        }
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
        }
        return false;
    }

    private boolean isInCollision(Sprite contact) {
        if(playerSprite.getY() < contact.getY() + contact.getHeight() && playerSprite.getY() + playerSprite.getHeight() > contact.getY()) {
            if(playerSprite.getX() < contact.getX() + contact.getWidth() && playerSprite.getX() + playerSprite.getWidth() > contact.getX()) {
                return true;
            }
        }
        return false;
    }

    private String sidecollision(Sprite contact) {
        if(playerSprite.getY() < contact.getY() + contact.getHeight() && playerSprite.getY() + playerSprite.getHeight() > contact.getY()) {
            if (playerSprite.getX() < contact.getX() + contact.getWidth() && playerSprite.getX() > contact.getX() + contact.getWidth() - 2*velocity) {
                return "right";
            } else if (playerSprite.getX() + playerSprite.getWidth() > contact.getX() && playerSprite.getX() + playerSprite.getWidth() < contact.getX() + 2*velocity) {
                return "left";
            } 
        }
        return "none";
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

    private void listRender(int[] list, Texture texture, int width, int height, ArrayList<Sprite> newList) {
        for (int i = 0; i < list.length; i += 2) {
            Sprite sprite = new Sprite(texture);
            sprite.setBounds(list[i], list[i + 1], width, height);
            newList.add(sprite);
        }
    }

    private void stageObjects(ArrayList<Sprite> platforms, ArrayList<Sprite> spikes, ArrayList<Sprite> flags, ArrayList<Sprite> coin) {
        platformcollisisons(platforms);
        for (Sprite s : spikes) {
            if(isInCollision(s)) {
                playerSprite.setPosition(spawnX, spawnY);
            }
        } if(isInCollision(portalSprite)) {
            stage++;
        } for (Sprite s : flags) {
            if(isInCollision(s)) {
                spawnX = s.getX();
                spawnY = s.getY();
            }
        } for (int i = 0; i < coin.size(); i++) {
            if(isInCollision(coin.get(i))) {
                coins++;
                coin.remove(i);
                i--;
            }
        }
    }

    private void stageDraws (ArrayList<Sprite> platforms, ArrayList<Sprite> spikes, ArrayList<Sprite> flags, ArrayList<Sprite> flagsAct, ArrayList<Sprite> coin) {
        for(Sprite s : platforms) {
            s.draw(batch);
        } for(Sprite s : spikes) {
            s.draw(batch);
        } for(int i = 0; i < flags.size(); i++) {
            if(spawnX == flags.get(i).getX() && spawnY == flags.get(i).getY()) {
                flagsAct.get(i).draw(batch);
            } else {
                flags.get(i).draw(batch);
            }
        } for (Sprite s : coin) {
            s.draw(batch);
        }
    }

    private void draw() {

        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        batch.draw(backgroundTexture1, -Constants.VIEWPORT_WIDTH/2, -Constants.VIEWPORT_HEIGHT/2, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        if (stage == 1) {
            stageDraws(platforms1, spikes1, flags1, flagsActivated1, coins1);
        }
        
        portalSprite.draw(batch);
        playerSprite.draw(batch);
        

        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture1.dispose();
    }
}
