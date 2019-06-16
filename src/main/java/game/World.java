package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;

public class World implements Progressable {

    @Getter
    private final Player player;
    private List<Obstacle> obstacles = new ArrayList<>();
    private double obstacleProbability = 0.02;
    private int previousObstacleCreatedTicksAgo = 0;
    private Random random;
    @Getter
    private long tick = 0;

    public World(Player player, long randomSeed) {
        this.player = player;
        player.setWorld(this);
        this.random = new Random(randomSeed);
    }

    @Override
    public void progressATick() {
        tick++;

        // Create new objects if needed
        previousObstacleCreatedTicksAgo++;

        if (previousObstacleCreatedTicksAgo > 200 && random.nextDouble() < obstacleProbability) {
            int height = Math.max(random.nextInt(250), 30);

            // Spawn random or block person
            if (random.nextDouble() < 0.25) {
                if (random.nextDouble() < 0.5) {
                    // Put obstacle at opposite side of map
                    obstacles.add(new Obstacle(800, Math.min(490 - player.getY(), 500 - height), height));
                } else {
                    // Random place of map
                    obstacles.add(new Obstacle(800, random.nextInt(500 - height), height));
                }
            } else {
                // in front of player
                obstacles.add(new Obstacle(800, player.getY() - height / 2, height));
            }
            previousObstacleCreatedTicksAgo = 0;
        }

        // Progress objects
        for (Obstacle obstacle : obstacles) {
            obstacle.progressATick();
        }

        // Kill player if they hit obstacle
        player.progressATick();
        for (Obstacle obstacle : obstacles) {
            if (obstacle.intersects(player)) {
                player.die();
            }
        }


    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void humanPlayerUp() {
        if (player instanceof HumanPlayer) {
            player.up();
        }
    }

    public void humanPlayerDown() {
        if (player instanceof HumanPlayer) {
            player.down();
        }
    }
}
