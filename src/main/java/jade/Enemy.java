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

    public Enemy (int startX, int startY, int hp, int size, int sn, double sp) {
        enemyXVal = startX;
        enemyYVal = startY;
        healthPoints = hp;
        sizeEn = size;
        slimeNumber = sn;
        speed = sp;
    }
    public int getEnNum() {
        return slimeNumber;
    }

    public void getCharMove(int mX, int mY) {
        moveX = mX;
        moveY = mY;
    }
    public int getNum() {
        return slimeNumber;
    }
    public void getCharHit(boolean hit) {
        slimeHit = hit;
    }
    public boolean getEnHitting() {
        return charHit;
    }
    public int getX() {
        return enemyXVal;
    }
    public int getY() {
        return enemyYVal;
    }
    public int getHP() {
        return healthPoints;
    }
    public int getSize() {
        return sizeEn;
    }
    public void gotHit() {
        hitVal += 1;
    }
    public int getHitVal() {return hitVal;}
    public void resetHit() {hitVal = 0;}

    public void enemyGone() {
        enemyDead = false;
        enemSpawnSide = (int)(Math.random() * 4);
        if (enemSpawnSide == 0) {
            enemyXVal = -3000;
            enemyYVal = (int)(Math.random() * 6000 - 3000);
        } else if (enemSpawnSide == 1) {
            enemyXVal = (int)(Math.random() * 6000 - 3000);
            enemyYVal = 3000;
        }
        else if (enemSpawnSide == 2) {
            enemyXVal = 3000;
            enemyYVal = (int)(Math.random() * 6000 - 3000);
        } else if (enemSpawnSide == 3) {
            enemyXVal = (int)(Math.random() * 6000 - 3000);
            enemyYVal = - 3000;
        }
    }
    public String getDir() {
        return slimeDir;
    }

    public void updateEnemy() {
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

        if (((enemyXVal < moveX + 100 && enemyXVal > moveX - 100) && (enemyYVal < moveY + 100 && enemyYVal > moveY - 100))) {
            charHit = true;
        } else {
            charHit = false;
        }
    }
}
