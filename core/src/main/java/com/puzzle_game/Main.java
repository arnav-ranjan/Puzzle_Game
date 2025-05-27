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

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Texture backgroundTexture;
    private Texture characterTexture; // TODO fix background

    private float VIEWPORT_WIDTH = 100f;
    private float VIEWPORT_HEIGHT = 100f;

    private double millitime = 0.0;
    private int time = 0;
    private int nextTime = 0;
    private double temptime = 0;
    private int up = 0;

    Sprite playerSprite;
    Body player;

    @Override
    public void create() {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT * height/width);
        camera.position.set(0, 0, 0);
        camera.update();

        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        batch = new SpriteBatch();
        backgroundTexture = new Texture("background.png");
        characterTexture = new Texture("Whiteboxguy.png");

        playerSprite = new Sprite(characterTexture);
        playerSprite.setSize(5, 5);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(0, 0);
        player = world.createBody(bodyDef);
        player.setUserData(playerSprite);
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
        
        debugRenderer.render(world, camera.combined);
        world.step(1/60f, 6, 2);

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
        
        gravity(-VIEWPORT_HEIGHT/2);
    }

    private void gravity(float bound) {

        float gravity = 0.5f;

        if(up > 0) {
            playerSprite.translateY(0.3f); // upwards velocity value
        } if(playerSprite.getY() > bound) {
            playerSprite.translateY(-gravity * (float)(millitime - temptime));
        } else {
            playerSprite.setY(bound);
            up = 0;
        }
    }

    private void draw() {

        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        
        batch.draw(backgroundTexture, -VIEWPORT_WIDTH/2, -VIEWPORT_HEIGHT/2, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        
        
        playerSprite.draw(batch);

        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
    }
}
