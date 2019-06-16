package renderers;

import java.util.List;

import game.Obstacle;
import game.Player;
import game.SimpleGame;
import game.World;

public class MasterRenderer {

    private static boolean renderVision = true;
    private static AILineVisionRenderer aiLineVisionRenderer = new AILineVisionRenderer();
    private static AIBlockVisionRenderer aiBlockVisionRenderer = new AIBlockVisionRenderer();

    public static void draw(World world, SimpleGame canvas) {
        // Obstacles
        List<Obstacle> obstacles = world.getObstacles();
        for (Obstacle obstacle : obstacles) {
            canvas.rect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

        // Player
        Player player = world.getPlayer();
        canvas.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        canvas.text(String.format("Player is alive: %s. Score: %s", player.isAlive(), player.getAge()), 8, 15);

        // Player vision
        if (renderVision) {
//            aiLineVisionRenderer.render(world, canvas);
            aiBlockVisionRenderer.render(world, canvas);
        }
    }
}
