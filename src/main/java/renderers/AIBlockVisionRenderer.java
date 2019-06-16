package renderers;

import game.AIBrainBlockVision;
import game.Brain;
import game.Player;
import game.SimpleGame;
import game.World;
import org.encog.ml.data.MLData;

public class AIBlockVisionRenderer implements Renderer {
    @Override
    public void render(World world, SimpleGame canvas) {
        Player player = world.getPlayer();
        Brain brain = player.getBrain();
        if (brain instanceof AIBrainBlockVision) {
            canvas.fill(canvas.color(204, 153, 0));
            canvas.stroke(canvas.color(255, 0, 0));

            AIBrainBlockVision aiBrain = (AIBrainBlockVision) brain;
            MLData inputs = aiBrain.getInputs(player, world);

            double[] data = inputs.getData();
            canvas.rect(player.getX(), (float) data[0], 3, 3);
            for (int i = 1; i < data.length; i += 4) {
                canvas.line((float) data[i], (float) data[i + 1], (float) data[i + 2], (float) data[i + 3]);
            }

            canvas.fill(canvas.color(255, 255, 255));
            canvas.stroke(canvas.color(0, 0, 0));
        }
    }
}
