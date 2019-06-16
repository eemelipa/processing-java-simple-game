package game;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class Population implements Progressable {

    public static Population mutate(
            Population population,
            double mutationProbability,
            int targetPopulationSize
    ) {

        /**
         * 1. take best(?) players from given population
         * 2. mutate taken players and generate new population of target size
         */


        return null;
    }

    private World world;
    private List<Player> players;

    public Population(List<Player> players) {
        this.players = players;
    }

    @Override
    public void progressATick() {
        // TODO: 2019-06-09 write
        for (Player player : players) {
            player.setWorld(world);
            player.progressATick();
        }
    }

    public List<Player> getAlivePlayers() {
        List<Player> alivePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isAlive()) {
                alivePlayers.add(player);
            }
        }
        return alivePlayers;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players); // performance
    }

    @Nullable
    public HumanPlayer getHumanPlayer() {
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                return (HumanPlayer) player;
            }
        }
        return null;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
