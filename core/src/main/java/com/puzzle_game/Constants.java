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
    public static final Texture platformTexture2 = new Texture("plat2.png");
    public static final Texture platformTexture3 = new Texture("plat3.png");
    public static final Texture platformTexture4 = new Texture("plat4.png");
    public static final Texture spikeTexture = new Texture("spikes.png");
    public static final Texture flagTexture = new Texture("flag.png");
    public static final Texture flagActivatedTexture = new Texture("flagActivated.png");
    public static final Texture portalTexture = new Texture("portal.png");
    public static final Texture coinTexture = new Texture("coin.png");
    public static final Texture speedTexture = new Texture("speed.png");


    public static final int[] stage1Plats = {-55, -53, -45, -53, -35, -53, -25, -53, -15, -53, -5, -53, 5, -53, 15, -53, 15, -53, 25, -53, 35, -53, 45, -53, 55, -53, 37, -40, 20, -35, 10, -35, -25, -30, -50, -21, -55, -16, 50, -16, 30, -7, 20, -7, 10, -7, 0, -7, -20, -7, -47, -47, -54, -1, -44, -1, -34, 3, -24, 8, -14, 13, -8, 13, -2, 13, 4, 13, 10, 13, 16, 13, 22, 13, 28, 13, 34, 13, 40, 13, 44, 13, 50, -1};
    public static final int[] stage1Spikes = {100, 100, 0, -2, 4, -2, 2, 18, 6, 18, 10, 18, 27, 18};
    public static final int[] stage1Flags = {100, 100, 32, -2, -12, 18};
    public static final int[] stage1coins = {100, 100, 32, -38, 24, -28, 14, -28, -32, -10, -20, -1, -12, -1, -16, -1, 7, 26, -43, -32, 41, -20};
    public static final int[] stage1speeds = {100, 100};

    public static final int[] stage2Plats = {-55, -53, -45, -53, -35, -53, -25, -53, -15, -53, -5, -53, 5, -53, 15, -53, 15, -53, 25, -53, 35, -53, 45, -53, 55, -53, -55, -48, -55, -43, -55, -38, -55, -33, -55, -28, -55, -23, -32, -33, -55, -18, -45, -18, -35, -18, 19, -33, 29, -33, 39, -33, 49, -33, 46, -28, 46, -23, 46, -18, 46, -13, 28, -8, 46, 3, 46, 8, 46, 13, -39, 12, -29, 12, -19, 12, -9, 12, -49, 7, -60, 12, -59, 7, -9, 17, -9, 22, -9, 27, -9, 32, 1, 32, 11, 32, 21, 32, 31, 32, 41, 32, 50, 32, -60, 32, 49, -48, 49, -43, 49, -38};
    public static final int[] stage2Spikes = {100, 100, 46, -8, -43, 12, -47, 12, -51, 12, 45, -48, 41, -48, 37, -48, 33, -48, 29, -48, -57, -13, -57, -10, -57, -6};
    public static final int[] stage2Flags = {100, 100, 21, -28, -38, 17};
    public static final int[] stage2coins = {100, 100, -37, -40, -37, -24, -2, -31, -5, -31, 1, -31, 29, -1, 32, -1, 35, -1, 47, 19, -28, 34, -32, 34, -24, 34};
    public static final int[] stage2speeds = {100, 100};

    public static final int[] stage3Plats = {-55, -53, -45, -53, -35, -53, -25, -53, -15, -53, -5, -53, 5, -53, 15, -53, 15, -53, 25, -53, 35, -53, 45, -53, 55, -53, -25, -46, 30, -38, -9, -34, -44, -29, -54, -27, -54, -22, 27, -13, -1, -9, -35, -6, -22, -19, 10, -24, 32, 3, 54, -22, 54, -26, 22, 5, 12, 8, 2, 12, -8, 17, -50, 24};
    public static final int[] stage3Spikes = {100, 100};
    public static final int[] stage3Flags = {100, 100};
    public static final int[] stage3coins = {100, 100, -40, -22, -18, -12, 3, -2, 31, -6, 14, -17, -5, -26, 26, 25, -25, 29};
    public static final int[] stage3speeds = {100, 100, -22, -39, 34, -31, -31, 0, -5, 23};

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
    public static final int[] stage4speeds = {100, 100};
}