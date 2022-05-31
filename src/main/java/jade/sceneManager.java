/*
Project 2 APCSA
Mrs. Woldseth Period 4
Brendan Aeria and Colin Finney

Pre-condition: The Window class gives the character, cloud, and camera movement values into the
scene manager class, and the Enemy class is used in order to manage several slime enemies.
GameObjects (what I use to render sprites) are all properly declared and have their values set.

Post-condition: Using these values the scene manager calculates and figures out how to
translate, scale, and animate sprites to make the game come to life. Then the sceneManager
 iterates through the gameObjects that have been declared and makes sure that all of the
 sprites have been updated. This scene also manages the sound by using the values that are
 readily available in this class.
 */



package jade;


import java.io.File;
import java.util.Collection;

import jade.fromTutorial.GameObject;
import jade.fromTutorial.Scene;
import jade.fromTutorial.Transform;
import org.joml.Vector2f;
import util.AssetPool;


import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import renderer.Texture;

public class sceneManager extends Scene {

    public GameObject obj1;
    public GameObject heartContainers;
    public GameObject villager;
    public GameObject slime1, slime2, slime3, slime5, slime6, slime7, slime8, slime9, slime10, slime11;
    public Enemy en1 = new Enemy(300, 700, 4, 120, 1, 3.5);
    public Enemy en2 = new Enemy(-1300, -7000, 5, 125, 2, 4.2);
    public Enemy en3 = new Enemy(3000, 700, 6, 130, 3, 4.6);
//    public Enemy en4 = new Enemy(-3000, -700, 7, 140, 4, 4);
    public Enemy en5 = new Enemy(3300, -7000, 10, 160, 5, 3.8);
    public Enemy en6 = new Enemy(-7000, 80000, 20, 200, 6, 4.5);
    public Enemy en7 = new Enemy(-2000, 2000, 8, 120, 7, 4.5);
    public Enemy en8 = new Enemy(-30000, -7000, 7, 140, 4, 4.2);
    public Enemy en9 = new Enemy(3300, -7000, 10, 160, 5, 4.5);
    public Enemy en10 = new Enemy(-4000, 4000, 40, 450, 6, 2.8);
    public Enemy en11 = new Enemy(2000, -3000, 8, 120, 7, 4.8);

    public Enemy[] enemyList = new Enemy[]{en1, en2, en3, en5, en6, en7, en8, en9, en10, en11};
    public GameObject cloud;
    public GameObject cloud1;
    public GameObject cloud2;
    public GameObject swing;
    public GameObject bodySword;
    public GameObject overText;
    private Spritesheet sprites;
    private static int charXVal = 100;
    private static int charYVal = 100;
    private static int camXVal = -570;
    private static int camYVal = -260;
    private static int enemyXVal = -7000;
    private static int enemyYVal = -7000;
    private static int cloudX = 500;
    private static int cloudY = 500;
    private static int cloud1X = -1000;
    private static int cloud1Y = -1000;
    private static int cloud2X = 1200;
    private static int cloud2Y = 500;
    private static int rightCounter = 0;
    private static int idleCounter = 0;
    private static String charRunning = "standing";
    private static Texture charSprtShtTexture = new Texture("assets/images/charAnim.png");
    private static Spritesheet charAnim = new Spritesheet(charSprtShtTexture, 21, 21, 13, 0);
    private static int charSpriteIndex = 0;
    private static String lastDir = "left";
    private static boolean charVerticle = false;


    private static Texture heartSprtShtTexture = new Texture("assets/images/heartContainer.png");
    private static Spritesheet heartAnim = new Spritesheet(heartSprtShtTexture, 44, 8, 4, 0);
    private static int heartSpriteIndex = 0;

    private static Texture swordSwingShtTexture = new Texture("assets/images/slash.png");
    private static Spritesheet swingAnim = new Spritesheet(swordSwingShtTexture, 330, 211, 24, 0);
    private static int swingSpriteIndex = 13;

    private static boolean swinging = false;
    private static boolean swinging1 = false;
    private static int swingCounter = 0;
    private static String swingDir = "none";
    private static boolean enemHit = false;

