package game;

import lombok.Getter;

public class Player implements Progressable {

    private int x = 70;
    private int y = 10;
    private int height = 10;
    private int width = 10;
    private int moveSpeed = 2;
    private boolean isAlive = true;
    private long age = 0;

    private World world;
    @Getter
    private Brain brain;

    public Player(Brain brain) {
        this.brain = brain;
    }

    @Override
    public void progressATick() {
        /**
         * 1. Fetch inputs
         * player x,y
         * whole game field? 800*500
         * 2. Feed inputs to brain
         * 3. Get result and act
         */
        if (isAlive) {
            age++;
            Brain.Direction nextMove = brain.getNextMove(this, world);

            switch (nextMove) {
                case UP:
                    up();
                    break;
                case DOWN:
                    down();
                    break;
                case NOTHING:
                    break;
            }
        }
    }

    public void up() {
        if (y <= 0) {
            return;
        }
        y -= moveSpeed;
    }

    public void down() {
        if (y >= 490) {
            return;
        }
        y += moveSpeed;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void die() {
        isAlive = false;
    }

    public long getAge() {
        return age;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
