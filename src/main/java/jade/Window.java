package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    public float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;

    private static Scene currentScene;
    private int moveX = 0;
    private int moveY = 0;
    private double momentumX = 0;
    private double momentumY = 0;
    private double playerAccel = 1.0;
    private double playerMaxSpeed = 10.0;
    private int camX = 0;
    private int camY = 0;
    private double distFromCam = 0;
    private double distCamX = 0;
    private double distCamY = 0;
    private double camMomX = 0;
    private double camMomY = 0;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                currentScene.update(dt);
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
                r = Math.max(r - 0.05f, 0);
                g = Math.max(g - 0.05f, 0);
                b = Math.max(b - 0.05f, 0);
            } else if(!(r > 1f) || !(g > 1f) || !(b > 1f)) {
                r = Math.max(r + 0.05f, 0);
                g = Math.max(g + 0.05f, 0);
                b = Math.max(b + 0.05f, 0);
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_A) || KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
                if (momentumX > -playerMaxSpeed) {
                    momentumX -= playerAccel;
                }
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
                if (momentumX < playerMaxSpeed) {
                    momentumX += playerAccel;
                }
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_S) || KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
                if (momentumY > -playerMaxSpeed) {
                    momentumY -= playerAccel;
                }
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_W) || KeyListener.isKeyPressed(GLFW_KEY_UP)) {
                if (momentumY < playerMaxSpeed) {
                    momentumY += playerAccel;
                }
            }

            if (!(KeyListener.isKeyPressed(GLFW_KEY_W) || KeyListener.isKeyPressed(GLFW_KEY_UP)) && !(KeyListener.isKeyPressed(GLFW_KEY_S) || KeyListener.isKeyPressed(GLFW_KEY_DOWN))) {
                if (momentumY > 0) {
                    momentumY -= 0.5;
                }
                if (momentumY < 0) {
                    momentumY += 0.5;
                }
            }

            if (!(KeyListener.isKeyPressed(GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) && !(KeyListener.isKeyPressed(GLFW_KEY_A) || KeyListener.isKeyPressed(GLFW_KEY_LEFT))) {
                if (momentumX > 0) {
                    momentumX -= 0.5;
                }
                if (momentumX < 0) {
                    momentumX += 0.5;
                }
            }



            moveX = moveX + (int)momentumX;
            moveY = moveY + (int)momentumY;

            distFromCam = Math.sqrt(Math.pow(Math.abs(camX - moveX), 2) + Math.pow(Math.abs(camY - moveY), 2));
            distCamX = moveX - camX;
            distCamY = moveY - camY;

            System.out.println(distFromCam);



            camX = (camX + (int)momentumX) + (int)(distCamX * 1.9);
            camY = (camY + (int)momentumY) + (int)(distCamY * 1.9);

            LevelEditorScene.trackCamera(camX, camY);
            LevelEditorScene.moveCharacter(moveX, moveY);

            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}