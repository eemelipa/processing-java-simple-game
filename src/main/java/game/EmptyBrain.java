package game;

public class EmptyBrain implements Brain {
    @Override
    public Direction getNextMove(Player player, World world) {
        return Direction.NOTHING;
    }
}
