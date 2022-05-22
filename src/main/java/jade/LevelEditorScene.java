package jade;


import java.io.File;
import java.util.Collection;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    public GameObject obj1;
    public GameObject obj3;
    public GameObject cloud;
    public GameObject cloud1;
    public GameObject cloud2;
    public GameObject swing;
    public GameObject swing1;
    private Spritesheet sprites;
    private static int charXVal = 100;
    private static int charYVal = 100;
    private static int camXVal = -570;
    private static int camYVal = -260;
    private static int enemyXVal = -500;
    private static int enemyYVal = -500;
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
    private static boolean charHit = false;
    private static String lastDir = "left";
    private static boolean charVerticle = false;

    private static Texture swordSwingShtTexture = new Texture("assets/images/slash.png");
    private static Spritesheet swingAnim = new Spritesheet(swordSwingShtTexture, 330, 211, 24, 0);
    private static int swingSpriteIndex = 13;
    private static boolean swinging = false;
    private static boolean swinging1 = false;
    private static int swingCounter = 0;
    private static String swingDir = "none";


    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-350, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(charXVal, charYVal), new Vector2f(150, 150)), 3);
        obj1.addComponent(new SpriteRenderer(charAnim.getSprite(charSpriteIndex)));


        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(-1560, -1500), new Vector2f(3280, 3050)), 3);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/grass.png"))));

        obj3 = new GameObject("Object 3", new Transform(new Vector2f(enemyXVal, enemyYVal), new Vector2f(100, 100)), 3);
        obj3.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/testImage2.png"))));

        cloud = new GameObject("Cloud", new Transform(new Vector2f(cloudX, cloudY), new Vector2f(-650, 700)), 3);
        cloud.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/cloud.png"))));

        cloud1 = new GameObject("Cloud1", new Transform(new Vector2f(cloud1X, cloud1Y), new Vector2f(700, 650)), 3);
        cloud1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/cloud.png"))));

        cloud2 = new GameObject("Cloud2", new Transform(new Vector2f(cloud2X, cloud2Y), new Vector2f(500, 550)), 3);
        cloud2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/cloud.png"))));

        swing = new GameObject("Swing", new Transform(new Vector2f(charXVal - 400, charYVal - 200), new Vector2f(500, 500)), 3);
        swing.addComponent(new SpriteRenderer(swingAnim.getSprite(swingSpriteIndex)));

        this.addGameObjectToScene(obj2);
        this.addGameObjectToScene(obj3);
        this.addGameObjectToScene(obj1);
        this.addGameObjectToScene(swing);
        this.addGameObjectToScene(cloud);
        this.addGameObjectToScene(cloud1);
        this.addGameObjectToScene(cloud2);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));

        AssetPool.addSound("assets/sounds/theme.ogg", true);
    }

    public static void moveCharacter(int x, int y) {
        charXVal = x;
        charYVal = y;
    }

    public static void moveEnemy(int x, int y) {
        enemyXVal = x;
        enemyYVal = y;
    }

    public static void trackCamera(int x, int y) {
        camXVal = x - 560;
        camYVal = y - 240;
    }

    public static void moveCloud(int x, int y) {
        cloudX = x;
        cloudY = y;
    }
    public static void moveCloud1(int x, int y) {
        cloud1X = x;
        cloud1Y = y;
    }
    public static void moveCloud2(int x, int y) {
        cloud2X = x;
        cloud2Y = y;
    }

    public static void hitChar() {
        charHit = true;
    }

    public static void unhitChar() {
        charHit = false;
    }

    public static void charRunningRight() {
        charRunning = "right";
    }
    public static void charRunningLeft() {
        charRunning = "left";
    }
    public static void charStanding() {
        charRunning = "standing";
    }
    public static void charVert() {
        charVerticle = true;
        charRunning = "verticle";
    }
    public static void charNotVert() {
        charVerticle = false;
    }
    public static void lastSeen(String l) {lastDir = l;}


    public static void swingSword(String dir) {
        swingDir = dir;
        if (swingDir == "botLeft" || swingDir == "topLeft" || swingDir == "topRight" || swingDir == "botRight") {
            swinging = true;
        } else {
            swinging1 = true;
        }
    }



    @Override
    public void update(float dt) {

        if(charRunning == "right") {
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
            if (charHit) {
                charSpriteIndex = 4;
            }
        }
        if(charRunning == "standing") {
            idleCounter += 3;
            if (idleCounter < 100) {
                charSpriteIndex = 0;
            } else if (idleCounter < 200){
                charSpriteIndex = 9;
            } else {
                idleCounter = 0;
            }
            if (charHit) {
                charSpriteIndex = 4;
            }
        }
        if(charRunning == "left" || charVerticle) {
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
            if (charHit) {
                charSpriteIndex = 4;
            }
        }
        obj1.getComponent(SpriteRenderer.class).setSprite(charAnim.getSprite(charSpriteIndex));

//        obj3.transform.position = new Vector2f(charXVal, charYVal);
//
//        obj1.transform.position = new Vector2f(charXVal, charYVal);
        camera.position = new Vector2f(camXVal, camYVal);


        if (swinging) {
            System.out.println("swinging");
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
            System.out.println("swinging 1");
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







        for (GameObject go : this.gameObjects) {
//            System.out.println("X val: " + charXVal);
//            System.out.println("Y val: " + charYVal);
            go.update(dt);
            if (go.equals(obj1)) {
                go.transform.position = new Vector2f(charXVal, charYVal);

            }
            if (go.equals(obj3)) {
                go.transform.position = new Vector2f(enemyXVal, enemyYVal);
//                System.out.println("en X: " + enemyXVal);
//                System.out.println("en Y: " + enemyYVal);
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

            }

        }

        this.renderer.render();
    }
}