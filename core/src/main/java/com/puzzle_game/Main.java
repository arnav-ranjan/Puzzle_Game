package com.puzzle_game;

import java.util.ArrayList;

import org.w3c.dom.Text;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
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
    private Texture backdropSprite;

    private OrthographicCamera camera;
    private float screenX = 600;
    private float screenY = 600;

    private ArrayList<Sprite> platformsList = new ArrayList<>();
    private ArrayList<Sprite> spikesList = new ArrayList<>();
    private ArrayList<Sprite> coinsList = new ArrayList<>();
    private ArrayList<Sprite> flagsList = new ArrayList<>();
    private ArrayList<Sprite> flagsActivatedList = new ArrayList<>();

    private float velocity = 0.3f;
    private double millitime;
    private int time;
    private int nextTime;
    private double temptime;
    private int up;
    private int stage;
    private boolean cheats = false;
    private int type;
    private float spawnX = 0;
    private float spawnY = -48;
    private int coins;

    private Sprite playerSprite;
    private Sprite portalSprite;

    /*
     * Creates every item initially before the loop starts and sets dimensions and textures as well as the camera
     */
    @Override
    public void create() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        // Setting up the camera of the screen
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT * height/width);
        camera.position.set(0, 0, 0);
        camera.update();

        batch = new SpriteBatch();
        backdropSprite = Constants.backgroundTexture1;

        // Rendering all of the lists in Constants class that contain coordinates of each object
        listRender(Constants.stage1Plats, Constants.platformTexture, 10, 5, platformsList);
        listRender(Constants.stage1Spikes, Constants.spikeTexture, 4, 4, spikesList);
        listRender(Constants.stage1Flags, Constants.flagTexture, 3, 7, flagsList);
        listRender(Constants.stage1Flags, Constants.flagActivatedTexture, 3, 7, flagsActivatedList);
        listRender(Constants.stage1coins, Constants.coinTexture, 2, 2, coinsList);

        // Renders the portal for next stage
        portalSprite = new Sprite(Constants.portalTexture);
        portalSprite.setSize(10, 10);
        portalSprite.setPosition(38, 20);

        // Renders the player of the game
        playerSprite = new Sprite(Constants.characterTexture);
        playerSprite.setSize(5, 4);
        playerSprite.setPosition(spawnX, spawnY);
    }

    // Changes the camera box when the window is resized to prevent major distortion
    @Override
    public void resize(int width, int height) {
        camera.update();
        screenX = width;
        screenY = height;
    }

    // The main part of the code that runs in a loop when the libGDX engine is started
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
            // System.out.println(playerSprite.getX()); Prints out the X coordinate when moved

        } else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            playerSprite.translateX(-speed * delta);
            // System.out.println(playerSprite.getX()); Prints out the X coordinate when moved

        } if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            if(up == 0 || up == 1) { // Allows for double jump
                playerSprite.translateY(velocity);
                up++;
                temptime = millitime;
            }
        } 
        // Turns on cheats if user presses Ctrl + q
        if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.Q)) {
            cheats = !cheats;
        }
        // Activates developer commands when entered cheat code
        if(cheats) {
            if(Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
                System.out.println("You have selected Platforms");
                type = 1;
            } else if(Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
                System.out.println("You have selected Spikes");
                type = 2;
            } else if(Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
                System.out.println("You have selected Coins");
                type = 3;
            } else if(Gdx.input.isKeyJustPressed(Keys.NUM_4)) {
                System.out.println("You have selected Flags");
                type = 4;
            } else if(Gdx.input.isKeyJustPressed(Keys.NUM_5)) {
                System.out.println("You have selected Portals");
                type = 5;
            } else if(Gdx.input.isKeyJustPressed(Keys.NUM_0)) {
                System.out.println("You have selected Player");
                type = 0;
            } worldBuilder();
            
        }

    }

    /*
     * Contains all the logic for wrapping, gravity, and collision boundaries of the player and runs them
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
            // System.out.println(time);  Prints the time
        }
        
        // Wraps the sprite around the screen so that it reappears on the screen no matter how far it travels
        if(xPos > Constants.VIEWPORT_WIDTH/2) {
            playerSprite.setPosition(-Constants.VIEWPORT_WIDTH/2 - playerSprite.getWidth(), yPos);

        } else if(xPos < -Constants.VIEWPORT_WIDTH/2 - playerSprite.getWidth()) {
            playerSprite.setPosition(Constants.VIEWPORT_WIDTH/2, yPos);
        } 
        
        // Implements all collisions and boundaries for each type of object depending on the stage the game is on
        stageObjects(platformsList, spikesList, flagsList, coinsList);
    }

    /*
     * Runs a constant gravity moving the player downwards and accelerating until it reaches a bound
     */
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
            // System.out.println(playerSprite.getY()); Prints out the changing Y coordinate of the player
        } else {
            playerSprite.setY(bound);
            up = 0;
            temptime = millitime;
        }
    }

    /*
     * Checks if the player goes under a spite object (used for platforms)
     */
    private boolean undercollision(Sprite contact) {
        if (playerSprite.getY() + playerSprite.getHeight() > contact.getY() && playerSprite.getY() + playerSprite.getHeight() - 2*velocity < contact.getY() && playerSprite.getX() > contact.getX() - playerSprite.getWidth() && playerSprite.getX() < contact.getX() + contact.getWidth()) {
            return true;
        }
        return false;
    }

    /*
     * Checks if the player goes inside of a sprite object
     */
    private boolean isInCollision(Sprite contact) {
        if(playerSprite.getY() < contact.getY() + contact.getHeight() && playerSprite.getY() + playerSprite.getHeight() > contact.getY()) {
            if(playerSprite.getX() < contact.getX() + contact.getWidth() && playerSprite.getX() + playerSprite.getWidth() > contact.getX()) {
                return true;
            }
        }
        return false;
    }

    /*
     * Checks if the player goes on either side of a sprite object (used for platforms)
     * returns either "right" or "left" or ignored if there is no collision
     */
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

    /*
     * Actual collision logic of where to put player when collisions are true to prevent player going in bounds of platforms
     */
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

    /*
     * An easy implement in the create function to minimize repetition of code when creating different types of objects
     */
    private void listRender(int[] list, Texture texture, int width, int height, ArrayList<Sprite> newList) {
        newList.clear();
        for (int i = 0; i < list.length; i += 2) {
            Sprite sprite = new Sprite(texture);
            sprite.setBounds(list[i], list[i + 1], width, height);
            newList.add(sprite);
        }
    }


    /*
     * Contains the logic of what happens to the player when collides with objects other than platforms
     */
    private void stageObjects(ArrayList<Sprite> platforms, ArrayList<Sprite> spikes, ArrayList<Sprite> flags, ArrayList<Sprite> coin) {
        platformcollisisons(platforms);
        for (Sprite s : spikes) {
            if(isInCollision(s)) {
                playerSprite.setPosition(spawnX, spawnY);
            }
        } if(isInCollision(portalSprite)) {
            stage++;
            nextStage();
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

    /*
     * An easy implement in the draw function to minimize repetition of code when drawing different types of objects
     */
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

    /*
     * A purely developer intended function that prints out where items have been placed in a world (can only be accessed when cheats are on)
     */
    private void worldBuilder() {
        if(Gdx.input.justTouched()) {
            if(type == 1) {
                Sprite plat = new Sprite();
                plat.set(platformsList.get(0));
                plat.setPosition((int)(((100 * Gdx.input.getX()/screenX) - 50) - plat.getWidth()/2), (int)((50 - (100 * Gdx.input.getY()/screenY)) - plat.getHeight()/2));
                platformsList.add(plat);
            } else if(type == 2) {
                Sprite spik = new Sprite();
                spik.set(spikesList.get(0));
                spik.setPosition((int)(((100 * Gdx.input.getX()/screenX) - 50) - spik.getWidth()/2), (int)((50 - (100 * Gdx.input.getY()/screenY)) - spik.getHeight()/2));
                spikesList.add(spik);
            } else if(type == 3) {
                Sprite coin = new Sprite();
                coin.set(coinsList.get(0));
                coin.setPosition((int)(((100 * Gdx.input.getX()/screenX) - 50) - coin.getWidth()/2), (int)((50 - (100 * Gdx.input.getY()/screenY)) - coin.getHeight()/2));
                coinsList.add(coin);
            } else if(type == 4) {
                Sprite flag = new Sprite();
                Sprite flagA = new Sprite();
                flag.set(flagsList.get(0));
                flagA.set(flagsActivatedList.get(0));
                flag.setPosition((int)(((100 * Gdx.input.getX()/screenX) - 50) - flag.getWidth()/2), (int)((50 - (100 * Gdx.input.getY()/screenY)) - flag.getHeight()/2));
                flagA.setPosition((int)(((100 * Gdx.input.getX()/screenX) - 50) - flagA.getWidth()/2), (int)((50 - (100 * Gdx.input.getY()/screenY)) - flagA.getHeight()/2));
                flagsList.add(flag);
                flagsActivatedList.add(flagA);
            } else if(type == 5) {
                portalSprite.setPosition((int)(((100 * Gdx.input.getX()/screenX) - 50) - portalSprite.getWidth()/2), (int)((50 - (100 * Gdx.input.getY()/screenY)) - portalSprite.getHeight()/2));
            } else if(type == 0) {
                playerSprite.setPosition((int)(((100 * Gdx.input.getX()/screenX) - 50) - playerSprite.getWidth()/2), (int)((50 - (100 * Gdx.input.getY()/screenY)) - playerSprite.getHeight()/2));
                temptime = millitime;
            } 
        } if(type == 1 && platformsList.size() > 13) {
            if(Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
                platformsList.removeLast();
            } else if(Gdx.input.isKeyJustPressed(Keys.W)) {
                platformsList.getLast().translateY(1);
            } else if(Gdx.input.isKeyJustPressed(Keys.A)) {
                platformsList.getLast().translateX(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.S)) {
                platformsList.getLast().translateY(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.D)) {
                platformsList.getLast().translateX(1);
            }
        } else if(type == 2 && spikesList.size() > 1) {
            if(Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
                spikesList.removeLast();
            } else if(Gdx.input.isKeyJustPressed(Keys.W)) {
                spikesList.getLast().translateY(1);
            } else if(Gdx.input.isKeyJustPressed(Keys.A)) {
                spikesList.getLast().translateX(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.S)) {
                spikesList.getLast().translateY(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.D)) {
                spikesList.getLast().translateX(1);
            }
        } else if(type == 3 && coinsList.size() > 1) {
            if(Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
                coinsList.removeLast();
            } else if(Gdx.input.isKeyJustPressed(Keys.W)) {
                coinsList.getLast().translateY(1);
            } else if(Gdx.input.isKeyJustPressed(Keys.A)) {
                coinsList.getLast().translateX(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.S)) {
                coinsList.getLast().translateY(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.D)) {
                coinsList.getLast().translateX(1);
            }
        } else if(type == 4 && flagsList.size() > 1) {
            if(Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
                flagsList.removeLast();
                flagsActivatedList.removeLast();
            } else if(Gdx.input.isKeyJustPressed(Keys.W)) {
                flagsList.getLast().translateY(1);
                flagsActivatedList.getLast().translateY(1);
            } else if(Gdx.input.isKeyJustPressed(Keys.A)) {
                flagsList.getLast().translateX(-1);
                flagsActivatedList.getLast().translateX(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.S)) {
                flagsList.getLast().translateY(-1);
                flagsActivatedList.getLast().translateY(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.D)) {
                flagsList.getLast().translateX(1);
                flagsActivatedList.getLast().translateX(1);
            }
        } else if(type == 5) {
            if(Gdx.input.isKeyJustPressed(Keys.W)) {
                portalSprite.translateY(1);
            } else if(Gdx.input.isKeyJustPressed(Keys.A)) {
                portalSprite.translateX(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.S)) {
                portalSprite.translateY(-1);
            } else if(Gdx.input.isKeyJustPressed(Keys.D)) {
                portalSprite.translateX(1);
            }
        }
        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            System.out.print("public static final int[] stage" + (stage + 1) + "Plats = {");
            for(Sprite s : platformsList) {
                System.out.print((int)s.getX() + ", " + (int)s.getY() + ", ");
            }
            System.out.println("\b\b};");
            System.out.print("public static final int[] stage" + (stage + 1) + "Spikes = {");
            for(Sprite s : spikesList) {
                System.out.print((int)s.getX() + ", " + (int)s.getY() + ", ");
            }
            System.out.println("\b\b};");
            System.out.print("public static final int[] stage" + (stage + 1) + "Flags = {");
            for(Sprite s : flagsList) {
                System.out.print((int)s.getX() + ", " + (int)s.getY() + ", ");
            }
            System.out.println("\b\b};");
            System.out.print("public static final int[] stage" + (stage + 1) + "coins = {");
            for(Sprite s : coinsList) {
                System.out.print((int)s.getX() + ", " + (int)s.getY() + ", ");
            }
            System.out.println("\b\b};");
            System.out.println("Portal placed at: (" + (int)portalSprite.getX() + ", " + (int)portalSprite.getY() + ")");
        }
    }

    /*
     * Creates different stages of the game after completing the previous stage called when player touches on portal
     */
    private void nextStage() {
        if (stage == 1) { // creates stage 2 (the caves)
            backdropSprite = Constants.backgroundTexture2;
            spawnX = 0;
            spawnY = -48;
            playerSprite.setPosition(spawnX, spawnY);
            portalSprite.setPosition(40, 40);
            listRender(Constants.stage2Plats, Constants.platformTexture, 10, 5, platformsList);
            listRender(Constants.stage2Spikes, Constants.spikeTexture, 4, 4, spikesList);
            listRender(Constants.stage2Flags, Constants.flagTexture, 3, 7, flagsList);
            listRender(Constants.stage2Flags, Constants.flagActivatedTexture, 3, 7, flagsActivatedList);
            listRender(Constants.stage2coins, Constants.coinTexture, 2, 2, coinsList);
        } else if (stage == 2) { // creates stage 3 (the night)
            backdropSprite = Constants.backgroundTexture3;
            spawnX = 0;
            spawnY = -48;
            playerSprite.setPosition(spawnX, spawnY);
            portalSprite.setPosition(40, 40);
            listRender(Constants.stage3Plats, Constants.platformTexture, 10, 5, platformsList);
            listRender(Constants.stage3Spikes, Constants.spikeTexture, 4, 4, spikesList);
            listRender(Constants.stage3Flags, Constants.flagTexture, 3, 7, flagsList);
            listRender(Constants.stage3Flags, Constants.flagActivatedTexture, 3, 7, flagsActivatedList);
            listRender(Constants.stage3coins, Constants.coinTexture, 2, 2, coinsList);
        } else if (stage == 3) { // creates stage 4 (the desert)
            backdropSprite = Constants.backgroundTexture4;
            spawnX = 0;
            spawnY = -48;
            playerSprite.setPosition(spawnX, spawnY);
            portalSprite.setPosition(40, 40);
            listRender(Constants.stage4Plats, Constants.platformTexture, 10, 5, platformsList);
            listRender(Constants.stage4Spikes, Constants.spikeTexture, 4, 4, spikesList);
            listRender(Constants.stage4Flags, Constants.flagTexture, 3, 7, flagsList);
            listRender(Constants.stage4Flags, Constants.flagActivatedTexture, 3, 7, flagsActivatedList);
            listRender(Constants.stage4coins, Constants.coinTexture, 2, 2, coinsList);
        }
    }

    /*
     * The function that draws all of the sprites during each loop of the game.
     */
    private void draw() {

        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        batch.draw(backdropSprite, -Constants.VIEWPORT_WIDTH/2, -Constants.VIEWPORT_HEIGHT/2, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        stageDraws(platformsList, spikesList, flagsList, flagsActivatedList, coinsList);
        
        portalSprite.draw(batch);
        if(cheats) {
            playerSprite.setTexture(Constants.cheaterTexture);
        } else {
            playerSprite.setTexture(Constants.characterTexture);
        }
        playerSprite.draw(batch);

        batch.end();
    }

    /*
     * Disposes of all textures after each loop so that drawings are not repeated over and over again
     */
    @Override
    public void dispose() {
        batch.dispose();
        Constants.backgroundTexture1.dispose();
    }
}
