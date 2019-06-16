package game;

import java.util.Arrays;
import java.util.Objects;

import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIBrainStraightLineVision implements Brain {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());

    private MLRegression model;

    public AIBrainStraightLineVision(MLRegression model) {
        this.model = model;
    }

    public Direction getNextMove(Player player, World world) {
        MLData inputs = null;
        try {
            inputs = getInputs(player, world);
        } catch (Exception e) {
            LOG.error("Exception in getting inputs", e);
        }

        MLData predict = model.compute(inputs);

        long up = Math.round(predict.getData(0));
        long down = Math.round(predict.getData(1));

        if (Objects.equals(up, down)) {
            return Direction.NOTHING;
        } else if (up == 1) {
            return Direction.UP;
        } else if (down == 1) {
            return Direction.DOWN;
        }

        throw new IllegalArgumentException("Couldn't get valid prediction");
    }

    public MLData getInputs(Player player, World world) {
        /*
        Inputs in this case is distance from player x to next object. There are 50 inputs each measures from specific y.
         */

        double[] distancesToObject = new double[52];
        Arrays.fill(distancesToObject, 800.0);
        distancesToObject[51] = player.getY();

        int playerX = world.getPlayer().getX();
        for (Obstacle obstacle : world.getObstacles()) {
            if (obstacle.getX() + obstacle.getWidth() >= playerX) {

                // TODO: 2019-06-15 now gets only the closest obstacle
                int obstacleY = obstacle.getY();
                int obstacleHeight = obstacle.getHeight();

                for (int i = obstacleY; i < obstacleY + obstacleHeight; i++) {
                    if (i % 10 == 0) {
                        int arrayCoordinate = i / 10;
                        if (distancesToObject[arrayCoordinate] != 800.0) {
                            // we want to record only the closest obstacle
                            continue;
                        }
                        distancesToObject[arrayCoordinate] = obstacle.getX();
                    }
                }

            }
        }

        return new BasicMLData(distancesToObject);
    }

}
