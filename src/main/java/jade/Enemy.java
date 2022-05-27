//TODO: update header
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

public class Enemy {
    private int enemyXVal;
    private int enemyYVal;
    private int healthPoints;
    private int sizeEn;
    public boolean enemyDead = false;
    private int enemSpawnSide;
    private int moveX;
    private int moveY;
    public boolean slimeHit;
    private int eCounter = 0;
    private int hitVal = 0;
    private String slimeDir = "none";
    public int enemCounter = 0;
    public int slimeSpriteIndex;
    public int enHitCounter = 0;
    public int enemyHitX;
    public int enemyHitY;
    private boolean charHit;
    private int slimeNumber;
    public int slimeDeadCounter = 0;
    private double speed;

    public Enemy (int startX, int startY, int hp, int size, int sn, double sp) { // constructs an enemy with the proper arguments
        enemyXVal = startX;
        enemyYVal = startY;
        healthPoints = hp;
        sizeEn = size;
        slimeNumber = sn;
        speed = sp;
    }
    public int getEnNum() {
        return slimeNumber;
    } // returns the id number of the current slime

    public void getCharMove(int mX, int mY) { // updates the character values to the slime's position
        moveX = mX;
        moveY = mY;
    }
    public int getNum() {
        return slimeNumber;
    } // returns the id of the slime
    public void getCharHit(boolean hit) {
        slimeHit = hit;
    } // updates whether the slime is touching an enemy
    public boolean getEnHitting() {
        return charHit;
    } // returns whether the slime is touching an enemy
    public int getX() {
        return enemyXVal;
    } // returns the x value of the slime
    public int getY() {
        return enemyYVal;
    } // returns the y value of the slime
    public int getHP() {
        return healthPoints;
    } // returns the current health of the slime
    public int getSize() {
        return sizeEn;
    } // returns the size of the current slime
    public void gotHit() {
        hitVal += 1;
    } // increments the amount of times the slime has been hit
    public int getHitVal() {return hitVal;} // return the amount of times the slime has been hit
    public void resetHit() {hitVal = 0;} // resets the amount of times the slime has been hit to zero

    public void enemyGone() { // deletes the current enemy
        enemyDead = false;
        enemSpawnSide = (int)(Math.random() * 4);
        if (enemSpawnSide == 0) {
            enemyXVal = -1500;
            enemyYVal = (int)(Math.random() * 6000 - 3000);
        } else if (enemSpawnSide == 1) {
            enemyXVal = (int)(Math.random() * 6000 - 3000);
            enemyYVal = 1500;
        }
        else if (enemSpawnSide == 2) {
            enemyXVal = 1500;
            enemyYVal = (int)(Math.random() * 6000 - 3000);
        } else if (enemSpawnSide == 3) {
            enemyXVal = (int)(Math.random() * 6000 - 3000);
            enemyYVal = - 1500;
        }
        speed += 0.5;
    }
    public String getDir() {
        return slimeDir;
    } // returns the direction that the slime is facing

    public void updateEnemy() { // updates the enemy's direction, whether or not the player is hit, and x & y vals
        if (!enemyDead) {
            if (moveX < enemyXVal) {
                enemyXVal -= speed;
                slimeDir = "left";
            }
            if (moveX > enemyXVal) {
                enemyXVal += speed;
                slimeDir = "right";
            }

            if (moveY < enemyYVal) {
                enemyYVal -= speed;
            }
            if (moveY > enemyYVal) {
                enemyYVal += speed;
            }
        }

        if (((enemyXVal < moveX + 60 && enemyXVal > moveX - 20) && (enemyYVal < moveY + 20 && enemyYVal > moveY - 30)) && !enemyDead) {
            charHit = true;
//            System.out.println("En hitting: " + slimeNumber);
        } else {
            charHit = false;
        }
    }
}