    private static int bodySwordX = charXVal;
    private static int bodySwordY = charYVal;
    private static String enemDir = "right";
    private static int enemCounter = 0;
    private static int enHitCounter = 0;
    private static int enemX = 0;
    private static int enemY = 0;
    private static int numEnemyHit = 0;
    private static int enemyHitX;
    private static int enemyHitY;
    private static boolean enemyDead = false;
    private static int enDeadCounter = 0;
    private static Sound smack = new Sound("assets/sounds/smack.ogg", false);



    private static Texture slimeShtTexture = new Texture("assets/images/slimeSprite.png");
    private static Spritesheet slimeAnim = new Spritesheet(slimeShtTexture, 10, 20, 12, 0);
    private static int slimeSpriteIndex = 0;


    private int charDamage;
    private int heartIndex = 0;


    private boolean charDead = false;
    private int deadX;
    private int deadY;
    private int charDeadCounter;
    private boolean alreadyDead = false;
    private int deadCamX;
    private int deadCamY;
    private int enemiesKilled = 0;

    public sceneManager() {

    }

    @Override
    public void init() { // Initiate the scene
        loadResources();

        this.camera = new Camera(new Vector2f(-350, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(charXVal, charYVal), new Vector2f(150, 150)), 3);
        obj1.addComponent(new SpriteRenderer(charAnim.getSprite(charSpriteIndex)));

        heartContainers = new GameObject("hearts", new Transform(new Vector2f(camXVal, camYVal), new Vector2f(440, 70)), 3);
        heartContainers.addComponent(new SpriteRenderer(heartAnim.getSprite(0)));


        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(-1560, -1500), new Vector2f(3280, 3050)), 3);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/grass.png"))));

        slime1 = new GameObject("Object 3", new Transform(new Vector2f(en1.getX(), en1.getY()), new Vector2f(en1.getSize(), en1.getSize())), 3);
        slime1.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime2 = new GameObject("Object 3", new Transform(new Vector2f(en2.getX(), en2.getY()), new Vector2f(en2.getSize(), en2.getSize())), 3);
        slime2.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime3 = new GameObject("Object 3", new Transform(new Vector2f(en3.getX(), en3.getY()), new Vector2f(en3.getSize(), en3.getSize())), 3);
        slime3.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
//        slime4 = new GameObject("Object 3", new Transform(new Vector2f(en4.getX(), en4.getY()), new Vector2f(en4.getSize(), en4.getSize())), 3);
//        slime4.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime5 = new GameObject("Object 3", new Transform(new Vector2f(en5.getX(), en5.getY()), new Vector2f(en5.getSize(), en5.getSize())), 3);
        slime5.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime6 = new GameObject("Object 3", new Transform(new Vector2f(en6.getX(), en6.getY()), new Vector2f(en6.getSize(), en6.getSize())), 3);
        slime6.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime7 = new GameObject("Object 3", new Transform(new Vector2f(en7.getX(), en7.getY()), new Vector2f(en7.getSize(), en7.getSize())), 3);
        slime7.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime8 = new GameObject("Object 3", new Transform(new Vector2f(en8.getX(), en8.getY()), new Vector2f(en8.getSize(), en8.getSize())), 3);
        slime8.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime9 = new GameObject("Object 3", new Transform(new Vector2f(en9.getX(), en9.getY()), new Vector2f(en9.getSize(), en9.getSize())), 3);
        slime9.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime10 = new GameObject("Object 3", new Transform(new Vector2f(en10.getX(), en10.getY()), new Vector2f(en10.getSize(), en10.getSize())), 3);
        slime10.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));
        slime11 = new GameObject("Object 3", new Transform(new Vector2f(en11.getX(), en11.getY()), new Vector2f(en11.getSize(), en11.getSize())), 3);
        slime11.addComponent(new SpriteRenderer(slimeAnim.getSprite(slimeSpriteIndex)));

        cloud = new GameObject("Cloud", new Transform(new Vector2f(cloudX, cloudY), new Vector2f(-650, 700)), 3);
        cloud.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/cloud.png"))));

        cloud1 = new GameObject("Cloud1", new Transform(new Vector2f(cloud1X, cloud1Y), new Vector2f(700, 650)), 3);
        cloud1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/cloud.png"))));

        cloud2 = new GameObject("Cloud2", new Transform(new Vector2f(cloud2X, cloud2Y), new Vector2f(500, 550)), 3);
        cloud2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/cloud.png"))));

        swing = new GameObject("Swing", new Transform(new Vector2f(charXVal - 400, charYVal - 200), new Vector2f(500, 500)), 3);
        swing.addComponent(new SpriteRenderer(swingAnim.getSprite(swingSpriteIndex)));

        bodySword = new GameObject("Swing", new Transform(new Vector2f(bodySwordX, bodySwordY), new Vector2f(500, 500)), 3);
        bodySword.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/bodySword.png"))));

        overText = new GameObject("Swing", new Transform(new Vector2f(-350, 0), new Vector2f(900, 150)), 3);
        overText.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/gameOver.png"))));

        villager = new GameObject("Swing", new Transform(new Vector2f(-350, 0), new Vector2f(900, 150)), 3);
        villager.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/gameOver.png"))));


        this.addGameObjectToScene(obj2);
        this.addGameObjectToScene(slime11);
        this.addGameObjectToScene(slime10);
        this.addGameObjectToScene(slime9);
        this.addGameObjectToScene(slime8);
        this.addGameObjectToScene(slime7);
        this.addGameObjectToScene(slime6);
        this.addGameObjectToScene(slime5);
