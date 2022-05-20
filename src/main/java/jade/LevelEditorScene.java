package jade;

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
    private static String charRunning = "standing";
    private static Texture charSprtShtTexture = new Texture("assets/images/WalkSpriteSheet.png");
    private static Spritesheet charAnim = new Spritesheet(charSprtShtTexture, 20, 32, 4, 0);
    private static int charSpriteIndex = 0;

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

        this.addGameObjectToScene(obj2);
        this.addGameObjectToScene(obj3);
        this.addGameObjectToScene(obj1);
        this.addGameObjectToScene(cloud);
        this.addGameObjectToScene(cloud1);
        this.addGameObjectToScene(cloud2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
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

    public static void charRunningRight() {
        charRunning = "right";
    }
    public static void charRunningLeft() {
        charRunning = "left";
    }
    public static void charStanding() {
        charRunning = "standing";
    }

    @Override
    public void update(float dt) {

        if(charRunning == "right") {
//            System.out.println("right");
            rightCounter += 20;
            if (rightCounter < 100) {
                charSpriteIndex = 1;
            } else if (rightCounter < 200) {
                charSpriteIndex = 2;
            } else if (rightCounter < 300){
                charSpriteIndex = 3;
            } else {
                rightCounter = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(charAnim.getSprite(charSpriteIndex));
        }
        if(charRunning == "standing") {
//            System.out.println("standing");
//            obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/flushedDeepFried.png"))));
            charSpriteIndex = 0;
            obj1.getComponent(SpriteRenderer.class).setSprite(charAnim.getSprite(charSpriteIndex));
        }
        if(charRunning == "left") {
//            System.out.println("left");
//            obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/MarioLeft.png"))));
//            obj1.getComponent(SpriteRenderer.class).flipSprite();
            rightCounter += 20;
            if (rightCounter < 100) {
                charSpriteIndex = 1;
            } else if (rightCounter < 200) {
                charSpriteIndex = 2;
            } else if (rightCounter < 300){
                charSpriteIndex = 3;
            } else {
                rightCounter = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(charAnim.getSprite(charSpriteIndex));
        }

//        obj3.transform.position = new Vector2f(charXVal, charYVal);
//
//        obj1.transform.position = new Vector2f(charXVal, charYVal);
        camera.position = new Vector2f(camXVal, camYVal);

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

        }

        this.renderer.render();
    }
}