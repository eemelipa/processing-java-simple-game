package game;

import java.util.Arrays;
import java.util.Objects;

import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIBrainBlockVision implements Brain {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass().getName());

    private MLRegression model;

    public AIBrainBlockVision(MLRegression model) {
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

        double[] distancesToObject = new double[61];
        Arrays.fill(distancesToObject, 800.0);
        distancesToObject[0] = player.getY();

        int dataCursor = 1;
        int playerX = world.getPlayer().getX();
        for (Obstacle obstacle : world.getObstacles()) {
            if (obstacle.getX() + obstacle.getWidth() >= playerX) {

                if (dataCursor + 4 < distancesToObject.length) {
                    int x1 = obstacle.getX();
                    int y1 = obstacle.getY();
                    int x2 = obstacle.getWidth() + x1;
                    int y2 = obstacle.getHeight() + y1;

                    distancesToObject[dataCursor] = x1;
                    distancesToObject[dataCursor + 1] = y1;
                    distancesToObject[dataCursor + 2] = x2;
                    distancesToObject[dataCursor + 3] = y2;

                    dataCursor += 4;
                }
            }
        }

        return new BasicMLData(distancesToObject);
    }

}