//        this.addGameObjectToScene(slime4);
        this.addGameObjectToScene(slime3);
        this.addGameObjectToScene(slime2);
        this.addGameObjectToScene(slime1);
        this.addGameObjectToScene(obj1);
        this.addGameObjectToScene(swing);
        this.addGameObjectToScene(bodySword);
        this.addGameObjectToScene(cloud);
        this.addGameObjectToScene(cloud1);
        this.addGameObjectToScene(cloud2);

        this.addGameObjectToScene(heartContainers);




    }

    private void loadResources() { // Loads all images & sounds
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));

        AssetPool.addSound("assets/sounds/theme.ogg", true);
        AssetPool.addSound("assets/sounds/smack.ogg", false);
        AssetPool.addSound("assets/sounds/lose.ogg", false);
    }

    public static void moveCharacter(int x, int y) { // sets the character's x and y values to the method arguments
        charXVal = x;
        charYVal = y;
    }

    public static void trackCamera(int x, int y) { // offsets the x and y values to match the camera
        camXVal = x - 560;
        camYVal = y - 240;
    }

    public static void moveCloud(int x, int y) { // Move the cloud to the specified x and y coordinates
        cloudX = x;
        cloudY = y;
    }
    public static void moveCloud1(int x, int y) { // Move the second cloud to the specified x and y coordinates
        cloud1X = x;
        cloud1Y = y;
    }
    public static void moveCloud2(int x, int y) { // Move the third cloud to the specified x and y coordinates
        cloud2X = x;
        cloud2Y = y;
    }

    public boolean isHitChar() { // detect whether an enemy is hitting the character
        if (en1.getEnHitting() || en2.getEnHitting() || en3.getEnHitting() || en5.getEnHitting() || en6.getEnHitting() || en7.getEnHitting() || en8.getEnHitting() || en9.getEnHitting() || en10.getEnHitting() || en11.getEnHitting()) {
            return true;
        } else {
            return false;
        }

    }

    public static void charRunningRight() { // set the character to be running to the right
        charRunning = "right";
    }
    public static void charRunningLeft() { // set the character to be running to the left
        charRunning = "left";
    }
    public static void charStanding() { // set the character to be standing still
        charRunning = "standing";
    }
    public static void charVert() { // set the character to be oriented vertically
        charVerticle = true;
        charRunning = "verticle";
    }
    public static void charNotVert() { // set the character to be oriented horizontally
        charVerticle = false;
    }
    public static void lastSeen(String l) {lastDir = l;} // update the lastSeen variable to the existing argument

    public static void swingSword(String dir) { // swings the sword in the specified direction
        swingDir = dir;
        if (swingDir == "botLeft" || swingDir == "topLeft" || swingDir == "topRight" || swingDir == "botRight") {
            swinging = true;
        } else {
            swinging1 = true;
        }
    }

    @Override
    public void update(float dt) { // updates the scene and normalizes the time between frames
        for (Enemy slime : enemyList) {
            slime.getCharMove(charXVal, charYVal);
            slime.updateEnemy();
            if (swinging || swinging1) {
                if (swingDir == "botLeft") {
                    if((slime.getX() < charXVal + 60) && (slime.getX() > charXVal - 270) && (slime.getY() < charYVal + 100) && (slime.getY() > charYVal - 270)) {
                        slime.getCharHit(true);
                    }
                }
                if (swingDir == "left") {
                    if((slime.getX() < charXVal + 20) && (slime.getX() > charXVal - 300) && (slime.getY() < charYVal + 230) && (slime.getY() > charYVal - 230)) {
//                        slime.gotHit();
                        slime.getCharHit(true);
                    }
                }
                if (swingDir == "topLeft") {
                    if((slime.getX() < charXVal + 60) && (slime.getX() > charXVal - 270) && (slime.getY() < charYVal + 270) && (slime.getY() > charYVal - 100)) {
//                        slime.gotHit();
                        slime.getCharHit(true);
                    }
                }
                if (swingDir == "topRight") {
                    if((slime.getX() < charXVal + 270) && (slime.getX() > charXVal - 60) && (slime.getY() < charYVal + 270) && (slime.getY() > charYVal - 100)) {
//                        slime.gotHit();
                        slime.getCharHit(true);
                    }
                }
                if (swingDir == "right") {
                    if((slime.getX() < charXVal + 300) && (slime.getX() > charXVal - 20) && (slime.getY() < charYVal + 230) && (slime.getY() > charYVal - 230)) {
//                        slime.gotHit();
                        slime.getCharHit(true);
                    }
                }
                if (swingDir == "botRight") {
                    if((slime.getX() < charXVal + 270) && (slime.getX() > charXVal - 60) && (slime.getY() < charYVal + 100) && (slime.getY() > charYVal - 270)) {
//                        slime.gotHit();
                        slime.getCharHit(true);
                    }
                }
            }
        }

        if(charRunning == "right") {
            if(!(swinging || swinging1)) {
                bodySword.transform.scale = new Vector2f(250, 250);
                bodySword.transform.position = new Vector2f(charXVal - 100, charYVal - 50);
            } else {
                bodySword.transform.scale = new Vector2f(0, 0);
            }

            rightCounter += 15;
            if (rightCounter < 100) {
                charSpriteIndex = 5;
            } else if (rightCounter < 200) {
                charSpriteIndex = 6;
            } else if (rightCounter < 300){
                charSpriteIndex = 7;
            } else {
                rightCounter = 0;
            }
            if (isHitChar()) {
                charSpriteIndex = 4;
                charDamage++;
            }
        }
        if(charRunning == "standing") {
            if(!(swinging || swinging1)) {
                bodySword.transform.scale = new Vector2f(-250, 250);
                bodySword.transform.position = new Vector2f(charXVal + 260, charYVal - 50);
            } else {
                bodySword.transform.scale = new Vector2f(0, 0);
            }
            idleCounter += 3;
            if (idleCounter < 100) {
                charSpriteIndex = 0;
            } else if (idleCounter < 200){
                charSpriteIndex = 9;
            } else {
                idleCounter = 0;
            }
            if (isHitChar()) {
                charSpriteIndex = 4;
                charDamage++;
            }
        }
        if(charRunning == "left" || charVerticle) {
            if(!(swinging || swinging1)) {
                bodySword.transform.scale = new Vector2f(-250, 250);
                bodySword.transform.position = new Vector2f(charXVal + 260, charYVal - 50);
            } else {
                bodySword.transform.scale = new Vector2f(0, 0);
            }
            rightCounter += 15;
            if (rightCounter < 100) {
                charSpriteIndex = 1;
            } else if (rightCounter < 200) {
                charSpriteIndex = 2;
            } else if (rightCounter < 300){
                charSpriteIndex = 3;
            } else {
                rightCounter = 0;
            }
            if (isHitChar()) {
                charSpriteIndex = 4;
                charDamage++;
            }
        }
        obj1.getComponent(SpriteRenderer.class).setSprite(charAnim.getSprite(charSpriteIndex));

        if (charDamage > 10) {
            heartIndex = 1;
        }
        if (charDamage > 40) {
            heartIndex = 2;
        }
        if (charDamage > 80) {
            heartIndex = 3;
            obj1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(6));
            charDead = true;
            deadX = charXVal;
            deadY = charYVal;
            deadCamX = camXVal;
            deadCamY = camYVal;
            charDamage = -99999;
        }
        heartContainers.getComponent(SpriteRenderer.class).setSprite(heartAnim.getSprite(heartIndex));



        if (charDead) {
            overText.transform.position = new Vector2f(0, 0);
            bodySword.transform.position = new Vector2f(-3000, -3000);
            charXVal = deadX;
            charYVal = deadY;
            camXVal = deadCamX;
            camYVal = deadCamY;
            swing.transform.position = new Vector2f(-3000, -3000);
            charDeadCounter++;
            if(charDeadCounter < 40) {
                obj1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(5));
            } else if (charDeadCounter < 80) {
                obj1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(6));
            } else if (charDeadCounter < 120) {
                obj1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(7));
            } else if (charDeadCounter < 160) {
                obj1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(8));
            } else {

                obj1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(8));
            }
        }







        camera.position = new Vector2f(camXVal, camYVal);


        if (swinging) {
            swingCounter += 40;
            if (swingCounter < 100) {
                swingSpriteIndex = 0;
            } else if (swingCounter < 200) {
                swingSpriteIndex = 1;
            } else if (swingCounter < 300){
                swingSpriteIndex = 2;
            } else if (swingCounter < 400) {
                swingSpriteIndex = 3;
            } else if (swingCounter < 550){
                swingSpriteIndex = 4;
            } else if (swingCounter < 700) {
                swingSpriteIndex = 5;
            } else if (swingCounter < 20000) {
                swingCounter = 0;
                swinging = false;
                swingSpriteIndex = 13;
            }
        }
        if (swinging1) {
            swingCounter += 40;
            if (swingCounter < 100) {
                swingSpriteIndex = 6;
            } else if (swingCounter < 200) {
                swingSpriteIndex = 7;
            } else if (swingCounter < 350){
                swingSpriteIndex = 8;
            } else if (swingCounter < 520) {
                swingSpriteIndex = 9;
            } else if (swingCounter < 700){
                swingSpriteIndex = 10;
            } else if (swingCounter < 20000) {
                swingCounter = 0;
                swinging1 = false;
                swingSpriteIndex = 13;
            }
        }

        swing.getComponent(SpriteRenderer.class).setSprite(swingAnim.getSprite(swingSpriteIndex));

        for (Enemy slime : enemyList) {
            if (slime.getDir() == "right") {
                slime.enemCounter += 5;
                if (slime.enemCounter < 100) {
                    slime.slimeSpriteIndex = 1;

                } else if (slime.enemCounter < 200) {
                    slime.slimeSpriteIndex = 0;
                } else {
                    slime.enemCounter = 0;
                }
            }
            if (slime.getDir() == "left") {
                slime.enemCounter += 5;
                if (slime.enemCounter < 100) {
                    slime.slimeSpriteIndex = 2;

                } else if (slime.enemCounter < 200) {
                    slime.slimeSpriteIndex = 3;
                } else {
                    slime.enemCounter = 0;
                }
            }

            if (slime.slimeHit) {
                if (slime.enHitCounter == 0) {
                    slime.gotHit();
                    slime.enHitCounter += 1;
                }
                slime.enemyHitX = slime.getX();
                slime.enemyHitY = slime.getY();


                if (slime.getNum() == 1) {
                    slime1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 2) {
                    slime2.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 3) {
                    slime3.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
//                if (slime.getNum() == 4) {
//                    slime4.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
//                }
                if (slime.getNum() == 5) {
                    slime5.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 6) {
                    slime6.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 7) {
                    slime7.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 8) {
                    slime8.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 9) {
                    slime9.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 10) {
                    slime10.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                if (slime.getNum() == 11) {
                    slime11.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
                }
                slime.enHitCounter += 1;
                if (slime.enHitCounter > 20) {
                    slime.slimeHit = false;
                    slime.enHitCounter = 0;
                }
            } else {
                if (slime.getNum() == 1) {
                    slime1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 2) {
                    slime2.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 3) {
                    slime3.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
//                if (slime.getNum() == 4) {
//                    slime4.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
//                }
                if (slime.getNum() == 5) {
                    slime5.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 6) {
                    slime6.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 7) {
                    slime7.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 8) {
                    slime8.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 9) {
                    slime9.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 10) {
                    slime10.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 11) {
                    slime11.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
            }

            if (slime.getHitVal() > slime.getHP()) {
                if (slime.getNum() == 1) {
                    slime1.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 2) {
                    slime2.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 3) {
                    slime3.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
//                if (slime.getNum() == 4) {
//                    slime4.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
//                }
                if (slime.getNum() == 5) {
                    slime5.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 6) {
                    slime6.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 7) {
                    slime7.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 8) {
                    slime8.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 9) {
                    slime9.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 10) {
                    slime10.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                if (slime.getNum() == 11) {
                    slime11.transform.position = new Vector2f(slime.enemyHitX, slime.enemyHitY);
                }
                slime.enemyDead = true;
                slime.resetHit();

            } else {
                if (slime.getNum() == 1) {
                    slime1.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 2) {
                    slime2.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 3) {
                    slime3.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
//                if (slime.getNum() == 4) {
//                    slime4.transform.position = new Vector2f(slime.getX(), slime.getY());
//                }
                if (slime.getNum() == 5) {
                    slime5.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 6) {
                    slime6.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 7) {
                    slime7.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 8) {
                    slime8.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 9) {
                    slime9.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 10) {
                    slime10.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
                if (slime.getNum() == 11) {
                    slime11.transform.position = new Vector2f(slime.getX(), slime.getY());
                }
            }
//            if (slime.getNum() == 1) {
//                slime1.transform.position = new Vector2f(slime.getX(), slime.getY());
//                System.out.println("hit value: " + slime.hitVal + " -- slimeHit?: " + slime.slimeHit);
//            }

            if (slime.enemyDead) {
                slime.slimeDeadCounter += 1;
                if(slime.slimeDeadCounter == 5) {
                    enemiesKilled++;
                }
                if(slime.slimeDeadCounter < 30) {
                    slime.slimeSpriteIndex = 4;
                } else if (slime.slimeDeadCounter < 60) {
                    slime.slimeSpriteIndex = 5;
                } else if (slime.slimeDeadCounter < 90) {
                    slime.slimeSpriteIndex = 6;
                } else if (slime.slimeDeadCounter < 120) {
                    slime.slimeSpriteIndex = 7;
                } else {
                    slime.slimeSpriteIndex = 8;
                    slime.enemyGone();
                    slime.slimeDeadCounter = 0;
                }
                if (slime.getNum() == 1) {
                    slime1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 2) {
                    slime2.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 3) {
                    slime3.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
//                if (slime.getNum() == 4) {
//                    slime4.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
//                }
                if (slime.getNum() == 5) {
                    slime5.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 6) {
                    slime6.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 7) {
                    slime7.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 8) {
                    slime8.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 9) {
                    slime9.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 10) {
                    slime10.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }
                if (slime.getNum() == 11) {
                    slime11.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slime.slimeSpriteIndex));
                }

            }

//            if (enemHit) {
//                if (enHitCounter == 0) {
//                    numEnemyHit += 1;
//                }
//                enemyHitX = enemyXVal;
//                enemyHitY = enemyYVal;
//
//
//                slime1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
//                enHitCounter += 1;
//                if (enHitCounter > 20) {
//                    enemHit = false;
//                    enHitCounter = 0;
//                }
//
//            } else {
//                slime1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slimeSpriteIndex));
//            }


        }


        
        


        Collection<Sound> sounds = AssetPool.getAllSounds();
        for (Sound sound : sounds) { // play all sounds from the game in the proper context
            File tmp = new File(sound.getFilepath());
            if (tmp.getName().equals("theme.ogg") && !(charDead)) {
                sound.play();
            }

            if ((swinging || swinging1) && (tmp.getName().equals("smack.ogg"))) {
                sound.play();
            }

            if (charDead && tmp.getName().equals("lose.ogg") && !(alreadyDead)) {
                sound.play();
                alreadyDead = true;
            }

            if (tmp.getName().equals("theme.ogg") && charDead) {
                sound.stop();
            }
        }

//        if (enemyDead) {
//            enDeadCounter += 1;
//            if(enDeadCounter < 30) {
//                slimeSpriteIndex = 4;
//            } else if (enDeadCounter < 60) {
//                slimeSpriteIndex = 5;
//            } else if (enDeadCounter < 90) {
//                slimeSpriteIndex = 6;
//            } else if (enDeadCounter < 120) {
//                slimeSpriteIndex = 7;
//            } else {
//                slimeSpriteIndex = 8;
////                Window.enemyGone();
//                enemyDead = false;
//                enDeadCounter = 0;
//            }
//        }

//        if (enemHit) {
//            if (enHitCounter == 0) {
//                numEnemyHit += 1;
//            }
//            enemyHitX = enemyXVal;
//            enemyHitY = enemyYVal;
//
//
//            slime1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(4));
//            enHitCounter += 1;
//            if (enHitCounter > 20) {
//                enemHit = false;
//                enHitCounter = 0;
//            }
//
//        } else {
//            slime1.getComponent(SpriteRenderer.class).setSprite(slimeAnim.getSprite(slimeSpriteIndex));
//        }






        for (GameObject go : this.gameObjects) { // update all of the game objects
            go.update(dt);
            if (go.equals(heartContainers)) {
                go.transform.position = new Vector2f(camXVal - 140, camYVal + 590);
            }
            if (go.equals(obj1)) {
                go.transform.position = new Vector2f(charXVal, charYVal);

            }
            if (go.equals(slime1)) {

            }
            if (go.equals(cloud)) {
                go.transform.position = new Vector2f(cloudX, cloudY);
            }
            if (go.equals(cloud1)) {
                go.transform.position = new Vector2f(cloud1X, cloud1Y);
            }
            if (go.equals(cloud2)) {
                go.transform.position = new Vector2f(cloud2X, cloud2Y);
            }
            if (go.equals(swing)) {
                if (swingDir == "topLeft") {
                    go.transform.scale = new Vector2f(500, -500);
                    go.transform.position = new Vector2f(charXVal - 150, charYVal + 390);
                }
                if (swingDir == "botLeft") {
                    go.transform.scale = new Vector2f(500, 500);
                    go.transform.position = new Vector2f(charXVal - 160, charYVal - 250);
                }
                if (swingDir == "topRight") {
                    go.transform.scale = new Vector2f(-500, -500);
                    go.transform.position = new Vector2f(charXVal + 260, charYVal + 390);
                }
                if (swingDir == "botRight") {
                    go.transform.scale = new Vector2f(-500, 500);
                    go.transform.position = new Vector2f(charXVal + 280, charYVal - 270);
                }



                if (swingDir == "left") {
                    go.transform.scale = new Vector2f(500, -400);
                    go.transform.position = new Vector2f(charXVal - 380, charYVal + 290);
                }
                if (swingDir == "right") {
                    go.transform.scale = new Vector2f(-500, -400);
                    go.transform.position = new Vector2f(charXVal + 530, charYVal + 290);
                }

                if (charDead) {
                    go.transform.position = new Vector2f(-3000, -3000);
                }


            }
            if(go.equals(bodySword)){
//                go.transform.position = new Vector2f(charXVal-100, charYVal-100);
            }
            if(go.equals(slime1)) {
//                go.
            }
        }


        this.renderer.render();
        System.out.println(enemiesKilled);
    }
}