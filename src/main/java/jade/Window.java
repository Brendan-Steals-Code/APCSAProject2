//TODO: update the header here
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

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

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
    private double playerAccel = 2.0;
    private double playerMaxSpeed = 10.0;
    private int camX = 0;
    private int camY = 0;
    private double distFromCam = 0;
    private double distCamX = 0;
    private double distCamY = 0;
    private boolean xCamInBound = true;
    private boolean yCamInBound = true;
    private boolean xMoveInBound = true;
    private boolean yMoveInBound = true;
    private int cloudX = 500;
    private int cloudY = 500;
    private int cloud1X =-1200;
    private int cloud1Y = -1200;
    private int cloud2X = 1200;
    private int cloud2Y = -500;
    private int inertia = 1;
    private int eCounter = 0;
    private int swingCool = 100000;

    private int mouseRelCharY = 0;
    private int mouseRelCharX = 0;

    private int midRange = 200;


    private long audioContext;
    private long audioDevice;

    private Window() { // constructs a window to the correct height and give it the correct parameters
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    public static void changeScene(int newScene) { // changes the scene to a frame representing the scene's id
        switch (newScene) {
            case 0:
                currentScene = new sceneManager();
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

    public static Window get() { // returns the instance of the window
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene() { // returns the instance of the scene
        return get().currentScene;
    }


    public void run() { // runs the program
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);



        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() { // initializes the GL window and assigns the appropriate values
//        theme.play();
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

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library not supported.";
        }


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

    public void loop() { // the mother-method that runs on loop, making up the core of the game
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


            if (KeyListener.isKeyPressed(GLFW_KEY_R) || KeyListener.isKeyPressed(GLFW_KEY_UP)) {
                if (momentumY < playerMaxSpeed) {
                    momentumY += playerAccel;
                }
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_G) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
                if (momentumX < playerMaxSpeed) {
                    momentumX += playerAccel;
                }
            }
            if (!(KeyListener.isKeyPressed(GLFW_KEY_R) || KeyListener.isKeyPressed(GLFW_KEY_UP)) && !(KeyListener.isKeyPressed(GLFW_KEY_F) || KeyListener.isKeyPressed(GLFW_KEY_DOWN))) {
                if (momentumY > 0) {
                    momentumY -= inertia;
                }
                if (momentumY < 0) {
                    momentumY += inertia;
                }
            }



            if (KeyListener.isKeyPressed(GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
                if (momentumX > -playerMaxSpeed) {
                    momentumX -= playerAccel;
                }
                sceneManager.lastSeen("left");
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_F) || KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
                if (momentumY > -playerMaxSpeed) {
                    momentumY -= playerAccel;
                }
                sceneManager.lastSeen("right");
            }
            if (!(KeyListener.isKeyPressed(GLFW_KEY_G) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) && !(KeyListener.isKeyPressed(GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW_KEY_LEFT))) {
                if (momentumX > 0) {
                    momentumX -= inertia;
                }
                if (momentumX < 0) {
                    momentumX += inertia;
                }
            }





            if(momentumX > 2) {
                sceneManager.charRunningRight();
                sceneManager.charNotVert();
            } else if (momentumX < -2) {
                sceneManager.charRunningLeft();
                sceneManager.charNotVert();
            } else if (momentumY > 2 || momentumY < -2){
                sceneManager.charVert();
            } else {
                sceneManager.charStanding();
                sceneManager.charNotVert();
            }

            if (swingCool < 1000) {
                swingCool++;
            }

            distFromCam = Math.sqrt(Math.pow(Math.abs(camX - moveX), 2) + Math.pow(Math.abs(camY - moveY), 2));
            distCamX = moveX - camX;
            distCamY = moveY - camY;

            mouseRelCharX = (int)((MouseListener.getX() - 949) - (distCamX * 1.5));
            mouseRelCharY = (int)((MouseListener.getY() - 579) + (distCamY * 1.5));

// 573, 920
            if(MouseListener.mouseButtonDown(0) && swingCool > 40) {
                if ((mouseRelCharY >= midRange) && (mouseRelCharX < 0)) {
                    sceneManager.swingSword("botLeft");
                    swingCool = 0;
                }
                if ((mouseRelCharY < -midRange) && (mouseRelCharX < 0)) {
                    sceneManager.swingSword("topLeft");
                    swingCool = 0;
                }
                if ((mouseRelCharY > -midRange) && (mouseRelCharY <= midRange) && (mouseRelCharX < 0)) {
                    sceneManager.swingSword("left");
                    swingCool = 0;
                }
                if ((mouseRelCharY > -midRange) && (mouseRelCharY <= midRange) && (mouseRelCharX >= 0)) {
                    sceneManager.swingSword("right");
                    swingCool = 0;
                }
                if ((mouseRelCharY >= midRange) && (mouseRelCharX >= 0)) {
                    sceneManager.swingSword("botRight");
                    swingCool = 0;
                }
                if ((mouseRelCharY < -midRange) && (mouseRelCharX >= 0)) {
                    sceneManager.swingSword("topRight");
                    swingCool = 0;
                }
            }

//            + (int)(distCamX * 1.9) > 1000

            if ((camX + (int)momentumX) + (int)(distCamX * 1.9) > 1000) {
                camX = 1000;
                xCamInBound = false;
            }

            if ((camY + (int)momentumY) + (int)(distCamY * 1.9) > 1000) {
                camY = 1000;
                yCamInBound = false;
            }


            if ((camX + (int)momentumX) + (int)(distCamX * 1.9) < -1000) {
                camX = -1000;
                xCamInBound = false;
            }

            if ((camY + (int)momentumY) + (int)(distCamY * 1.9) < -1000) {
                camY = -1000;
                yCamInBound = false;
            }

            if(xCamInBound) {
                camX = (camX + (int)momentumX);
            }
            if(yCamInBound) {
                camY = (camY + (int)momentumY);
            }
            xCamInBound = true;
            yCamInBound = true;



            if (moveX + (int)momentumX > 1570) {
                moveX = 1570;
                xMoveInBound = false;
            }
            if (moveX + (int)momentumX < -1620) {
                moveX = -1620;
                xMoveInBound = false;
            }

            if (moveY + (int)momentumY > 1300) {
                moveY = 1300;
                yMoveInBound = false;
            }
            if (moveY + (int)momentumY < -1240) {
                moveY = -1240;
                yMoveInBound = false;
            }

            if(xMoveInBound) {
                moveX = moveX + (int)momentumX;
            }
            if(yMoveInBound) {
                moveY = moveY + (int)momentumY;
            }
            xMoveInBound = true;
            yMoveInBound = true;

            // Cloud
            if (cloudX > -2300){
                cloudX--;
            }
            else{
                cloudY = (int)(Math.random() * 2000 - 1200);
                cloudX = 1920;
            }

            if (cloud1X > -2300){
                cloud1X--;
            }
            else{
                cloud1Y = (int)(Math.random() * 2000 - 1200);
                cloud1X = 1920;
            }

            if (cloud2X > -2300){
                cloud2X--;
            }
            else{
                cloud2Y = (int)(Math.random() * 2000 - 1200);
                cloud2X = 1920;
            }

            sceneManager.trackCamera(camX, camY);
            sceneManager.moveCharacter(moveX, moveY);

            sceneManager.moveCloud(cloudX, cloudY);
            sceneManager.moveCloud1(cloud1X, cloud1Y);
            sceneManager.moveCloud2(cloud2X, cloud2Y);


            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;


        }
    }
}