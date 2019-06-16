package game;

public class Obstacle implements Progressable {

    private int x;
    private int y;
    private int height;
    private int width;

    public Obstacle(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = 10;
    }

    @Override
    public void progressATick() {
        x -= 1;
    }

    public boolean isOutOfCanvas() {
        return x < -width;
    }

    public boolean intersects(Player player) {
        int rx = player.getX();
        int ry = player.getY();
        int rheight = player.getHeight();
        int rwidth = player.getWidth();

        return x < rx + rwidth && x + width > rx && y < ry + rheight && y + height > ry;
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
}
