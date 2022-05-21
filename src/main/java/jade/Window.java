package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.logging.Level;

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
    private int enemX = -500;
    private int enemY = -500;
    private int cloudX = 500;
    private int cloudY = 500;
    private int cloud1X =-1200;
    private int cloud1Y = -1200;
    private int cloud2X = 1200;
    private int cloud2Y = -500;
    private int inertia = 1;
    private int eCounter = 0;
    private double enemXDist;
    private double enemYDist;
    private double xEnemSlope;
    private double yEnemSlope;
    private int swingCool = 100000;

    private int mouseRelCharY = 0;
    private int mouseRelCharX = 0;

    private int midRange = 200;


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


            if (KeyListener.isKeyPressed(GLFW_KEY_W) || KeyListener.isKeyPressed(GLFW_KEY_UP)) {
                if (momentumY < playerMaxSpeed) {
                    momentumY += playerAccel;
                }
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
                if (momentumX < playerMaxSpeed) {
                    momentumX += playerAccel;
                }
            }
            if (!(KeyListener.isKeyPressed(GLFW_KEY_W) || KeyListener.isKeyPressed(GLFW_KEY_UP)) && !(KeyListener.isKeyPressed(GLFW_KEY_S) || KeyListener.isKeyPressed(GLFW_KEY_DOWN))) {
                if (momentumY > 0) {
                    momentumY -= inertia;
                }
                if (momentumY < 0) {
                    momentumY += inertia;
                }
            }



            if (KeyListener.isKeyPressed(GLFW_KEY_A) || KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
                if (momentumX > -playerMaxSpeed) {
                    momentumX -= playerAccel;
                }
                LevelEditorScene.lastSeen("left");
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_S) || KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
                if (momentumY > -playerMaxSpeed) {
                    momentumY -= playerAccel;
                }
                LevelEditorScene.lastSeen("right");
            }
            if (!(KeyListener.isKeyPressed(GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) && !(KeyListener.isKeyPressed(GLFW_KEY_A) || KeyListener.isKeyPressed(GLFW_KEY_LEFT))) {
                if (momentumX > 0) {
                    momentumX -= inertia;
                }
                if (momentumX < 0) {
                    momentumX += inertia;
                }
            }





            if(momentumX > 2) {
                LevelEditorScene.charRunningRight();
                LevelEditorScene.charNotVert();
            } else if (momentumX < -2) {
                LevelEditorScene.charRunningLeft();
                LevelEditorScene.charNotVert();
            } else if (momentumY > 2 || momentumY < -2){
                LevelEditorScene.charVert();
            } else {
                LevelEditorScene.charStanding();
                LevelEditorScene.charNotVert();
            }

            if (swingCool < 1000) {
                swingCool++;
            }

            distFromCam = Math.sqrt(Math.pow(Math.abs(camX - moveX), 2) + Math.pow(Math.abs(camY - moveY), 2));
            distCamX = moveX - camX;
            distCamY = moveY - camY;

            mouseRelCharX = (int)((MouseListener.getX() - 949) - (distCamX * 1.5));
            mouseRelCharY = (int)((MouseListener.getY() - 579) + (distCamY * 1.5));

//            System.out.println("X Without: " + MouseListener.getX());
//            System.out.println("Y Without: " + MouseListener.getY());
//            System.out.println("X: " + mouseRelCharX);
            System.out.println("Y: " + mouseRelCharY);
// 573, 920
            if(MouseListener.mouseButtonDown(0) && swingCool > 40) {
                if ((mouseRelCharY >= midRange) && (mouseRelCharX < 0)) {
                    LevelEditorScene.swingSword("botLeft");
                    swingCool = 0;
                    System.out.println("botLeft");
                }
                if ((mouseRelCharY < -midRange) && (mouseRelCharX < 0)) {
                    LevelEditorScene.swingSword("topLeft");
                    swingCool = 0;
                    System.out.println("topLeft");
                }
                if ((mouseRelCharY > -midRange) && (mouseRelCharY <= midRange) && (mouseRelCharX < 0)) {
                    LevelEditorScene.swingSword("left");
                    swingCool = 0;
                    System.out.println("left");
                }
                if ((mouseRelCharY > -midRange) && (mouseRelCharY <= midRange) && (mouseRelCharX >= 0)) {
                    LevelEditorScene.swingSword("right");
                    swingCool = 0;
                    System.out.println("right");
                }
                if ((mouseRelCharY >= midRange) && (mouseRelCharX >= 0)) {
                    LevelEditorScene.swingSword("botRight");
                    swingCool = 0;
                    System.out.println("botRight");
                }
                if ((mouseRelCharY < -midRange) && (mouseRelCharX >= 0)) {
                    LevelEditorScene.swingSword("topRight");
                    swingCool = 0;
                    System.out.println("topRight");
                }
            }



//            System.out.println(distFromCam);


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
                camX = (camX + (int)momentumX) + (int)(distCamX * 1.9);
            }
            if(yCamInBound) {
                camY = (camY + (int)momentumY) + (int)(distCamY * 1.9);
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

//            enemXDist = moveX - enemX;
//            enemYDist = moveY - enemY;
//            xEnemSlope = enemYDist/enemXDist;
//            yEnemSlope = enemXDist/enemYDist;
//            System.out.println(xEnemSlope);
//
//            if (xEnemSlope < 1 && xEnemSlope > -1) {
//                System.out.println("X slope");
//                if (enemX > moveX) {
//                    enemX += yEnemSlope;
//                    enemY += -(xEnemSlope);
//                } else {
//                    enemX += 1;
//                    enemY += xEnemSlope;
//                }
//            } else {
//                System.out.println("Y slope");
//                if (enemY > moveY) {
//
//                }
//            }


            if (moveX < enemX) {
                enemX -= 3;
            }
            if (moveX > enemX) {
                enemX += 3;
            }

            if (moveY < enemY) {
                enemY -= 3;
            }
            if (moveY > enemY) {
                enemY += 3;
            }

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
//            System.out.println("In X range: " + (enemX < moveX + 100 && enemX > moveX - 100));
//            System.out.println("In Y range: " + (enemY < moveY + 100 && enemX > moveY - 100));

            if ((enemX < moveX + 100 && enemX > moveX - 100) && (enemY < moveY + 100 && enemY > moveY - 100)) {
                eCounter++;
                if(eCounter % 100 == 0) {
//                    System.out.println("Enemy in range");
                }
                LevelEditorScene.hitChar();
            } else {
                LevelEditorScene.unhitChar();
            }

//            System.out.println("enemyX: " + enemX);
//            System.out.println("enemyY: " + enemY);
//            System.out.println("moveX: " + moveX);
//            System.out.println("moveY: " + moveY);

//            if (enemX < )

//            if ()



            LevelEditorScene.trackCamera(camX, camY);
            LevelEditorScene.moveCharacter(moveX, moveY);
            LevelEditorScene.moveEnemy(enemX, enemY);

            LevelEditorScene.moveCloud(cloudX, cloudY);
            LevelEditorScene.moveCloud1(cloud1X, cloud1Y);
            LevelEditorScene.moveCloud2(cloud2X, cloud2Y);


            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}