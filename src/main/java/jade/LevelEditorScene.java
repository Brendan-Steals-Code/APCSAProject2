package jade;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    public GameObject obj1;
    private Spritesheet sprites;
    private static int charXVal = 100;
    private static int charYVal = 100;
    private static int camXVal = -570;
    private static int camYVal = -260;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-350, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(charXVal, charYVal), new Vector2f(150, 150)), 3);
        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/flushedDeepFried.png"))));


        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(camXVal, camYVal), new Vector2f(2560, 2560)), 3);
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

    @Override
    public void update(float dt) {

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