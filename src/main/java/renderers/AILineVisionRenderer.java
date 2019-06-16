package renderers;

import game.AIBrainStraightLineVision;
import game.Brain;
import game.Player;
import game.SimpleGame;
import game.World;
import org.encog.ml.data.MLData;

public class AILineVisionRenderer implements Renderer {
    @Override
    public void render(World world, SimpleGame canvas) {
        Player player = world.getPlayer();
        Brain brain = player.getBrain();
        if (brain instanceof AIBrainStraightLineVision) {
            canvas.fill(canvas.color(204, 153, 0));
            canvas.stroke(canvas.color(255, 0, 0));

            AIBrainStraightLineVision aiBrain = (AIBrainStraightLineVision) brain;
            MLData inputs = aiBrain.getInputs(player, world);

            double[] data = inputs.getData();
            for (int i = 0; i < data.length; i++) {
                double datum = data[i];
                if (i == 51) {
                    canvas.rect(player.getX(), (float) datum, 3, 3);
                } else {
                    canvas.line(player.getX(), i * 10, (float) datum, i * 10);
                }
            }

            canvas.fill(canvas.color(255, 255, 255));
            canvas.stroke(canvas.color(0, 0, 0));
        }
    }
}
