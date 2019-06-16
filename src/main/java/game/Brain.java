package game;

public interface Brain {
    Direction getNextMove(Player player, World world);

    enum Direction {
        UP, DOWN, NOTHING
    }
}
