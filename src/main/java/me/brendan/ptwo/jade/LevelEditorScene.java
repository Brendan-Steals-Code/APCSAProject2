package me.brendan.ptwo.jade;

import me.brendan.ptwo.components.Sprite;
import me.brendan.ptwo.components.SpriteRenderer;
import me.brendan.ptwo.components.Spritesheet;
import org.joml.Vector2f;
import me.brendan.ptwo.util.AssetPool;

public class LevelEditorScene extends Scene {

    public GameObject obj1;
    private Spritesheet sprites;
    private static int charXVal = 100;
    private static int charYVal = 100;
    private static int camXVal = -570;
    private static int camYVal = -260;
    private static String charRunning = "standing";

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-350, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(charXVal, charYVal), new Vector2f(150, 150)), 3);
        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/testImage.png"))));


        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(-2500, -2500), new Vector2f(5000, 5000)), 3);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/villager1.png"))));

        this.addGameObjectToScene(obj2);

        this.addGameObjectToScene(obj1);
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

    public static void trackCamera(int x, int y) {
        camXVal = x - 560;
        camYVal = y - 240;
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
            System.out.println("right");
//            obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/testImage.png"))));
        }
        if(charRunning == "standing") {
            System.out.println("standing");
//            obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/flushedDeepFried.png"))));
        }
        if(charRunning == "left") {
            System.out.println("left");
//            obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/MarioLeft.png"))));
        }

        obj1.transform.position = new Vector2f(charXVal, charYVal);
        camera.position = new Vector2f(camXVal, camYVal);

        for (GameObject go : this.gameObjects) {
//            System.out.println("X val: " + charXVal);
//            System.out.println("Y val: " + charYVal);
            go.update(dt);
        }

        this.renderer.render();
    }
}