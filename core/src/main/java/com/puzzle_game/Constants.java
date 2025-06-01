package com.puzzle_game;

import com.badlogic.gdx.graphics.Texture;

public final class Constants {

    public static final float VIEWPORT_HEIGHT = 100f;
    public static final float VIEWPORT_WIDTH = 100f;
    public static final Texture backgroundTexture1 = new Texture("background.png");
    public static final Texture backgroundTexture2 = new Texture("backdrop2.png");
    public static final Texture backgroundTexture3 = new Texture("backdrop3.png");
    public static final Texture backgroundTexture4 = new Texture("backdrop4.png");
    public static final Texture characterTexture = new Texture("Whiteboxguy.png");
    public static final Texture cheaterTexture = new Texture("RedBoxGuy.png");
    public static final Texture platformTexture = new Texture("platformNew.png");
    public static final Texture spikeTexture = new Texture("spikes.png");
    public static final Texture flagTexture = new Texture("flag.png");
    public static final Texture flagActivatedTexture = new Texture("flagActivated.png");
    public static final Texture portalTexture = new Texture("portal.png");
    public static final Texture coinTexture = new Texture("coin.png");


    public static final int[] stage1Plats = {-47, -47,
         -55, -53, 
         -45, -53, 
         -35, -53, 
         -25, -53, 
         -15, -53, 
         -5, -53, 
         5, -53, 
         15, -53, 
         15, -53, 
         25, -53, 
         35, -53, 
         45, -53, 
         55, -53, 
         37, -40, 
         20, -35,
         10, -35,
         -25, -30,
         -50, -21,
         -55, -16,
         50, -16,
         30, -7,
         20, -7,
         10, -7,
         0, -7,
         -20, -7};
    public static final int[] stage1Spikes = {0, -2, 4, -2, 10, -49};
    public static final int[] stage1Flags = {32, -2, -49, -40};
    public static final int[] stage1coins = {0, 0, 5, 0, 10, 0};

    public static final int[] stage2Plats = {-55, -53, 
         -45, -53, 
         -35, -53, 
         -25, -53, 
         -15, -53, 
         -5, -53, 
         5, -53, 
         15, -53, 
         15, -53, 
         25, -53, 
         35, -53, 
         45, -53, 
         55, -53};
    public static final int[] stage2Spikes = {100, 100};
    public static final int[] stage2Flags = {100, 100};
    public static final int[] stage2coins = {100, 100};

    public static final int[] stage3Plats = {-55, -53, 
         -45, -53, 
         -35, -53, 
         -25, -53, 
         -15, -53, 
         -5, -53, 
         5, -53, 
         15, -53, 
         15, -53, 
         25, -53, 
         35, -53, 
         45, -53, 
         55, -53};
    public static final int[] stage3Spikes = {100, 100};
    public static final int[] stage3Flags = {100, 100};
    public static final int[] stage3coins = {100, 100};

    public static final int[] stage4Plats = {-55, -53, 
         -45, -53, 
         -35, -53, 
         -25, -53, 
         -15, -53, 
         -5, -53, 
         5, -53, 
         15, -53, 
         15, -53, 
         25, -53, 
         35, -53, 
         45, -53, 
         55, -53};
    public static final int[] stage4Spikes = {100, 100};
    public static final int[] stage4Flags = {100, 100};
    public static final int[] stage4coins = {100, 100};
}